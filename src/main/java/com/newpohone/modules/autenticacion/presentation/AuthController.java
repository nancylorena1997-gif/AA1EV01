package com.newpohone.modules.autenticacion.presentation;

import com.newpohone.modules.autenticacion.application.AuthService;
import com.newpohone.modules.autenticacion.domain.AuthException;
import com.newpohone.shared.presentation.SessionUsers;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.Map;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login")
    public String loginPage(@RequestParam(required = false) String next,
            HttpSession session,
            Model model) {
        String safeNext = authService.safeNext(next);
        model.addAttribute("next", safeNext);
        Map<String, Object> user = SessionUsers.current(session);
        if (user != null) {
            return redirectAfterAuth(user, safeNext);
        }
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerPage(@RequestParam(required = false) String next,
            HttpSession session,
            Model model) {
        String safeNext = authService.safeNext(next);
        model.addAttribute("next", safeNext);
        Map<String, Object> user = SessionUsers.current(session);
        if (user != null) {
            return redirectAfterAuth(user, safeNext);
        }
        return "auth/register";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
            @RequestParam String password,
            @RequestParam(required = false) String next,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes) {
        try {
            Map<String, Object> user = authService.login(email, password);
            HttpSession session = request.getSession(true);
            session.setAttribute(SessionUsers.USER_SESSION_KEY, user);
            return redirectAfterAuth(user, authService.safeNext(next));
        } catch (AuthException exception) {
            redirectAttributes.addFlashAttribute("error", exception.getMessage());
            return loginRedirect(next);
        } catch (DataAccessException exception) {
            redirectAttributes.addFlashAttribute("error",
                    "No fue posible iniciar sesión en este momento.");
            return loginRedirect(next);
        }
    }

    @PostMapping("/register")
    public String register(@RequestParam String nombre,
            @RequestParam String telefono,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String confirmation,
            @RequestParam String cedula,
            @RequestParam(required = false) String next,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes) {
        try {
            Map<String, Object> user = authService.register(nombre, telefono, email, password, confirmation, cedula);
            HttpSession session = request.getSession(true);
            session.setAttribute(SessionUsers.USER_SESSION_KEY, user);
            redirectAttributes.addFlashAttribute("success", "Cuenta creada. Bienvenido a Newphone.");
            return redirectAfterAuth(user, authService.safeNext(next));
        } catch (AuthException exception) {
            redirectAttributes.addFlashAttribute("error", exception.getMessage());
            return "redirect:/register";
        } catch (DataAccessException exception) {
            redirectAttributes.addFlashAttribute("error",
                    "No fue posible registrar la cuenta. Verifica que el correo o la cédula no existan.");
            return "redirect:/register";
        }
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String logoutGet(HttpServletRequest request) {
        return logout(request);
    }

    private String redirectAfterAuth(Map<String, Object> user, String next) {
        if (next != null) {
            return "redirect:" + next;
        }
        if ("CLIENTE".equals(String.valueOf(user.get("rol")))) {
            return "redirect:/catalog";
        }
        return "redirect:/dashboard";
    }

    private String loginRedirect(String next) {
        String safe = authService.safeNext(next);
        return safe == null ? "redirect:/login" : "redirect:/login?next=" + safe;
    }
}
