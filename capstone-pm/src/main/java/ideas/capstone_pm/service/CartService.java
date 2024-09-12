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
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private CartServiceUtils cartServiceUtils;

    public Cart addCart(AddToCartDTO addToCartDTO) {
        return cartRepository.save(cartServiceUtils.buildCart(addToCartDTO));
    }

    public List<CartProjection> getCartsByUser(ApplicationUser user) {
        return cartRepository.findByUser(user);
    }

    public void deleteCartByUserAndFund(DeleteCartDTO deleteCartDTO) {
        CartProjection cartProjection = cartRepository.findByFundAndUser(deleteCartDTO.getFund(), deleteCartDTO.getUser());
        cartRepository.deleteById(cartProjection.getCartId());
    }

    public double computeTotalAmountInCart(ApplicationUser user) {
        List<CartProjection> userCarts = cartRepository.findByUser(user);
        return userCarts.stream().mapToDouble(CartProjection::getPlannedInvestment).sum();
    }

    public void processCart(ApplicationUser user) {
        List<CartProjection> cartItems = cartRepository.findByUser(user);
        for (CartProjection cartItem: cartItems) {
            Transaction transaction = new Transaction();
            Fund fund = new Fund();
            fund.setFundId(cartItem.getFund().getFundId());

            transaction.setFund(fund);
            transaction.setUser(user);
            transaction.setTransactionInitialInvestment(cartItem.getPlannedInvestment());
            transaction.setTransactionDate(new Date());
            transaction.setTransactionDuration("3 Years");
            transactionRepository.save(transaction);
        }

        cartRepository.deleteByUser(user);
    }
}
