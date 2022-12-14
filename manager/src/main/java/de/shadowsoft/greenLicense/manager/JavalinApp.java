package de.shadowsoft.greenLicense.manager;

import de.shadowsoft.greenLicense.common.exception.DecryptionException;
import de.shadowsoft.greenLicense.common.exception.InvalidSignatureException;
import de.shadowsoft.greenLicense.common.exception.SystemValidationException;
import de.shadowsoft.greenLicense.common.license.GreenLicense;
import de.shadowsoft.greenLicense.common.license.GreenLicenseReader;
import de.shadowsoft.greenLicense.common.license.GreenLicenseValidator;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HexFormat;
import java.util.Map;

public class JavalinApp {

    public static void main(String[] args) {
        //absolute path needed
        byte[] pk = readPublicKey("C:\\Workplace\\greenLicense\\manager\\src\\main\\java\\de\\shadowsoft\\greenLicense\\manager\\test\\licensepublicKey.txt");
        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/public", Location.CLASSPATH);
        }).start(7070);

        app.get("/key/<key>",ctx ->{
            if (check(ctx.pathParam("key"),pk)) {
                ctx.result("License OK!");
            } else {
                ctx.redirect("https://piratelol.ytmnd.com");
            }
        });
    }

    public static byte[] readPublicKey(String path){
        byte[] publicKey;
            try {
                publicKey = Files.readAllBytes(Paths.get(path));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return publicKey;
    }

    public static boolean check(String key, byte[] publicKey) {
        HexFormat hexFormat = HexFormat.of();
        byte[] hexBytes = null;
        try {
            hexBytes = hexFormat.parseHex(key);
        } catch (IllegalArgumentException e){
            return false;
        }
        GreenLicenseValidator validator = new GreenLicenseReader(publicKey);
        GreenLicense license = null;
        try {
            license = validator.readLicense(hexBytes);
        } catch (DecryptionException | SystemValidationException | InvalidSignatureException e) {
            return false;
        }
        return license.isValid();
    }

}
    
    