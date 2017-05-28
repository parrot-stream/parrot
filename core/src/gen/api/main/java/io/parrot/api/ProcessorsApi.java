package io.parrot.api;

import io.parrot.api.model.*;

import io.swagger.annotations.ApiParam;
import io.swagger.jaxrs.*;

import io.parrot.api.model.ErrorApi;
import io.parrot.api.model.ParrotProcessorApi;
import io.parrot.api.model.ParrotProcessorClusterApi;
import io.parrot.api.model.ParrotProcessorNodeApi;
import io.parrot.api.model.ParrotProcessorsApi;

import java.util.List;

import java.io.InputStream;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.*;
import javax.validation.constraints.*;

@Path("/processors")

@Produces({ "application/json" })
@io.swagger.annotations.Api(description = "the processors API")

public interface ProcessorsApi  {
   
    @POST
    
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Parrot Processor creation", notes = "Adds a new Parrot Processor", response = ParrotProcessorApi.class, tags={  })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "The created Processor", response = ParrotProcessorApi.class),
        
        @io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error", response = ParrotProcessorApi.class) })
    public Response addProcessor(@ApiParam(value = "Processor definition" ,required=true) ParrotProcessorApi processor,@Context SecurityContext securityContext);
    @DELETE
    @Path("/{id}")
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Parrot Processor delete", notes = "Deletes a Parrot Processor", response = Void.class, tags={  })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "If the Parrot Processor had been successfully deleted", response = Void.class),
        
        @io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error", response = Void.class) })
    public Response deleteProcessor( @PathParam("id") String id,@Context SecurityContext securityContext);
    @GET
    @Path("/{id}")
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "A Parrot Processors", notes = "Gets the configuration info for a Parrot Processor", response = ParrotProcessorApi.class, tags={  })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "The configuration info for a Parrot Processor", response = ParrotProcessorApi.class),
        
        @io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error", response = ParrotProcessorApi.class) })
    public Response getProcessor( @PathParam("id") String id,@Context SecurityContext securityContext);
    @GET
    @Path("/{id}/cluster")
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Gets the Processor cluster current composition", notes = "Gets the Processor cluster current composition", response = ParrotProcessorClusterApi.class, tags={  })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "The Processor cluster composition", response = ParrotProcessorClusterApi.class),
        
        @io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error", response = ParrotProcessorClusterApi.class) })
    public Response getProcessorCluster( @PathParam("id") String id,@Context SecurityContext securityContext);
    @GET
    
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Parrot Processors' list", notes = "Lists all existing Parrot Processors", response = ParrotProcessorsApi.class, tags={  })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "The list of all existing Parrot Processors", response = ParrotProcessorsApi.class),
        
        @io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error", response = ParrotProcessorsApi.class) })
    public Response getProcessors(@Context SecurityContext securityContext);
    @POST
    @Path("/{id}/restart")
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Restarts a Parrot Processor", notes = "Restarts a Parrot Processor", response = ParrotProcessorApi.class, tags={  })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "The restarted Parrot Processor", response = ParrotProcessorApi.class),
        
        @io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error", response = ParrotProcessorApi.class) })
    public Response restartProcessor( @PathParam("id") String id,@Context SecurityContext securityContext);
    @POST
    @Path("/{id}/start")
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Starts a Parrot Processor", notes = "Starts a Parrot Processor", response = ParrotProcessorApi.class, tags={  })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "The started Parrot Processor", response = ParrotProcessorApi.class),
        
        @io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error", response = ParrotProcessorApi.class) })
    public Response startProcessor( @PathParam("id") String id,@Context SecurityContext securityContext);
    @POST
    @Path("/{id}/cluster/{node}/start")
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Starts a Parrot Processor node", notes = "Starts a Parrot Processor node", response = ParrotProcessorNodeApi.class, tags={  })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "The started Parrot Processor node", response = ParrotProcessorNodeApi.class),
        
        @io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error", response = ParrotProcessorNodeApi.class) })
    public Response startProcessorNode( @PathParam("id") String id, @PathParam("node") String node,@Context SecurityContext securityContext);
    @POST
    @Path("/{id}/stop")
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Stops a Parrot Processor", notes = "Stops a Parrot Processor", response = ParrotProcessorApi.class, tags={  })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "The stopped Parrot Processor", response = ParrotProcessorApi.class),
        
        @io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error", response = ParrotProcessorApi.class) })
    public Response stopProcessor( @PathParam("id") String id,@Context SecurityContext securityContext);
    @POST
    @Path("/{id}/cluster/{node}/stop")
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Stops a Parrot Processor node", notes = "Stops a Parrot Processor node", response = ParrotProcessorNodeApi.class, tags={  })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "The stopped Parrot Processor node", response = ParrotProcessorNodeApi.class),
        
        @io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error", response = ParrotProcessorNodeApi.class) })
    public Response stopProcessorNode( @PathParam("id") String id, @PathParam("node") String node,@Context SecurityContext securityContext);
    @PUT
    @Path("/{id}")
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Parrot Processor update", notes = "Updates a Parrot Processor", response = ParrotProcessorApi.class, tags={  })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "The updated Parrot Processor", response = ParrotProcessorApi.class),
        
        @io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error", response = ParrotProcessorApi.class) })
    public Response updateProcessor( @PathParam("id") String id,@ApiParam(value = "Processor definition" ,required=true) ParrotProcessorApi processor,@Context SecurityContext securityContext);
}
