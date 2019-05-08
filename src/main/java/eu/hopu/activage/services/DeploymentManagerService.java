package eu.hopu.activage.services;

import eu.hopu.activage.services.dto.*;

import java.util.List;

public interface DeploymentManagerService {

    List<Device> listAllDevices();

    List<DeploymentUnit> listAllDeployments();

    DeploymentUnit getDeploymentUnitById(String deploymentUnitId);

    DeploymentUnit createDeploymentUnit(String deploymentUnitId, String date, String location, Organization organization, Platform platform);

    DeploymentUnit createDeploymentUnit(String deploymentUnitId, String context);

    boolean deleteDeploymentUnit(String deploymentUnitId);

    DeploymentUnit addDeviceToDeploymentUnit(String deploymentUnitId, String deviceId);

    DeploymentUnit deleteDeviceInDeploymentUnit(String deploymentUnitId, String deviceId);

    //    TODO
    List<Record> getDeploymentUnitHistory(String deploymentUnitId);

}
