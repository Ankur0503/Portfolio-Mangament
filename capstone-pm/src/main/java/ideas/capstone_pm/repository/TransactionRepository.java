package ideas.capstone_pm.repository;

import ideas.capstone_pm.projection.TransactionProjection;
import ideas.capstone_pm.entity.ApplicationUser;
import ideas.capstone_pm.entity.Fund;
import ideas.capstone_pm.entity.Transaction;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TransactionRepository extends CrudRepository<Transaction, Integer> {
    List<TransactionProjection> findByUser(ApplicationUser user);
    List<TransactionProjection> findByFund(Fund fund);
    Transaction findByUserAndFund(ApplicationUser user, Fund fund);
}
