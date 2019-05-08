package eu.hopu.activage.metadataStorageClient;

import eu.hopu.activage.metadataStorageClient.dto.DeploymentMetadata;
import eu.hopu.activage.metadataStorageClient.dto.DeviceMetadata;
import eu.hopu.activage.utils.HopAsserts;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

// This tests must be executed with a Metadata Storage Server Instance
@Ignore
public class MetadataOkHttpClientIntegrationTest {

    private String metadataStorage;
    private MetadataStorageClient client;

    @Before
    public void setUp() {
        metadataStorage = "http://localhost:8081";
        client = new MetadataOkHttpClient(metadataStorage);
    }

    @Test
    public void test_device_operations() {

        String deviceId = "1_1";
        String context = "@prefix iotpex: <http://inter-iot.eu/GOIoTP#> . " +
                "@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> . " +
                "@prefix sosa: <http://www.w3.org/ns/sosa/> . " +
                "@prefix act: <http://www.semanticweb.org/activage/ontologies/2018/4/activage-core#> . " +
                "<DeviceMetadata/1_1> a iotpex:IoTDevice ; " +
                "rdfs:label \"Fibaro motion sensor\"@en ;" +
                " sosa:hosts <Sensor/1_1> ;" +
                " sosa:hosts <Sensor/1_2> ;" +
                " sosa:hosts <Sensor/1_3> . " +
                "<Sensor/1_1 > a act:IlluminanceSensor . " +
                "<Sensor/1_2 > a act:TemperatureSensor . " +
                "<Sensor/1_3 > a act:UserOccupancySensor . ";

        DeviceMetadata actual = client.createDevice(deviceId, context);
        DeviceMetadata expected = new DeviceMetadata(deviceId, context);
        HopAsserts.assertEqualsJson(expected, actual);

        List<DeviceMetadata> actualDeviceMetadata = client.listAllDevices();
        List<DeviceMetadata> expectedDeviceMetadata = new LinkedList<>();
        expectedDeviceMetadata.add(expected);
        HopAsserts.assertEqualsJson(expectedDeviceMetadata, actualDeviceMetadata);

        actual = client.getDeviceById(deviceId);
        HopAsserts.assertEqualsJson(expected, actual);

        String updatedContext = "@prefix iotpex: <http://inter-iot.eu/GOIoTP#> . " +
                "@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> . " +
                "@prefix sosa: <http://www.w3.org/ns/sosa/> . " +
                "@prefix act: <http://www.semanticweb.org/activage/ontologies/2018/4/activage-core#> . " +
                "<DeviceMetadata/1_1> a iotpex:IoTDevice ; " +
                "rdfs:label \"Fibaro motion sensor\"@en ;" +
                " sosa:hosts <Sensor/1_1> ;" +
                " sosa:hosts <Sensor/1_2> ;" +
                " sosa:hosts <Sensor/1_3> ; " +
                " sosa:hosts <Sensor/1_4> . " +
                "<Sensor/1_1 > a act:IlluminanceSensor . " +
                "<Sensor/1_2 > a act:TemperatureSensor . " +
                "<Sensor/1_3 > a act:UserOccupancySensor . " +
                "<Sensor/1_4 > a act:UserOccupancySensor . ";

        actual = client.updateDevice(new DeviceMetadata(deviceId, updatedContext));
        expected = new DeviceMetadata(deviceId, updatedContext);
        HopAsserts.assertEqualsJson(expected, actual);

        Assert.assertTrue(client.deleteDevice(deviceId));
        HopAsserts.assertEqualsJson(new LinkedList<>(), client.listAllDevices());
    }

    @Test
    public void test_deployment_operations() {

        String deploymentId = "deploymentId";
        String context = "@prefix prov: <http://www.w3.org/ns/prov#>. " +
                "@prefix ssn: <http://www.w3.org/ns/ssn/> . " +
                "@prefix iot: <http://inter-iot.eu/GOIoTP#> . " +
                "@prefix org: <http://www.w3.org/ns/org#> . " +
                "@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>. " +
                "@prefix sosa: <http://www.w3.org/ns/sosa/> . " +
                "<Organization/1> a org:Organization ; " +
                "rdfs:label \"Municipality of Thessaloniki.\"@en . " +
                "<Deployment/PX_HOME1> a ssn:Deployment ; " +
                "prov:startedAtTime \"2017-06-06\"^^xsd:date ; " +
                "iot:hasLocation \"AREA[“Thessaloniki\"]\"^^http://www.opengis.net/ont/geosparql#wktLiteral prov:wasAssociatedWith <Organization/1> ; " +
                "ssn:deployedOnPlatform <Platform/1> . " +
                "<Platform/1> a sosa:Platform ; " +
                "rdfs:label \"Activage Platform GR 1\"@en ; " +
                "sosa:hosts <Device/1_1> ; " +
                "sosa:hosts <Device/1_2> . ";

        DeploymentMetadata actual = client.createDeployment(deploymentId, context);
        DeploymentMetadata expected = new DeploymentMetadata(deploymentId, context);
        HopAsserts.assertEqualsJson(expected, actual);

        List<DeploymentMetadata> actualDeploymentMetadata = client.listAllDeployments();
        List<DeploymentMetadata> expectedDeploymentMetadata = new LinkedList<>();
        expectedDeploymentMetadata.add(expected);
        HopAsserts.assertEqualsJson(expectedDeploymentMetadata, actualDeploymentMetadata);

        actual = client.getDeploymentById(deploymentId);
        HopAsserts.assertEqualsJson(expected, actual);

        String updatedContext = "@prefix prov: <http://www.w3.org/ns/prov#>. " +
                "@prefix ssn: <http://www.w3.org/ns/ssn/> . " +
                "@prefix iot: <http://inter-iot.eu/GOIoTP#> . " +
                "@prefix org: <http://www.w3.org/ns/org#> . " +
                "@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>. " +
                "@prefix sosa: <http://www.w3.org/ns/sosa/> . " +
                "<Organization/1> a org:Organization ; " +
                "rdfs:label \"Municipality of Thessaloniki.\"@en . " +
                "<Deployment/PX_HOME1> a ssn:Deployment ; " +
                "prov:startedAtTime \"2017-06-06\"^^xsd:date ; " +
                "iot:hasLocation \"AREA[“Thessaloniki\"]\"^^http://www.opengis.net/ont/geosparql#wktLiteral prov:wasAssociatedWith <Organization/1> ; " +
                "ssn:deployedOnPlatform <Platform/1> . " +
                "<Platform/1> a sosa:Platform ; " +
                "rdfs:label \"Activage Platform GR 1\"@en ; " +
                "sosa:hosts <Device/1_1> ; " +
                "sosa:hosts <Device/1_2> ; " +
                "sosa:hosts <Device/1_3> . ";

        actual = client.updateDeployment(new DeploymentMetadata(deploymentId, updatedContext));
        expected = new DeploymentMetadata(deploymentId, updatedContext);
        HopAsserts.assertEqualsJson(expected, actual);

        Assert.assertTrue(client.deleteDeployment(deploymentId));
        HopAsserts.assertEqualsJson(new LinkedList<>(), client.listAllDeployments());

    }
}
