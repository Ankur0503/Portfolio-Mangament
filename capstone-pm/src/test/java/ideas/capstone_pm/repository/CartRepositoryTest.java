package ideas.capstone_pm.repository;

import ideas.capstone_pm.dto.CartProjection;
import ideas.capstone_pm.entity.ApplicationUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class CartRepositoryTest {
    @Autowired
    private CartRepository cartRepository;

    @Test
    void shouldFindCartByUser() {
        ApplicationUser user = new ApplicationUser();
        user.setUserId(352);
        List<CartProjection> carts = cartRepository.findByUser(user);

        assertNotNull(carts);
    }
}
