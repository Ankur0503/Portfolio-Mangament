package ideas.capstone_pm.handler;

import ideas.capstone_pm.exception.userexpcetions.TokenExpiredException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApiLevelHandler {

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<String> handleEmployeeNotFoundException(TokenExpiredException exception) {
        return new ResponseEntity<>("Token Expired" + exception.getMessage(), HttpStatus.NOT_FOUND);
    }
}