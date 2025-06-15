package com.sportsclubmanager.backend;

import com.sportsclubmanager.backend.user.model.AffiliationStatus;
import com.sportsclubmanager.backend.user.model.Authority;
import com.sportsclubmanager.backend.user.model.Role;
import com.sportsclubmanager.backend.user.model.User;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserDataProvider {

    public static Role getRoleUser() {
        return new Role(
                1L,
                "ROLE_USER",
                new HashSet<>(List.of(new Authority(1L, "AUTHORITY_READ"))));
    }

    public static Authority getAuthorityRead() {
        return new Authority(1L, "AUTHORITY_READ");
    }

    public static Set<Role> getUserRoles() {
        return new HashSet<>(Set.of(getRoleUser()));
    }

    public static Set<Authority> getUserAuthorities() {
        return new HashSet<>(Set.of(getAuthorityRead()));
    }

    public static User getNewUser() {
        return new User(
                10L,
                98765432L,
                "Jane",
                "Doe",
                9876543210L,
                "janedoe@example.com",
                "janedoe",
                "Password123#",
                true,
                true,
                true,
                true,
                false,
                getUserRoles(),
                AffiliationStatus.ACTIVE);
    }

    public static User getNewInvalidUser() {
        return new User(
                15L,
                null,
                "Ax",
                "Na",
                null,
                "a@example.com",
                "aaaa#",
                "passw",
                true,
                true,
                true,
                true,
                false,
                getUserRoles(),
                AffiliationStatus.ACTIVE);
    }

    public static User getUser() {
        return new User(
                6L,
                12345678L,
                "John",
                "Doe",
                9876543210L,
                "johndoe@example.com",
                "johndoe",
                "Password456#",
                true,
                true,
                true,
                true,
                false,
                getUserRoles(),
                AffiliationStatus.ACTIVE);
    }

    public static User getUserWithId(Long id) {
        return new User(
                id,
                1234567890L,
                "Alice",
                "Johnson",
                9876543210L,
                "alice.johnson@example.com",
                "alicejohnson",
                "Passwordabc#",
                true,
                true,
                true,
                true,
                false,
                getUserRoles(),
                AffiliationStatus.ACTIVE);
    }

    public static List<User> getUsers() {
        return List.of(
                new User(
                        1L,
                        1234567890L,
                        "John",
                        "Doe",
                        9876543210L,
                        "john.doe@example.com",
                        "johndoe",
                        "Password123#",
                        true,
                        true,
                        true,
                        true,
                        false,
                        getUserRoles(),
                        AffiliationStatus.ACTIVE),
                new User(
                        2L,
                        2345678901L,
                        "Jane",
                        "Smith",
                        8765432109L,
                        "jane.smith@example.com",
                        "janesmith",
                        "Password456#",
                        true,
                        true,
                        true,
                        true,
                        false,
                        getUserRoles(),
                        AffiliationStatus.ACTIVE),
                new User(
                        3L,
                        3456789012L,
                        "Michael",
                        "Johnson",
                        7654321098L,
                        "michael.johnson@example.com",
                        "michaelj",
                        "Password789#",
                        true,
                        true,
                        true,
                        true,
                        false,
                        getUserRoles(),
                        AffiliationStatus.INACTIVE),
                new User(
                        4L,
                        4567890123L,
                        "Emily",
                        "Brown",
                        6543210987L,
                        "emily.brown@example.com",
                        "emilyb",
                        "Passwordabc#",
                        true,
                        true,
                        true,
                        true,
                        false,
                        getUserRoles(),
                        AffiliationStatus.ACTIVE),
                new User(
                        5L,
                        5678901234L,
                        "David",
                        "Wilson",
                        5432109876L,
                        "david.wilson@example.com",
                        "davidw",
                        "Passworddef#",
                        true,
                        true,
                        true,
                        true,
                        false,
                        getUserRoles(),
                        AffiliationStatus.SUSPENDED));
    }
}
