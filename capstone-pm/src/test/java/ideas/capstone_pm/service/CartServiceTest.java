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
import ideas.capstone_pm.utils.MockUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Slf4j
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
        when(cartRepository.findByUser(any(ApplicationUser.class))).thenReturn(expectedCarts);

        List<CartProjection> actualCarts = cartService.getCartsByUser(1);
        assertNotNull(actualCarts);
        assertEquals(expectedCarts, actualCarts);
    }

    @Test
    public void shouldThrowCartNotFoundExceptionWhenErrorOccurs() {
        // Given
        int userId = 1;
        ApplicationUser user = new ApplicationUser();
        user.setUserId(userId);

        when(cartRepository.findByUser(user)).thenThrow(new RuntimeException("Database error"));

        assertThrows(CartNotFoundException.class, () -> cartService.getCartsByUser(userId));
    }

    @Test
    public void shouldAddCartSuccessfully() {
        AddToCartDTO addToCartDTO = new AddToCartDTO();
        Cart expectedCart = new Cart();
        when(cartServiceUtils.buildCart(addToCartDTO)).thenReturn(expectedCart);
        when(cartRepository.save(expectedCart)).thenReturn(expectedCart);

        Cart result = cartService.addCart(addToCartDTO);
        assertEquals(expectedCart, result);
    }

    @Test
    public void shouldThrowRuntimeExceptionWhenAddingCartFails() {
        AddToCartDTO addToCartDTO = new AddToCartDTO();
        when(cartServiceUtils.buildCart(addToCartDTO)).thenThrow(new RuntimeException("Error while building cart"));

        RuntimeException exception = org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class, () -> {
            cartService.addCart(addToCartDTO);
        });
        assertEquals("An error occurred while adding the cart.", exception.getMessage());
    }

    @Test
    public void shouldDeleteCartSuccessfullyWhenCartExists() {
        Integer cartId = 1;
        Cart cart = new Cart();
        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));

        cartService.deleteCartByCartId(cartId);
        verify(cartRepository, times(1)).deleteById(cartId);
    }

    @Test
    public void shouldThrowRuntimeExceptionWhenCartNotFound() {
        Integer cartId = 1;
        when(cartRepository.findById(cartId)).thenReturn(Optional.empty());
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            cartService.deleteCartByCartId(cartId);
        });

        assertEquals("Cart not found", exception.getMessage());
    }

    @Test
    void shouldComputeTotalAmountInCartWithMultipleCarts() {
        ApplicationUser user = buildApplicationUser();
        List<CartProjection> mockedCartProjections = MockUtils.mockCartProjectionList();
        double expectedTotalAmountInCart = 5000.0;

        when(cartRepository.findByUser(user)).thenReturn(mockedCartProjections);

        double actualTotalAmountCart = cartService.computeTotalAmountInCart(user);
        assertEquals(expectedTotalAmountInCart, actualTotalAmountCart);
    }

    @Test
    void shouldReturnZeroWhenUserHasNoCarts() {
        ApplicationUser user = buildApplicationUser();
        when(cartRepository.findByUser(user)).thenReturn(Collections.emptyList());

        double actualTotalAmount = cartService.computeTotalAmountInCart(user);
        assertEquals(0.0, actualTotalAmount);
    }

    @Test
    void shouldDoNothingWhenCartIsEmpty() {
        ApplicationUser user = new ApplicationUser();
        when(cartRepository.findByUser(user)).thenReturn(Collections.emptyList());

        cartService.processCart(user);

        verify(cartRepository).findByUser(user);
        verify(transactionRepository, never()).saveAll(any());
        verify(cartRepository, never()).deleteByUser(any());
    }

    @Test
    void shouldSaveTransactionsAndDeleteCart_WhenCartHasMultipleItems() {
        ApplicationUser user = new ApplicationUser();
        List<CartProjection> mockedCartProjections = MockUtils.mockCartProjectionList();

        when(cartRepository.findByUser(user)).thenReturn(mockedCartProjections);

        cartService.processCart(user);

        verify(transactionRepository).saveAll(any(List.class));
        verify(cartRepository).deleteByUser(user);
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
