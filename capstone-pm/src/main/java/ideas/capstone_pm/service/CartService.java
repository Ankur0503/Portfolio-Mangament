package ideas.capstone_pm.service;

import ideas.capstone_pm.dto.AddToCartDTO;
import ideas.capstone_pm.dto.CartProjection;
import ideas.capstone_pm.dto.DeleteCartDTO;
import ideas.capstone_pm.entity.ApplicationUser;
import ideas.capstone_pm.entity.Cart;
import ideas.capstone_pm.entity.Fund;
import ideas.capstone_pm.entity.Transaction;
import ideas.capstone_pm.repository.CartRepository;
import ideas.capstone_pm.repository.TransactionRepository;
import ideas.capstone_pm.util.CartServiceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private CartServiceUtils cartServiceUtils;

    public Cart addCart(AddToCartDTO addToCartDTO) {
        try {
            Cart cart = cartServiceUtils.buildCart(addToCartDTO);
            return cartRepository.save(cart);
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while adding the cart.");
        }
    }

    public List<CartProjection> getCartsByUser(ApplicationUser user) {
        try {
            return cartRepository.findByUser(user);
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while retrieving carts for the user.");
        }
    }

    public void deleteCartByCartId(Integer cartId) {
        try {
            Optional<Cart> cart = cartRepository.findById(cartId);
            if (cart.isPresent()) {
                cartRepository.deleteById(cartId);
            } else {
                throw new RuntimeException("Cart not found with ID: " + cartId);
            }
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while deleting the cart.");
        }
    }

    public double computeTotalAmountInCart(ApplicationUser user) {
        try {
            List<CartProjection> userCarts = cartRepository.findByUser(user);
            return userCarts.stream().mapToDouble(CartProjection::getPlannedInvestment).sum();
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while computing the total amount in the cart.");
        }
    }


    public void processCart(ApplicationUser user) {
        try {
            List<CartProjection> cartItems = cartRepository.findByUser(user);

            for (CartProjection cartItem : cartItems) {
                Transaction transaction = new Transaction();
                Fund fund = new Fund();
                fund.setFundId(cartItem.getFund().getFundId());

                transaction.setFund(fund);
                transaction.setUser(user);
                transaction.setTransactionInitialInvestment(cartItem.getPlannedInvestment());
                transaction.setTransactionDate(new Date());
                transactionRepository.save(transaction);
            }

            cartRepository.deleteByUser(user);
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while processing the cart.");
        }
    }

}
