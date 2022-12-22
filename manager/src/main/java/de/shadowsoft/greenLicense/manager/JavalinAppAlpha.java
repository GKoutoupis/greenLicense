package de.shadowsoft.greenLicense.manager;

import de.shadowsoft.greenLicense.manager.model.software.Software;
import io.javalin.Javalin;
import de.shadowsoft.greenLicense.common.exception.DecryptionException;
import de.shadowsoft.greenLicense.common.exception.InvalidSignatureException;
import de.shadowsoft.greenLicense.common.exception.SystemValidationException;
import de.shadowsoft.greenLicense.common.license.GreenLicense;
import de.shadowsoft.greenLicense.common.license.GreenLicenseReader;
import de.shadowsoft.greenLicense.common.license.GreenLicenseValidator;
import io.javalin.http.UploadedFile;
import io.javalin.http.staticfiles.Location;
import io.javalin.util.FileUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HexFormat;
import java.util.Map;

public class JavalinAppAlpha {
    private static byte[] pk;
    private static GreenLicense lic;

    public static Software software;

    public static String fileF;

    public static UploadedFile F;

    public static String s;

    public static void main(String[] args) {
        readPK("C:\\Workplace\\greenLicense\\manager\\src\\main\\java\\de\\shadowsoft\\greenLicense\\manager\\test\\licensepublicKey.txt");

        readLicFile("C:\\Workplace\\greenLicense\\manager\\src\\main\\java\\de\\shadowsoft\\greenLicense\\manager\\test\\license.lic");
        if (lic.isValid()) {
            System.out.println("License OK!");
            for (Map.Entry<String, String> feature : lic.getFeature().entrySet()) {
                System.out.println(feature.getKey() + "=" + feature.getValue());
            }
        } else {
            System.err.println("https://piratelol.ytmnd.com");
        }



        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/public", Location.CLASSPATH);
        }).start(7070);

        app.attribute("key","License OK!");
        app.get("/attribute",ctx -> {
            String key = ctx.appAttribute("key");
            ctx.result(key);
        });

        app.get("/output", ctx -> {
            System.out.println(ctx.uploadedFiles());

            ctx.status(201);
        });

        app.get("/key/<key>",ctx ->{
            if (check(ctx.pathParam("key"))) {
                System.out.println("Valid Key");
            } else {
                System.out.println("Invalid Key");
            }
        });

        app.post("/upload_single", ctx -> {
            ctx.uploadedFiles("files").forEach(file -> {
                FileUtil.streamToFile(file.content(), "upload/" + file.filename());
            });
        ctx.html("placeholder");
        System.out.println(s);
        });
    }

    public static void readPK(String path){
        byte[] pk;
            try {
                pk = Files.readAllBytes(Paths.get(path));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            JavalinAppAlpha.pk = pk;
    }

    public static void readLicFile(String path){
        GreenLicenseValidator validator = new GreenLicenseReader(pk);
        try {
            fileF = Files.readString(
                    Path.of(path)
            );
            lic = validator.readLicense(
                    Base64.getDecoder().decode(
                            fileF.getBytes()
                    )
            );
        } catch (IOException | InvalidSignatureException | SystemValidationException | DecryptionException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean check(String key) throws InvalidSignatureException, SystemValidationException, DecryptionException {
        HexFormat hexFormat = HexFormat.of();
        byte[] hexBytes = hexFormat.parseHex(key);
        GreenLicenseValidator validator = new GreenLicenseReader(pk);
        GreenLicense lic2 = validator.readLicense(hexBytes);
        return lic2.isValid();
    }

}
    
    