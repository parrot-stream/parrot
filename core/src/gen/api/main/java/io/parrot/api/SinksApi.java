package io.parrot.api;

import io.parrot.api.model.*;

import io.swagger.annotations.ApiParam;
import io.swagger.jaxrs.*;

import io.parrot.api.model.ErrorApi;
import io.parrot.api.model.ParrotSinkApi;
import io.parrot.api.model.ParrotSinksApi;

import java.util.List;

import java.io.InputStream;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.*;
import javax.validation.constraints.*;

@Path("/sinks")

@Produces({ "application/json" })
@io.swagger.annotations.Api(description = "the sinks API")

public interface SinksApi  {
   
    @POST
    
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Parrot Sink creation", notes = "Adds a new Parrot Sink", response = ParrotSinkApi.class, tags={  })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "The created Sink", response = ParrotSinkApi.class),
        
        @io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error", response = ParrotSinkApi.class) })
    public Response addSink(@ApiParam(value = "Sink definition" ,required=true) ParrotSinkApi sink,@Context SecurityContext securityContext);
    @GET
    @Path("/{id}")
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "A Parrot Sink", notes = "Gets the configuration info for a Parrot Sink", response = ParrotSinkApi.class, tags={  })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "The configuration info for a Parrot Sink", response = ParrotSinkApi.class),
        
        @io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error", response = ParrotSinkApi.class) })
    public Response getSink( @PathParam("id") String id,@Context SecurityContext securityContext);
    @GET
    
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Parrot Sinks' list", notes = "Lists all existing Parrot Sinks", response = ParrotSinksApi.class, tags={  })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "The list of all existing Parrot Sinks", response = ParrotSinksApi.class),
        
        @io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error", response = ParrotSinksApi.class) })
    public Response getSinks(@Context SecurityContext securityContext);
    @PUT
    @Path("/{id}")
    
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Parrot Sink update", notes = "Updates an existing Sink", response = ParrotSinkApi.class, tags={ "galeve", })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "The updated Sink", response = ParrotSinkApi.class),
        
        @io.swagger.annotations.ApiResponse(code = 200, message = "Unexpected error", response = ParrotSinkApi.class) })
    public Response updateSink( @PathParam("id") String id,@ApiParam(value = "Sink definition" ,required=true) ParrotSinkApi sink,@Context SecurityContext securityContext);
}
