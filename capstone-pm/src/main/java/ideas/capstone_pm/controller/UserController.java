package ideas.capstone_pm.controller;

import ideas.capstone_pm.dto.UserDTO;
import ideas.capstone_pm.projection.UserProjection;
import ideas.capstone_pm.entity.ApplicationUser;
import ideas.capstone_pm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("/users")
    public ApplicationUser addUser(@RequestBody ApplicationUser applicationUser) {
        return userService.addUser(applicationUser);
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
