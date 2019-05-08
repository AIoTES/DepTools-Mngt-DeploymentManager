package eu.hopu.activage.mappers;

import eu.hopu.activage.metadataStorageClient.dto.DeploymentMetadata;
import eu.hopu.activage.services.dto.DeploymentUnit;
import eu.hopu.activage.services.dto.Organization;
import eu.hopu.activage.services.dto.Platform;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeploymentMapper {


    private static String DEPLOYMENT_REGEX = "<Deployment/(.*)> a .* prov:startedAtTime \"(.*)\"\\^\\^xsd:date ; iot:hasLocation \"(.*)\"\\^\\^.* prov:wasAssociatedWith <Organization/(.*)> ; ssn:deployedOnPlatform <Platform/(.*)>";
    private static String ORGANIZATION_REGEX = "<Organization/(.*)> a .*:.* ; rdfs:label \"(.*)\".*";
    private static String PLATFORM_REGEX = "<Platform/(.*)> a .* rdfs:label \"(.*)\"@en(( ; sosa:hosts <Device/[^>]+>)*)";
    private static String DEVICES_REGEX = "sosa:hosts <Device/([^>]+)>";
    private final Pattern deployPattern;
    private final Pattern organizationPattern;
    private final Pattern platformPattern;
    private final Pattern devicesPattern;

    public DeploymentMapper() {
        deployPattern = Pattern.compile(DEPLOYMENT_REGEX);
        organizationPattern = Pattern.compile(ORGANIZATION_REGEX);
        platformPattern = Pattern.compile(PLATFORM_REGEX);
        devicesPattern = Pattern.compile(DEVICES_REGEX);
    }

    public List<DeploymentUnit> metadataListToDeploymentList(List<DeploymentMetadata> deploymentMetadatas) {
        List<DeploymentUnit> deploymentUnits = new LinkedList<>();
        for (DeploymentMetadata metadata : deploymentMetadatas)
            deploymentUnits.add(metadaToDeployment(metadata));
        return deploymentUnits;
    }

    public DeploymentUnit metadaToDeployment(DeploymentMetadata metadata) {
        String[] splitContext = metadata.getContext().split(" \\. ");
        DeploymentUnit deployment = null;
        String organizationId = "";
        String platformId = "";

        Organization organization = null;
        Platform platform = null;

        for (String s : splitContext) {
            Matcher deployMatcher = deployPattern.matcher(s);
            Matcher organizationMatcher = organizationPattern.matcher(s);
            Matcher platformMatcher = platformPattern.matcher(s);
            if (deployMatcher.matches()) {
                Organization org = null;
                Platform plat = null;
                organizationId = deployMatcher.group(4);
                platformId = deployMatcher.group(5);
                if (organization != null && organization.getId().equals(organizationId))
                    org = organization;

                deployment = new DeploymentUnit(deployMatcher.group(1), deployMatcher.group(2), deployMatcher.group(3), org, plat);
            }
            else if (organizationMatcher.matches()) {
                organization = new Organization(organizationMatcher.group(1), organizationMatcher.group(2));
            }
            else if (platformMatcher.matches()) {
                if (platformId.equals(platformMatcher.group(1))) {

                    LinkedList<String> devices = new LinkedList<>();
                    if (platformMatcher.groupCount() > 3) {
                        String[] splitDevices = platformMatcher.group(3).split(" ; ");
                        for (String device: splitDevices) {
                            Matcher deviceMatcher = devicesPattern.matcher(device);
                            if (deviceMatcher.matches())
                                devices.add(deviceMatcher.group(1));

                        }
                    }
                    deployment.setPlatform(new Platform(platformMatcher.group(1), platformMatcher.group(2), devices));
                }
            }
        }
        return deployment;
    }

    public DeploymentMetadata deploymentToMetadata(DeploymentUnit deploymentUnit) {
        String context = "@prefix prov: <http://www.w3.org/ns/prov#> . " +
                "@prefix ssn: <http://www.w3.org/ns/ssn/> . " +
                "@prefix iot: <http://inter-iot.eu/GOIoTP#> . " +
                "@prefix org: <http://www.w3.org/ns/org#> . " +
                "@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> . " +
                "@prefix sosa: <http://www.w3.org/ns/sosa/> . " +
                "<Organization/" + deploymentUnit.getOrganization().getId() + "> a org:Organization ; " +
                "rdfs:label \"" + deploymentUnit.getOrganization().getLabel() +"\"@en . " +
                "<Deployment/" + deploymentUnit.getId() + "> a ssn:Deployment ; " +
                "prov:startedAtTime \"" + deploymentUnit.getDate() + "\"^^xsd:date ; " +
                "iot:hasLocation \"" + deploymentUnit.getLocation() + "\"^^http://www.opengis.net/ont/geosparql#wktLiteral prov:wasAssociatedWith <Organization/" + deploymentUnit.getOrganization().getId() + "> ; " +
                "ssn:deployedOnPlatform <Platform/" + deploymentUnit.getPlatform().getId() + "> . " +
                "<Platform/" + deploymentUnit.getPlatform().getId() + "> a sosa:Platform ; " +
                "rdfs:label \"" + deploymentUnit.getPlatform().getLabel() + "\"@en";
        for (String device: deploymentUnit.getPlatform().getDevices()) {
            context +=
                    " ; sosa:hosts <Device/" + device + ">";
        }
        context += " . ";

        DeploymentMetadata dm = new DeploymentMetadata(deploymentUnit.getId(), context);
        return dm;
    }
}
