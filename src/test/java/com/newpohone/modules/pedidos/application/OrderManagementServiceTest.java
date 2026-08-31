package com.newpohone.modules.pedidos.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.newpohone.modules.catalogo.infrastructure.CatalogRepository;
import com.newpohone.modules.pedidos.domain.OrderException;
import com.newpohone.modules.pedidos.domain.OrderStatus;
import com.newpohone.modules.pedidos.infrastructure.OrderRepository;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderManagementServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private CatalogRepository catalogRepository;

    private OrderManagementService service;

    @BeforeEach
    void setUp() {
        service = new OrderManagementService(orderRepository, catalogRepository);
    }

    @Test
    void updatesStatusWhenConfirmed() {
        when(orderRepository.findById(7)).thenReturn(Map.of("id", 7, "estado", "Pendiente", "total", 1000));
        String message = service.updateStatus(7, "preparado", true);
        assertTrue(message.contains("Preparado"));
        verify(orderRepository).updateStatus(7, "Preparado");
    }

    @Test
    void restoresStockWhenCancelling() {
        when(orderRepository.findById(8)).thenReturn(Map.of("id", 8, "estado", "Pendiente", "total", 2000));
        when(orderRepository.findItems(8)).thenReturn(List.of(Map.of("productoId", 3, "cantidad", 2)));

        service.updateStatus(8, "cancelado", true);

        verify(orderRepository).updateStatus(8, "Cancelado");
        verify(catalogRepository).incrementStock(3, 2);
    }

    @Test
    void rejectsUnconfirmedOrInvalidTransitions() {
        OrderException unconfirmed = assertThrows(OrderException.class,
                () -> service.updateStatus(1, "preparado", false));
        assertEquals("Confirma la modificación del estado para guardarla.", unconfirmed.getMessage());

        when(orderRepository.findById(9)).thenReturn(Map.of("id", 9, "estado", "Entregado", "total", 500));
        OrderException locked = assertThrows(OrderException.class,
                () -> service.updateStatus(9, "enviado", true));
        assertTrue(locked.getMessage().contains("ya no admite cambios"));
    }

    @Test
    void buildsTimelineForCurrentStatus() {
        List<Map<String, Object>> steps = service.timeline(OrderStatus.ENVIADO);
        assertEquals(4, steps.size());
        assertEquals(true, steps.get(2).get("current"));
        assertEquals(true, steps.get(1).get("done"));
        assertEquals(false, steps.get(3).get("done"));
    }
}
