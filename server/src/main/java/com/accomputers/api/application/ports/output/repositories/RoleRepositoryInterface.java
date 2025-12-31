package com.accomputers.api.application.ports.output.repositories;

import com.accomputers.api.domain.entities.Role;

public interface RoleRepositoryInterface {
    Role save(Role role);
}
