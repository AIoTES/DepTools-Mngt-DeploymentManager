package eu.hopu.activage.metadataStorageClient;

import eu.hopu.activage.metadataStorageClient.dto.DeploymentMetadata;
import eu.hopu.activage.metadataStorageClient.dto.DeviceMetadata;

import java.util.List;

public interface MetadataStorageClient {

    DeviceMetadata createDevice(String deviceId, String context);

    List<DeviceMetadata> listAllDevices();

    DeviceMetadata getDeviceById(String deviceId);

    DeviceMetadata updateDevice(DeviceMetadata deviceMetadataToBeUpdated);

    boolean deleteDevice(String deviceId);

    DeploymentMetadata createDeployment(String deploymentId, String context);

    List<DeploymentMetadata> listAllDeployments();

    DeploymentMetadata getDeploymentById(String deploymentId);

    DeploymentMetadata updateDeployment(DeploymentMetadata deploymentMetadataToBeUpdated);

    boolean deleteDeployment(String deploymentId);

}
