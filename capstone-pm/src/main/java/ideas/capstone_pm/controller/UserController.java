package ideas.capstone_pm.controller;

import ideas.capstone_pm.dto.UserDTO;
import ideas.capstone_pm.projection.UserProjection;
import ideas.capstone_pm.entity.ApplicationUser;
import ideas.capstone_pm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/users")
    public ResponseEntity<ApplicationUser> addUser(@RequestBody ApplicationUser applicationUser) {
        ApplicationUser createdUser = userService.addUser(applicationUser);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @GetMapping("/users")
    public ResponseEntity<UserProjection> getCurrentUser(@RequestHeader("Authorization") String authorizationHeader) {
        UserProjection currentUser = userService.getCurrentUser(authorizationHeader);
        return new ResponseEntity<>(currentUser, HttpStatus.OK);
    }

    @PutMapping("/users")
    public ResponseEntity<ApplicationUser> updateUser(@RequestBody UserDTO userDTO) {
        ApplicationUser updatedUser = userService.updateUser(userDTO);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }
}
