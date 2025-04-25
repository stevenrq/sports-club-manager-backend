package com.sportsclubmanager.backend.mapper;

import com.sportsclubmanager.backend.model.Role;
import com.sportsclubmanager.backend.model.User;
import com.sportsclubmanager.backend.model.dto.UserResponse;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-25T10:36:09-0500",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.42.0.z20250331-1358, environment: Java 21.0.6 (Eclipse Adoptium)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserResponse toUserResponse(User user) {
        if ( user == null ) {
            return null;
        }

        UserResponse userResponse = new UserResponse();

        userResponse.setId( user.getId() );
        userResponse.setName( user.getName() );
        userResponse.setLastName( user.getLastName() );
        userResponse.setPhoneNumber( user.getPhoneNumber() );
        userResponse.setEmail( user.getEmail() );
        userResponse.setUsername( user.getUsername() );
        Set<Role> set = user.getRoles();
        if ( set != null ) {
            userResponse.setRoles( new LinkedHashSet<Role>( set ) );
        }

        return userResponse;
    }
}
