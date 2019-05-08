package eu.hopu.activage.metadataStorageClient;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import eu.hopu.activage.metadataStorageClient.dto.DeploymentMetadata;
import eu.hopu.activage.metadataStorageClient.dto.DeviceMetadata;
import okhttp3.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class MetadataOkHttpClient implements MetadataStorageClient {


    private static final Logger LOG = LogManager.getLogger(MetadataOkHttpClient.class);

    private static String DEVICES_API = "/api/devices";
    private static String DEPLOYMENT_API = "/api/deployments";

    private Gson gson;
    private OkHttpClient client;
    private String metadataStorageServerUrl;

    public MetadataOkHttpClient(String metadataStorageServerUrl) {
        this.metadataStorageServerUrl = metadataStorageServerUrl;
        this.gson = new Gson();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectionPool(new ConnectionPool(10, 30L, TimeUnit.SECONDS));
        this.client = builder.build();
    }

    public DeviceMetadata createDevice(String deviceId, String context) {
        LOG.info("Recording deviceMetadata creation → {}", deviceId);

        String stringUrl = metadataStorageServerUrl + DEVICES_API;
        URL url;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            LOG.error("Cannot perform operation over a malformed url → {}", stringUrl);
            return new DeviceMetadata("NULL", "");
        }

        DeviceMetadata deviceMetadata = new DeviceMetadata(deviceId, context);
        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), gson.toJson(deviceMetadata)))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                LOG.debug("DeviceMetadata created successfully → {}", deviceMetadata.toString());
                return deviceMetadata;
            } else {
                LOG.warn("Cannot create deviceMetadata → {}", deviceId);
                return new DeviceMetadata("NULL", "");
            }
        } catch (IOException e) {
            LOG.error("{} request throws exception → {}", stringUrl, e.getMessage());
            return new DeviceMetadata("NULL", "");
        }
    }

    public List<DeviceMetadata> listAllDevices() {
        LOG.info("Retrieving all devices stored.");

        String stringUrl = metadataStorageServerUrl + DEVICES_API;
        URL url;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            LOG.error("Cannot perform operation over a malformed url → {}", stringUrl);
            return new LinkedList<>();
        }

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                List<DeviceMetadata> deviceMetadata = gson.fromJson(response.body().string(), new TypeToken<List<DeviceMetadata>>() {
                }.getType());
                LOG.debug("All deviceMetadata correctly retrieved");
                return deviceMetadata;
            } else {
                LOG.warn("Cannot retrieve devices");
                return new LinkedList<>();
            }
        } catch (IOException e) {
            LOG.error("{} request throws exception → {}", stringUrl, e.getMessage());
            return new LinkedList<>();
        }
    }

    public DeviceMetadata getDeviceById(String deviceId) {
        LOG.info("Retrieving device by id → {}", deviceId);

        String stringUrl = metadataStorageServerUrl + DEVICES_API + "/" + deviceId;
        URL url;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            LOG.error("Cannot perform operation over a malformed url → {}", stringUrl);
            return new DeviceMetadata("NULL", "");
        }

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                DeviceMetadata deviceMetadata = gson.fromJson(response.body().string(), DeviceMetadata.class);
                LOG.debug("{} deviceMetadata correctly retrieved", deviceId);
                return deviceMetadata;
            } else {
                LOG.warn("Cannot retrieve device with id → {}", deviceId);
                return new DeviceMetadata("NULL", "");
            }
        } catch (IOException e) {
            LOG.error("{} request throws exception → {}", stringUrl, e.getMessage());
            return new DeviceMetadata("NULL", "");
        }
    }

    public DeviceMetadata updateDevice(DeviceMetadata deviceMetadataToBeUpdated) {
        LOG.info("Updating device → {}", deviceMetadataToBeUpdated.getDeviceID());

        String stringUrl = metadataStorageServerUrl + DEVICES_API;
        URL url;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            LOG.error("Cannot perform operation over a malformed url → {}", stringUrl);
            return new DeviceMetadata("NULL", "");
        }

        Request request = new Request.Builder()
                .url(url)
                .put(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), gson.toJson(deviceMetadataToBeUpdated)))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                DeviceMetadata deviceMetadata = gson.fromJson(response.body().string(), DeviceMetadata.class);
                LOG.debug("{} deviceMetadata correctly updated", deviceMetadataToBeUpdated.getDeviceID());
                return deviceMetadata;
            } else {
                LOG.warn("Cannot update device with id → {}", deviceMetadataToBeUpdated.getDeviceID());
                return new DeviceMetadata("NULL", "");
            }
        } catch (IOException e) {
            LOG.error("{} request throws exception → {}", stringUrl, e.getMessage());
            return new DeviceMetadata("NULL", "");
        }
    }

    public boolean deleteDevice(String deviceId) {
        LOG.info("Deleting device → {}", deviceId);

        String stringUrl = metadataStorageServerUrl + DEVICES_API + "/" + deviceId;
        URL url;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            LOG.error("Cannot perform operation over a malformed url → {}", stringUrl);
            return false;
        }

        Request request = new Request.Builder()
                .url(url)
                .delete()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                LOG.debug("{} device correctly deleted", deviceId);
                return true;
            } else {
                LOG.warn("Cannot delete device with id → {}", deviceId);
                return false;
            }
        } catch (IOException e) {
            LOG.error("{} request throws exception → {}", stringUrl, e.getMessage());
            return false;
        }
    }

    public DeploymentMetadata createDeployment(String deploymentId, String context) {
        LOG.info("Recording deploymentMetadata creation → {}", deploymentId);

        String stringUrl = metadataStorageServerUrl + DEPLOYMENT_API;
        URL url;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            LOG.error("Cannot perform operation over a malformed url → {}", stringUrl);
            return new DeploymentMetadata("NULL", "");
        }

        DeploymentMetadata deploymentMetadata = new DeploymentMetadata(deploymentId, context);
        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), gson.toJson(deploymentMetadata)))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                LOG.debug("DeploymentMetadata created successfully → {}", deploymentMetadata.toString());
                return deploymentMetadata;
            } else {
                LOG.warn("Cannot create deploymentMetadata → {}", deploymentId);
                return new DeploymentMetadata("NULL", "");
            }
        } catch (IOException e) {
            LOG.error("{} request throws exception → {}", stringUrl, e.getMessage());
            return new DeploymentMetadata("NULL", "");
        }
    }

    public List<DeploymentMetadata> listAllDeployments() {
        LOG.info("Retrieving all deployments stored.");

        String stringUrl = metadataStorageServerUrl + DEPLOYMENT_API;
        URL url;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            LOG.error("Cannot perform operation over a malformed url → {}", stringUrl);
            return new LinkedList<>();
        }

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                List<DeploymentMetadata> deploymentMetadata = gson.fromJson(response.body().string(), new TypeToken<List<DeploymentMetadata>>() {
                }.getType());
                LOG.debug("All deploymentMetadata correctly retrieved");
                return deploymentMetadata;
            } else {
                LOG.warn("Cannot retrieve deployments");
                return new LinkedList<>();
            }
        } catch (IOException e) {
            LOG.error("{} request throws exception → {}", stringUrl, e.getMessage());
            return new LinkedList<>();
        }
    }

    public DeploymentMetadata getDeploymentById(String deploymentId) {
        LOG.info("Retrieving deployment by id → {}", deploymentId);

        String stringUrl = metadataStorageServerUrl + DEPLOYMENT_API + "/" + deploymentId;
        URL url;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            LOG.error("Cannot perform operation over a malformed url → {}", stringUrl);
            return new DeploymentMetadata("NULL", "");
        }

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                DeploymentMetadata deploymentMetadata = gson.fromJson(response.body().string(), DeploymentMetadata.class);
                LOG.debug("{} deploymentMetadata correctly retrieved", deploymentId);
                return deploymentMetadata;
            } else {
                LOG.warn("Cannot retrieve deployment with id → {}", deploymentId);
                return new DeploymentMetadata("NULL", "");
            }
        } catch (IOException e) {
            LOG.error("{} request throws exception → {}", stringUrl, e.getMessage());
            return new DeploymentMetadata("NULL", "");
        }
    }

    public DeploymentMetadata updateDeployment(DeploymentMetadata deploymentMetadataToBeUpdated) {
        LOG.info("Updating deployment → {}", deploymentMetadataToBeUpdated.getId());

        String stringUrl = metadataStorageServerUrl + DEPLOYMENT_API;
        URL url;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            LOG.error("Cannot perform operation over a malformed url → {}", stringUrl);
            return new DeploymentMetadata("NULL", "");
        }

        Request request = new Request.Builder()
                .url(url)
                .put(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), gson.toJson(deploymentMetadataToBeUpdated)))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                DeploymentMetadata deploymentMetadata = gson.fromJson(response.body().string(), DeploymentMetadata.class);
                LOG.debug("{} deploymentMetadata correctly updated", deploymentMetadataToBeUpdated.getId());
                return deploymentMetadata;
            } else {
                LOG.warn("Cannot update deployment with id → {}", deploymentMetadataToBeUpdated.getId());
                return new DeploymentMetadata("NULL", "");
            }
        } catch (IOException e) {
            LOG.error("{} request throws exception → {}", stringUrl, e.getMessage());
            return new DeploymentMetadata("NULL", "");
        }
    }

    public boolean deleteDeployment(String deploymentId) {
        LOG.info("Deleting deployment → {}", deploymentId);

        String stringUrl = metadataStorageServerUrl + DEPLOYMENT_API + "/" + deploymentId;
        URL url;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            LOG.error("Cannot perform operation over a malformed url → {}", stringUrl);
            return false;
        }

        Request request = new Request.Builder()
                .url(url)
                .delete()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                LOG.debug("{} deployment correctly deleted", deploymentId);
                return true;
            } else {
                LOG.warn("Cannot delete deployment with id → {}", deploymentId);
                return false;
            }
        } catch (IOException e) {
            LOG.error("{} request throws exception → {}", stringUrl, e.getMessage());
            return false;
        }
    }

}
