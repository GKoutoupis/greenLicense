package de.shadowsoft.greenLicense.manager.server;

import de.shadowsoft.greenLicense.manager.model.keypair.FssKeyPair;
import de.shadowsoft.greenLicense.manager.model.software.Feature;
import de.shadowsoft.greenLicense.manager.model.software.Software;

public class SoftwareGenerator {

    Software software;

    public static String SOFTWARE_NAME = "Software3000";

    public static String SOFTWARE_VERSION = "1.0";

    public static int FEATURES_NUMBER = 5;

    SoftwareGenerator(FssKeyPair keypair){
        this.software = createSoftware(keypair);
        addFeatures(this.software);
    }

    private Software createSoftware(final FssKeyPair keyPair) {
        Software software = new Software();
        software.setKeyPairId(keyPair.getId());
        software.setName(SOFTWARE_NAME);
        software.setVersion(SOFTWARE_VERSION);
        return software;
    }

    private void addFeatures(Software software){
        for(int i = 0; i < FEATURES_NUMBER; i++){
            Feature feature = new Feature();
            feature.setName("feature" + i);
            feature.setValue("The value of feature No" + i);
            software.addFeature(feature);
        }
    }
}
