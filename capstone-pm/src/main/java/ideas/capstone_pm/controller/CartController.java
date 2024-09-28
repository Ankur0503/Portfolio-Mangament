package ideas.capstone_pm.controller;

import ideas.capstone_pm.dto.AddToCartDTO;
import ideas.capstone_pm.exception.fundexceptions.FundNotFoundException;
import ideas.capstone_pm.exception.userexpcetions.EmailNotFound;
import ideas.capstone_pm.projection.CartProjection;
import ideas.capstone_pm.entity.ApplicationUser;
import ideas.capstone_pm.entity.Cart;
import ideas.capstone_pm.service.CartService;
import jakarta.validation.constraints.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CartController {
    @Autowired
    private CartService cartService;

    @PostMapping("/user/cart")
    public Cart addCart(@RequestBody AddToCartDTO addToCartDTO) {
        if(addToCartDTO.getFund() == null) {
            throw new FundNotFoundException();
        }
        if(addToCartDTO.getUser() == null) {
            throw new EmailNotFound();
        }
        return cartService.addCart(addToCartDTO);
    }

    @GetMapping("/user/cart")
    public List<CartProjection> getAllCarts(@RequestParam Integer userId) {
        return cartService.getCartsByUser(userId);
    }

    @DeleteMapping("/user/cart")
    public void deleteCartByCartId(@RequestParam Integer cartId) {
        cartService.deleteCartByCartId(cartId);
    }

    @GetMapping("/user/cart/total")
    public double computeTotalAmountInCart(@RequestBody ApplicationUser user) {
        return cartService.computeTotalAmountInCart(user);
    }

    @PostMapping("/user/cart/process")
    public void proceedToPay(@RequestBody ApplicationUser user) {
        if(user == null) {
            throw new EmailNotFound();
        }
        cartService.processCart(user);
    }
}
