package com.accomputers.api.application.ports.output.repositories;

import com.accomputers.api.domain.entities.Role;

public interface RoleRepositoryInterface {
    Role findById(Integer id);
    Role save(Role role);
}
