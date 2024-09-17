package blog.user.services.api.rest.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequestDTO {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String bio;
    private String city;

}
