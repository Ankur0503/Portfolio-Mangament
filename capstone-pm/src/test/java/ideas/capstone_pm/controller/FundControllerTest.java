package ideas.capstone_pm.controller;

import ideas.capstone_pm.dto.DashBoardFilters;
import ideas.capstone_pm.dto.DashBoardFundProjection;
import ideas.capstone_pm.dto.FundDescriptionDTO;
import ideas.capstone_pm.service.ApplicationUserDetailsService;
import ideas.capstone_pm.service.FundService;
import ideas.capstone_pm.util.JwtUtil;
import ideas.capstone_pm.utils.MockUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(FundController.class)
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
}
