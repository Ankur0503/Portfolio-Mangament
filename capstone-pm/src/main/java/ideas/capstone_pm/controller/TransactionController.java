package ideas.capstone_pm.controller;

import ideas.capstone_pm.dto.TransactionDTO;
import ideas.capstone_pm.dto.TransactionResponseDTO;
import ideas.capstone_pm.entity.ApplicationUser;
import ideas.capstone_pm.entity.Transaction;
import ideas.capstone_pm.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class TransactionController {
    @Autowired
    TransactionService transactionService;

    @GetMapping("mutual-funds/user/investment")
    public List<TransactionResponseDTO> getFundsByUser(@RequestParam Integer userId) {
        ApplicationUser user = new ApplicationUser();
        user.setUserId(userId);
        return transactionService.getFundsByUser(user);
    }

    @GetMapping("mutual-funds/user/investment/{fundId}")
    public TransactionDTO getTransactionByUserAndFund(@RequestBody ApplicationUser user, @PathVariable("fundId") Integer fundId) {
        return transactionService.getTransactionByUserAndFund(user, fundId);
    }

    @PostMapping("mutual-funds/user/investment")
    public TransactionDTO addTransaction(@RequestBody Transaction transaction) {
        return transactionService.addTransaction(transaction);
    }
}
