package com.zestindiait.controller;

import com.zestindiait.dto.LoginDto;
import com.zestindiait.entities.User;
import com.zestindiait.repository.UserRepository;
import com.zestindiait.security.JwtUtils;
import com.zestindiait.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<String> saveUser(@RequestBody User user) {
        User saveUser = userService.saveUser(user);
        return ResponseEntity.ok("User saved successfully" + saveUser);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUser() {
        return ResponseEntity.ok(userService.getAllUser());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginDto request) {
        User loginUser = userService.login(request);
        String jwtToken = jwtUtils.generateToken(request.getUserName());

        Map<String, Object> response = new HashMap<>();
        response.put("token", jwtToken);
        response.put("user", loginUser);

        return ResponseEntity.ok(response);
    }


    @PutMapping
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.saveUser(user));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok("User deleted successfully");
    }

    @GetMapping("/token/validate")
    public ResponseEntity<Optional<User>> validateToken(@RequestHeader("Authorization") String token) {
        String jwt = token.substring(7);
        String username = jwtUtils.extractUsername(jwt);
       Optional<User> user = userRepository.findByUserName(username);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }


        return ResponseEntity.ok(user);
    }
}