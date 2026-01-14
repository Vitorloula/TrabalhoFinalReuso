package com.sebastian.inventory_management.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sebastian.inventory_management.controller.base.AbstractCrudController;
import com.sebastian.inventory_management.controller.base.CrudService;
import com.sebastian.inventory_management.controller.util.ResponseBuilder;
import com.sebastian.inventory_management.DTO.User.UserUpdateRequestDTO;
import com.sebastian.inventory_management.DTO.User.UserResponseDTO;
import com.sebastian.inventory_management.DTO.User.UserStatsResponseDTO;
import com.sebastian.inventory_management.service.IUserService;

@RestController
@RequestMapping("/api/users")
public class UserController extends AbstractCrudController<UserResponseDTO, UserUpdateRequestDTO, Long> {

    private final IUserService userService;

    @Autowired
    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @Override
    protected CrudService<UserResponseDTO, UserUpdateRequestDTO, Long> getService() {
        return new CrudService<UserResponseDTO, UserUpdateRequestDTO, Long>() {
            @Override
            public UserResponseDTO getById(Long id) {
                return userService.getUserById(id);
            }

            @Override
            public List<UserResponseDTO> getAll() {
                return userService.getAllUsers();
            }

            @Override
            public Page<UserResponseDTO> getAllPaginated(Pageable pageable) {
                return userService.getAllUsersPaginated(pageable);
            }

            @Override
            public UserResponseDTO save(UserUpdateRequestDTO request) {
                return userService.addUser(request);
            }

            @Override
            public UserResponseDTO update(Long id, UserUpdateRequestDTO request) {
                return userService.updateUser(id, request);
            }

            @Override
            public void delete(Long id) {
                userService.deleteUser(id);
            }
        };
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    public ResponseEntity<UserResponseDTO> getById(Long id) {
        return super.getById(id);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    public ResponseEntity<List<UserResponseDTO>> getAll() {
        return super.getAll();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    public ResponseEntity<Page<UserResponseDTO>> getAllPaginated(Pageable pageable) {
        return super.getAllPaginated(pageable);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponseDTO> getUserByEmail(@PathVariable String email) {
        UserResponseDTO user = userService.getUserByEmail(email);
        return ResponseBuilder.ok(user);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/stats")
    public ResponseEntity<UserStatsResponseDTO> getUserStats() {
        UserStatsResponseDTO stats = userService.getUserStats();
        return ResponseBuilder.ok(stats);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/search")
    public ResponseEntity<Page<UserResponseDTO>> searchUserByName(
            @RequestParam String name,
            Pageable pageable) {
        Page<UserResponseDTO> users = userService.findByNameContainingIgnoreCase(name, pageable);
        return ResponseBuilder.ok(users);
    }
}
