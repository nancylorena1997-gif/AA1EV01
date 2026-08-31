package com.newpohone.modules.pedidos.presentation;

import com.newpohone.modules.crud.presentation.ModuleController;
import com.newpohone.modules.dashboard.presentation.DashboardController;
import com.newpohone.modules.inventario.presentation.InventoryController;
import com.newpohone.modules.pedidos.infrastructure.OrderRepository;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice(assignableTypes = {
        DashboardController.class,
        ModuleController.class,
        InventoryController.class,
        OrderManagementController.class
})
public class OrderAdvice {

    private final OrderRepository orderRepository;

    public OrderAdvice(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @ModelAttribute("pendingOrderCount")
    public int pendingOrderCount() {
        return orderRepository.countOpen();
    }
}
