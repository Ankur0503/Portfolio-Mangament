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
public class CapstonePmApplication {
	public static void main(String[] args) {
		SpringApplication.run(CapstonePmApplication.class, args);
	}
}
