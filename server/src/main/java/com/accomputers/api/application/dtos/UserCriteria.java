package com.accomputers.api.application.dtos;

/**
 * Criteria for filtering and paginating users.
 * Follows hexagonal architecture principles by being in the application layer.
 */
public class UserCriteria {
    private Integer page;
    private Integer perPage;
    private String search;
    private Integer roleId;

    public UserCriteria() {
        this.page = 1;
        this.perPage = 10;
    }

    public UserCriteria(Integer page, Integer perPage, String search, Integer roleId) {
        this.page = page != null && page > 0 ? page : 1;
        this.perPage = perPage != null && perPage > 0 ? perPage : 10;
        this.search = search;
        this.roleId = roleId;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page != null && page > 0 ? page : 1;
    }

    public Integer getPerPage() {
        return perPage;
    }

    public void setPerPage(Integer perPage) {
        this.perPage = perPage != null && perPage > 0 ? perPage : 10;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }
}

