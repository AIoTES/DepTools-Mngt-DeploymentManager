package eu.hopu.activage.services.dto;

import java.util.List;

public class Platform {

    private String id;
    private String label;
    private List<String> devices;

    public Platform() {}

    public Platform(String id, String label, List<String> devices) {
        this.id = id;
        this.label = label;
        this.devices = devices;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<String> getDevices() {
        return devices;
    }

    public void setDevices(List<String> devices) {
        this.devices = devices;
    }

    @Override
    public String toString() {
        return "Platform{" +
                "id='" + id + '\'' +
                ", label='" + label + '\'' +
                ", devices=" + devices +
                '}';
    }
}
