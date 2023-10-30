package org.masonord.delivery.exception;

import org.masonord.delivery.config.PropertiesConfig;
import org.masonord.delivery.enums.EntityType;
import org.masonord.delivery.enums.ExceptionType;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.MessageFormat;
import java.util.Optional;

public class ExceptionHandler {

    private static PropertiesConfig propertiesConfig;

    @Autowired
    public ExceptionHandler(PropertiesConfig propertiesConfig) {
        ExceptionHandler.propertiesConfig = propertiesConfig;
    }


    public static RuntimeException throwException(String message, String... args) {
        return new RuntimeException(format(message, args));
    }

    public static RuntimeException throwException(EntityType entityType, ExceptionType exceptionType, String ...args) {
        String message = getMessage(entityType, exceptionType);
        return throwException(exceptionType, message, args);
    }

    public static RuntimeException throwException(EntityType entityType, ExceptionType exceptionType, String id, String ...args) {
        String message = getMessage(entityType, exceptionType).concat(".").concat(id);
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
        }
        return new RuntimeException(format(message, args));
    }

    private static String format(String message, String ...args) {
        Optional<String> content = Optional.ofNullable(propertiesConfig.getValue(message));
        if (content.isPresent())
            return MessageFormat.format(content.get(), (Object[]) args);

        return String.format(message, (Object[]) args);
    }


    private static String getMessage(EntityType entityType, ExceptionType exceptionType) {
        return entityType.name().concat(".").concat(exceptionType.getValue()).toLowerCase();
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

}
