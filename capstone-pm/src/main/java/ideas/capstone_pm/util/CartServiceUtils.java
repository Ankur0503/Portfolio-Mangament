package ideas.capstone_pm.util;

import ideas.capstone_pm.dto.AddToCartDTO;
import ideas.capstone_pm.entity.Cart;
import org.springframework.stereotype.Component;

@Component
public class CartServiceUtils {
    public Cart buildCart(AddToCartDTO addToCartDTO) {
        return new Cart(0, addToCartDTO.getFund(), addToCartDTO.getUser(), addToCartDTO.getPlannedInvestment());
    }
}
