package org.masonord.delivery.exception;

import org.masonord.delivery.dto.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandlerController.class);

    @ExceptionHandler(org.masonord.delivery.exception.ExceptionHandler.EntityNotFoundException.class)
    public final ResponseEntity handledNotFoundExceptions(Exception exception, WebRequest request) {
        LOGGER.error("Exception Caused By: ", exception);

        Response response = Response.notFoundException();
        response.addMessageToResponse(exception.getMessage(), exception);
        return new ResponseEntity(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(org.masonord.delivery.exception.ExceptionHandler.DuplicateEntityException.class)
    public final ResponseEntity handledDuplicatedException(Exception exception, WebRequest request) {
        LOGGER.error("Exception Caused By: ", exception);

        Response response = Response.duplicateException();
        response.addMessageToResponse(exception.getMessage(), exception);
        return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(org.masonord.delivery.exception.ExceptionHandler.NotUuidFormatException.class)
    public final ResponseEntity handledNotUuidFormatException(Exception exception, WebRequest request) {
        LOGGER.error("Exception Caused By: ", exception);

        Response response = Response.notUuidFormat();
        response.addMessageToResponse(exception.getMessage(), exception);
        return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(org.masonord.delivery.exception.ExceptionHandler.RangeNotSatisfiableException.class)
    public final ResponseEntity handlerNotSatisfiableException(Exception exception, WebRequest request) {
        LOGGER.error("Exception Caused By: ", exception);

        Response response  = Response.requestedRangeNotSatisfiable();
        response.addMessageToResponse(exception.getMessage(), exception);
        return new ResponseEntity(response, HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE);
    }

    @ExceptionHandler(org.masonord.delivery.exception.ExceptionHandler.ConflictException.class)
    public final ResponseEntity handlerConflictException(Exception exception, WebRequest request) {
        LOGGER.error("Exception Caused By: ", exception);

        Response response = Response.conflictException();
        response.addMessageToResponse(exception.getMessage(), exception);
        return new ResponseEntity(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(org.masonord.delivery.exception.ExceptionHandler.Exception.class)
    public final ResponseEntity handlerException(Exception exception, WebRequest request) {
        LOGGER.error("Exception Caused By: ", exception);

        Response response = Response.exception();
        response.addMessageToResponse(exception.getMessage(), exception);
        return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpServerErrorException.InternalServerError.class)
    public final ResponseEntity handledInternalServerError(Exception exception, WebRequest request) {
        LOGGER.error("Exception Caused By: ", exception);

        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}