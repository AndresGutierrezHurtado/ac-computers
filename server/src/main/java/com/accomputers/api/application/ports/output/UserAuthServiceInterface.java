package com.accomputers.api.application.ports.output;

import com.accomputers.api.domain.entities.User;

public interface UserAuthServiceInterface {
    public void authenticateUser(User user);
    public User getAuthenticatedUser();
    public void refreshSession(User user);
    public void revokeSession(User user);
    public void invalidateAllSessions(User user);
    public void logoutUser(User user);
}
