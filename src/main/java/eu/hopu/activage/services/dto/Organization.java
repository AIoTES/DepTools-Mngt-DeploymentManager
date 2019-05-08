package eu.hopu.activage.services.dto;

public class Organization {
    private String id;
    private String label;

    public Organization() {}

    public Organization(String id, String label) {
        this.id = id;
        this.label = label;
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

    @Override
    public String toString() {
        return "Organization{" +
                "id='" + id + '\'' +
                ", label='" + label + '\'' +
                '}';
    }

}
