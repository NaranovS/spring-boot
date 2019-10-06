package com.naranov.spring_boot_v1.controller.rest;

import com.naranov.spring_boot_v1.model.User;
import com.naranov.spring_boot_v1.model.UserProfile;
import com.naranov.spring_boot_v1.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/roles")
public class RoleController {
    @Autowired
    private UserProfileRepository userProfileRepository;

    @GetMapping
    public List<UserProfile> getAllRoles(){
        return userProfileRepository.findAll();
    }

//    @GetMapping("/{id}")
//    public UserProfile getById(@PathVariable int id){
//        UserProfile userProfile = userProfileRepository.getOne(id);
//        return userProfile;
//    }

    @GetMapping("/{id}")
    public UserProfile getUser(@PathVariable int id) {
        return userProfileRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Role", "id", id));
    }
}
