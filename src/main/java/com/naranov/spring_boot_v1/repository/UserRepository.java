package com.naranov.spring_boot_v1.repository;

import java.util.List;
import java.util.Optional;

import com.naranov.spring_boot_v1.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import com.naranov.spring_boot_v1.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByLogin(String login);
    User findByEmail(String email);
}