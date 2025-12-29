package com.accomputers.api.infrastructure.persistence.jpa.repositories;

import com.accomputers.api.application.ports.output.repositories.UserRepositoryInterface;
import com.accomputers.api.domain.entities.User;
import com.accomputers.api.domain.valueobjects.Email;
import com.accomputers.api.infrastructure.persistence.jpa.entities.UserEntity;
import com.accomputers.api.infrastructure.persistence.jpa.jpaRepositories.UserJpaRepository;
import com.accomputers.api.infrastructure.persistence.jpa.mappers.UserMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class UserRepository implements UserRepositoryInterface {

    private final UserJpaRepository jpaRepository;
    private final UserMapper mapper;

    public UserRepository(UserJpaRepository jpaRepository, UserMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public List<User> findAll() {
        return jpaRepository.findAll().stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public User findById(Integer id) {
        return jpaRepository.findById(id)
            .map(mapper::toDomain)
            .orElse(null);
    }

    @Override
    public User findByEmail(Email email) {
        if (email == null) {
            return null;
        }
        return jpaRepository.findByEmail(email.getValue())
            .map(mapper::toDomain)
            .orElse(null);
    }

    @Override
    public User save(User user) {
        if (user == null) {
            return null;
        }

        UserEntity entity = mapper.toEntity(user);
        UserEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public void delete(Integer id) {
        jpaRepository.deleteById(id);
    }
}

