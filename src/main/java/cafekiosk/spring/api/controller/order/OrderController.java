package cafekiosk.spring.api.controller.order;

import cafekiosk.spring.api.controller.order.request.OrderCreateRequest;
import cafekiosk.spring.api.service.order.OrderService;
import cafekiosk.spring.api.service.order.response.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@RestController
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/api/v1/order/new")
    public OrderResponse createOrder(
            @RequestBody OrderCreateRequest request
    ) {
        LocalDateTime registeredAt = LocalDateTime.now();
        return orderService.createOrder(request, registeredAt);
    }
}
