package ideas.capstone_pm.handler;

import ideas.capstone_pm.exception.userexpcetions.EmailAlreadyRegisteredException;
import ideas.capstone_pm.exception.userexpcetions.EmailNotFound;
import ideas.capstone_pm.exception.userexpcetions.InvalidCredentialException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UserExceptionHandler {

    @ExceptionHandler(EmailAlreadyRegisteredException.class)
    public ResponseEntity<String> handleEmailAlreadyExists(EmailAlreadyRegisteredException emailAlreadyRegisteredException) {
        return new ResponseEntity<>("Email Already Exists", HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidCredentialException.class)
    public ResponseEntity<String> handleInvalidCredentials(InvalidCredentialException invalidCredentialException) {
        return new ResponseEntity<>("Invalid Credentials", HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(EmailNotFound.class)
    public ResponseEntity<String> handleEmailNotFound(EmailNotFound emailNotFound) {
        return new ResponseEntity<>("Email Not Found", HttpStatus.NOT_FOUND);
    }
}
