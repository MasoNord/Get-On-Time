package org.masonord.delivery.exception;

import org.masonord.delivery.config.PropertiesConfig;
import org.masonord.delivery.enums.ModelType;
import org.masonord.delivery.enums.ExceptionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Optional;

@Component
public class ExceptionHandler {
    private static Environment env;

    @Autowired
    public ExceptionHandler(Environment env) {
        this.env = env;
    }

    public RuntimeException throwExceptionNonStatic(ModelType modelType, ExceptionType exceptionType, String ...args) {
        String message = getMessage(modelType, exceptionType);
        return throwException(exceptionType, message, args);
    }

    public RuntimeException throwException(String message, String... args) {
        return new RuntimeException(format(message, args));
    }

    public RuntimeException throwException(ModelType modelType, ExceptionType exceptionType, String ...args) {
        String message = getMessage(modelType, exceptionType);
        return throwException(exceptionType, message, args);
    }

    public RuntimeException throwException(String model, ExceptionType exceptionType, String ...args) {
        String message = getMessage(model, exceptionType);
        return throwException(exceptionType, message, args);
    }

    public RuntimeException throwException(ModelType modelType, ExceptionType exceptionType, String id, String ...args) {
        String message = getMessage(modelType, exceptionType).concat(".").concat(id);
        return throwException(exceptionType, message, args);
    }

    // TODO: refactoring strictly necessary
    private RuntimeException throwException(ExceptionType exceptionType, String message, String ...args) {
        if (ExceptionType.ENTITY_NOT_FOUND.equals(exceptionType)) {
            return new EntityNotFoundException(format(message, args));
        }else if(ExceptionType.DUPLICATE_ENTITY.equals(exceptionType)) {
            return new DuplicateEntityException(format(message, args));
        }else if (ExceptionType.NOT_UUID_FORMAT.equals(exceptionType)){
            return new NotUuidFormatException(format(message, args));
        }else if (ExceptionType.WRONG_PASSWORD.equals(exceptionType)){
            return new WrongPasswordException(format(message, args));
        }else if (ExceptionType.RANGE_NOT_SATISFIABLE.equals(exceptionType)) {
            return new RangeNotSatisfiableException(format(message, args));
        }else if (ExceptionType.CONFLICT_EXCEPTION.equals(exceptionType)) {
            return new ConflictException(format(message, args));
        }else if (ExceptionType.ENTITY_EXCEPTION.equals(exceptionType)) {
            return new Exception(format(message, args));
        }else if (ExceptionType.LOCATION_NOT_SET.equals(exceptionType)) {
            return new LocationNotSetException(format(message, args));
        }
        return new RuntimeException(format(message, args));
    }

    private String format(String message, String ...args) {
        Optional<String> content = Optional.ofNullable(env.getProperty(message));
        return content.map(s -> MessageFormat.format(s, (Object[]) args)).orElseGet(() -> String.format(message, (Object[]) args));
    }

    private String getMessage(ModelType modelType, ExceptionType exceptionType) {
        return modelType.name().concat(".").concat(exceptionType.getValue()).toLowerCase();
    }

    private String getMessage(String model, ExceptionType exceptionType) {
        return model.concat(".").concat(exceptionType.getValue()).toLowerCase();
    }

    public class EntityNotFoundException extends RuntimeException {
        public EntityNotFoundException(String message) {
            super(message);
        }
    }

    public class DuplicateEntityException extends RuntimeException {
        public DuplicateEntityException(String message) {
            super(message);
        }
    }

    public  class NotUuidFormatException extends RuntimeException {
        public NotUuidFormatException(String message) {
            super(message);
        }
    }

    public  class WrongPasswordException extends RuntimeException {
        public WrongPasswordException(String message) {
            super(message);
        }
    }

    public  class RangeNotSatisfiableException extends RuntimeException {
        public RangeNotSatisfiableException(String message) {super(message);}
    }

    public  class ConflictException extends RuntimeException {
        public ConflictException(String message)  {
            super(message);
        }
    }

    public  class LocationNotSetException extends RuntimeException {
        public LocationNotSetException(String message) {super(message);}
    }

    public  class Exception extends RuntimeException {
        public Exception(String message) {super(message);}
    }

}

