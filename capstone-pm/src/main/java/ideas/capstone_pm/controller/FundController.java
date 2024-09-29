package ideas.capstone_pm.controller;

import ideas.capstone_pm.dto.*;
import ideas.capstone_pm.entity.Fund;
import ideas.capstone_pm.exception.fundexceptions.InvalidArguments;
import ideas.capstone_pm.projection.DashBoardFundProjection;
import ideas.capstone_pm.util.DashBoardFundProjectionConverter;
import ideas.capstone_pm.service.FundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class FundController {

    @Autowired
    FundService fundService;

    @GetMapping("filters")
    public ResponseEntity<DashBoardFilters> getAllFilters() {
        DashBoardFilters filters = fundService.getAllFilters();
        return new ResponseEntity<>(filters, HttpStatus.OK);
    }

    @GetMapping("mutual-funds")
    public ResponseEntity<List<DashBoardFundProjection>> getFunds() {
        List<DashBoardFundProjection> funds = fundService.getAllFunds();
        Collections.shuffle(funds);
        return new ResponseEntity<>(funds, HttpStatus.OK);
    }

    @GetMapping("mutual-funds/{id}")
    public ResponseEntity<FundDescriptionDTO> getFundById(@PathVariable("id") int fundId) {
        FundDescriptionDTO fund = fundService.getFundById(fundId);
        return new ResponseEntity<>(fund, HttpStatus.OK);
    }

    @PostMapping("mutual-funds/add")
    public ResponseEntity<Fund> addFund(@RequestBody FundDescriptionDTO fundDTO) {
        Fund fund = fundService.addFund(fundDTO);
        return new ResponseEntity<>(fund, HttpStatus.CREATED);
    }

    @GetMapping("mutual-funds/filter")
    public ResponseEntity<List<DashBoardFundDTO>> getFundsByFilter(@RequestParam List<String> fundAMCs, @RequestParam List<String> fundRisks, @RequestParam Double fundAUM) {
        List<DashBoardFundProjection> funds = fundService.getFundsByFilter(fundAMCs, fundRisks, fundAUM);
        List<DashBoardFundDTO> fundDTOs = DashBoardFundProjectionConverter.convertToDTOList(funds);
        return new ResponseEntity<>(fundDTOs, HttpStatus.OK);
    }

    @GetMapping("mutual-funds/calculate/return")
    public ResponseEntity<Double> calculateFundValue(@RequestParam("fundId") Integer fundId, @RequestParam("initialInvestment") Double initialInvestment, @RequestParam("years") Integer years) {
        if (initialInvestment < 1000 || (years != 1 && years != 3 && years != 5)) {
            throw new InvalidArguments();
        }
        Double calculatedValue = fundService.calculateFundValue(fundId, initialInvestment, years);
        return new ResponseEntity<>(calculatedValue, HttpStatus.OK);
    }
}
