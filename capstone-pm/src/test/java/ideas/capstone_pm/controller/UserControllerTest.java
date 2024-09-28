package ideas.capstone_pm.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ideas.capstone_pm.CapstonePmApplication;
import ideas.capstone_pm.dto.UserDTO;
import ideas.capstone_pm.projection.UserProjection;
import ideas.capstone_pm.entity.ApplicationUser;
import ideas.capstone_pm.service.ApplicationUserDetailsService;
import ideas.capstone_pm.service.UserService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(classes = CapstonePmApplication.class)
@AutoConfigureMockMvc
public class UserControllerTest {
    @MockBean
    ApplicationUserDetailsService applicationUserDetailsService;
    @MockBean
    UserService userService;
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
    void shouldAddUser() throws Exception {
        ApplicationUser user = buildApplicationUser();
        when(userService.addUser(user)).thenReturn(user);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", header)
                .content(new ObjectMapper().writeValueAsString(user)))
                .andExpect(status().isOk());
    }

    @WithMockUser(username = "test@gmail.com", roles = {"USER"})
    @Test
    void shouldGetCurrentUser() throws Exception {
        String authorizationHeader = "Bearer mockJwtToken";
        UserProjection userProjection = buildUserProjection();

        when(userService.getCurrentUser(authorizationHeader)).thenReturn(userProjection);

        mockMvc.perform(get("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", header))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userAge").value(30))
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.userEmail").value("test@gmail.com"))
                .andExpect(jsonPath("$.userPassword").value("Test@12345"))
                .andExpect(jsonPath("$.userRole").value("USER"));
    }

    @WithMockUser(username = "test@gmail.com", roles = {"USER"})
    @Test
    public void shouldUpdateUser() throws Exception {
        UserDTO userDTO = buildUserDTO();
        ApplicationUser user = buildApplicationUser();

        when(userService.updateUser(any(UserDTO.class))).thenReturn(user);

        mockMvc.perform(put("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", header)
                .content(new ObjectMapper().writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userAge").value(30))
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.userEmail").value("test@gmail.com"))
                .andExpect(jsonPath("$.userPassword").value("Test@12345"))
                .andExpect(jsonPath("$.userRole").value("USER"));
    }

    private ApplicationUser buildApplicationUser() {
        return new ApplicationUser(1, "testUser", "test@gmail.com", "Test@12345", "0987654321", 30, "USER", null, null);
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

    private UserDTO buildUserDTO() {
        return new UserDTO(1, "test@gmail.com", "Test@12345", "0987654321");
    }
}
