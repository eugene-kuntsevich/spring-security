package oauth2.github.endpoint.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * @author Nikolay Bondarchuk
 * @since 2020-04-05
 */
public class UncaughtExceptionMapper implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable t) {
        return Response.serverError().entity(Error.of(t)).type(MediaType.APPLICATION_JSON_TYPE).build();
    }

    public static class Error {
        @JsonProperty
        private String message;

        @JsonProperty
        private String stacktrace;

        public String getMessage()
        {
            return message;
        }

        public void setMessage(String message)
        {
            this.message = message;
        }

        public String getStacktrace()
        {
            return stacktrace;
        }

        public void setStacktrace(String stacktrace)
        {
            this.stacktrace = stacktrace;
        }

        public static Error of(Throwable t) {
            Error error = new Error();
            error.setMessage(t.getMessage());
            error.setStacktrace(ExceptionUtils.getStackTrace(t));
            return error;
        }
    }
}
