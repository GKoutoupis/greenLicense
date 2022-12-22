package de.shadowsoft.greenLicense.manager.test;

import de.shadowsoft.greenLicense.common.exception.DecryptionException;
import de.shadowsoft.greenLicense.common.exception.InvalidSignatureException;
import de.shadowsoft.greenLicense.common.exception.SystemValidationException;
import de.shadowsoft.greenLicense.common.license.GreenLicense;
import de.shadowsoft.greenLicense.common.license.GreenLicenseReader;
import de.shadowsoft.greenLicense.common.license.GreenLicenseValidator;
import de.shadowsoft.greenLicense.common.license.generator.core.generator.BasicIdGenerator;
import de.shadowsoft.greenLicense.common.license.generator.core.generator.IdGenerator;
import de.shadowsoft.greenLicense.manager.config.ConfigService;
import de.shadowsoft.greenLicense.manager.exceptions.NoSuchKeyPairException;
import de.shadowsoft.greenLicense.manager.license.LicenseCreator;
import de.shadowsoft.greenLicense.manager.model.keypair.FssKeyPair;
import de.shadowsoft.greenLicense.manager.model.keypair.KeyPairService;
import de.shadowsoft.greenLicense.manager.model.license.License;
import de.shadowsoft.greenLicense.manager.model.software.Feature;
import de.shadowsoft.greenLicense.manager.model.software.Software;
import de.shadowsoft.greenLicense.manager.tools.serializer.exception.DataLoadingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Comparator;
import java.util.HexFormat;
import java.util.concurrent.ThreadLocalRandom;

public class MyTest {

    public static FssKeyPair KP;

    private static final String TEST_DATA_PATH = "./test/";

    public MyTest(){}

