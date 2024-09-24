package ideas.capstone_pm.handler;

import ideas.capstone_pm.exception.cartexceptions.CartNotFoundException;
import ideas.capstone_pm.exception.cartexceptions.PaymentProcessingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CartExceptionHandler {
    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<String> handleCartNotFound(CartNotFoundException cartNotFoundException) {
        return new ResponseEntity<>("Cart Not Found", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PaymentProcessingException.class)
    public ResponseEntity<String> handlePaymentProcessing(PaymentProcessingException paymentProcessingException) {
        return new ResponseEntity<>("Payment Processing Error", HttpStatus.BAD_REQUEST);
    }
}
