package eu.hopu.activage.services.dto;

import java.util.List;

public class Device {
    private String id;
    private String label;
    private String type;
    private List<Sensor> sensors;

    public Device(String id, String label, String type, List<Sensor> sensors) {
        this.id = id;
        this.label = label;
        this.type = type;
        this.sensors = sensors;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public List<Sensor> getSensors() {
        return sensors;
    }

    public void setSensors(List<Sensor> sensors) {
        this.sensors = sensors;
    }

    @Override
    public String toString() {
        return "Device{" +
                "id='" + id + '\'' +
                ", label='" + label + '\'' +
                ", type='" + type + '\'' +
                ", sensors=" + sensors +
                '}';
    }


}
