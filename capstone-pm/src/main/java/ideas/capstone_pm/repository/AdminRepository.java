package ideas.capstone_pm.repository;

import ideas.capstone_pm.projection.AdminProjection;
import ideas.capstone_pm.entity.Admin;
import org.springframework.data.repository.CrudRepository;

public interface AdminRepository extends CrudRepository<Admin, Integer> {
    Boolean existsByAdminEmail(String adminEmail);
    AdminProjection findByAdminEmail(String adminEmail);
}
