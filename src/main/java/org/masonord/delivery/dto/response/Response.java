package org.masonord.delivery.dto.response;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.masonord.delivery.enums.StatusCode;
import org.masonord.delivery.util.DateUtils;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Response<T> {

    private StatusCode statusCode;
    private T payload;
    private Object errors;
    private Object metadata;

    public static <T> Response<T> ok() {
        Response<T> response = new Response<>();
        response.setStatusCode(StatusCode.OK);
        return response;
    }

    public static <T> Response<T> noContent() {
        Response<T> response = new Response<>();
        response.setStatusCode(StatusCode.NO_CONTENT);
        return response;
    }

    public static <T> Response<T> duplicateException() {
        Response<T> response = new Response<>();
        response.setStatusCode(StatusCode.BAD_REQUEST);
        return response;
    }

    public static <T> Response<T> wrongPasswordException() {
        Response<T> response = new Response<>();
        response.setStatusCode(StatusCode.BAD_REQUEST);
        return response;
    }

    public static <T> Response<T> notUuidFormat() {
        Response<T> response = new Response<>();
        response.setStatusCode(StatusCode.BAD_REQUEST);
        return response;
    }

    public static <T> Response<T> notFoundException() {
        Response<T> response = new Response<>();
        response.setStatusCode(StatusCode.NOT_FOUND);
        return response;
    }

    public void addMessageToResponse(String message, Exception ex) {
        ResponseError error = new ResponseError()
                .setMessage(ex.getMessage())
                .setDetails(message)
                .setDate(DateUtils.today());
        setErrors(error);
    }
}

