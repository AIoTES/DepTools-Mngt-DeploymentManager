package eu.hopu.activage.mappers;

import eu.hopu.activage.metadataStorageClient.dto.DeploymentMetadata;
import eu.hopu.activage.services.dto.DeploymentUnit;
import eu.hopu.activage.services.dto.Organization;
import eu.hopu.activage.services.dto.Platform;
import eu.hopu.activage.utils.HopAsserts;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

public class DeploymentMapperTest {

    private DeploymentMapper mapper;
    private DeploymentUnit deploymentUnit;
    private DeploymentMetadata deploymentMetadata;

    @Before
    public void setUp() {
        List<String> devices = new LinkedList<>();
        devices.add("1_1");
        devices.add("1_2");
        deploymentUnit = new DeploymentUnit("PX_HOME1", "2017-06-06",
                "AREA[“Thessaloniki\"]",
                new Organization("1", "Municipality of Thessaloniki."),
                new Platform("1", "Activage Platform GR 1", devices)
        );

        String context = "@prefix prov: <http://www.w3.org/ns/prov#> . " +
                "@prefix ssn: <http://www.w3.org/ns/ssn/> . " +
                "@prefix iot: <http://inter-iot.eu/GOIoTP#> . " +
                "@prefix org: <http://www.w3.org/ns/org#> . " +
                "@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> . " +
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

        deploymentMetadata = new DeploymentMetadata(deploymentUnit.getId(), context);

        mapper = new DeploymentMapper();
    }

    @Test
    public void metadataToDeployment() {
        DeploymentUnit actual = mapper.metadaToDeployment(deploymentMetadata);
        HopAsserts.assertEqualsJson(deploymentUnit, actual);
    }

    @Test
    public void deploymentToMetadata() {
        DeploymentMetadata actual = mapper.deploymentToMetadata(deploymentUnit);
        HopAsserts.assertEqualsJson(deploymentMetadata, actual);
    }

}
