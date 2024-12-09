package ar.com.l_airline.exceptionHandler;

import ar.com.l_airline.exceptionHandler.custom_exceptions.AccessDeniedException;
import ar.com.l_airline.exceptionHandler.custom_exceptions.ExistingObjectException;
import ar.com.l_airline.exceptionHandler.custom_exceptions.MissingDataException;
import ar.com.l_airline.exceptionHandler.custom_exceptions.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionAdvice {

    /**
     * Return a 409 code if an existing record with matching data is found in the database.
     *
     * @return 409, "This object already exists in the DataBase.".
     */
    @ExceptionHandler(value = ExistingObjectException.class)
    public ResponseEntity<ExceptionDTO> ExistingObjectExcHandler() {
        ExceptionDTO dto = ExceptionDTO.builder().message("This object already exists in the DataBase.")
                .code(HttpStatusCode.valueOf(409)).build();

        log.error("An error has occurred during User Persistence: the user already exists.");

        return new ResponseEntity<>(dto, dto.getCode());
    }

    /**
     * Return a 400 code if required data was not received.
     *
     * @return 400, "Insert all required information.".
     */
    @ExceptionHandler(value = MissingDataException.class)
    public ResponseEntity<ExceptionDTO> missingDataExcHandler() {
        ExceptionDTO dto = ExceptionDTO.builder().message("Insert all required information.")
                .code(HttpStatusCode.valueOf(400)).build();

        log.error("An error has occurred during User Persistence: missing information for registration.");

        return new ResponseEntity<>(dto, dto.getCode());
    }

    /**
     * Return a 404 code if it doesn't found a record with data matching in the database.
     *
     * @return 404, "Not found in the DataBase.".
     */
    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<ExceptionDTO> notFoundExcHandler() {
        ExceptionDTO dto = ExceptionDTO.builder().message("Not found in the DataBase.")
                .code(HttpStatusCode.valueOf(404)).build();

        log.error("An error has occurred during the return of information: record does not exists.");

        return new ResponseEntity<>(dto, dto.getCode());
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseEntity<ExceptionDTO> accessDeniedExcHandler() {
        ExceptionDTO dto = ExceptionDTO.builder()
                .code(HttpStatusCode.valueOf(403))
                .message("You has no permissions to access.").build();

        log.error("An error has occurred during authentication process: invalid user's credentials.");

        return new ResponseEntity<>(dto, dto.getCode());
    }
}
