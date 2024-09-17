package blog.user.services.domain.service.impl;

import blog.user.services.api.rest.dto.request.UserCreateRequestDTO;
import blog.user.services.api.rest.dto.request.UserUpdateRequestDTO;
import blog.user.services.api.rest.dto.response.UserResponseDTO;
import blog.user.services.domain.model.dto.AuditMessageDTO;
import blog.user.services.domain.producer.AuditLogRequestProducer;
import blog.user.services.domain.service.UserService;
import blog.user.services.infra.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final AuditLogRequestProducer producer;

    /**
     * @param userCreateRequestDTO
     * @return
     */
    @Override
    public UserResponseDTO create(UserCreateRequestDTO userCreateRequestDTO) {
        log.info("[UserServiceImpl] -> (create): Criando usuário com dados: {}", userCreateRequestDTO);
        UserResponseDTO userDB;
        userDB = repository.save(userCreateRequestDTO.toEntity()).toDTO();
        userDB.setCreatedUser("Usuário criado com sucesso.");

        AuditMessageDTO auditMessageDTO = AuditMessageDTO.builder()
                                                         .entityName("USER")
                                                         .entityId(1L)
                                                         .action("CREATE")
                                                         .performedBy(userDB.getFirstName())
                                                         .details("Usuário criado com sucesso.")
                                                         .build();

        log.info("[UserServiceImpl] -> (create): Enviando mensagem de auditoria: {}", auditMessageDTO);
        producer.integration(auditMessageDTO);

        return userDB;
    }

    /**
     * @param id
     * @return
     */
    @Override
    public UserResponseDTO findById(Long id) {
        log.info("[UserServiceImpl] -> (findById): Buscando usuário com ID: {}", id);
        UserResponseDTO userResponseDTO = repository.findById(id)
                                                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado."))
                                                    .toDTO();

        log.info("[UserServiceImpl] -> (findById): Usuário encontrado: {}", userResponseDTO);
        return userResponseDTO;
    }

    /**
     * @param userUpdateRequestDTO
     * @param id
     * @return
     */
    @Override
    public UserResponseDTO update(UserUpdateRequestDTO userUpdateRequestDTO, Long id) {
        log.info("[UserServiceImpl] -> (update): Atualizando usuário com ID: {} e dados: {}", id, userUpdateRequestDTO);

        UserResponseDTO userDB = findById(id);

        var userSaved = userDB.toUpdate(userUpdateRequestDTO);

        AuditMessageDTO auditMessageDTO = AuditMessageDTO.builder()
                                                         .entityName("USER")
                                                         .entityId(1L)
                                                         .action("UPDATE")
                                                         .performedBy(userDB.getFirstName())
                                                         .details("Usuário atualizando com sucesso.")
                                                         .build();

        log.info("[UserServiceImpl] -> (update): Enviando mensagem de auditoria: {}", auditMessageDTO);
        producer.integration(auditMessageDTO);

        log.info("[UserServiceImpl] -> (update): Usuário atualizado com sucesso: {}", userSaved);
        return repository.save(userSaved).toDTO();
    }

    /**
     * @param email
     * @return
     */
    @Override
    public UserResponseDTO findByEmail(String email) {
        log.info("[UserServiceImpl] -> (findByEmail): Buscando usuário com e-mail: {}", email);
        UserResponseDTO userResponseDTO = repository.findByEmail(email)
                                                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado."))
                                                    .toDTO();

        log.info("[UserServiceImpl] -> (findByEmail): Usuário encontrado: {}", userResponseDTO);
        return userResponseDTO;
    }
}
