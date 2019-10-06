package com.naranov.spring_boot_v1.controller.rest;

import com.naranov.spring_boot_v1.model.User;
import com.naranov.spring_boot_v1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class UserController {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;



    @PutMapping("/id/{id}")
    public User updateUser(@PathVariable int id,
                           @Valid @RequestBody User user) {
        User currentUser = userRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("User", "id", id));

        currentUser.setLogin(user.getLogin());
        currentUser.setName(user.getName());
        currentUser.setUserProfiles(user.getUserProfiles());
        currentUser.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(currentUser);
    }

    @GetMapping("/id/{id}")
    public User getUser(@PathVariable int id) {
        return userRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("User", "id", id));
    }

    @GetMapping
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable int id) {
        User currentUser = userRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("User", "id", id));

        userRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
