package io.parrot.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import io.parrot.api.model.ErrorApi;

@Provider
public class JaxrsExceptionMapper implements ExceptionMapper<RuntimeException> {

	@Override
	public Response toResponse(RuntimeException e) {
		ParrotApiException pae;
		if (e.getCause() != null && e.getCause() instanceof ParrotApiException) {
			pae = (ParrotApiException) e.getCause();
		} else if (e instanceof ParrotApiException) {
			pae = (ParrotApiException) e;
		} else {
			ErrorApi error = new ErrorApi();
			error.setErrorCode(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
			error.setMessage(e.getMessage());
			pae = new ParrotApiException(error, e);
		}
		return Response.status(pae.getStatusCodeResponse()).entity(pae.getErrorApi()).build();
	}

}