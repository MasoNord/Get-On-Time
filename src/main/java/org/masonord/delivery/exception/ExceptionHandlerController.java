package org.masonord.delivery.exception;

import org.masonord.delivery.dto.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(org.masonord.delivery.exception.ExceptionHandler.EntityNotFoundException.class)
    public final ResponseEntity handledNotFoundExceptions(Exception exception, WebRequest request) {
        Response response = Response.notFoundException();
        response.addMessageToResponse(exception.getMessage(), exception);
        return new ResponseEntity(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(org.masonord.delivery.exception.ExceptionHandler.DuplicateEntityException.class)
    public final ResponseEntity handledDuplicatedException(Exception exception, WebRequest request) {
        Response response = Response.duplicateException();
        response.addMessageToResponse(exception.getMessage(), exception);
        return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(org.masonord.delivery.exception.ExceptionHandler.NotUuidFormatException.class)
    public final ResponseEntity handledNotUuidFormatException(Exception exception, WebRequest request) {
        Response response = Response.notUuidFormat();
        response.addMessageToResponse(exception.getMessage(), exception);
        return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(org.masonord.delivery.exception.ExceptionHandler.RangeNotSatisfiableException.class)
    public final ResponseEntity handlerNotSatisfiableException(Exception exception, WebRequest request) {
        Response response  = Response.requestedRangeNotSatisfiable();
        response.addMessageToResponse(exception.getMessage(), exception);
        return new ResponseEntity(response, HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE);
    }

    @ExceptionHandler(org.masonord.delivery.exception.ExceptionHandler.ConflictException.class)
    public final ResponseEntity handlerConflictException(Exception exception, WebRequest request) {
        Response response = Response.conflictException();
        response.addMessageToResponse(exception.getMessage(), exception);
        return new ResponseEntity(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(org.masonord.delivery.exception.ExceptionHandler.Exception.class)
    public final ResponseEntity handlerException(Exception exception, WebRequest request) {
        Response response = Response.exception();
        response.addMessageToResponse(exception.getMessage(), exception);
        return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
    }
}