package dev.alex.klepov.controller.exception.handling;

import java.sql.SQLException;
import java.util.Arrays;

import dev.alex.klepov.client.exception.OnlinesimClientException;
import dev.alex.klepov.controller.exception.BadParamException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.InvalidResultSetAccessException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


// Is better than ExceptionHandler, bc it's global, so you can have a centralized strategy for exception handling
// This also might be viewed as a downside, cause you it seems like can't configure same exception
// but in practice there are multiple ways to handle this
@ControllerAdvice
public class TestAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleDefault(Exception e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Something went wrong ¯\\_( ͡° ͜ʖ ͡°)_/¯"
                        + e.getMessage()
                        + Arrays.toString(e.getStackTrace()));
    }

    @ExceptionHandler
    public ResponseEntity<String> handleOnlinesimClientException(OnlinesimClientException e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error during executing request to onlinesim API: " + e.getMessage());
    }

    // it might (or might not) be better to process db level exceptions on other level
    @ExceptionHandler({
            DataAccessException.class,
            InvalidResultSetAccessException.class,
            SQLException.class})
    public ResponseEntity<String> handleDbLevelException(Exception e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error encountered while making request to persistence storage: "
                        + e.getMessage()
                        + Arrays.toString(e.getStackTrace()));
    }

    @ExceptionHandler()
    public ResponseEntity<String> handleBadParamException(BadParamException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Bad parameter found: " + e.getMessage());
    }
}
