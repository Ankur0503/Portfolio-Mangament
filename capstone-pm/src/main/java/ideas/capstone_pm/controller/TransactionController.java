package ideas.capstone_pm.controller;

import ideas.capstone_pm.dto.TransactionDTO;
import ideas.capstone_pm.dto.TransactionResponseDTO;
import ideas.capstone_pm.entity.ApplicationUser;
import ideas.capstone_pm.entity.Transaction;
import ideas.capstone_pm.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @GetMapping("mutual-funds/user/investment")
    public ResponseEntity<List<TransactionResponseDTO>> getFundsByUser(@RequestParam Integer userId) {
        ApplicationUser user = new ApplicationUser();
        user.setUserId(userId);
        List<TransactionResponseDTO> transactions = transactionService.getFundsByUser(user);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    @GetMapping("mutual-funds/user/investment/{fundId}")
    public ResponseEntity<TransactionDTO> getTransactionByUserAndFund(@RequestBody ApplicationUser user, @PathVariable("fundId") Integer fundId) {
        TransactionDTO transaction = transactionService.getTransactionByUserAndFund(user, fundId);
        return new ResponseEntity<>(transaction, HttpStatus.OK);
    }

    @PostMapping("mutual-funds/user/investment")
    public ResponseEntity<TransactionDTO> addTransaction(@RequestBody Transaction transaction) {
        TransactionDTO newTransaction = transactionService.addTransaction(transaction);
        return new ResponseEntity<>(newTransaction, HttpStatus.CREATED);
    }
}
