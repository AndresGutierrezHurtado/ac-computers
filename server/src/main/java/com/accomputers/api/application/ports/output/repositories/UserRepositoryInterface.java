package com.accomputers.api.application.ports.output.repositories;

import com.accomputers.api.domain.entities.User;
import com.accomputers.api.domain.valueobjects.Email;
import java.util.List;

public interface UserRepositoryInterface {
    List<User> findAll();
    User findById(Integer id);
    User findByEmail(Email email);
    User save(User user);
    void delete(Integer id);
}
