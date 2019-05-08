package eu.hopu.activage.services;

import eu.hopu.activage.mappers.DeploymentMapper;
import eu.hopu.activage.mappers.DeviceMapper;
import eu.hopu.activage.metadataStorageClient.MetadataStorageClient;
import eu.hopu.activage.metadataStorageClient.dto.DeploymentMetadata;
import eu.hopu.activage.metadataStorageClient.dto.DeviceMetadata;
import eu.hopu.activage.services.dto.*;

import java.util.List;

public class DeploymentManagerServiceImpl implements DeploymentManagerService {

    private MetadataStorageClient metadataStorageClient;
    private DeviceMapper deviceMapper;
    private DeploymentMapper deploymentMapper;

    public DeploymentManagerServiceImpl(MetadataStorageClient metadataStorageClient) {
        this.metadataStorageClient = metadataStorageClient;
        this.deviceMapper = new DeviceMapper();
        this.deploymentMapper = new DeploymentMapper();
    }

    @Override
    public List<Device> listAllDevices() {
        List<DeviceMetadata> deviceMetadatas = this.metadataStorageClient.listAllDevices();
        return deviceMapper.metadataListToDeviceList(deviceMetadatas);
    }

    @Override
    public List<DeploymentUnit> listAllDeployments() {
        List<DeploymentMetadata> deploymentMetadatas = this.metadataStorageClient.listAllDeployments();
        return deploymentMapper.metadataListToDeploymentList(deploymentMetadatas);
    }

    @Override
    public DeploymentUnit getDeploymentUnitById(String deploymentUnitId) {
        DeploymentMetadata deploymentMetadata = metadataStorageClient.getDeploymentById(deploymentUnitId);
        if (deploymentMetadata != null)
            return deploymentMapper.metadaToDeployment(deploymentMetadata);
        return null;
    }

    @Override
    public DeploymentUnit createDeploymentUnit(String deploymentUnitId, String date, String location, Organization organization, Platform platform) {
        DeploymentUnit deploymentUnit = new DeploymentUnit(deploymentUnitId,date,location,organization,platform);
        DeploymentMetadata deploymentMetadata = deploymentMapper.deploymentToMetadata(deploymentUnit);
        if (metadataStorageClient.createDeployment(deploymentUnitId, deploymentMetadata.getContext()).equals(deploymentMetadata))
            return deploymentUnit;

        return null;
    }

    @Override
    public DeploymentUnit createDeploymentUnit(String deploymentUnitId, String context) {
        DeploymentMetadata deploymentMetadata = metadataStorageClient.createDeployment(deploymentUnitId, context);

        return deploymentMapper.metadaToDeployment(deploymentMetadata);
    }

    @Override
    public boolean deleteDeploymentUnit(String deploymentUnitId) {
        return metadataStorageClient.deleteDeployment(deploymentUnitId);
    }

    @Override
    public DeploymentUnit addDeviceToDeploymentUnit(String deploymentUnitId, String deviceId) {
        DeploymentMetadata deploymentMetadata = metadataStorageClient.getDeploymentById(deploymentUnitId);
        if (deploymentMetadata == null)
            return null;
        DeploymentUnit deploymentUnit = deploymentMapper.metadaToDeployment(deploymentMetadata);

        List<String> devices = deploymentUnit.getPlatform().getDevices();
        devices.add(deviceId);
        deploymentUnit.getPlatform().setDevices(devices);

        deploymentMetadata = deploymentMapper.deploymentToMetadata(deploymentUnit);
        metadataStorageClient.updateDeployment(deploymentMetadata);

        return deploymentUnit;
    }

    @Override
    public DeploymentUnit deleteDeviceInDeploymentUnit(String deploymentUnitId, String deviceId) {
        DeploymentMetadata deploymentMetadata = metadataStorageClient.getDeploymentById(deploymentUnitId);
        if (deploymentMetadata == null)
            return null;
        DeploymentUnit deploymentUnit = deploymentMapper.metadaToDeployment(deploymentMetadata);

        List<String> devices = deploymentUnit.getPlatform().getDevices();
        devices.remove(deviceId);
        deploymentUnit.getPlatform().setDevices(devices);

        deploymentMetadata = deploymentMapper.deploymentToMetadata(deploymentUnit);
        metadataStorageClient.updateDeployment(deploymentMetadata);

        return deploymentUnit;
    }

    @Override
    public List<Record> getDeploymentUnitHistory(String deploymentUnitId) {
        return null;
    }
}
