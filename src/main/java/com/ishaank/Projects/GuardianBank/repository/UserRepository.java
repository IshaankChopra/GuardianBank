package com.ishaank.Projects.GuardianBank.repository;

import com.ishaank.Projects.GuardianBank.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUserId(String userId);
}
