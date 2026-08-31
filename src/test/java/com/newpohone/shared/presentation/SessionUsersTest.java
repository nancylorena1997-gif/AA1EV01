package com.newpohone.shared.presentation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;

class SessionUsersTest {

    @Test
    void returnsNullWhenThereIsNoSessionUser() {
        assertNull(SessionUsers.current(null));
        assertNull(SessionUsers.current(new MockHttpSession()));
        assertNull(SessionUsers.customerCedula(new MockHttpSession()));
    }

    @Test
    void readsCustomerCedulaFromSession() {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(SessionUsers.USER_SESSION_KEY, Map.of(
                "rol", "CLIENTE",
                "cedula", 1010,
                "nombre", "Ana"));

        assertEquals("Ana", SessionUsers.current(session).get("nombre"));
        assertEquals(1010, SessionUsers.customerCedula(session));
    }

    @Test
    void ignoresAdministratorWhenAskingCustomerCedula() {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(SessionUsers.USER_SESSION_KEY, Map.of(
                "rol", "ADMINISTRADOR",
                "cedula", 2020));

        assertNull(SessionUsers.customerCedula(session));
    }
}
