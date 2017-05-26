package io.parrot.exception.mapper;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import io.parrot.api.model.ErrorApi;
import io.parrot.exception.ParrotApiException;

@Provider
public class JaxrsExceptionMapper implements ExceptionMapper<RuntimeException> {

	@Override
	public Response toResponse(RuntimeException e) {
		ParrotApiException pae;
		ErrorApi error = new ErrorApi();
		if (e instanceof ParrotApiException) {
			pae = (ParrotApiException) e;
			error.setCode(pae.getStatusCodeResponse());
			error.setMessage(pae.getMessage());
		} else {
			error.setCode(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
			error.setMessage(e.getMessage());
		}
		return Response.serverError().entity(error).build();
	}

}