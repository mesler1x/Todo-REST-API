package com.roslabsystem.todo.domain.user;

import com.roslabsystem.todo.domain.common.BaseDomainEntity;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Type;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "user_info")
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class UserEntity extends BaseDomainEntity implements UserDetails {
    String username;
    String password;
    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    Set<UserRole> authorities;
    boolean accountNonExpired;
    boolean credentialsNonExpired;
    boolean enabled;
    boolean accountNonLocked;

    public static UserEntity from(Context context) {
        UserEntity user = new UserEntity();
        user.username = context.username();
        user.password = context.password;
        Set<UserRole> defaultRole = new HashSet<>();
        defaultRole.add(UserRole.ROLE_USER);
        user.authorities = defaultRole;
        user.accountNonExpired = true;
        user.credentialsNonExpired = true;
        user.enabled = true;
        user.accountNonLocked = true;
        return user;
    }

    public record Context(String username,
                          String password) {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity user = (UserEntity) o;
        return id != null && id.equals(user.getId());
    }

    @Override
    public int hashCode() {
        return 31;
    }
}
