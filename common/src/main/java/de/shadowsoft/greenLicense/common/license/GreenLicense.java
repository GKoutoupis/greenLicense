package de.shadowsoft.greenLicense.common.license;

import java.util.HashMap;
import java.util.Map;

public class GreenLicense {

    private Map<String, String> feature;
    private boolean validMagicBytes;
    private boolean validSignature;
    private boolean validSystem;

    private boolean validDevice;

    public GreenLicense() {
        feature = new HashMap<>();
        validSystem = false;
        validMagicBytes = false;
        validSignature = false;
        validDevice = false;
    }

    public final Map<String, String> getFeature() {
        return feature;
    }

    public final void setFeature(final Map<String, String> feature) {
        this.feature = feature;
    }


    public boolean isValid() {
        return validSignature & validSystem & validMagicBytes;
    }

    public final boolean isValidMagicBytes() {
        return validMagicBytes;
    }

    public final void setValidMagicBytes(final boolean validMagicBytes) {
        this.validMagicBytes = validMagicBytes;
    }

    public boolean isValidSignature() {
        return validSignature;
    }

    public void setValidSignature(final boolean validSignature) {
        this.validSignature = validSignature;
    }

    public final boolean isValidSystem() {
        return validSystem;
    }

    public final void setValidSystem(final boolean validSystem) {
        this.validSystem = validSystem;
    }

    public final boolean isValidDevice() {
        return validDevice;
    }

    public final void setValidDevice(final boolean validDevice) {
        this.validDevice = validDevice;
    }
}
    
    