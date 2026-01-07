package com.accomputers.api.infrastructure.persistence.jpa.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import jakarta.persistence.criteria.Predicate;

import com.accomputers.api.application.dtos.PageDTO;
import com.accomputers.api.application.dtos.UserCriteria;
import com.accomputers.api.application.ports.output.repositories.UserRepositoryInterface;
import com.accomputers.api.domain.entities.User;
import com.accomputers.api.domain.valueobjects.Email;
import com.accomputers.api.infrastructure.persistence.jpa.entities.RoleEntity;
import com.accomputers.api.infrastructure.persistence.jpa.entities.UserEntity;
import com.accomputers.api.infrastructure.persistence.jpa.jpaRepositories.RoleJpaRepository;
import com.accomputers.api.infrastructure.persistence.jpa.jpaRepositories.UserJpaRepository;
import com.accomputers.api.infrastructure.persistence.jpa.mappers.UserMapper;

@Repository
public class UserRepository implements UserRepositoryInterface {

    private final UserJpaRepository jpaRepository;
    private final RoleJpaRepository roleJpaRepository;
    private final UserMapper mapper;

    public UserRepository(UserJpaRepository jpaRepository, RoleJpaRepository roleJpaRepository, UserMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.roleJpaRepository = roleJpaRepository;
        this.mapper = mapper;
    }

    @Override
    public PageDTO<User> findAll(UserCriteria userCriteria) {
        Pageable pageable = PageRequest.of(userCriteria.getPage() - 1, userCriteria.getPerPage());
        Specification<UserEntity> spec = buildSpecification(userCriteria);
        Page<UserEntity> page = jpaRepository.findAll(spec, pageable);
        List<User> users = page.getContent().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
        return new PageDTO<>(users, page.getTotalElements());
    }

    private Specification<UserEntity> buildSpecification(UserCriteria criteria) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Search filter (firstName, lastName, or email)
            if (criteria.getSearch() != null && !criteria.getSearch().trim().isEmpty()) {
                String searchTerm = "%" + criteria.getSearch().toLowerCase() + "%";
                Predicate firstNamePredicate = cb.like(cb.lower(root.get("firstName")), searchTerm);
                Predicate lastNamePredicate = cb.like(cb.lower(root.get("lastName")), searchTerm);
                Predicate emailPredicate = cb.like(cb.lower(root.get("email")), searchTerm);
                predicates.add(cb.or(firstNamePredicate, lastNamePredicate, emailPredicate));
            }

            // Role filter
            if (criteria.getRoleId() != null) {
                predicates.add(cb.equal(root.get("role").get("id"), criteria.getRoleId()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
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
        
        RoleEntity role = roleJpaRepository.findById(user.getRoleId())
                .orElse(null);
        savedEntity.setRole(role);

        return mapper.toDomain(savedEntity);
    }

    @Override
    public void delete(Integer id) {
        jpaRepository.deleteById(id);
    }
}
