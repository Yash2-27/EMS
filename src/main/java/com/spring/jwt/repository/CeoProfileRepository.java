package com.spring.jwt.repository;

import com.spring.jwt.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CeoProfileRepository extends JpaRepository<User, Integer> {
}
