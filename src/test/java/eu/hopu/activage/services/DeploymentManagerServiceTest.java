package eu.hopu.activage.services;

import eu.hopu.activage.metadataStorageClient.MetadataStorageClient;
import eu.hopu.activage.metadataStorageClient.dto.DeploymentMetadata;
import eu.hopu.activage.metadataStorageClient.dto.DeviceMetadata;
import eu.hopu.activage.services.dto.*;
import eu.hopu.activage.utils.HopAsserts;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DeploymentManagerServiceTest {


    private MetadataStorageClient client;
    private DeploymentManagerService service;

    @Before
    public void setUp() throws Exception {
        client = mock(MetadataStorageClient.class);
        service = new DeploymentManagerServiceImpl(client);
    }

    @Test
    public void listAllDevices() {
        String context = "@prefix iotpex: <http://inter-iot.eu/GOIoTP#> . " +
                "@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> . " +
                "@prefix sosa: <http://www.w3.org/ns/sosa/> . " +
                "@prefix act: <http://www.semanticweb.org/activage/ontologies/2018/4/activage-core#> . " +
                "<Device/1_1> a iotpex:IoTDevice ; " +
                "rdfs:label \"Fibaro motion sensor\"@en ;" +
                " sosa:hosts <Sensor/1_1> ;" +
                " sosa:hosts <Sensor/1_2> ;" +
                " sosa:hosts <Sensor/1_3> . " +
                "<Sensor/1_1> a act:IlluminanceSensor . " +
                "<Sensor/1_2> a act:TemperatureSensor . " +
                "<Sensor/1_3> a act:UserOccupancySensor . ";


        List<DeviceMetadata> mockedDevicesMetadata = new LinkedList<>();
        mockedDevicesMetadata.add(new DeviceMetadata("1_1", context));
        when(client.listAllDevices()).thenReturn(mockedDevicesMetadata);


        List<Device> actual = service.listAllDevices();
        List<Device> expected = new LinkedList<>();
        String deviceId = "1_1";
        String deviceType = "IoTDevice";
        String deviceLabel = "Fibaro motion sensor";
        List<Sensor> sensors = new LinkedList<>();
        sensors.add(new Sensor("1_1", "IlluminanceSensor"));
        sensors.add(new Sensor("1_2", "TemperatureSensor"));
        sensors.add(new Sensor("1_3", "UserOccupancySensor"));
        Device expectedDevice = new Device(deviceId, deviceType, deviceLabel, sensors);
        expected.add(expectedDevice);

        HopAsserts.assertEqualsJson(expected, actual);

    }

    @Test
    public void listAllDeploymentUnits() {
        String deploymentId = "PX_HOME1";
        String context = "@prefix prov: <http://www.w3.org/ns/prov#>. " +
                "@prefix ssn: <http://www.w3.org/ns/ssn/> . " +
                "@prefix iot: <http://inter-iot.eu/GOIoTP#> . " +
                "@prefix org: <http://www.w3.org/ns/org#> . " +
                "@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>. " +
                "@prefix sosa: <http://www.w3.org/ns/sosa/> . " +
                "<Organization/1> a org:Organization ; " +
                "rdfs:label \"Municipality of Thessaloniki.\"@en . " +
                "<Deployment/PX_HOME1> a ssn:DeploymentMetadata ; " +
                "prov:startedAtTime \"2017-06-06\"^^xsd:date ; " +
                "iot:hasLocation \"AREA[“Thessaloniki\"]\"^^http://www.opengis.net/ont/geosparql#wktLiteral prov:wasAssociatedWith <Organization/1> ; " +
                "ssn:deployedOnPlatform <Platform/1> . " +
                "<Platform/1> a sosa:Platform ; " +
                "rdfs:label \"Activage Platform GR 1\"@en ; " +
                "sosa:hosts <Device/1_1> ; " +
                "sosa:hosts <Device/1_2> . ";

        List<DeploymentMetadata> mockedDeploymentMetadata = new LinkedList<>();
        mockedDeploymentMetadata.add(new DeploymentMetadata(deploymentId, context));
        when(client.listAllDeployments()).thenReturn(mockedDeploymentMetadata);

        List<DeploymentUnit> actual = service.listAllDeployments();
        List<DeploymentUnit> expected = new LinkedList<>();

        List<String> devices = new LinkedList<>();
        devices.add("1_1");
        devices.add("1_2");
        expected.add(
                new DeploymentUnit("PX_HOME1", "2017-06-06",
                        "AREA[“Thessaloniki\"]",
                        new Organization("1", "Municipality of Thessaloniki."),
                        new Platform("1", "Activage Platform GR 1", devices)
                )
        );

        HopAsserts.assertEqualsJson(expected, actual);
    }

    @Test
    public void createDeploymentUnit() {
        String context = "@prefix prov: <http://www.w3.org/ns/prov#>. " +
                "@prefix ssn: <http://www.w3.org/ns/ssn/> . " +
                "@prefix iot: <http://inter-iot.eu/GOIoTP#> . " +
                "@prefix org: <http://www.w3.org/ns/org#> . " +
                "@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>. " +
                "@prefix sosa: <http://www.w3.org/ns/sosa/> . " +
                "<Organization/organization> a org:Organization ; " +
                "rdfs:label \"label\"@en . " +
                "<Deployment/deploymentUnit> a ssn:Deployment ; " +
                "prov:startedAtTime \"date\"^^xsd:date ; " +
                "iot:hasLocation \"location\"^^http://www.opengis.net/ont/geosparql#wktLiteral prov:wasAssociatedWith <Organization/organization> ; " +
                "ssn:deployedOnPlatform <Platform/platform> . " +
                "<Platform/platform> a sosa:Platform ; " +
                "rdfs:label \"label\"@en ; " +
                "sosa:hosts <Device/device1> ; " +
                "sosa:hosts <Device/device2> . ";
        when(client.createDeployment(eq("deploymentUnit"), eq(context))).thenReturn(new DeploymentMetadata("deploymentUnit", context));

        Organization organization = new Organization("organization", "label");
        List<String> devices = new LinkedList<>();
        devices.add("device1");
        devices.add("device2");
        Platform platform = new Platform("platform", "label", devices);
        DeploymentUnit expected = new DeploymentUnit("deploymentUnit", "date", "location", organization, platform);
        DeploymentUnit actual = service.createDeploymentUnit("deploymentUnit", "date", "location", organization, platform);
        HopAsserts.assertEqualsJson(expected, actual);
    }

    @Test
    public void addDeviceToDeploymentUnit() {
        String context2 = "@prefix prov: <http://www.w3.org/ns/prov#>. " +
                "@prefix ssn: <http://www.w3.org/ns/ssn/> . " +
                "@prefix iot: <http://inter-iot.eu/GOIoTP#> . " +
                "@prefix org: <http://www.w3.org/ns/org#> . " +
                "@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>. " +
                "@prefix sosa: <http://www.w3.org/ns/sosa/> . " +
                "<Organization/organization> a org:Organization ; " +
                "rdfs:label \"label\"@en . " +
                "<Deployment/deploymentUnit> a ssn:Deployment ; " +
                "prov:startedAtTime \"date\"^^xsd:date ; " +
                "iot:hasLocation \"area\"^^http://www.opengis.net/ont/geosparql#wktLiteral prov:wasAssociatedWith <Organization/organization> ; " +
                "ssn:deployedOnPlatform <Platform/platform> . " +
                "<Platform/platform> a sosa:Platform ; " +
                "rdfs:label \"label\"@en ; " +
                "sosa:hosts <Device/device1> ; " +
                "sosa:hosts <Device/device2> . ";
        when(client.getDeploymentById("deploymentUnit")).thenReturn(new DeploymentMetadata("deploymentUnit", context2));

        String context = "@prefix prov: <http://www.w3.org/ns/prov#>. " +
                "@prefix ssn: <http://www.w3.org/ns/ssn/> . " +
                "@prefix iot: <http://inter-iot.eu/GOIoTP#> . " +
                "@prefix org: <http://www.w3.org/ns/org#> . " +
                "@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>. " +
                "@prefix sosa: <http://www.w3.org/ns/sosa/> . " +
                "<Organization/organization> a org:Organization ; " +
                "rdfs:label \"label\"@en . " +
                "<Deployment/deploymentUnit> a ssn:Deployment ; " +
                "prov:startedAtTime \"date\"^^xsd:date ; " +
                "iot:hasLocation \"area\"^^http://www.opengis.net/ont/geosparql#wktLiteral prov:wasAssociatedWith <Organization/organization> ; " +
                "ssn:deployedOnPlatform <Platform/platform> . " +
                "<Platform/platform> a sosa:Platform ; " +
                "rdfs:label \"label\"@en ; " +
                "sosa:hosts <Device/device1> ; " +
                "sosa:hosts <Device/device2> ; " +
                "sosa:hosts <Device/device3> . ";
        when(client.updateDeployment(new DeploymentMetadata("deploymentUnit", context))).thenReturn(new DeploymentMetadata("deploymentUnit", context));


        DeploymentUnit actual = service.addDeviceToDeploymentUnit("deploymentUnit", "device3");

        Organization organization = new Organization("organization", "label");
        List<String> devices = new LinkedList<>();
        devices.add("device1");
        devices.add("device2");
        devices.add("device3");
        Platform platform = new Platform("platform", "label", devices);
        DeploymentUnit expected = new DeploymentUnit("deploymentUnit", "date", "area", organization, platform);

        HopAsserts.assertEqualsJson(expected, actual);
    }

    @Test
    public void deleteDeviceInDeploymentUnit() {
        String context2 = "@prefix prov: <http://www.w3.org/ns/prov#>. " +
                "@prefix ssn: <http://www.w3.org/ns/ssn/> . " +
                "@prefix iot: <http://inter-iot.eu/GOIoTP#> . " +
                "@prefix org: <http://www.w3.org/ns/org#> . " +
                "@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>. " +
                "@prefix sosa: <http://www.w3.org/ns/sosa/> . " +
                "<Organization/organization> a org:Organization ; " +
                "rdfs:label \"label\"@en . " +
                "<Deployment/deploymentUnit> a ssn:Deployment ; " +
                "prov:startedAtTime \"date\"^^xsd:date ; " +
                "iot:hasLocation \"area\"^^http://www.opengis.net/ont/geosparql#wktLiteral prov:wasAssociatedWith <Organization/organization> ; " +
                "ssn:deployedOnPlatform <Platform/platform> . " +
                "<Platform/platform> a sosa:Platform ; " +
                "rdfs:label \"label\"@en ; " +
                "sosa:hosts <Device/device1> ; " +
                "sosa:hosts <Device/device2> ; " +
                "sosa:hosts <Device/device3> . ";
        when(client.getDeploymentById("deploymentUnit")).thenReturn(new DeploymentMetadata("deploymentUnit", context2));

        String context = "@prefix prov: <http://www.w3.org/ns/prov#>. " +
                "@prefix ssn: <http://www.w3.org/ns/ssn/> . " +
                "@prefix iot: <http://inter-iot.eu/GOIoTP#> . " +
                "@prefix org: <http://www.w3.org/ns/org#> . " +
                "@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>. " +
                "@prefix sosa: <http://www.w3.org/ns/sosa/> . " +
                "<Organization/organization> a org:Organization ; " +
                "rdfs:label \"label\"@en . " +
                "<Deployment/deploymentUnit> a ssn:Deployment ; " +
                "prov:startedAtTime \"date\"^^xsd:date ; " +
                "iot:hasLocation \"area\"^^http://www.opengis.net/ont/geosparql#wktLiteral prov:wasAssociatedWith <Organization/organization> ; " +
                "ssn:deployedOnPlatform <Platform/platform> . " +
                "<Platform/platform> a sosa:Platform ; " +
                "rdfs:label \"label\"@en ; " +
                "sosa:hosts <Device/device1> ; " +
                "sosa:hosts <Device/device2> . ";
        when(client.updateDeployment(new DeploymentMetadata("deploymentUnit", context))).thenReturn(new DeploymentMetadata("deploymentUnit", context));

        DeploymentUnit actual = service.deleteDeviceInDeploymentUnit("deploymentUnit", "device3");

        Organization organization = new Organization("organization", "label");
        List<String> devices = new LinkedList<>();
        devices.add("device1");
        devices.add("device2");
        Platform platform = new Platform("platform", "label", devices);
        DeploymentUnit expected = new DeploymentUnit("deploymentUnit", "date", "area", organization, platform);

        HopAsserts.assertEqualsJson(expected, actual);
    }

    @Test
    public void getDeploymentUnitById() {
        String context = "@prefix prov: <http://www.w3.org/ns/prov#>. " +
                "@prefix ssn: <http://www.w3.org/ns/ssn/> . " +
                "@prefix iot: <http://inter-iot.eu/GOIoTP#> . " +
                "@prefix org: <http://www.w3.org/ns/org#> . " +
                "@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>. " +
                "@prefix sosa: <http://www.w3.org/ns/sosa/> . " +
                "<Organization/organization> a org:Organization ; " +
                "rdfs:label \"label\"@en . " +
                "<Deployment/deploymentUnit> a ssn:Deployment ; " +
                "prov:startedAtTime \"date\"^^xsd:date ; " +
                "iot:hasLocation \"area\"^^http://www.opengis.net/ont/geosparql#wktLiteral prov:wasAssociatedWith <Organization/organization> ; " +
                "ssn:deployedOnPlatform <Platform/platform> . " +
                "<Platform/platform> a sosa:Platform ; " +
                "rdfs:label \"label\"@en ; " +
                "sosa:hosts <Device/device1> ; " +
                "sosa:hosts <Device/device2> . ";
        when(client.getDeploymentById("deploymentUnit")).thenReturn(new DeploymentMetadata("deploymentUnit", context));

        DeploymentUnit actual = service.getDeploymentUnitById("deploymentUnit");

        Organization organization = new Organization("organization", "label");
        List<String> devices = new LinkedList<>();
        devices.add("device1");
        devices.add("device2");
        Platform platform = new Platform("platform", "label", devices);
        DeploymentUnit expected = new DeploymentUnit("deploymentUnit", "date", "area", organization, platform);

        HopAsserts.assertEqualsJson(expected, actual);
    }

    @Test
    public void deleteDeploymentUnit() {
        when(client.deleteDeployment("deploymentUnit")).thenReturn(true);
        when(client.deleteDeployment("nonExistingUnit")).thenReturn(false);

        Assert.assertTrue(service.deleteDeploymentUnit("deploymentUnit"));
        Assert.assertFalse(service.deleteDeploymentUnit("nonExistingUnit"));
    }
}
