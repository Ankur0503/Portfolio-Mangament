package ideas.capstone_pm.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ideas.capstone_pm.CapstonePmApplication;
import ideas.capstone_pm.dto.DashBoardFilters;
import ideas.capstone_pm.projection.DashBoardFundProjection;
import ideas.capstone_pm.dto.FundDescriptionDTO;
import ideas.capstone_pm.entity.Fund;
import ideas.capstone_pm.service.ApplicationUserDetailsService;
import ideas.capstone_pm.service.FundService;
import ideas.capstone_pm.util.JwtUtil;
import ideas.capstone_pm.utils.MockUtils;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest(classes = CapstonePmApplication.class)
@AutoConfigureMockMvc
public class FundControllerTest {
    @MockBean
    FundService fundService;
    @MockBean
    ApplicationUserDetailsService applicationUserDetailsService;
    @MockBean
    JwtUtil jwtUtil;

    String header;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("abc@gmail.com");

        when(applicationUserDetailsService.loadUserByUsername("abc@gmail.com")).thenReturn(userDetails);

        when(jwtUtil.generateToken(userDetails)).thenReturn("mockJwtToken");
        header = "Bearer mockJwtToken";
    }

    @WithMockUser(username = "abc@gmail.com", roles = {"USER"})
    @Test
    void shouldGetAllFilters() throws Exception {
        List<String> fundAMCs = List.of("AMC1", "AMC2");
        List<String> fundTypes = List.of("Type1", "Type2");
        List<String> fundRisks = List.of("Risk1", "Risk2");

        DashBoardFilters dashBoardFilters = new DashBoardFilters(fundAMCs, fundTypes, fundRisks);
        when(fundService.getAllFilters()).thenReturn(dashBoardFilters);

        mockMvc.perform(get("/filters")
                        .header("Authorization", header))
                .andExpect(status().isOk());
    }

    @WithMockUser(username = "abc@gmail.com", roles = {"USER"})
    @Test
    void shouldGetAllFunds() throws Exception {
        DashBoardFundProjection dashBoardFundProjection = buildDashBoardFundProjection();

        when(fundService.getAllFunds()).thenReturn(List.of(dashBoardFundProjection));

        mockMvc.perform(get("/mutual-funds")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", header))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].fundName").value("SBI Blue Chip Fund"))
                .andExpect(jsonPath("$[0].fundAMC").value("SBI Mutual Fund"))
                .andExpect(jsonPath("$[0].fundAUM").value(10000.0))
                .andExpect(jsonPath("$[0].fundNAV").value(21.0))
                .andExpect(jsonPath("$[0].fundRating").value(3.7))
                .andExpect(jsonPath("$[0].fundRisk").value("High"))
                .andExpect(jsonPath("$[0].fundType").value("Debt"));
    }

    @WithMockUser(username = "abc@gmail.com", roles = {"USER"})
    @Test
    void shouldGetFundById() throws Exception {
        int fundId = 1;
        FundDescriptionDTO mockFundDescription = new FundDescriptionDTO();
        mockFundDescription.setFundId(fundId);
        mockFundDescription.setFundName("Test Fund");

        when(fundService.getFundById(fundId)).thenReturn(mockFundDescription);

        mockMvc.perform(get("/mutual-funds/{id}", fundId)
                        .header("Authorization", header))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fundId").value(fundId))
                .andExpect(jsonPath("$.fundName").value("Test Fund"));
    }

    @WithMockUser(username = "test@gmail.com", roles = {"ADMIN"})
    @Test
    void shouldAddFund() throws Exception {
        Fund fund = buildFund();
        FundDescriptionDTO fundDescriptionDTO = buildFundDescriptionDTO();

        when(fundService.addFund(any(FundDescriptionDTO.class))).thenReturn(fund);

        mockMvc.perform(post("/mutual-funds/add")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", header)
                .content(new ObjectMapper().writeValueAsString(fundDescriptionDTO)))
                .andExpect(status().isCreated());
    }

    @WithMockUser(username = "abc@gmail.com", roles = {"USER"})
    @Test
    void shouldGetFundsByFilter() throws Exception {
        List<String> fundAMCs = List.of("AMC1", "AMC2");
        List<String> fundRisks = List.of("High", "Low");
        Double fundAUM = 1000000.0;

        List<DashBoardFundProjection> mockedFundProjection = MockUtils.mockFundProjectionsList();

        when(fundService.getFundsByFilter(eq(fundAMCs), eq(fundRisks), eq(fundAUM))).thenReturn(mockedFundProjection);

        mockMvc.perform(get("/mutual-funds/filter")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", header)
                        .param("fundAMCs", "AMC1")
                        .param("fundAMCs", "AMC2")
                        .param("fundRisks", "High")
                        .param("fundRisks", "Low")
                        .param("fundAUM", "1000000.0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].fundId").value(1))
                .andExpect(jsonPath("$[0].fundName").value("Fund1"))
                .andExpect(jsonPath("$[0].fundAMC").isEmpty())
                .andExpect(jsonPath("$[0].fundRisk").value("Low"))
                .andExpect(jsonPath("$[0].fundNAV").value(0.0))
                .andExpect(jsonPath("$[0].fundType").value("Equity"))
                .andExpect(jsonPath("$[0].fundAUM").value(0.0))
                .andExpect(jsonPath("$[0].fundRating").value(4.5))
                .andExpect(jsonPath("$[0].fundReturn.fundReturn1Year").value(10.5))
                .andExpect(jsonPath("$[0].fundReturn.fundReturn3Year").value(25.0))
                .andExpect(jsonPath("$[0].fundReturn.fundReturn5Year").value(40.0));
    }

    @WithMockUser(username = "abc@gmail.com", roles = {"USER"})
    @Test
    void shouldCalculateFundValue() throws Exception {
        Fund fund = buildFund();
        Double initialInvestment = 2000.0;
        Integer years = 3;

        when(fundService.calculateFundValue(1, initialInvestment, years)).thenReturn(1500.0);

        mockMvc.perform(get("/mutual-funds/calculate/return")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", header)
                .param("fundId", "1")
                .param("initialInvestment", "2000.0")
                .param("years", "3"))
                .andExpect(status().isOk());
    }

    private Fund buildFund() {
        return new Fund(1, "SBI Blue Chip Fund", "SBI Mutual Fund", "High", "Debt", 100000000.0, 79.3, "Jane Doe", "A long-term growth oriented fund", 3.9, null, null, null);
    }

    private FundDescriptionDTO buildFundDescriptionDTO() {
        return new FundDescriptionDTO(1, "SBI Blue Chip Fund", "SBI Mutual Fund", "High", "Debt", 100000000.0, 79.3, "Jane Doe", "A long-term growth oriented fund", 3.9, null, null, null);
    }

    private DashBoardFundProjection buildDashBoardFundProjection() {
        return new DashBoardFundProjection() {
            @Override
            public Integer getFundId() {
                return 1;
            }

            @Override
            public String getFundName() {
                return "SBI Blue Chip Fund";
            }

            @Override
            public String getFundAMC() {
                return "SBI Mutual Fund";
            }

            @Override
            public String getFundRisk() {
                return "High";
            }

            @Override
            public Double getFundNAV() {
                return 21.0;
            }

            @Override
            public String getFundType() {
                return "Debt";
            }

            @Override
            public Double getFundAUM() {
                return 10000.0;
            }

            @Override
            public Double getFundRating() {
                return 3.7;
            }

            @Override
            public DashBoardFundReturnProjection getFundReturn() {
                return buildDashBoardFundReturnProjection();
            }
        };
    }

    private DashBoardFundProjection.DashBoardFundReturnProjection buildDashBoardFundReturnProjection() {
        return new DashBoardFundProjection.DashBoardFundReturnProjection() {
            @Override
            public Float getFundReturn1Year() {
                return 20.6f;
            }

            @Override
            public Float getFundReturn3Year() {
                return 61.4f;
            }

            @Override
            public Float getFundReturn5Year() {
                return 90.6f;
            }
        };
    }
}
