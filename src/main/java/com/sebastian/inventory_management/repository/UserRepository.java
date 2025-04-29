package com.sebastian.inventory_management.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sebastian.inventory_management.enums.Role;
import com.sebastian.inventory_management.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional <User> findByEmail(String email);
    Page<User> findByNameContainingIgnoreCase(String name, Pageable pageable);
    long count();
    long countByRole(Role role);
    @Query("SELECT COUNT(u) FROM User u WHERE u.createdAt >= :startDate")
    long countNewUsersInLast30Days(@Param("startDate") LocalDateTime startDate);
} 
