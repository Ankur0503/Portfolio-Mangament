package ideas.capstone_pm.service;

import ideas.capstone_pm.projection.FundReturnProjection;
import ideas.capstone_pm.dto.TransactionDTO;
import ideas.capstone_pm.projection.TransactionProjection;
import ideas.capstone_pm.dto.TransactionResponseDTO;
import ideas.capstone_pm.entity.ApplicationUser;
import ideas.capstone_pm.entity.Fund;
import ideas.capstone_pm.entity.Transaction;
import ideas.capstone_pm.repository.FundReturnRepository;
import ideas.capstone_pm.repository.TransactionRepository;
import ideas.capstone_pm.util.InvestmentCalculator;
import ideas.capstone_pm.util.TransactionServiceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class TransactionService {
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    FundReturnRepository fundReturnRepository;
    @Autowired
    TransactionServiceUtils transactionServiceUtils;
    @Autowired
    InvestmentCalculator investmentCalculator;

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

        FundReturnProjection fundReturnProjection = fundReturnRepository.findByFundFundId(fund.getFundId());
        Double currentValue = investmentCalculator.calculateCurrentValue(transactionProjection.getTransactionInitialInvestment(), fundReturnProjection.getFundReturn1Year(), transactionProjection.getTransactionDate());

        TransactionResponseDTO transactionResponseDTO = new TransactionResponseDTO();
        transactionResponseDTO.setTransactionProjection(transactionProjection);
        transactionResponseDTO.setCurrentValue(currentValue);

        return transactionResponseDTO;
    }
}
