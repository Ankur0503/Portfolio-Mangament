package ideas.capstone_pm.controller;

import ideas.capstone_pm.dto.*;
import ideas.capstone_pm.entity.Fund;
import ideas.capstone_pm.exception.fundexceptions.InvalidArguments;
import ideas.capstone_pm.service.DashBoardFundProjectionConverter;
import ideas.capstone_pm.service.FundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*")
public class FundController {

    @Autowired
    FundService fundService;

    @GetMapping("filters")
    public DashBoardFilters getAllFilters() {
        return fundService.getAllFilters();
    }

    @GetMapping("mutual-funds")
    public List<DashBoardFundProjection> getFunds() {
        return fundService.getAllFunds();
    }

    @GetMapping("mutual-funds/{id}")
    public FundDescriptionDTO getFundById(@PathVariable("id") int fundId) {
        return fundService.getFundById(fundId);
    }

    @PostMapping("mutual-funds/add")
    public Fund addFund(@RequestBody FundDescriptionDTO fundDTO) {
        return fundService.addFund(fundDTO);
    }

    @GetMapping("mutual-funds/filter")
    public List<DashBoardFundDTO> getFundsByFilter(@RequestParam List<String> fundAMCs, @RequestParam List<String> fundRisks, @RequestParam Double fundAUM) {
        List<DashBoardFundProjection> funds = fundService.getFundsByFilter(fundAMCs, fundRisks, fundAUM);
        return DashBoardFundProjectionConverter.convertToDTOList(funds);
    }

    @PostMapping("mutual-funds/fund-calculator")
    public Double calculateFundValue(@RequestBody  Fund fund, @RequestParam("initialInvestment") Double initialInvestment, @RequestParam("years") Integer years) {
        if(years != 1 && years != 3 && years != 5) {
            throw new InvalidArguments();
        }
        return fundService.calculateFundValue(fund, initialInvestment, years);
    }
}
