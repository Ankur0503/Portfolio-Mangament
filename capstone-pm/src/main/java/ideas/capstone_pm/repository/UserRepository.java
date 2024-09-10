package ideas.capstone_pm.repository;

import ideas.capstone_pm.dto.UserDTO;
import ideas.capstone_pm.entity.ApplicationUser;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<ApplicationUser, Integer> {
    List<UserDTO> findBy();
    Boolean existsByUserEmail(String userEmail);
    UserDTO findByUserEmail(String userEmail);
}
