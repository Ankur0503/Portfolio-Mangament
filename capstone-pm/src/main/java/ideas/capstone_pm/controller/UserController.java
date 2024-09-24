package ideas.capstone_pm.controller;

import ideas.capstone_pm.dto.LoginDTO;
import ideas.capstone_pm.dto.UserDTO;
import ideas.capstone_pm.dto.UserProjection;
import ideas.capstone_pm.entity.ApplicationUser;
import ideas.capstone_pm.exception.userexpcetions.EmailAlreadyRegisteredException;
import ideas.capstone_pm.exception.userexpcetions.EmailNotFound;
import ideas.capstone_pm.exception.userexpcetions.InvalidCredentialException;
import ideas.capstone_pm.repository.UserRepository;
import ideas.capstone_pm.roles.Roles;
import ideas.capstone_pm.service.UserService;
import ideas.capstone_pm.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("/users/register")
    public ApplicationUser addUser(@RequestBody ApplicationUser applicationUser) {
        return userService.addUser(applicationUser);
    }

    @PostMapping("/user/login")
    public UserProjection loginUser(@RequestBody LoginDTO loginDTO) {
        return userService.loginUser(loginDTO);
    }

    @GetMapping("users")
    public UserProjection getCurrentUser(@RequestHeader("Authorization") String authorizationHeader) {
        return userService.getCurrentUser(authorizationHeader);
    }

    @PutMapping("users")
    public ApplicationUser updateUser(@RequestBody UserDTO userDTO) {
        return userService.updateUser(userDTO);
    }
}
