package com.accomputers.api.domain.entities;

import com.accomputers.api.domain.valueobjects.Email;
import com.accomputers.api.domain.valueobjects.Password;

public class User {
    private Integer id;
    private String firstName;
    private String lastName;
    private Email email;
    private Password password;
    private Integer roleId;
    private Role role;

    public User(Integer id, String firstName, String lastName, Email email, Password password, Integer roleId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.roleId = roleId;
    }

    // Getters
    public Integer getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Email getEmail() {
        return email;
    }

    public Password getPassword() {
        return password;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public Role getRole() {
        return role;
    }

    // Setters
    public void setId(Integer id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(Email email) {
        this.email = email;
    }

    public void setPassword(Password password) {
        this.password = password;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
