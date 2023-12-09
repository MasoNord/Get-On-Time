package org.masonord.delivery.exception;

import org.masonord.delivery.config.PropertiesConfig;
import org.masonord.delivery.enums.ModelType;
import org.masonord.delivery.enums.ExceptionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Optional;

@Component
public class ExceptionHandler {

    private static PropertiesConfig propertiesConfig;

    @Autowired
    public ExceptionHandler(PropertiesConfig propertiesConfig) {
        ExceptionHandler.propertiesConfig = propertiesConfig;
    }

    public static RuntimeException throwException(String message, String... args) {
        return new RuntimeException(format(message, args));
    }

    public static RuntimeException throwException(ModelType modelType, ExceptionType exceptionType, String ...args) {
        String message = getMessage(modelType, exceptionType);
        return throwException(exceptionType, message, args);
    }

    public static RuntimeException throwException(String model, ExceptionType exceptionType, String ...args) {
        String message = getMessage(model, exceptionType);
        return throwException(exceptionType, message, args);
    }

    public static RuntimeException throwException(ModelType modelType, ExceptionType exceptionType, String id, String ...args) {
        String message = getMessage(modelType, exceptionType).concat(".").concat(id);
        return throwException(exceptionType, message, args);
    }

    private static RuntimeException throwException(ExceptionType exceptionType, String message, String ...args) {
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
        }
        return new RuntimeException(format(message, args));
    }

    private static String format(String message, String ...args) {
        Optional<String> content = Optional.ofNullable(propertiesConfig.getConfigValue(message));
        return content.map(s -> MessageFormat.format(s, (Object[]) args)).orElseGet(() -> String.format(message, (Object[]) args));
    }

    private static String getMessage(ModelType modelType, ExceptionType exceptionType) {
        return modelType.name().concat(".").concat(exceptionType.getValue()).toLowerCase();
    }

    private  static String getMessage(String model, ExceptionType exceptionType) {
        return model.concat(".").concat(exceptionType.getValue()).toLowerCase();
    }

    public static class EntityNotFoundException extends RuntimeException {
        public EntityNotFoundException(String message) {
            super(message);
        }
    }

    public static class DuplicateEntityException extends RuntimeException {
        public DuplicateEntityException(String message) {
            super(message);
        }
    }

    public static class NotUuidFormatException extends RuntimeException {
        public NotUuidFormatException(String message) {
            super(message);
        }
    }

    public static class WrongPasswordException extends RuntimeException {
        public WrongPasswordException(String message) {
            super(message);
        }
    }

    public static class RangeNotSatisfiableException extends RuntimeException {
        public RangeNotSatisfiableException(String message) {super(message);}
    }

    public static class ConflictException extends RuntimeException {
        public ConflictException(String message)  {
            super(message);
        }
    }

    public static class Exception extends RuntimeException {
        public Exception(String message) {super(message);}
    }

}

