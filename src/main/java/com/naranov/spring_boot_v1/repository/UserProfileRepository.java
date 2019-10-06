package com.naranov.spring_boot_v1.repository;

import com.naranov.spring_boot_v1.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserProfileRepository extends JpaRepository<UserProfile, Integer> {
//    UserProfile findById(int id);



}
