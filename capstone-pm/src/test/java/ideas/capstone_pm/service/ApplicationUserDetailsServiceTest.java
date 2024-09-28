package ideas.capstone_pm.service;

import ideas.capstone_pm.projection.AdminProjection;
import ideas.capstone_pm.projection.UserProjection;
import ideas.capstone_pm.repository.AdminRepository;
import ideas.capstone_pm.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ApplicationUserDetailsServiceTest {
    @Mock
    UserRepository userRepository;
    @Mock
    AdminRepository adminRepository;

    @InjectMocks
    ApplicationUserDetailsService applicationUserDetailsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldLoadUserByUserName() {
        String username = "test@gmail.com";
        when(userRepository.existsByUserEmail(username)).thenReturn(true);

        UserProjection userProjection = buildUserProjection();
        when(userRepository.findByUserEmail(username)).thenReturn(userProjection);

        UserDetails userDetails = applicationUserDetailsService.loadUserByUsername(username);
        assertNotNull(userDetails);
        assertEquals(userDetails.getUsername(), userProjection.getUserEmail());
        assertEquals(userDetails.getPassword(), userProjection.getUserPassword());
    }

    @Test
    void shouldLoadAdminByUserName() {
        String username = "test@gmail.com";
        when(userRepository.existsByUserEmail(username)).thenReturn(false);
        when(adminRepository.existsByAdminEmail(username)).thenReturn(true);

        AdminProjection adminProjection = buildAdminProjection();
        when(adminRepository.findByAdminEmail(username)).thenReturn(adminProjection);

        UserDetails userDetails = applicationUserDetailsService.loadUserByUsername(username);
        assertEquals(userDetails.getUsername(), adminProjection.getAdminEmail());
        assertEquals(userDetails.getPassword(), adminProjection.getAdminPassword());
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

    private AdminProjection buildAdminProjection() {
        return new AdminProjection() {
            @Override
            public String getAdminEmail() {
                return "test@gmail.com";
            }

            @Override
            public String getAdminPassword() {
                return "Task@12345";
            }

            @Override
            public String getAdminRole() {
                return "ADMIN";
            }
        };
    }
}
