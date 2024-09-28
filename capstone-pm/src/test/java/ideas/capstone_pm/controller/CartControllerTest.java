package ideas.capstone_pm.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ideas.capstone_pm.CapstonePmApplication;
import ideas.capstone_pm.dto.AddToCartDTO;
import ideas.capstone_pm.projection.CartProjection;
import ideas.capstone_pm.entity.ApplicationUser;
import ideas.capstone_pm.entity.Cart;
import ideas.capstone_pm.entity.Fund;
import ideas.capstone_pm.service.ApplicationUserDetailsService;
import ideas.capstone_pm.service.CartService;
import ideas.capstone_pm.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

@SpringBootTest(classes = CapstonePmApplication.class)
@AutoConfigureMockMvc
public class CartControllerTest {
    @MockBean
    CartService cartService;
    @MockBean
    ApplicationUserDetailsService applicationUserDetailsService;
    @MockBean
    JwtUtil jwtUtil;

    String header;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("abc@gmail.com");

        when(applicationUserDetailsService.loadUserByUsername("abc@gmail.com")).thenReturn(userDetails);

        when(jwtUtil.generateToken(userDetails)).thenReturn("mockJwtToken");
        header = "Bearer mockJwtToken";
    }

    @WithMockUser(username = "test@gmail.com", roles = {"USER"})
    @Test
    void shouldAddCart() throws Exception {
        AddToCartDTO addToCartDTO = buildAddToCartDTO();
        Cart cart = buildCart();

        when(cartService.addCart(any(AddToCartDTO.class))).thenReturn(cart);

        mockMvc.perform(post("/user/cart")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", header)
                .content(new ObjectMapper().writeValueAsString(addToCartDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cartId").value(1))
                .andExpect(jsonPath("$.plannedInvestment").value(1000.0));
    }

    @WithMockUser(username = "test@gmail.com", roles = {"USER"})
    @Test
    public void shouldGetAllCartsByUser() throws Exception {
        Integer userId = 1;

        CartProjection cartProjection = buildCartProjection();
        when(cartService.getCartsByUser(userId)).thenReturn(List.of(cartProjection));

        mockMvc.perform(get("/user/cart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer your_token") // Replace with actual token
                        .param("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].plannedInvestment").value(2000.0))
                .andExpect(jsonPath("$[0].cartId").value(1));

        verify(cartService, times(1)).getCartsByUser(userId);
    }

    @WithMockUser(username = "test@gmail.com", roles = {"USER"})
    @Test
    void shouldDeleteCartByCartId() throws Exception {
        Integer cartId = 1;

        doNothing().when(cartService).deleteCartByCartId(1);

        mockMvc.perform(delete("/user/cart")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", header)
                .param("cartId", "1"))
                .andExpect(status().isOk());
    }

    @WithMockUser(username = "test@gmail.com", roles = {"USER"})
    @Test
    void shouldComputeTotalAmountInCart() throws Exception {
        ApplicationUser user = buildApplicationUser();
        double expectedValue = 3420.0;
        when(cartService.computeTotalAmountInCart(any(ApplicationUser.class))).thenReturn(expectedValue);

        mockMvc.perform(get("/user/cart/total")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", header)
                .content(new ObjectMapper().writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(expectedValue));
    }

    @WithMockUser(username = "test@gmail.com", roles = {"USER"})
    @Test
    void shouldProceedToPay() throws Exception {
        ApplicationUser user = buildApplicationUser();
        doNothing().when(cartService).processCart(user);

        mockMvc.perform(post("/user/cart/process")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", header)
                .content(new ObjectMapper().writeValueAsString(user)))
                .andExpect(status().isOk());
    }

    private ApplicationUser buildApplicationUser() {
        return new ApplicationUser(1, "testUser", "test@gmail.com", "Test@12345", "0987654321", 30, "USER", null, null);
    }

    private AddToCartDTO buildAddToCartDTO() {
        return new AddToCartDTO(new Fund(), new ApplicationUser(), 1000.0);
    }

    private Cart buildCart() {
        return new Cart(1, new Fund(), new ApplicationUser(), 1000.0);
    }

    private CartProjection buildCartProjection() {
        return new CartProjection() {
            @Override
            public Integer getCartId() {
                return 1;
            }

            @Override
            public Double getPlannedInvestment() {
                return 2000.0;
            }

            @Override
            public CartItemsProjection getFund() {
                return null;
            }
        };
    }
}
