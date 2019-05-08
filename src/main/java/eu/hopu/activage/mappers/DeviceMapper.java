package eu.hopu.activage.mappers;

import eu.hopu.activage.metadataStorageClient.dto.DeviceMetadata;
import eu.hopu.activage.services.dto.Device;
import eu.hopu.activage.services.dto.Sensor;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeviceMapper {

    private static String DEVICE_REGEX = "<Device/(.*)> a .*:(.*) ; rdfs:label \"(.*)\"@(.)*";
    private static String SENSOR_REGEX = "<Sensor/(.*)> a act:(.*)";
    private final Pattern devicePattern;
    private final Pattern sensorPattern;

    public DeviceMapper() {
        devicePattern = Pattern.compile(DEVICE_REGEX);
        sensorPattern = Pattern.compile(SENSOR_REGEX);
    }

    public List<Device> metadataListToDeviceList(List<DeviceMetadata> deviceMetadatas) {
        List<Device> devices = new LinkedList<>();
        for (DeviceMetadata metadata : deviceMetadatas)
            devices.add(metadataToDevice(metadata));
        return devices;
    }

    public Device metadataToDevice(DeviceMetadata metadata) {
        String[] splitContext = metadata.getContext().split(" \\. ");
        Device device = null;
        for (String s : splitContext) {
            Matcher deviceMatcher = devicePattern.matcher(s);
            Matcher sensorMatcher = sensorPattern.matcher(s);
            if (deviceMatcher.matches())
                device = new Device(deviceMatcher.group(1), deviceMatcher.group(2), deviceMatcher.group(3), new LinkedList<>());
            else if (sensorMatcher.matches() && device != null)
                device.getSensors().add(new Sensor(sensorMatcher.group(1), sensorMatcher.group(2)));

        }

        return device;
    }

    public DeviceMetadata deviceToMetadata(Device device) {
        String context = "@prefix iotpex: <http://inter-iot.eu/GOIoTP#> . " +
                "@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> . " +
                "@prefix sosa: <http://www.w3.org/ns/sosa/> . " +
                "@prefix act: <http://www.semanticweb.org/activage/ontologies/2018/4/activage-core#> . " +
                "<Device/" + device.getId() + "> a iotpex:" + device.getLabel() + " ; " +
                "rdfs:label \"" + device.getType() + "\"@en";
        for (Sensor s: device.getSensors()) {
            context += " ; sosa:hosts <Sensor/" + s.getId() + ">";
        }
        for (Sensor s: device.getSensors()) {
            context += " . <Sensor/" + s.getId() + "> a act:" + s.getType();
        }
        context += " . ";
        DeviceMetadata dm = new DeviceMetadata(device.getId(), context);

        return dm;
    }

}
