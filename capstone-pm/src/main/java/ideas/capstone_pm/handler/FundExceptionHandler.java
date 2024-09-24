package ideas.capstone_pm.handler;

import ideas.capstone_pm.exception.fundexceptions.FilterNotFoundException;
import ideas.capstone_pm.exception.fundexceptions.FundNotFoundException;
import ideas.capstone_pm.exception.fundexceptions.InvalidArguments;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class FundExceptionHandler {
    @ExceptionHandler(FundNotFoundException.class)
    public ResponseEntity<String> handleFundNotFound(FundNotFoundException fundNotFoundException) {
        return new ResponseEntity<>("Fund Not Exists", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidArguments.class)
    public ResponseEntity<String> handleInvalidArguments(InvalidArguments invalidArguments) {
        return new ResponseEntity<>("Illegal Arguments passed", HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(FilterNotFoundException.class)
    public ResponseEntity<String> handleFilterNotFound(FilterNotFoundException filterNotFoundException) {
        return new ResponseEntity<>("Filter Not Found", HttpStatus.NOT_FOUND);
    }
}
