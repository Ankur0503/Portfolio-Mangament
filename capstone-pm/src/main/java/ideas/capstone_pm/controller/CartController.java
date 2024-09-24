package ideas.capstone_pm.controller;

import ideas.capstone_pm.dto.AddToCartDTO;
import ideas.capstone_pm.dto.CartProjection;
import ideas.capstone_pm.dto.DeleteCartDTO;
import ideas.capstone_pm.entity.ApplicationUser;
import ideas.capstone_pm.entity.Cart;
import ideas.capstone_pm.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CartController {
    @Autowired
    private CartService cartService;

    @PostMapping("/user/cart")
    public Cart addCart(@RequestBody AddToCartDTO addToCartDTO) {
        return cartService.addCart(addToCartDTO);
    }

    @GetMapping("/user/cart")
    public List<CartProjection> getAllCarts(@RequestParam Integer userId) {
        ApplicationUser user = new ApplicationUser();
        user.setUserId(userId);
        return cartService.getCartsByUser(user);
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
        cartService.processCart(user);
    }
}
