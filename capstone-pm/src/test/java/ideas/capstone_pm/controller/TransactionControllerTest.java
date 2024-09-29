package ideas.capstone_pm.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ideas.capstone_pm.CapstonePmApplication;
import ideas.capstone_pm.dto.TransactionDTO;
import ideas.capstone_pm.projection.TransactionProjection;
import ideas.capstone_pm.dto.TransactionResponseDTO;
import ideas.capstone_pm.entity.ApplicationUser;
import ideas.capstone_pm.entity.Transaction;
import ideas.capstone_pm.service.ApplicationUserDetailsService;
import ideas.capstone_pm.service.TransactionService;
import ideas.capstone_pm.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = CapstonePmApplication.class)
@AutoConfigureMockMvc
public class TransactionControllerTest {
    @MockBean
    ApplicationUserDetailsService applicationUserDetailsService;
    @MockBean
    TransactionService transactionService;
    @MockBean
    JwtUtil jwtUtil;

    String header;

    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("test@gmail.com");

        when(applicationUserDetailsService.loadUserByUsername("test@gmail.com")).thenReturn(userDetails);

        when(jwtUtil.generateToken(userDetails)).thenReturn("mockJwtToken");
        header = "Bearer mockJwtToken";
    }

    @WithMockUser(username = "test@gmail.com", roles = {"USER"})
    @Test
    void shouldGetFundsByUser() throws Exception {
        ApplicationUser user = buildApplicationUser();
        TransactionResponseDTO transactionResponseDTO = buildTransactionResponseDTO();

        when(transactionService.getFundsByUser(any(ApplicationUser.class))).thenReturn(List.of(transactionResponseDTO));

        mockMvc.perform(get("/mutual-funds/user/investment")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", header)
                .param("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].transactionProjection.transactionInitialInvestment").value(10000.0))
                .andExpect(jsonPath("$[0].currentValue").value(3240.0))
                .andExpect(jsonPath("$[0].transactionProjection.user.userName").value("test@gmail.com"))
                .andExpect(jsonPath("$[0].transactionProjection.fund.fundName").value("SBI Blue Chip Fund"))
                .andExpect(jsonPath("$[0].transactionProjection.fund.fundType").value("Debt"));
    }

    @WithMockUser(username = "test@gmail.com", roles = {"USER"})
    @Test
    void shouldAddTransaction() throws Exception {
        Transaction transaction = buildTransaction();
        TransactionDTO transactionDTO = buildTransactionDTO();

        when(transactionService.addTransaction(any(Transaction.class))).thenReturn(transactionDTO);

        mockMvc.perform(post("/mutual-funds/user/investment")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", header)
                .content(new ObjectMapper().writeValueAsString(transaction)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.transactionId").value(1))
                .andExpect(jsonPath("$.transactionInitialInvestment").value(1000.0));
    }

    private Transaction buildTransaction() {
        return new Transaction(1, 1000.0, new Date(), null, null);
    }

    private TransactionDTO buildTransactionDTO() {
        return new TransactionDTO(1, 1000.0, new Date(), 1, 1);
    }

    private ApplicationUser buildApplicationUser() {
        return new ApplicationUser(1, "testUser", "test@gmail.com", "Test@12345", "0987654321", 30, "USER", null, null);
    }

    private TransactionResponseDTO buildTransactionResponseDTO() {
        TransactionProjection transactionProjection = buildTransactionProjection();
        return new TransactionResponseDTO(transactionProjection, 3240.0);
    }

    private TransactionProjection buildTransactionProjection() {
        return new TransactionProjection() {
            @Override
            public Double getTransactionInitialInvestment() {
                return 10000.0;
            }

            @Override
            public Date getTransactionDate() {
                return new Date();
            }

            @Override
            public UserDTO getUser() {
                return buildUserDTO();
            }

            @Override
            public FundDTO getFund() {
                return buildFundDTO();
            }
        };
    }

    private TransactionProjection.UserDTO buildUserDTO() {
        return new TransactionProjection.UserDTO() {
            @Override
            public Integer getUserId() {
                return 1;
            }

            @Override
            public String getUserName() {
                return "test@gmail.com";
            }
        };
    }

    private TransactionProjection.FundDTO buildFundDTO() {
        return new TransactionProjection.FundDTO() {
            @Override
            public Integer getFundId() {
                return 1;
            }

            @Override
            public String getFundName() {
                return "SBI Blue Chip Fund";
            }

            @Override
            public String getFundType() {
                return "Debt";
            }
        };
    }
}
