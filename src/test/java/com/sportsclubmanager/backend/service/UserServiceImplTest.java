package com.sportsclubmanager.backend.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.sportsclubmanager.backend.UserDataProvider;
import com.sportsclubmanager.backend.shared.util.RoleAuthorityUtils;
import com.sportsclubmanager.backend.user.dto.UserUpdateRequest;
import com.sportsclubmanager.backend.user.model.Role;
import com.sportsclubmanager.backend.user.model.User;
import com.sportsclubmanager.backend.user.repository.RoleRepository;
import com.sportsclubmanager.backend.user.repository.UserRepository;
import com.sportsclubmanager.backend.user.service.UserServiceImpl;
import com.sportsclubmanager.backend.user.validation.NoSpecialCharactersValidator;
import com.sportsclubmanager.backend.user.validation.PasswordStrengthValidator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @Test
    void givenValidUser_whenSave_thenUserIsSavedCorrectly() {
        // Given
        User mockNewUser = UserDataProvider.getNewUser();

        String rawPassword = mockNewUser.getPassword();

        String encodedPassword = "$2a$12$5g5ZWX9nZiHdysdHmzLyNOQVniecOoXbOe./JJ/fGiiPb.5dSSasy";

        PasswordStrengthValidator passwordStrengthValidator = new PasswordStrengthValidator();

        NoSpecialCharactersValidator noSpecialCharactersValidator = new NoSpecialCharactersValidator();

        Role mockRole = UserDataProvider.getRoleUser();

        // When
        when(roleRepository.findByName(anyString())).thenReturn(
                Optional.of(mockRole));
        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);

        userServiceImpl.save(mockNewUser);

        // Then
        verify(roleRepository, times(1)).findByName("ROLE_USER");
        verify(passwordEncoder, times(1)).encode(rawPassword);
        verify(userRepository, times(1)).save(any(User.class));

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();

        assertAll(
                () -> {
                    assertNotNull(
                            savedUser,
                            "El usuario guardado no debe ser nulo.");
                    assertEquals(
                            mockNewUser,
                            savedUser,
                            "El usuario guardado debe ser igual al usuario de prueba.");
                },
                () -> {
                    assertEquals(
                            RoleAuthorityUtils.getRoles(savedUser, roleRepository),
                            savedUser.getRoles(),
                            "Los roles del usuario guardado no son los esperados.");
                },
                () -> {
                    assertTrue(
                            passwordStrengthValidator.isValid(rawPassword, null),
                            "La contraseña debe cumplir con los requisitos de seguridad.");
                    assertEquals(
                            encodedPassword,
                            savedUser.getPassword(),
                            "La contraseña del usuario guardado no es la esperada.");
                },
                () -> {
                    assertNotNull(
                            savedUser.getId(),
                            "El id del usuario guardado no debe ser nulo.");
                    assertEquals(
                            10L,
                            savedUser.getId(),
                            "El id del usuario guardado no es el esperado.");
                    assertEquals(
                            "Jane",
                            savedUser.getName(),
                            "El nombre del usuario guardado no es el esperado.");
                },
                () -> {
                    assertTrue(
                            String.valueOf(savedUser.getNationalId()).length() >= 7 &&
                                    String.valueOf(savedUser.getNationalId()).length() <= 10,
                            "El número de identificación debe tener entre 7 y 10 dígitos.");
                    assertTrue(
                            String.valueOf(savedUser.getName()).length() >= 3 &&
                                    String.valueOf(savedUser.getName()).length() <= 20,
                            "El nombre debe tener entre 3 y 20 caracteres.");
                    assertTrue(
                            String.valueOf(savedUser.getLastName()).length() >= 3 &&
                                    String.valueOf(savedUser.getLastName()).length() <= 20,
                            "El apellido debe tener entre 3 y 20 caracteres.");
                    assertEquals(
                            10L,
                            String.valueOf(savedUser.getPhoneNumber()).length(),
                            "El número de teléfono debe tener 10 dígitos.");
                    assertTrue(
                            String.valueOf(savedUser.getEmail()).length() >= 16 &&
                                    String.valueOf(savedUser.getEmail()).length() <= 40,
                            "El correo electrónico debe tener entre 16 y 40 caracteres.");
                    assertTrue(
                            String.valueOf(savedUser.getUsername()).length() >= 6 &&
                                    String.valueOf(savedUser.getUsername()).length() <= 20,
                            "El nombre de usuario debe tener entre 6 y 20 caracteres.");
                    assertTrue(
                            noSpecialCharactersValidator.isValid(
                                    savedUser.getUsername(),
                                    null),
                            "El nombre de usuario no debe contener caracteres especiales.");
                    assertTrue(
                            String.valueOf(savedUser.getPassword()).length() >= 6 &&
                                    String.valueOf(savedUser.getPassword()).length() <= 60,
                            "La contraseña debe tener entre 6 y 60 caracteres.");
                    assertNotNull(
                            savedUser.getRoles(),
                            "Los roles no deben ser nulos.");
                    assertFalse(
                            savedUser.getRoles().isEmpty(),
                            "Los roles no deben estar vacíos.");
                    assertNotNull(
                            savedUser.getAffiliationStatus(),
                            "El estado de afiliación no debe ser nulo.");
                });
    }

    @Test
    void givenInvalidUser_whenSave_thenUserIsSavedWithInvalidFields() {
        // Given
        User mockNewInvalidUser = UserDataProvider.getNewInvalidUser();

        String rawPassword = mockNewInvalidUser.getPassword();

        String encodedPassword = "$2a$12$5g5ZWX9nZiHdysdHmzLyNOQVniecOoXbOe./JJ/fGiiPb.5dSSasy";

        PasswordStrengthValidator passwordStrengthValidator = new PasswordStrengthValidator();

        NoSpecialCharactersValidator noSpecialCharactersValidator = new NoSpecialCharactersValidator();

        Role mockRole = UserDataProvider.getRoleUser();

        when(roleRepository.findByName(anyString())).thenReturn(
                Optional.of(mockRole));
        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);

        // When
        userServiceImpl.save(mockNewInvalidUser);

        // Then
        verify(roleRepository, times(1)).findByName("ROLE_USER");
        verify(passwordEncoder, times(1)).encode(rawPassword);
        verify(userRepository, times(1)).save(any(User.class));

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();

        assertAll(
                () -> {
                    assertNotNull(
                            savedUser,
                            "El usuario guardado no debe ser nulo.");
                    assertEquals(
                            mockNewInvalidUser,
                            savedUser,
                            "El usuario guardado debe ser igual al usuario de prueba.");
                },
                () -> {
                    assertEquals(
                            RoleAuthorityUtils.getRoles(savedUser, roleRepository),
                            savedUser.getRoles(),
                            "Los roles del usuario guardado no son los esperados.");
                },
                () -> {
                    assertFalse(
                            passwordStrengthValidator.isValid(rawPassword, null),
                            "La contraseña no debe cumplir con los requisitos de seguridad.");
                    assertEquals(
                            encodedPassword,
                            savedUser.getPassword(),
                            "La contraseña del usuario guardado no es la esperada.");
                },
                () -> {
                    assertNotNull(
                            savedUser.getId(),
                            "El id del usuario guardado no debe ser nulo.");
                    assertEquals(
                            15L,
                            savedUser.getId(),
                            "El id del usuario guardado no es el esperado.");
                    assertEquals(
                            "Ax",
                            savedUser.getName(),
                            "El nombre del usuario guardado no es el esperado.");
                },
                () -> {
                    assertTrue(
                            String.valueOf(savedUser.getNationalId()).length() < 7 ||
                                    String.valueOf(savedUser.getNationalId()).length() > 10,
                            "El número de identificación debe ser inválido (no entre 7 y 10 dígitos).");
                    assertTrue(
                            String.valueOf(savedUser.getName()).length() < 3 ||
                                    String.valueOf(savedUser.getName()).length() > 20,
                            "El nombre debe ser inválido (no entre 3 y 20 caracteres).");
                    assertTrue(
                            String.valueOf(savedUser.getLastName()).length() < 3 ||
                                    String.valueOf(savedUser.getLastName()).length() > 20,
                            "El apellido debe ser inválido (no entre 3 y 20 caracteres).");
                    assertNotEquals(
                            10L,
                            String.valueOf(savedUser.getPhoneNumber()).length(),
                            "El número de teléfono debe ser inválido (no 10 dígitos).");
                    assertTrue(
                            String.valueOf(savedUser.getEmail()).length() < 16 ||
                                    String.valueOf(savedUser.getEmail()).length() > 40,
                            "El correo electrónico debe ser inválido (no entre 16 y 40 caracteres).");
                    assertTrue(
                            String.valueOf(savedUser.getUsername()).length() < 6 ||
                                    String.valueOf(savedUser.getUsername()).length() > 20,
                            "El nombre de usuario debe ser inválido (no entre 6 y 20 caracteres).");
                    assertFalse(
                            noSpecialCharactersValidator.isValid(
                                    savedUser.getUsername(),
                                    null),
                            "El nombre de usuario debe contener caracteres especiales (inválido).");
                    assertTrue(
                            String.valueOf(rawPassword).length() < 6 ||
                                    String.valueOf(rawPassword).length() > 60,
                            "La contraseña debe ser inválida (no entre 6 y 60 caracteres).");
                    assertNotNull(
                            savedUser.getRoles(),
                            "Los roles no deben ser nulos.");
                    assertFalse(
                            savedUser.getRoles().isEmpty(),
                            "Los roles no deben estar vacíos.");
                    assertNotNull(
                            savedUser.getAffiliationStatus(),
                            "El estado de afiliación no debe ser nulo.");
                });
    }

    @Test
    void givenNullUser_whenSave_thenThrows() {
        // Given
        User nullUser = null;

        // When & Then
        assertThrows(
                NullPointerException.class,
                () -> userServiceImpl.save(nullUser),
                "Guardar un usuario nulo debe lanzar NullPointerException");
    }

    @Test
    void givenExistingUserId_whenFindById_thenReturnsUser() {
        // Given
        Long userId = 6L;
        User expectedUser = UserDataProvider.getUser();
        when(userRepository.findById(userId)).thenReturn(
                Optional.of(expectedUser));

        // When
        Optional<User> resultOpt = userServiceImpl.findById(userId);

        // Then
        assertAll(
                "El usuario devuelto debe tener los datos correctos",
                () -> assertTrue(
                        resultOpt.isPresent(),
                        "El usuario debe existir (Optional presente)"),
                () -> {
                    User actualUser = resultOpt.orElseThrow();
                    assertNotNull(actualUser, "El usuario no debe ser nulo");
                    assertEquals(
                            expectedUser,
                            actualUser,
                            "El usuario devuelto debe coincidir con el esperado (equals)");
                    assertEquals(
                            expectedUser.getId(),
                            actualUser.getId(),
                            "El id debe coincidir");
                    assertEquals(
                            expectedUser.getName(),
                            actualUser.getName(),
                            "El nombre debe coincidir");
                    assertEquals(
                            expectedUser.getLastName(),
                            actualUser.getLastName(),
                            "El apellido debe coincidir");
                    assertEquals(
                            expectedUser.getUsername(),
                            actualUser.getUsername(),
                            "El nombre de usuario debe coincidir");
                    assertEquals(
                            expectedUser.getEmail(),
                            actualUser.getEmail(),
                            "El correo electrónico debe coincidir");
                });

        verify(userRepository, times(1)).findById(userId);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void givenNonExistingUserId_whenFindById_thenReturnsEmptyOptional() {
        // Given
        Long id = 999L;
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        // When
        Optional<User> result = userServiceImpl.findById(id);

        // Then
        assertAll(
                "Validar el comportamiento cuando el usuario no existe",
                () -> assertNotNull(result, "El Optional devuelto no debe ser nulo"),
                () -> assertTrue(
                        result.isEmpty(),
                        "Si el usuario no existe, el Optional debe estar vacío"),
                () -> assertFalse(
                        result.isPresent(),
                        "Si el usuario no existe, el Optional no debe estar presente"));

        verify(userRepository, times(1)).findById(id);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void givenUsersInRepository_whenFindAll_thenReturnsUserListWithCorrectData() {
        // Given
        List<User> expectedUsers = UserDataProvider.getUsers();
        when(userRepository.findAll()).thenReturn(expectedUsers);

        // When
        List<User> result = userServiceImpl.findAll();

        // Then
        assertAll(
                "Validar la lista de usuarios devuelta",
                () -> assertNotNull(result, "La lista no debe ser nula"),
                () -> assertEquals(
                        expectedUsers.size(),
                        result.size(),
                        "El tamaño de la lista no coincide con el esperado"),
                () -> assertIterableEquals(
                        expectedUsers,
                        result,
                        "La lista de usuarios devuelta debe coincidir con la esperada"),
                () -> assertTrue(
                        result.stream().allMatch(Objects::nonNull),
                        "La lista no debe contener usuarios nulos"),
                () -> assertEquals(
                        result.stream().map(User::getId).distinct().count(),
                        result.size(),
                        "Los IDs de usuario deben ser únicos"),
                // Validar campos del primer usuario de la lista
                () -> {
                    User first = result.get(0);
                    assertEquals(
                            "John",
                            first.getName(),
                            "El nombre del primer usuario es incorrecto");
                    assertEquals(
                            "Doe",
                            first.getLastName(),
                            "El apellido del primer usuario es incorrecto");
                    assertEquals(
                            "johndoe",
                            first.getUsername(),
                            "El nombre de usuario del primer usuario es incorrecto");
                });

        verify(userRepository, times(1)).findAll();
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void givenExistingUserIdAndValidUpdateRequest_whenUpdate_thenUserIsUpdatedCorrectly() {
        // Given
        Long userId = 10L;
        User existingUser = UserDataProvider.getUserWithId(userId);

        UserUpdateRequest updateRequest = new UserUpdateRequest();
        updateRequest.setName("Nuevo Nombre");
        updateRequest.setLastName("Nuevo Apellido");
        updateRequest.setPhoneNumber(1234567890L);
        updateRequest.setEmail("nuevo@email.com");
        updateRequest.setUsername("nuevousuario");
        updateRequest.setRoles(existingUser.getRoles());

        when(userRepository.findById(userId)).thenReturn(
                Optional.of(existingUser));
        when(roleRepository.findByName(anyString())).thenReturn(
                Optional.of(UserDataProvider.getRoleUser()));
        when(userRepository.save(any(User.class))).thenAnswer(
                // Devuelve el mismo usuario que se le pasa como argumento
                invocation -> invocation.getArgument(0));

        // When
        Optional<User> resultOpt = userServiceImpl.update(
                userId,
                updateRequest);

        // Then
        assertTrue(
                resultOpt.isPresent(),
                "El usuario actualizado debe estar presente");

        User updatedUser = resultOpt.get();

        assertEquals("Nuevo Nombre", updatedUser.getName());
        assertEquals("Nuevo Apellido", updatedUser.getLastName());
        assertEquals(1234567890L, updatedUser.getPhoneNumber());
        assertEquals("nuevo@email.com", updatedUser.getEmail());
        assertEquals("nuevousuario", updatedUser.getUsername());
        assertNotNull(updatedUser.getRoles());

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(existingUser);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void givenNonExistingUserId_whenUpdate_thenReturnsEmptyOptional() {
        // Given
        Long userId = 999L;
        UserUpdateRequest updateRequest = new UserUpdateRequest();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When
        Optional<User> resultOpt = userServiceImpl.update(
                userId,
                updateRequest);

        // Then
        assertTrue(
                resultOpt.isEmpty(),
                "Debe retornar Optional.empty() si el usuario no existe");
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, never()).save(any(User.class));
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void givenValidUserId_whenDeleteById_thenUserIsDeleted() {
        // Given
        Long id = 1L;

        // When
        userServiceImpl.deleteById(id);

        // Then
        ArgumentCaptor<Long> longCaptor = ArgumentCaptor.forClass(Long.class);

        verify(userRepository).deleteById(anyLong());
        verify(userRepository).deleteById(longCaptor.capture());
        assertEquals(1L, longCaptor.getValue());
    }

    @Test
    void givenNonExistingUserId_whenDeleteById_thenNoExceptionAndDeleteCalled() {
        // Given
        Long id = 999L; // No existe

        // When
        userServiceImpl.deleteById(id);

        // Then
        ArgumentCaptor<Long> longCaptor = ArgumentCaptor.forClass(Long.class);

        verify(userRepository).deleteById(anyLong());
        verify(userRepository).deleteById(longCaptor.capture());
        assertEquals(999L, longCaptor.getValue());
    }
}
