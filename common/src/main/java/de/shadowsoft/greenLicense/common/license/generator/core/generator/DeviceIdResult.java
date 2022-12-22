package de.shadowsoft.greenLicense.common.license.generator.core.generator;

public class DeviceIdResult {

    private byte[] deviceId;

    public DeviceIdResult(){
        deviceId = null;
    }

    public byte[] getDeviceId(){
        return this.deviceId;
    }

    public void setDeviceId(final byte[] deviceId){
        this.deviceId = deviceId;
    }
}
