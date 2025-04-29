package com.sebastian.inventory_management.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.sebastian.inventory_management.DTO.User.UserUpdateRequestDTO;
import com.sebastian.inventory_management.DTO.User.UserResponseDTO;
import com.sebastian.inventory_management.DTO.User.UserStatsResponseDTO;
import com.sebastian.inventory_management.model.User;

public interface IUserService {
    UserResponseDTO addUser(UserUpdateRequestDTO user);
    UserResponseDTO updateUser(Long id, UserUpdateRequestDTO user);
    void deleteUser(Long id);
    UserResponseDTO getUserById(Long id);
    UserResponseDTO getUserByEmail(String email);
    List<UserResponseDTO> getAllUsers();
    Page<UserResponseDTO> getAllUsersPaginated(Pageable pageable);
    Page <UserResponseDTO> findByNameContainingIgnoreCase(String name, Pageable pageable);
    UserStatsResponseDTO getUserStats();
    User getUserByIdEntity(Long id);
    User getCurrentUser();
}
