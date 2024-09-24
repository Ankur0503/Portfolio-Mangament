package ideas.capstone_pm.service;

import ideas.capstone_pm.dto.FundReturnDTO;
import ideas.capstone_pm.dto.TransactionDTO;
import ideas.capstone_pm.dto.TransactionProjection;
import ideas.capstone_pm.dto.TransactionResponseDTO;
import ideas.capstone_pm.entity.ApplicationUser;
import ideas.capstone_pm.entity.Fund;
import ideas.capstone_pm.entity.Transaction;
import ideas.capstone_pm.repository.FundReturnRepository;
import ideas.capstone_pm.repository.TransactionRepository;
import ideas.capstone_pm.util.TransactionServiceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    FundReturnRepository fundReturnRepository;
    @Autowired
    TransactionServiceUtils transactionServiceUtils;

    public List<TransactionResponseDTO> getFundsByUser(ApplicationUser user) {
        List<TransactionProjection> transactionProjections = transactionRepository.findByUser(user);

        return transactionProjections.stream()
                .map(this::createTransactionResponseDTO)
                .collect(Collectors.toList());
    }

    public TransactionDTO getTransactionByUserAndFund(ApplicationUser user, Integer fundId) {
        Fund fund = new Fund();
        fund.setFundId(fundId);

        Transaction transaction = transactionRepository.findByUserAndFund(user, fund);

        return transactionServiceUtils.buildTransactionDTO(transaction);
    }

    public TransactionDTO addTransaction(Transaction transaction) {
        transaction.setTransactionDate(new Date());
        return transactionServiceUtils.buildTransactionDTO(transactionRepository.save(transaction));
    }

    private TransactionResponseDTO createTransactionResponseDTO(TransactionProjection transactionProjection) {
        Fund fund = new Fund();
        fund.setFundId(transactionProjection.getFund().getFundId());

        FundReturnDTO fundReturnDTO = fundReturnRepository.findByFund(fund);
        Double currentValue = calculateCurrentValue(transactionProjection.getTransactionInitialInvestment(), fundReturnDTO.getFundReturnTotal());

        TransactionResponseDTO transactionResponseDTO = new TransactionResponseDTO();
        transactionResponseDTO.setTransactionProjection(transactionProjection);
        transactionResponseDTO.setCurrentValue(currentValue);

        return transactionResponseDTO;
    }

    private Double calculateCurrentValue(Double initialInvestment, Double fundReturnTotal) {
        return initialInvestment * (1 + (fundReturnTotal / 100));
    }
}
