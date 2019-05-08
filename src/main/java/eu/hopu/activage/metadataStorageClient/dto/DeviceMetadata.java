package eu.hopu.activage.metadataStorageClient.dto;

public class DeviceMetadata {
    private String deviceID;
    private String context;

    public DeviceMetadata(String deviceID, String context) {
        this.deviceID = deviceID;
        this.context = context;
    }

    public DeviceMetadata() {
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    @Override
    public String toString() {
        return "DeviceMetadata{" +
                "deviceID='" + deviceID + '\'' +
                ", context='" + context + '\'' +
                '}';
    }

}
