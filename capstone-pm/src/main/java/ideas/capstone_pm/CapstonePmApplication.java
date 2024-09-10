package ideas.capstone_pm;

import ideas.capstone_pm.dto.*;
import ideas.capstone_pm.entity.ApplicationUser;
import ideas.capstone_pm.entity.Fund;
import ideas.capstone_pm.entity.FundReturn;
import ideas.capstone_pm.entity.Transaction;
import ideas.capstone_pm.repository.FundRepository;
import ideas.capstone_pm.repository.FundReturnRepository;
import ideas.capstone_pm.repository.TransactionRepository;
import ideas.capstone_pm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@SpringBootApplication
public class CapstonePmApplication implements CommandLineRunner {

	@Autowired
	UserRepository userRepository;
	@Autowired
	FundRepository fundRepository;
	@Autowired
	FundReturnRepository fundReturnRepository;
	@Autowired
	TransactionRepository transactionRepository;

	public static void main(String[] args) {
		SpringApplication.run(CapstonePmApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
//		TODO: Saving User
//		ApplicationUser user1 = userRepository.save(new ApplicationUser(0, "John Doe", "john.doe@gmail.com", "john@123", "987654321", 36, null));
//		ApplicationUser user2 = userRepository.save(new ApplicationUser(0, "Jane Smith", "jane.smith@gmail.com", "jane@123", "3287825485", 37, null));

//		TODO: Saving Fund
//		Fund fund1 = fundRepository.save(new Fund(0, "HDFC Equity Fund-Direct Plan", "HDFC Mutual Fund", "High",
//				"Equity", 22000000.00, 210.45, "Prashant Jain",
//				"Primarily invests in equity and equity related instruments", null, null));
//		Fund fund2 = fundRepository.save(new Fund(0, "SBI Bluechip Fund-Direct Plan", "SBI Mutual Fund", "Moderate",
//				"Equity", 320000000.00, 45.78, "Sohini Andani",
//				"Focuses on large cap companies with a strong growth potential", null, null));
//		Fund fund3 = fundRepository.save(new Fund(0, "ICICI Prudential Balanced Advantage Fund"	, "ICICI Pridential Mutual Fund", "Moderate",
//				"Balanced", 350000000, 55.23, "Sankaran Naren",
//				"Dynamically manages equity and dept instrument based on market conditions", null, null));
//		Fund fund4 = fundRepository.save(new Fund(0, "Axis Long Term Equity Fund-Direct Plan", "Axis Mutual Fund", "High",
//				"Equity", 22000000, 65.80, "Jinesh Gopani",
//				"An equity-linked saving scheme with a focus on long-term capital growth", null, null));

//		TODO: Saving Fund Returns
//		FundReturn fundReturn1 = fundReturnRepository.save(new FundReturn(0, 2.0, 12.5, 38.7, 75.4, 250.4, new Fund(1, null, null, null, null, 0.00, 0.00, null, null, null, null)));
//		FundReturn fundReturn2 = fundReturnRepository.save(new FundReturn(0, 1.5, 10.3, 29.5, 60.1, 180.2, new Fund(2, null, null, null, null, 0.00, 0.00, null, null, null, null)));
//		FundReturn fundReturn3 = fundReturnRepository.save(new FundReturn(0, 1.2, 8.9, 25.0, 50.2, 130.5, new Fund(3, null, null, null, null, 0.00, 0.00, null, null, null, null)));
//		FundReturn fundReturn4 = fundReturnRepository.save(new FundReturn(0, 2.3, 14.2, 40.8, 82.6, 270.1, new Fund(4, null, null, null, null, 0.00, 0.00, null, null, null, null)));

//		TODO: Saving Transactions
//		Transaction transaction1 = transactionRepository.save(new Transaction(0, 500.00, "1 Year", Date.from(LocalDate.of(2023, 1, 15).atStartOfDay(ZoneId.systemDefault()).toInstant()), new Fund(1, null, null, null, null, 0.00, 0.00, null, null, null, null), new ApplicationUser(1, null, null, null, null, 0, null)));
//		Transaction transaction2 = transactionRepository.save(new Transaction(0, 1000.00, "3 Year", Date.from(LocalDate.of(2023, 5, 15).atStartOfDay(ZoneId.systemDefault()).toInstant()), new Fund(3, null, null, null, null, 0.00, 0.00, null, null, null, null), new ApplicationUser(1, null, null, null, null, 0, null)));
//		Transaction transaction3 = transactionRepository.save(new Transaction(0, 500.00, "1 Year", Date.from(LocalDate.of(2023, 3, 15).atStartOfDay(ZoneId.systemDefault()).toInstant()), new Fund(4, null, null, null, null, 0.00, 0.00, null, null, null, null), new ApplicationUser(1, null, null, null, null, 0, null)));
//		Transaction transaction4 = transactionRepository.save(new Transaction(0, 1000.00, "5 Year", Date.from(LocalDate.of(2023, 2, 15).atStartOfDay(ZoneId.systemDefault()).toInstant()), new Fund(2, null, null, null, null, 0.00, 0.00, null, null, null, null), new ApplicationUser(2, null, null, null, null, 0, null)));
//		Transaction transaction5 = transactionRepository.save(new Transaction(0, 5000.00, "3 Year", Date.from(LocalDate.of(2023, 11, 15).atStartOfDay(ZoneId.systemDefault()).toInstant()), new Fund(4, null, null, null, null, 0.00, 0.00, null, null, null, null), new ApplicationUser(2, null, null, null, null, 0, null)));

//		TODO: Fetching all funds of a particular user.
//		List<TransactionDTO> userTransactions = transactionRepository.findByUser(new ApplicationUser(1, null, null, null, null, 0, null));
//		for (TransactionDTO userTransaction: userTransactions) {
//			System.out.println(userTransaction.getUser().getUserName()+", "+userTransaction.getFund().getFundName()+", "+userTransaction.getFund().getFundType());
//		}

//		TODO: Fetching all users who invested in a particular fund.
//		List<TransactionDTO> fundTransactions = transactionRepository.findByFund(new Fund(4, null, null, null, null, 0.00, 0.00, null, null, null, null));
//		for (TransactionDTO fundTransaction: fundTransactions) {
//			System.out.println(fundTransaction.getFund().getFundName()+", "+fundTransaction.getFund().getFundType()+", "+fundTransaction.getUser().getUserName());
//		}

//		TODO: Fetching Fund History.
//		FundReturnDTO fundReturnDTO = fundReturnRepository.findByFund(new Fund(4, null, null, null, null, 0.00, 0.00, null, null, null, null));
//		System.out.println(fundReturnDTO.getFund().getFundName()+" returns -> 1 Month -> "+fundReturnDTO.getFundReturn1Month()+"%, 1 Year -> "+fundReturnDTO.getFundReturn1Year()+"%, 3 Year -> "+fundReturnDTO.getFundReturn3Year()+"%, 5 Year -> "+fundReturnDTO.getFundReturn5Year()+"%");

//		TODO: Fetching fund filtered by Fund AMC.
//		List<DashBoardFundDTO> fundsByAMC = fundRepository.findByFundAMC("ICICI Pridential Mutual Fund");
//		for (DashBoardFundDTO fund: fundsByAMC) {
//			System.out.println(fund.getFundName()+" "+fund.getFundType()+" "+fund.getFundRisk()+" "+fund.getFundReturn().getFundReturn1Year()+" "+fund.getFundReturn().getFundReturn3Year()+" "+fund.getFundReturn().getFundReturn5Year());
//		}

//		TODO: Fetching fund filtered by Fund Risk.
//		List<DashBoardFundDTO> fundsByRisk = fundRepository.findByFundRisk("High");
//		for (DashBoardFundDTO fund: fundsByRisk) {
//			System.out.println(fund.getFundName()+" "+fund.getFundType()+" "+fund.getFundRisk()+" "+fund.getFundReturn().getFundReturn1Year()+" "+fund.getFundReturn().getFundReturn3Year()+" "+fund.getFundReturn().getFundReturn5Year());
//		}

//		TODO: Fetching fund filtered by Fund AUM.
//		List<DashBoardFundDTO> fundByAUM = fundRepository.findByFundAUMLessThanEqual(320000000.00);
//		for (DashBoardFundDTO fund: fundByAUM) {
//			System.out.println(fund.getFundName()+" "+fund.getFundType()+" "+fund.getFundRisk()+" "+fund.getFundReturn().getFundReturn1Year()+" "+fund.getFundReturn().getFundReturn3Year()+" "+fund.getFundReturn().getFundReturn5Year());
//		}

//		TODO: Fetching fund filtered by Fund ID.
//		Fund fund = fundRepository.findById(1);
//		System.out.println(fund.getFundName());

//		TODO: Searching fund by fund name.
//		Fund fund = fundRepository.findByFundName("SBI Bluechip Fund-Direct Plan");
//		System.out.println(fund.toString());

//		TODO: Sorting of funds by fund name.
//		Sort sort = Sort.by("fundName").ascending();
//		List<Fund> funds = fundRepository.findAll(sort);
//		for (Fund fund: funds) {
//			System.out.println(fund.toString());
//		}

//		TODO: Fetching transaction of a specific user on a specific fund.
//		List<Transaction> transactions = transactionRepository.findByFundAndUser(new Fund(1, null, null, null, null, 0.00, 0.00, null, null, null, null), new ApplicationUser(1, null, null, null, null, 0, null));
//		for (Transaction transaction: transactions) {
//			System.out.println(transaction.getFund().getFundName()+", "+transaction.getTransactionDate()+", "+transaction.getTransactionInitialInvestment());
//		}

//		TODO: Fetching all peer funds, based on fund type (Equity, Balanced, ....)
//		List<PeerFundsDTO> peerFunds = fundRepository.findByFundTypeAndFundIdNotIn("Equity", List.of(1));
//		for (PeerFundsDTO peerFund: peerFunds) {
//			System.out.println(peerFund.getFundName()+", "+peerFund.getFundReturn().getFundReturn1Year()+", "+peerFund.getFundReturn().getFundReturn3Year()+", "+peerFund.getFundReturn().getFundReturnTotal());
//		}

//		TODO: Fetching Average returns within a particular category.
//		AverageReturnDTO averageReturnDTO = fundReturnRepository.findAverageReturnsByFundType("Equity");
//		System.out.println("Average 1 year return:"+averageReturnDTO.getAverage1YearReturn());
//		System.out.println("Average 3 year return:"+averageReturnDTO.getAverage3YearReturn());
//		System.out.println("Average total return:"+averageReturnDTO.getAverageTotalReturn());

//		TODO: Fetching all FundAMCs.
//		List<FundAMC> fundAMCS = fundRepository.findAllDistinctFundAMCs();
//		for (FundAMC fundAMC: fundAMCS) {
//			System.out.println(fundAMC.getFundAMC());
//		}

//		TODO: Fetching all FundCategories.
//		List<FundType> fundTypes = fundRepository.findAllDistinctFundTypes();
//		for (FundType fundType: fundTypes) {
//			System.out.println(fundType.getFundType());
//		}


	}
}
