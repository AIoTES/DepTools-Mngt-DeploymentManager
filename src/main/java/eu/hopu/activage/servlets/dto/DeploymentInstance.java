package eu.hopu.activage.servlets.dto;

import eu.hopu.activage.services.dto.Organization;
import eu.hopu.activage.services.dto.Platform;

public class DeploymentInstance {

    private String id;
    private String date;
    private String location;
    private Organization organization;
    private Platform platform;

    public DeploymentInstance() {
    }

    public DeploymentInstance(String id, String date, String location, Organization organization, Platform platform) {
        this.id = id;
        this.date = date;
        this.location = location;
        this.organization = organization;
        this.platform = platform;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public Platform getPlatform() {
        return platform;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }

    @Override
    public String toString() {
        return "DeploymentInstance{" +
                "id='" + id + '\'' +
                ", date='" + date + '\'' +
                ", location='" + location + '\'' +
                ", organization=" + organization +
                ", platform=" + platform +
                '}';
    }
}
