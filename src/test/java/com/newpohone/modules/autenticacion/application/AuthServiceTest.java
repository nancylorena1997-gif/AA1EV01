package com.newpohone.modules.autenticacion.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.newpohone.modules.autenticacion.domain.AuthException;
import com.newpohone.modules.autenticacion.infrastructure.AuthRepository;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthRepository authRepository;

    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthService(authRepository);
    }

    @Test
    void rejectsBlankCredentials() {
        assertEquals("Ingresa el correo y la contraseña.", authService.validateCredentials("", "x"));
        AuthException exception = assertThrows(AuthException.class, () -> authService.login(" ", " "));
        assertEquals("Ingresa el correo y la contraseña.", exception.getMessage());
    }

    @Test
    void authenticatesValidUser() {
        Map<String, Object> user = Map.of("email", "admin@newphone.com", "rol", "ADMINISTRADOR");
        when(authRepository.authenticate("admin@newphone.com", "admin123")).thenReturn(user);

        assertEquals(user, authService.login("admin@newphone.com", "admin123"));
        verify(authRepository).authenticate("admin@newphone.com", "admin123");
    }

    @Test
    void rejectsUnknownCredentials() {
        when(authRepository.authenticate("a@b.co", "x")).thenReturn(null);
        AuthException exception = assertThrows(AuthException.class, () -> authService.login("a@b.co", "x"));
        assertEquals("El correo o la contraseña son incorrectos.", exception.getMessage());
    }

    @Test
    void validatesRegistrationFields() {
        assertEquals("Ingresa un nombre y un teléfono válidos.",
                authService.validateRegistration("Al", "300", "a@b.co", "123456", "123456", "1010"));
        assertEquals("El correo electrónico no es válido.",
                authService.validateRegistration("Ana Pérez", "3001001001", "correo", "123456", "123456", "1010"));
        assertEquals("La contraseña debe tener al menos 6 caracteres.",
                authService.validateRegistration("Ana Pérez", "3001001001", "ana@newphone.com", "123", "123", "1010"));
        assertEquals("Las contraseñas no coinciden.",
                authService.validateRegistration("Ana Pérez", "3001001001", "ana@newphone.com", "123456", "654321", "1010"));
        assertEquals("La cédula no es válida.",
                authService.validateRegistration("Ana Pérez", "3001001001", "ana@newphone.com", "123456", "123456", "abc"));
        assertNull(authService.validateRegistration("Ana Pérez", "3001001001", "ana@newphone.com", "123456", "123456", "1010"));
    }

    @Test
    void safeNextRejectsExternalUrls() {
        assertEquals("/catalog", authService.safeNext("/catalog"));
        assertNull(authService.safeNext("https://evil.test"));
        assertNull(authService.safeNext("//evil.test"));
        assertNull(authService.safeNext(""));
    }
}
