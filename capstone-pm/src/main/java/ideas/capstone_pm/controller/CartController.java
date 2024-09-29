package ideas.capstone_pm.controller;

import ideas.capstone_pm.dto.AddToCartDTO;
import ideas.capstone_pm.exception.fundexceptions.FundNotFoundException;
import ideas.capstone_pm.exception.userexpcetions.EmailNotFound;
import ideas.capstone_pm.projection.CartProjection;
import ideas.capstone_pm.entity.ApplicationUser;
import ideas.capstone_pm.entity.Cart;
import ideas.capstone_pm.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping
    public ResponseEntity<Cart> addCart(@RequestBody AddToCartDTO addToCartDTO) {
        if (addToCartDTO.getFund() == null) {
            throw new FundNotFoundException();
        }
        if (addToCartDTO.getUser() == null) {
            throw new EmailNotFound();
        }
        Cart newCart = cartService.addCart(addToCartDTO);
        return new ResponseEntity<>(newCart, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CartProjection>> getAllCarts(@RequestParam Integer userId) {
        List<CartProjection> carts = cartService.getCartsByUser(userId);
        return new ResponseEntity<>(carts, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteCartByCartId(@RequestParam Integer cartId) {
        cartService.deleteCartByCartId(cartId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/total")
    public ResponseEntity<Double> computeTotalAmountInCart(@RequestBody ApplicationUser user) {
        if (user == null) {
            throw new EmailNotFound();
        }
        double totalAmount = cartService.computeTotalAmountInCart(user);
        return new ResponseEntity<>(totalAmount, HttpStatus.OK);
    }

    @PostMapping("/process")
    public ResponseEntity<Void> proceedToPay(@RequestBody ApplicationUser user) {
        if (user == null) {
            throw new EmailNotFound();
        }
        cartService.processCart(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
