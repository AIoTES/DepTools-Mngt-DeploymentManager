package eu.hopu.activage.services.dto;

public class DeploymentUnit {

    private String id;
    private String date;
    private String location;
    private Organization organization;
    private Platform platform;

    public DeploymentUnit(String id, String date, String location, Organization organization, Platform platform) {
        this.id = id;
        this.date = date;
        this.location = location;
        this.organization = organization;
        this.platform = platform;
    }

    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }

    public Organization getOrganization() {
        return organization;
    }

    public Platform getPlatform() {
        return platform;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }

    @Override
    public String toString() {
        return "DeploymentUnit{" +
                "id='" + id + '\'' +
                ", date='" + date + '\'' +
                ", location='" + location + '\'' +
                ", organization=" + organization +
                ", platform=" + platform +
                '}';
    }
}
