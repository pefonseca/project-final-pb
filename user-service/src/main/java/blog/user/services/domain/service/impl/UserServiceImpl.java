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

        producer.integration(auditMessageDTO);

        return userDB;
    }

    /**
     * @param id
     * @return
     */
    @Override
    public UserResponseDTO findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Usuário não encontrado.")).toDTO();
    }

    /**
     * @param userUpdateRequestDTO
     * @param id
     * @return
     */
    @Override
    public UserResponseDTO update(UserUpdateRequestDTO userUpdateRequestDTO, Long id) {

        UserResponseDTO userDB = findById(id);

        var userSaved = userDB.toUpdate(userUpdateRequestDTO);

        AuditMessageDTO auditMessageDTO = AuditMessageDTO.builder()
                                                         .entityName("USER")
                                                         .entityId(1L)
                                                         .action("UPDATE")
                                                         .performedBy(userDB.getFirstName())
                                                         .details("Usuário atualizando com sucesso.")
                                                         .build();

        producer.integration(auditMessageDTO);

        return repository.save(userSaved).toDTO();
    }

    /**
     * @param email
     * @return
     */
    @Override
    public UserResponseDTO findByEmail(String email) {
        return repository.findByEmail(email)
                         .orElseThrow(() -> new RuntimeException("User not found,."))
                         .toDTO();
    }

    /**
     * @param payload
     */
    @Override
    public void errorAuditLog(String payload) {
        System.err.println(" ===== RESPOSTA ERRO AUDITORIA ===== " + payload);
        log.error("[UserServiceImpl] -> (successAuditLog): Erro ao fazer auditoria.");
    }

    /**
     * @param payload
     */
    @Override
    public void successAuditLog(String payload) {
        System.out.println(" ===== RESPOSTA SUCESSO AUDITORIA ===== " + payload);
        log.info("[UserServiceImpl] -> (successAuditLog): Sucesso ao fazer auditoria.");
    }
}