    private String randomValue(int minLength, int maxLength) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ThreadLocalRandom.current().nextInt(minLength, maxLength + 1); i++) {
            sb.append((char) ThreadLocalRandom.current().nextInt(97, 123));
        }
        return sb.toString();
    }

    private void addFeatures(Software software, int amount){
        for(int i = 0; i < amount; i++){
            Feature feature = new Feature();
            feature.setName("feature" + i);
            feature.setValue("The value of feature No" + i);
            System.out.println(feature);
            software.addFeature(feature);
        }
    }

    private FssKeyPair createKeyPair(int keySize) throws GeneralSecurityException, IOException, DataLoadingException {
        FssKeyPair keyPair = KeyPairService.getInstance().createKeyPair(randomValue(5, 15), keySize);
        KP = keyPair;
        System.out.println("KeyPair created");
        System.out.println(keyPair.toString());
        return keyPair;
    }

    private byte[] createLicense(final License createdLicense) throws DataLoadingException, GeneralSecurityException, NoSuchKeyPairException, InterruptedException, IOException {
        return new LicenseCreator().createLicense(createdLicense);
    }

    private Software createSoftware(final FssKeyPair keyPair) {
        System.out.println("Creating new software");
        Software software = new Software();
        software.setKeyPairId(keyPair.getId());
        software.setName("softwareSoftware3000");
        software.setVersion("1.0");
        System.out.println(software.toString());
        return software;
    }

    private boolean deleteDirectory(File directoryToBeDeleted) {
        Path pathToBeDeleted = directoryToBeDeleted.toPath();
        try {
            Files.walk(pathToBeDeleted)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
            return true;
        } catch (IOException e) {
        }
        return false;
    }

    private void readGeneralLicense(GreenLicenseValidator validator, final License licenseConfiguration, final byte[] licenseBytes) throws DecryptionException, SystemValidationException, InvalidSignatureException {
        GreenLicense license = validator.readDeviceLicense(licenseBytes);
        assert license.isValid();
        System.out.println("License has " + license.getFeature().entrySet().size() + " features");
        for (Feature item : licenseConfiguration.getSoftware().getFeatures()) {
            System.out.println("Feature id: "+ item.getId() + " Feature value: " + license.getFeature().get(item.getId()));
            assert (license.getFeature().get(item.getId()).equals(item.getValue()));
        }
    }

    private void readLicense(GreenLicenseValidator validator, final License licenseConfiguration, final String licenseB64) throws DecryptionException, SystemValidationException, InvalidSignatureException {
        System.out.println("---------------- Reading from base64 /----------------");
        readGeneralLicense(validator, licenseConfiguration, Base64.getDecoder().decode(licenseB64.getBytes()));
    }

    private void readLicense(GreenLicenseValidator validator, final License licenseConfiguration, final byte[] licenseBytes) throws DecryptionException, SystemValidationException, InvalidSignatureException {
        System.out.println("---------------- Reading from byteArray ----------------");
        readGeneralLicense(validator, licenseConfiguration, licenseBytes);
    }

    private String getDeviceId() throws InterruptedException, IOException {
        final IdGenerator generator = new BasicIdGenerator();
        return new String(Base64.getEncoder().encode(generator.generateDeviceId()));
    }

    private License setupLicense(final Software software, byte selector) throws IOException, InterruptedException {
        License createdLicense = new License();
        createdLicense.setName("license");
        createdLicense.setSoftware(software.clone());// why clone
        createdLicense.setDeviceId(getDeviceId());
        System.out.println(createdLicense);
        return createdLicense;
    }



    @Test
    @DisplayName("Full license test")
    public void testFullLicense() {
        ConfigService.getInstance().getSettings().setBasePath(TEST_DATA_PATH);
        try {
            FssKeyPair keyPair = createKeyPair(1024);
            Software software = createSoftware(keyPair);
            addFeatures(software, 5);//change
            License licenseConfiguration = setupLicense(software, Byte.parseByte("00000000", 2));//check | s -> binary
            byte[] licenseBytes = createLicense(licenseConfiguration);
            //byte to Hex
            System.out.println("Hex:");
            StringBuilder s = new StringBuilder();
            HexFormat hexFormat = HexFormat.of();
            for (byte b : licenseBytes) {
                String st = hexFormat.toHexDigits(b);
                s.append(st);
            }
            System.out.println(s);
            //hex to byte
            byte[] hexBytes = hexFormat.parseHex(s);
            System.out.println("HexBytes: " + Arrays.toString(hexBytes));
            System.out.println("licenseBytes: " + Arrays.toString(licenseBytes));
            String licBytesString = Arrays.toString(licenseBytes);
            System.out.println();
            System.out.println("licenseBytesString: " + s);
            String licenseB64 = new String(Base64.getEncoder().encode(licenseBytes));
            System.out.println(licenseB64);
            //public key needed for validation in Back-end
            OutputStream os = new FileOutputStream("./src/main/java/de/shadowsoft/greenLicense/manager/test/" + licenseConfiguration.getName() +"publicKey.txt");
            os.write(KP.getKeyPair().getPublic().getEncoded());
            os.close();

            FileWriter fw = new FileWriter("./src/main/java/de/shadowsoft/greenLicense/manager/test/" + licenseConfiguration.getName() +".lic");
            fw.write(licenseB64);
            fw.close();

            FileWriter fw2 = new FileWriter("./src/main/java/de/shadowsoft/greenLicense/manager/test/" + licenseConfiguration.getName() +"Bytes.txt");
            fw2.write(licBytesString);
            fw2.close();

            FileWriter fw3 = new FileWriter("./src/main/java/de/shadowsoft/greenLicense/manager/test/" + licenseConfiguration.getName() +"Hex.txt");
            fw3.write(s.toString());
            fw3.close();

            fileChecker();

        } catch (IOException | GeneralSecurityException | DecryptionException | InterruptedException | DataLoadingException | NoSuchKeyPairException | SystemValidationException | InvalidSignatureException e) {
            System.out.println("Error while creating license");
        }


        deleteDirectory(new File(TEST_DATA_PATH));
    }

    public void fileChecker() throws IOException, DataLoadingException, GeneralSecurityException, InvalidSignatureException, SystemValidationException, DecryptionException {
        Path fileName = Path.of("./src/main/java/de/shadowsoft/greenLicense/manager/test/license.lic");
        String licenseB64 = Files.readString(fileName);
        byte[] licenseBytes = Base64.getDecoder().decode(licenseB64.getBytes());
        for (byte b : licenseBytes) {
            String st = String.format("%02X", b);
            System.out.print(st);
        }
        GreenLicenseValidator validator = new GreenLicenseReader(KP.getKeyPair().getPublic().getEncoded());
        GreenLicense license = validator.readLicense(licenseBytes);
        if (license.isValid()) {
            System.out.println("Your license is valid");
        } else {
            System.out.println("You are a pirate");
        }
    }

//    public void fileChecker2() throws IOException, DataLoadingException, GeneralSecurityException, InvalidSignatureException, SystemValidationException, DecryptionException {
//        Path fileName = Path.of("./src/main/java/de/shadowsoft/greenLicense/manager/test/licenseBytes.txt");
//        String licenseBytes = Files.readString(fileName);
//        byte[] licenseBytes = Base64.getDecoder().decode(licenseB64.getBytes());
//        GreenLicenseValidator validator = new GreenLicenseReader(KP.getKeyPair().getPublic().getEncoded());
//        GreenLicense license = validator.readLicense(licenseBytes);
//        if (license.isValid()) {
//            System.out.println("Your license is valid");
//        } else {
//            System.out.println("You are a pirate");
//        }
//    }

}
