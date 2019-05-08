package eu.hopu.activage.mappers;

import eu.hopu.activage.metadataStorageClient.dto.DeviceMetadata;
import eu.hopu.activage.services.dto.Device;
import eu.hopu.activage.services.dto.Sensor;
import eu.hopu.activage.utils.HopAsserts;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

public class DeviceMapperTest {

    private DeviceMapper mapper;
    private DeviceMetadata deviceMetadata;
    private Device device;

    @Before
    public void setUp() {
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

        List<Sensor> sensors = new LinkedList<>();
        sensors.add(new Sensor("1_1", "IlluminanceSensor"));
        sensors.add(new Sensor("1_2", "TemperatureSensor"));
        sensors.add(new Sensor("1_3", "UserOccupancySensor"));
        device = new Device("1_1", "IoTDevice", "Fibaro motion sensor", sensors);

        deviceMetadata = new DeviceMetadata(device.getId(), context);

        mapper = new DeviceMapper();
    }

    @Test
    public void metadataToDevice() {
        Device actual = mapper.metadataToDevice(deviceMetadata);
        HopAsserts.assertEqualsJson(device, actual);
    }

    @Test
    public void deviceToMetadata() {
        DeviceMetadata actual = mapper.deviceToMetadata(device);
        HopAsserts.assertEqualsJson(deviceMetadata, actual);
    }

}
