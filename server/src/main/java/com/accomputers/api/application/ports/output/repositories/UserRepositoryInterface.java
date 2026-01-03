package com.accomputers.api.application.ports.output.repositories;

import com.accomputers.api.application.dtos.PageDTO;
import com.accomputers.api.application.dtos.UserCriteria;
import com.accomputers.api.domain.entities.User;
import com.accomputers.api.domain.valueobjects.Email;

public interface UserRepositoryInterface {
    PageDTO<User> findAll(UserCriteria userCriteria);
    User findById(Integer id);
    User findByEmail(Email email);
    User save(User user);
    void delete(Integer id);
}
