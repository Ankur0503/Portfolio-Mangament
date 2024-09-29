package ideas.capstone_pm.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ideas.capstone_pm.CapstonePmApplication;
import ideas.capstone_pm.entity.FundReturn;
import ideas.capstone_pm.service.ApplicationUserDetailsService;
import ideas.capstone_pm.service.FundReturnService;
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

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = CapstonePmApplication.class)
@AutoConfigureMockMvc
public class FundReturnControllerTest {
    @MockBean
    ApplicationUserDetailsService applicationUserDetailsService;
    @MockBean
    FundReturnService fundReturnService;
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

    @WithMockUser(username = "test@gmail.com", roles = {"ADMIN"})
    @Test
    void shouldAddFundReturn() throws Exception {
        FundReturn fundReturn = buildFundReturn();

        when(fundReturnService.addFundReturn(fundReturn)).thenReturn(fundReturn);

        mockMvc.perform(post("/fund-returns")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", header)
                .content(new ObjectMapper().writeValueAsString(fundReturn)))
                .andExpect(status().isCreated());
    }

    @WithMockUser(username = "test@gmail.com", roles = {"ADMIN"})
    @Test
    void shouldGetAllFilters() throws Exception {
        FundReturn fundReturn = buildFundReturn();
        when(fundReturnService.getAllFundReturns()).thenReturn(List.of(fundReturn));

        mockMvc.perform(get("/fund-returns")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", header))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].fundReturn1Month").value(3.6))
                .andExpect(jsonPath("$[0].fundReturn1Year").value(40.9))
                .andExpect(jsonPath("$[0].fundReturn3Year").value(52.0))
                .andExpect(jsonPath("$[0].fundReturn5Year").value(24.0));
    }

    private FundReturn buildFundReturn() {
        return new FundReturn(1, 3.6, 40.9, 52.0, 24.0, 34.0, null);
    }
}
