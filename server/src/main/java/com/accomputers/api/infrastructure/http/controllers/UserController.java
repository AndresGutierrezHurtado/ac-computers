package com.accomputers.api.infrastructure.http.controllers;

// Spring
import com.accomputers.api.application.dtos.PageDTO;
import com.accomputers.api.application.dtos.UserFiltersDTO;
import com.accomputers.api.application.dtos.response.UserResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

// Application
import com.accomputers.api.application.dtos.auth.UpdateUserDTO;
import com.accomputers.api.application.dtos.auth.InviteUserDTO;
import com.accomputers.api.application.ports.input.UserServiceInterface;
import com.accomputers.api.infrastructure.http.responses.PaginatedResponseDTO;

// Infrastructure
import com.accomputers.api.infrastructure.http.responses.ResponseDTO;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserServiceInterface userServiceInterface;

    @Autowired
    public UserController(UserServiceInterface userServiceInterface) {
        this.userServiceInterface = userServiceInterface;
    }

    @GetMapping
    public ResponseEntity<PaginatedResponseDTO<UserResponseDTO>> getAllUsers(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer perPage,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Integer roleId) {

        UserFiltersDTO queryParams = new UserFiltersDTO();
        queryParams.setPage(page);
        queryParams.setPerPage(perPage);
        queryParams.setSearch(search);
        queryParams.setRoleId(roleId);

        PageDTO<UserResponseDTO> users = userServiceInterface.getAllUsers(queryParams);

        PaginatedResponseDTO<UserResponseDTO> responseDTO = new PaginatedResponseDTO<>(
                "Users retrieved successfully",
                true,
                users.total(),
                users.data());

        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<UserResponseDTO>> getUserById(@PathVariable Integer id) {
        UserResponseDTO user = userServiceInterface.getUserById(id);

        ResponseDTO<UserResponseDTO> responseDTO = new ResponseDTO<>(
                "User retrieved successfully",
                true,
                user);

        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/invite")
    public ResponseEntity<ResponseDTO<UserResponseDTO>> inviteUser(
            @RequestBody @Valid InviteUserDTO inviteUserDTO) {
        UserResponseDTO user = userServiceInterface.inviteUser(inviteUserDTO);

        ResponseDTO<UserResponseDTO> responseDTO = new ResponseDTO<>(
                "User invited successfully",
                true,
                user);

        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO<UserResponseDTO>> updateUser(
            @PathVariable Integer id,
            @RequestBody @Valid UpdateUserDTO updateUserDTO) {

        UserResponseDTO user = userServiceInterface.updateUser(id, updateUserDTO);

        ResponseDTO<UserResponseDTO> responseDTO = new ResponseDTO<>(
                "User updated successfully",
                true,
                user);

        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<Void>> deleteUser(@PathVariable Integer id) {
        userServiceInterface.deleteUser(id);

        ResponseDTO<Void> responseDTO = new ResponseDTO<>(
                "User deleted successfully",
                true);

        return ResponseEntity.ok(responseDTO);
    }
}
