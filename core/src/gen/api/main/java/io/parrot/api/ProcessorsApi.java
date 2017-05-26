package io.parrot.api;

import io.parrot.api.model.*;

import io.swagger.annotations.ApiParam;
import io.swagger.jaxrs.*;

import io.parrot.api.model.ErrorApi;
import io.parrot.api.model.ParrotProcessorApi;
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
    @io.swagger.annotations.ApiOperation(value = "Parrot Processor creation", notes = "Creates a new Parrot Processor", response = ParrotProcessorApi.class, tags={  })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "The created Processor", response = ParrotProcessorApi.class),
        
        @io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error", response = ParrotProcessorApi.class) })
    public Response createProcessor(@ApiParam(value = "Processor definition" ,required=true) ParrotProcessorApi processor,@Context SecurityContext securityContext);
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
    
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Parrot Processors' list", notes = "Lists all existing Parrot Processors", response = ParrotProcessorsApi.class, tags={  })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "The list of all existing Parrot Processors", response = ParrotProcessorsApi.class),
        
        @io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error", response = ParrotProcessorsApi.class) })
    public Response getProcessors(@Context SecurityContext securityContext);
    @PUT
    @Path("/{id}")
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Parrot Processor update", notes = "Updates a Parrot Processor", response = ParrotProcessorApi.class, tags={  })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "The updated Parrot Processor", response = ParrotProcessorApi.class),
        
        @io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error", response = ParrotProcessorApi.class) })
    public Response updateProcessor( @PathParam("id") String id,@ApiParam(value = "Processor definition" ,required=true) ParrotProcessorApi processor,@Context SecurityContext securityContext);
}
