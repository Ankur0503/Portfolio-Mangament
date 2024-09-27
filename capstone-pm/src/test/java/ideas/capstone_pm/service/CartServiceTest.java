package ideas.capstone_pm.service;

import ideas.capstone_pm.dto.AddToCartDTO;
import ideas.capstone_pm.dto.CartProjection;
import ideas.capstone_pm.entity.ApplicationUser;
import ideas.capstone_pm.entity.Cart;
import ideas.capstone_pm.entity.Fund;
import ideas.capstone_pm.entity.Transaction;
import ideas.capstone_pm.exception.cartexceptions.CartNotFoundException;
import ideas.capstone_pm.repository.CartRepository;
import ideas.capstone_pm.repository.TransactionRepository;
import ideas.capstone_pm.util.CartServiceUtils;
import ideas.capstone_pm.utils.MockUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {

    private static final String USER_NAME = "John Doe";
    private static final String USER_EMAIL = "johndoe@example.com";
    private static final String USER_PASSWORD = "password123";
    private static final String USER_PHONE = "1234567890";
    private static final int USER_AGE = 30;
    private static final String USER_ROLE = "USER";

    private static final int USER_ID = 1;

    @Mock
    private CartRepository cartRepository;
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private CartServiceUtils cartServiceUtils;
    @InjectMocks
    private CartService cartService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getCartsByUser() {
        ApplicationUser user = new ApplicationUser(1, "John Doe", "johndoe@example.com", "password123", "1234567890", 30, "USER", null, null);
        List<CartProjection> expectedCarts = MockUtils.mockCartProjectionList();
        when(cartRepository.findByUser(user)).thenReturn(expectedCarts);

        List<CartProjection> actualCarts = cartService.getCartsByUser(user);
        assertNotNull(actualCarts);
        assertEquals(expectedCarts, actualCarts);
    }

    @Test
    void shouldThrowCartNotFoundException() {
        ApplicationUser user = new ApplicationUser();
        when(cartRepository.findByUser(user)).thenThrow(new RuntimeException());

        assertThrows(CartNotFoundException.class, () -> {
            cartService.getCartsByUser(user);
        });
        verify(cartRepository, times(1)).findByUser(user);
    }

    @Test
    void addCart() {
        AddToCartDTO mockedAddTOCartDTO = buildAddToCartDTO();
        Cart mockedCart = buildCart();

        when(cartServiceUtils.buildCart(mockedAddTOCartDTO)).thenReturn(mockedCart);
        when(cartRepository.save(mockedCart)).thenReturn(mockedCart);

        Cart actualCart = cartService.addCart(mockedAddTOCartDTO);
        assertNotNull(actualCart);
        assertEquals(mockedCart, actualCart);
    }

    @Test
    void deleteCartByCartId() {
        Cart mockedCart = buildCart();
        when(cartRepository.findById(mockedCart.getCartId())).thenReturn(Optional.of(mockedCart));
        doNothing().when(cartRepository).deleteById(mockedCart.getCartId());

        cartService.deleteCartByCartId(mockedCart.getCartId());
        verify(cartRepository).deleteById(mockedCart.getCartId());
    }

    @Test
    void computeTotalAmountInCart() {
        ApplicationUser user = buildApplicationUser();
        List<CartProjection> mockedCartProjections = MockUtils.mockCartProjectionList();
        double expectedTotalAmountInCart = 5000.0;

        when(cartRepository.findByUser(user)).thenReturn(mockedCartProjections);

        double actualTotalAmountCart = cartService.computeTotalAmountInCart(user);
        assertEquals(expectedTotalAmountInCart, actualTotalAmountCart);
    }

    @Test
    void processCart() {
        List<CartProjection> mockedCartProjections = MockUtils.mockCartProjectionList();
        Transaction mockedTransaction = buildTransaction();
        ApplicationUser user = buildApplicationUser();

        when(transactionRepository.save(any(Transaction.class))).thenReturn(mockedTransaction);
        when(cartRepository.findByUser(user)).thenReturn(mockedCartProjections);
        doNothing().when(cartRepository).deleteByUser(user);

        cartService.processCart(user);

        verify(cartRepository).deleteByUser(user);
        verify(transactionRepository).save(any(Transaction.class));
    }

    private AddToCartDTO buildAddToCartDTO() {
        return new AddToCartDTO(buildFund(), buildApplicationUser(), 7000.0);
    }

    private Fund buildFund() {
        return new Fund(1, "Global Equity Fund", "XYZ Asset Management", "Medium", "Equity", 250.50, 95.75, "Alice Johnson", "A diversified global equity fund focusing on growth stocks.", 4.5, null, null, null);
    }

    private ApplicationUser buildApplicationUser() {
        return new ApplicationUser(USER_ID, USER_NAME, USER_EMAIL, USER_PASSWORD, USER_PHONE, USER_AGE, USER_ROLE, null, null);
    }

    private Cart buildCart() {
        return new Cart(1, buildFund(), buildApplicationUser(), 7000.0);
    }

    private Transaction buildTransaction() {
        return new Transaction(1, 1000.0, new Date(), null, null);
    }
}
