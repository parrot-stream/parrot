/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package io.parrot.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import io.parrot.api.model.ErrorApi;

public class ParrotApiException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private ResponseBuilder response;
    private ErrorApi error;

    public ParrotApiException(Status statusError, String pMessage) {
        super(pMessage);
        error = new ErrorApi();
        error.setErrorCode(statusError.getStatusCode());
        error.setMessage(pMessage);
    }

    public ParrotApiException(String pMessage) {
        super(pMessage);
        error = new ErrorApi();
        error.setErrorCode(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
        error.setMessage(pMessage);
    }

    public ParrotApiException(ErrorApi pError) {
        super(pError.getMessage());
        error = pError;
    }

    public ParrotApiException(ErrorApi pError, Throwable pThrowable) {
        super(pError.getMessage(), pThrowable);
        error = pError;
    }

    public ResponseBuilder getErrorResponse() {
        return response;
    }

    public ErrorApi getErrorApi() {
        return error;
    }

    public Status getStatusCodeResponse() {
        return Response.Status.fromStatusCode(error.getErrorCode());
    }
}
