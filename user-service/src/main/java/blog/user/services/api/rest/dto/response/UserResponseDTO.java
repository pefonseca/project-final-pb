package blog.user.services.api.rest.dto.response;

import blog.user.services.api.rest.dto.request.UserUpdateRequestDTO;
import blog.user.services.domain.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String bio;
    private String city;
    private String createdUser;
    private LocalDateTime createDate;

    public User toUpdate(UserUpdateRequestDTO userUpdateRequestDTO) {
        return User.builder()
                    .id(this.id)
                    .firstName(userUpdateRequestDTO.getFirstName())
                    .lastName(userUpdateRequestDTO.getLastName())
                    .email(userUpdateRequestDTO.getEmail())
                    .password(this.password)
                    .bio(userUpdateRequestDTO.getBio())
                    .city(userUpdateRequestDTO.getCity())
                    .createDate(this.createDate)
                    .updateDate(LocalDateTime.now())
                    .build();
    }

}
