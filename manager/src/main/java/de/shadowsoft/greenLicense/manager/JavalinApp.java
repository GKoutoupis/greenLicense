package de.shadowsoft.greenLicense.manager;

import de.shadowsoft.greenLicense.manager.model.software.Feature;
import de.shadowsoft.greenLicense.manager.model.software.Software;
import io.javalin.Javalin;
import de.shadowsoft.greenLicense.common.exception.DecryptionException;
import de.shadowsoft.greenLicense.common.exception.InvalidSignatureException;
import de.shadowsoft.greenLicense.common.exception.SystemValidationException;
import de.shadowsoft.greenLicense.common.license.GreenLicense;
import de.shadowsoft.greenLicense.common.license.GreenLicenseReader;
import de.shadowsoft.greenLicense.common.license.GreenLicenseValidator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Map;

public class JavalinApp {
    private static byte[] pk;
    private static GreenLicense lic;

    public static Software software;

    public static String file;

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


        Javalin app = Javalin.create(/*config*/)
                .get("/", ctx -> ctx.result("Hello World"));

        app.attribute("key","License OK!");
        app.get("/attribute",ctx -> {
            String key = ctx.appAttribute("key");
            ctx.result(key);
        });

        app.start(7070);
        app.get("/output/$s", ctx -> {
            System.out.println(ctx.result("Correct!"));

            ctx.status(201);
        });
        app.post("/" + file, ctx -> {
            System.out.println("unlocked");
            ctx.status(201);
        });
    }

    private void E_mail(String e_mail){
        Feature feature = new Feature("E-mail", e_mail);
        software.addFeature(feature);
    }

    private void Username(String username){
            Feature feature = new Feature("Username", username);
            software.addFeature(feature);
    }

    private void Password(String password){
        Feature feature = new Feature("Password", password);
        software.addFeature(feature);

    }

    public static void readPK(String path){
        byte[] pk;
            try {
                pk = Files.readAllBytes(Paths.get(path));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            JavalinApp.pk = pk;
    }

    public static void readLicFile(String path){
        GreenLicenseValidator validator = new GreenLicenseReader(pk);
        try {
            file = Files.readString(
                    Path.of(path)
            );
            lic = validator.readLicense(
                    Base64.getDecoder().decode(
                            file.getBytes()
                    )
            );
        } catch (IOException | InvalidSignatureException | SystemValidationException | DecryptionException e) {
            throw new RuntimeException(e);
        }
    }

}
    
    