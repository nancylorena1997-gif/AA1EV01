package com.newpohone.modules.autenticacion.application;

import com.newpohone.modules.autenticacion.domain.AuthException;
import com.newpohone.modules.autenticacion.infrastructure.AuthRepository;
import java.util.Map;
import java.util.regex.Pattern;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$", Pattern.CASE_INSENSITIVE);

    private final AuthRepository authRepository;

    public AuthService(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public Map<String, Object> login(String email, String password) {
        String error = validateCredentials(email, password);
        if (error != null) {
            throw new AuthException(error);
        }
        Map<String, Object> user = authRepository.authenticate(email, password);
        if (user == null) {
            throw new AuthException("El correo o la contraseña son incorrectos.");
        }
        return user;
    }

    public Map<String, Object> register(String nombre, String telefono, String email,
            String password, String confirmation, String cedula) {
        String error = validateRegistration(nombre, telefono, email, password, confirmation, cedula);
        if (error != null) {
            throw new AuthException(error);
        }
        return authRepository.register(Integer.parseInt(cedula.trim()), nombre, telefono, email, password);
    }

    public String validateCredentials(String email, String password) {
        if (email == null || email.isBlank() || password == null || password.isBlank()) {
            return "Ingresa el correo y la contraseña.";
        }
        return null;
    }

    public String validateRegistration(String nombre, String telefono, String email,
            String password, String confirmation, String cedula) {
        if (nombre == null || nombre.trim().length() < 3 || telefono == null || telefono.trim().length() < 7) {
            return "Ingresa un nombre y un teléfono válidos.";
        }
        if (email == null || !EMAIL_PATTERN.matcher(email.trim()).matches()) {
            return "El correo electrónico no es válido.";
        }
        if (password == null || password.length() < 6) {
            return "La contraseña debe tener al menos 6 caracteres.";
        }
        if (!password.equals(confirmation)) {
            return "Las contraseñas no coinciden.";
        }
        try {
            Integer.parseInt(cedula == null ? "" : cedula.trim());
        } catch (NumberFormatException exception) {
            return "La cédula no es válida.";
        }
        return null;
    }

    public String safeNext(String next) {
        if (next == null || next.isBlank()) {
            return null;
        }
        String value = next.trim();
        if (!value.startsWith("/") || value.startsWith("//") || value.contains("://") || value.contains("\\")) {
            return null;
        }
        return value;
    }
}
