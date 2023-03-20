package zerobase.weather.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import zerobase.weather.dto.ErrorResponse;
import zerobase.weather.type.ErrorCode;

import javax.persistence.EntityNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger= LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(WeatherException.class)
    public ResponseEntity<ErrorResponse> handleWeatherException(WeatherException e){
        logger.error(e.getErrorMessage());

        return ResponseEntity.status(e.getErrorCode().getErrorStatus())
                .body(
                        ErrorResponse.builder()
                        .errorCode(e.getErrorCode())
                        .errorMessage(e.getErrorMessage())
                        .build()
                );
    }

}
