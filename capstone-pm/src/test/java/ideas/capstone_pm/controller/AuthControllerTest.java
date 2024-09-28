package ideas.capstone_pm.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ideas.capstone_pm.CapstonePmApplication;
import ideas.capstone_pm.dto.LoginDTO;
import ideas.capstone_pm.projection.UserProjection;
import ideas.capstone_pm.dto.AuthenticationResponse;
import ideas.capstone_pm.service.ApplicationUserDetailsService;
import ideas.capstone_pm.service.AuthService;
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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = CapstonePmApplication.class)
@AutoConfigureMockMvc
public class AuthControllerTest {
    @MockBean
    private ApplicationUserDetailsService applicationUserDetailsService;
    @MockBean
    private AuthService authService;
    @MockBean
    private JwtUtil jwtUtil;

    String header;

    @Autowired
    private MockMvc mockMvc;

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
    void shouldCreateAuthenticationToken() throws Exception {
        LoginDTO loginDTO = buildLoginDTO();
        AuthenticationResponse authenticationResponse = buildAuthenticationResponse();

        when(authService.authenticate(loginDTO)).thenReturn(authenticationResponse);

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", header)
                .content(new ObjectMapper().writeValueAsString(loginDTO)))
                .andExpect(status().isOk());
    }

    private AuthenticationResponse buildAuthenticationResponse() {
        UserProjection userProjection = buildUserProjection();
        return new AuthenticationResponse(userProjection, "mockJwtToken");
    }

    private UserProjection buildUserProjection() {
        return new UserProjection() {
            @Override
            public Integer getUserId() {
                return 1;
            }

            @Override
            public String getUserName() {
                return "testUser";
            }

            @Override
            public String getUserEmail() {
                return "test@gmail.com";
            }

            @Override
            public String getUserPassword() {
                return "Test@12345";
            }

            @Override
            public String getUserPhone() {
                return "0987654321";
            }

            @Override
            public Integer getUserAge() {
                return 30;
            }

            @Override
            public String getUserRole() {
                return "USER";
            }
        };
    }

    private LoginDTO buildLoginDTO() {
        return new LoginDTO("test@gmail.com", "Test@12345");
    }
}
