package ideas.capstone_pm.controller;

import ideas.capstone_pm.entity.FundReturn;
import ideas.capstone_pm.service.FundReturnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("/fund-returns")
public class FundReturnController {
    @Autowired
    FundReturnService fundReturnService;

    @PostMapping
    public ResponseEntity<FundReturn> addFundReturn(@RequestBody FundReturn fundReturn) {
        FundReturn createdFundReturn = fundReturnService.addFundReturn(fundReturn);
        return new ResponseEntity<>(createdFundReturn, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<FundReturn>> getAllFundReturns() {
        List<FundReturn> fundReturns = fundReturnService.getAllFundReturns();
        return new ResponseEntity<>(fundReturns, HttpStatus.OK);
    }
}
