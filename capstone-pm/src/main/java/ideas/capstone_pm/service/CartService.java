package ideas.capstone_pm.service;

import ideas.capstone_pm.dto.AddToCartDTO;
import ideas.capstone_pm.projection.CartProjection;
import ideas.capstone_pm.entity.ApplicationUser;
import ideas.capstone_pm.entity.Cart;
import ideas.capstone_pm.entity.Fund;
import ideas.capstone_pm.entity.Transaction;
import ideas.capstone_pm.exception.cartexceptions.CartNotFoundException;
import ideas.capstone_pm.repository.CartRepository;
import ideas.capstone_pm.repository.TransactionRepository;
import ideas.capstone_pm.util.CartServiceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public List<CartProjection> getCartsByUser(Integer userId) {
        try {
            ApplicationUser user = new ApplicationUser();
            user.setUserId(userId);
            return cartRepository.findByUser(user);
        } catch (Exception e) {
            throw new CartNotFoundException("Cart Not Found");
        }
    }

    public void deleteCartByCartId(Integer cartId) {
        Optional<Cart> cart = cartRepository.findById(cartId);
        if (cart.isPresent()) {
            cartRepository.deleteById(cartId);
        } else {
            throw new CartNotFoundException("Cart not found");
        }
    }

    public double computeTotalAmountInCart(ApplicationUser user) {
        List<CartProjection> userCarts = cartRepository.findByUser(user);
        return userCarts.stream()
                .mapToDouble(CartProjection::getPlannedInvestment)
                .sum();
    }

    public void processCart(ApplicationUser user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        List<CartProjection> cartItems = cartRepository.findByUser(user);
        if (cartItems.isEmpty()) {
            return;
        }

        try {
            List<Transaction> transactions = createTransactions(cartItems, user);
            transactionRepository.saveAll(transactions);
            cartRepository.deleteByUser(user);
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while processing the cart.", e);
        }
    }

    private List<Transaction> createTransactions(List<CartProjection> cartItems, ApplicationUser user) {
        return cartItems.stream().map(cartItem -> {
            Transaction transaction = new Transaction();
            Fund fund = new Fund();
            fund.setFundId(cartItem.getFund().getFundId());

            transaction.setFund(fund);
            transaction.setUser(user);
            transaction.setTransactionInitialInvestment(cartItem.getPlannedInvestment());
            transaction.setTransactionDate(new Date());
            return transaction;
        }).collect(Collectors.toList());
    }
}
