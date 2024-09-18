package blog.user.services.domain.model.entity;

import blog.user.services.api.rest.dto.response.UserResponseDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "tb_user")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private String bio;
    private String city;
    @Enumerated(EnumType.STRING)
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getUsername() {
        return email;
    }

    public UserResponseDTO toDTO() {
        return UserResponseDTO.builder()
                              .id(this.id)
                              .firstName(this.firstName)
                              .lastName(this.lastName)
                              .email(this.email)
                              .password(this.password)
                              .bio(this.bio)
                              .city(this.city)
                              .createDate(this.createDate)
                              .build();
    }
}
