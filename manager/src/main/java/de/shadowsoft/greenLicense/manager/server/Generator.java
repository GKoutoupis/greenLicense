package de.shadowsoft.greenLicense.manager.server;

import de.shadowsoft.greenLicense.manager.model.keypair.FssKeyPair;
import de.shadowsoft.greenLicense.manager.model.keypair.KeyPairService;
import de.shadowsoft.greenLicense.manager.model.software.Feature;
import de.shadowsoft.greenLicense.manager.model.software.Software;
import de.shadowsoft.greenLicense.manager.tools.serializer.exception.DataLoadingException;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class Generator {

    private String randomValue(int minLength, int maxLength) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ThreadLocalRandom.current().nextInt(minLength, maxLength + 1); i++) {
            sb.append((char) ThreadLocalRandom.current().nextInt(97, 123));
        }
        return sb.toString();
    }

    private FssKeyPair createKeyPair(int keySize) {
        FssKeyPair keyPair = null;
        try {
            keyPair = KeyPairService.getInstance().createKeyPair(randomValue(5, 15), keySize);
        } catch (GeneralSecurityException | IOException | DataLoadingException e) {
            throw new RuntimeException(e);
        }
        return keyPair;
    }

}