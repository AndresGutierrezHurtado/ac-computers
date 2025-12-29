package com.accomputers.api.application.ports.input;

import com.accomputers.api.application.dtos.auth.LoginDTO;
import com.accomputers.api.domain.entities.User;
import java.util.List;

public interface AuthUseCase {
    User login(LoginDTO loginDTO);
    User registerUser(String firstName, String lastName, String email, String password, Integer roleId);
    User getUserById(Integer id);
    List<User> getAllUsers();
    User updateUser(Integer id, String firstName, String lastName, String email, Integer roleId);
    void deleteUser(Integer id);
}

