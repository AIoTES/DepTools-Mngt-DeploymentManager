package eu.hopu.activage.servlets;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import eu.hopu.activage.Initializer;
import eu.hopu.activage.services.DeploymentManagerService;
import eu.hopu.activage.services.dto.DeploymentUnit;
import eu.hopu.activage.services.dto.Device;
import eu.hopu.activage.services.dto.Record;
import eu.hopu.activage.servlets.dto.DeploymentInstance;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.lang.reflect.Type;
import java.util.List;

@Path("deployments")
public class DeploymentServlet {
    private DeploymentManagerService deploymentManagerService;
    public DeploymentServlet() {
        deploymentManagerService = Initializer.service;
    }

    @GET
    @Produces({"application/json", "plain/text"})
    public Response listAllDeployments(@Context HttpServletRequest request) {
        List<DeploymentUnit> deployments = deploymentManagerService.listAllDeployments();
        Gson gson = new Gson();
        Type deploymentType = new TypeToken<List<DeploymentUnit>>(){}.getType();
        return Response.ok(gson.toJson(deployments, deploymentType), MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Consumes({"application/json"})
    @Produces({"application/json", "plain/text"})
    public Response createDeploymentUnit(@Context HttpServletRequest request, DeploymentInstance deploymentInstance) {
        DeploymentUnit deploymentUnit = deploymentManagerService.createDeploymentUnit(deploymentInstance.getId(), deploymentInstance.getDate(), deploymentInstance.getLocation(), deploymentInstance.getOrganization(), deploymentInstance.getPlatform());
        Gson gson = new Gson();
        if (deploymentUnit != null)
            return Response.ok(gson.toJson(deploymentUnit), MediaType.APPLICATION_JSON).build();
        return Response.serverError().build();
    }

    @GET
    @Produces({"application/json", "plain/text"})
    @Path("{deploymentId}")
    public Response getDeploymentUnitById(@Context HttpServletRequest request, @PathParam("deploymentId") String deploymentId) {
        DeploymentUnit deploymentUnit = deploymentManagerService.getDeploymentUnitById(deploymentId);
        Gson gson = new Gson();
        if (deploymentUnit != null)
            return Response.ok(gson.toJson(deploymentUnit), MediaType.APPLICATION_JSON).build();
        return Response.status(Response.Status.NOT_FOUND).entity("Deployment not found for ID: " + deploymentId).build();
    }

    @DELETE
    @Produces({"application/json", "plain/text"})
    @Path("{deploymentId}")
    public Response deleteDeploymentUnit(@Context HttpServletRequest request, @PathParam("deploymentId") String deploymentId) {
        boolean success = deploymentManagerService.deleteDeploymentUnit(deploymentId);
        if (success)
            return Response.ok().build();
        return Response.status(Response.Status.NOT_FOUND).entity("Deployment not found for ID: " + deploymentId).build();
    }

    @PUT
    @Produces({"application/json", "plain/text"})
    @Path("{deploymentId}/devices/{deviceId}")
    public Response addDeviceToDeploymentUnit(@Context HttpServletRequest request, @PathParam("deploymentId") String deploymentId, @PathParam("deviceId") String deviceId) {

        DeploymentUnit deploymentUnit = deploymentManagerService.addDeviceToDeploymentUnit(deploymentId, deviceId);
        Gson gson = new Gson();
        if (deploymentUnit != null)
            return Response.ok(gson.toJson(deploymentUnit), MediaType.APPLICATION_JSON).build();
        return Response.status(Response.Status.NOT_FOUND).entity("Deployment not found for ID: " + deploymentId).build();
    }

    @DELETE
    @Produces({"application/json", "plain/text"})
    @Path("{deploymentId}/devices/{deviceId}")
    public Response deleteDeviceInDeploymentUnit(@Context HttpServletRequest request, @PathParam("deploymentId") String deploymentId, @PathParam("deviceId") String deviceId) {
        DeploymentUnit deploymentUnit = deploymentManagerService.deleteDeviceInDeploymentUnit(deploymentId, deviceId);
        Gson gson = new Gson();
        if (deploymentUnit != null)
            return Response.ok(gson.toJson(deploymentUnit), MediaType.APPLICATION_JSON).build();
        return Response.status(Response.Status.NOT_FOUND).entity("Deployment not found for ID: " + deploymentId).build();
    }

    @GET
    @Produces({"application/json", "plain/text"})
    @Path("{deploymentId}/history")
    public Response getDeploymentUnitHistory(@Context HttpServletRequest request, @PathParam("deploymentId") String deploymentId) {
        List<Record> history = deploymentManagerService.getDeploymentUnitHistory(deploymentId);
        Type recordType = new TypeToken<List<Record>>(){}.getType();
        Gson gson = new Gson();
        if (history == null)
            return Response.status(Response.Status.NOT_FOUND).entity("Deployment not found for ID: " + deploymentId).build();
        return Response.ok(gson.toJson(history, recordType), MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Produces({"application/json", "plain/text"})
    @Path("devices")
    public Response listAllDevices(@Context HttpServletRequest request) {
        List<Device> deployments = deploymentManagerService.listAllDevices();
        Gson gson = new Gson();
        Type deviceType = new TypeToken<List<Device>>(){}.getType();
        return Response.ok(gson.toJson(deployments, deviceType), MediaType.APPLICATION_JSON).build();
    }

}
