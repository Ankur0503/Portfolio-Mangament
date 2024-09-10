package ideas.capstone_pm.service;

import ideas.capstone_pm.dto.TransactionDTO;
import ideas.capstone_pm.dto.TransactionProjection;
import ideas.capstone_pm.entity.ApplicationUser;
import ideas.capstone_pm.entity.Fund;
import ideas.capstone_pm.entity.Transaction;
import ideas.capstone_pm.repository.TransactionRepository;
import ideas.capstone_pm.util.TransactionServiceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    TransactionServiceUtils transactionServiceUtils;

    public List<TransactionProjection> getFundsByUser(ApplicationUser user) {
        return transactionRepository.findByUser(user);
    }

    public TransactionDTO getTransactionByUserAndFund(ApplicationUser user, Integer fundId) {
        Fund fund = new Fund();
        fund.setFundId(fundId);

        Transaction transaction = transactionRepository.findByUserAndFund(user, fund);

        return transactionServiceUtils.buildTransactionDTO(transaction);
    }

    public TransactionDTO addTransaction(Transaction transaction) {
        return transactionServiceUtils.buildTransactionDTO(transactionRepository.save(transaction));
    }
}
