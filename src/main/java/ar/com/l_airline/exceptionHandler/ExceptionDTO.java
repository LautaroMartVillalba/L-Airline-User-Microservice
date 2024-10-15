package ar.com.l_airline.exceptionHandler;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatusCode;

@Builder
@Data
public class ExceptionDTO {
    public String message;
    public HttpStatusCode code;
}
