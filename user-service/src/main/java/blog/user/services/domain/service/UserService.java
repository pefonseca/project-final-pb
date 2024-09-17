package blog.user.services.domain.service;

import blog.user.services.api.rest.dto.request.UserCreateRequestDTO;
import blog.user.services.api.rest.dto.request.UserUpdateRequestDTO;
import blog.user.services.api.rest.dto.response.UserResponseDTO;

public interface UserService {

    UserResponseDTO create(UserCreateRequestDTO userCreateRequestDTO);
    UserResponseDTO findById(Long id);
    UserResponseDTO update(UserUpdateRequestDTO userUpdateRequestDTO, Long id);
    UserResponseDTO findByEmail(String email);
    void errorAuditLog(String payload);
    void successAuditLog(String payload);
}
