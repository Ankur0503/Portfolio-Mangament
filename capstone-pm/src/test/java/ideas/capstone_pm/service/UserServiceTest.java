package ideas.capstone_pm.service;

import ideas.capstone_pm.dto.UserDTO;
import ideas.capstone_pm.exception.userexpcetions.EmailNotFound;
import ideas.capstone_pm.projection.UserProjection;
import ideas.capstone_pm.entity.ApplicationUser;
import ideas.capstone_pm.exception.userexpcetions.EmailAlreadyRegisteredException;
import ideas.capstone_pm.repository.UserRepository;
import ideas.capstone_pm.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_PASSWORD = "password";
    private static final String ENCODED_PASSWORD = "encodedPassword";
    private static final Integer TEST_USER_ID = 1;
    private static final String UPDATED_USER_NAME = "Updated Name";
    private static final String UPDATED_USER_PHONE = "1234567890";
    private static final String OLD_USER_NAME = "Old Name";
    private static final String OLD_USER_PHONE = "0987654321";
    private static final String OLD_USER_PASSWORD = "oldPassword";
    private static final String NEW_USER_PASSWORD = "newPassword";
    private static final String JWT_TOKEN = "Bearer some.jwt.token";
    private static final String AUTHORIZATION_HEADER = "Bearer some.jwt.token";

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldAddUser() {
        ApplicationUser applicationUser = createApplicationUser();
        when(userRepository.existsByUserEmail(TEST_EMAIL)).thenReturn(false);
        when(passwordEncoder.encode(TEST_PASSWORD)).thenReturn(ENCODED_PASSWORD);
        when(userRepository.save(any(ApplicationUser.class))).thenReturn(applicationUser);

        ApplicationUser result = userService.addUser(applicationUser);

        assertAddUserSuccess(result);
        verifyAddUserSuccess();
    }

    @Test
    void shouldThrowEmailAlreadyRegistered() {
        ApplicationUser applicationUser = createApplicationUser();
        when(userRepository.existsByUserEmail(TEST_EMAIL)).thenReturn(true);

        assertThrows(EmailAlreadyRegisteredException.class, () -> userService.addUser(applicationUser));
        verifyUserExistsCall();
    }

    @Test
    void shouldUpdateUser() {
        UserDTO userDTO = createUserDTO();
        ApplicationUser existingUser = createExistingUser();

        when(userRepository.findById(TEST_USER_ID)).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.encode(userDTO.getUserPassword())).thenReturn(ENCODED_PASSWORD);
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        ApplicationUser updatedUser = userService.updateUser(userDTO);

        assertUpdateUserSuccess(updatedUser);
        verifyUpdateUserSuccess(userDTO);
    }

    @Test
    void shouldGetCurrentUser() {
        String authorizationHeader = "Bearer mockJwtToken";

        String username = "test@gmail.com";
        when(jwtUtil.extractUsername(any(String.class))).thenReturn(username);

        UserProjection expectedUser = buildUserProjection();
        when(userRepository.findByUserEmail(username)).thenReturn(expectedUser);

        UserProjection actualUser = userService.getCurrentUser(authorizationHeader);
        assertNotNull(actualUser);
        assertEquals(expectedUser, actualUser);
    }

    @Test
    void shouldThrowInvalidAuthorizationHeader() {
        String authorizationHeader = "Invalid Header";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.getCurrentUser(authorizationHeader);
        });

        assertEquals("Invalid authorization header.", exception.getMessage());
        verifyNoInteractions(jwtUtil, userRepository);
    }

    @Test
    public void shouldThrowEmailNotFound() {
        // Arrange
        String authorizationHeader = "Bearer valid-jwt";
        String jwt = "valid-jwt";
        String username = "unknown@example.com";

        when(jwtUtil.extractUsername(jwt)).thenReturn(username);
        when(userRepository.findByUserEmail(username)).thenReturn(null); // Simulate user not found

        EmailNotFound exception = assertThrows(EmailNotFound.class, () -> {
            userService.getCurrentUser(authorizationHeader);
        });

        assertNotNull(exception);
        verify(jwtUtil, times(1)).extractUsername(jwt);
        verify(userRepository, times(1)).findByUserEmail(username);
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

    private ApplicationUser createApplicationUser() {
        ApplicationUser user = new ApplicationUser();
        user.setUserEmail(TEST_EMAIL);
        user.setUserPassword(TEST_PASSWORD);
        return user;
    }

    private UserDTO createUserDTO() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(TEST_USER_ID);
        userDTO.setUserName(UPDATED_USER_NAME);
        userDTO.setUserPhone(UPDATED_USER_PHONE);
        userDTO.setUserPassword(NEW_USER_PASSWORD);
        return userDTO;
    }

    private ApplicationUser createExistingUser() {
        ApplicationUser user = new ApplicationUser();
        user.setUserId(TEST_USER_ID);
        user.setUserName(OLD_USER_NAME);
        user.setUserPhone(OLD_USER_PHONE);
        user.setUserPassword(OLD_USER_PASSWORD);
        return user;
    }

    private void assertAddUserSuccess(ApplicationUser result) {
        assertNotNull(result);
        assertEquals(TEST_EMAIL, result.getUserEmail());
        assertEquals(ENCODED_PASSWORD, result.getUserPassword());
    }

    private void verifyAddUserSuccess() {
        verify(userRepository).existsByUserEmail(TEST_EMAIL);
        verify(passwordEncoder).encode(TEST_PASSWORD);
        verify(userRepository).save(any(ApplicationUser.class));
    }

    private void verifyUserExistsCall() {
        verify(userRepository).existsByUserEmail(TEST_EMAIL);
        verify(userRepository, never()).save(any(ApplicationUser.class));
    }

    private void assertGetCurrentUserSuccess(UserProjection result) {
        assertNotNull(result);
        assertEquals(TEST_EMAIL, result.getUserEmail());
    }

    private void verifyGetCurrentUserSuccess() {
        verify(jwtUtil).extractUsername(JWT_TOKEN);
        verify(userRepository).findByUserEmail(TEST_EMAIL);
    }

    private void assertUpdateUserSuccess(ApplicationUser updatedUser) {
        assertNotNull(updatedUser);
        assertEquals(UPDATED_USER_NAME, updatedUser.getUserName());
        assertEquals(UPDATED_USER_PHONE, updatedUser.getUserPhone());
        assertEquals(ENCODED_PASSWORD, updatedUser.getUserPassword());
    }

    private void verifyUpdateUserSuccess(UserDTO userDTO) {
        verify(userRepository).findById(TEST_USER_ID);
        verify(passwordEncoder).encode(userDTO.getUserPassword());
        verify(userRepository).save(any(ApplicationUser.class));
    }
}
