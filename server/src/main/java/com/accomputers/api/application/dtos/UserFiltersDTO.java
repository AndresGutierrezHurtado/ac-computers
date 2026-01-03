package com.accomputers.api.application.dtos;

/**
 * DTO for receiving user query parameters from HTTP requests.
 * This DTO is used in the infrastructure layer (controllers) and then
 * transformed to UserCriteria in the application layer.
 */
public class UserFiltersDTO {
    private Integer page;
    private Integer perPage;
    private String search;
    private Integer roleId;

    public UserFiltersDTO() {
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPerPage() {
        return perPage;
    }

    public void setPerPage(Integer perPage) {
        this.perPage = perPage;
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

    /**
     * Transforms this DTO to UserCriteria.
     * @return UserCriteria instance with the same values
     */
    public UserCriteria toUserCriteria() {
        return new UserCriteria(
            this.page,
            this.perPage,
            this.search,
            this.roleId
        );
    }
}

