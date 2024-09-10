package ideas.capstone_pm.repository;

import ideas.capstone_pm.dto.CartProjection;
import ideas.capstone_pm.dto.TotalCartAmountProjection;
import ideas.capstone_pm.entity.ApplicationUser;
import ideas.capstone_pm.entity.Cart;
import ideas.capstone_pm.entity.Fund;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CartRepository extends CrudRepository<Cart, Integer> {
    List<CartProjection> findByUser(ApplicationUser user);
    CartProjection findByFundAndUser(Fund fund, ApplicationUser user);
}
