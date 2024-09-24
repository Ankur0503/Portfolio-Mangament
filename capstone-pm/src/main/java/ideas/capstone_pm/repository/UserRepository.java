package ideas.capstone_pm.repository;

import ideas.capstone_pm.dto.UserProjection;
import ideas.capstone_pm.entity.ApplicationUser;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<ApplicationUser, Integer> {
    List<UserProjection> findBy();
    Boolean existsByUserEmail(String userEmail);
    UserProjection findByUserEmail(String userEmail);
}
