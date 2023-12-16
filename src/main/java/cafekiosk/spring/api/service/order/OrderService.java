package cafekiosk.spring.api.service.order;

import cafekiosk.spring.api.controller.order.request.OrderCreateRequest;
import cafekiosk.spring.api.service.order.response.OrderResponse;
import cafekiosk.spring.domain.order.Order;
import cafekiosk.spring.domain.order.OrderRepository;
import cafekiosk.spring.domain.product.Product;
import cafekiosk.spring.domain.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class OrderService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public OrderResponse createOrder(OrderCreateRequest request, LocalDateTime registeredAt) {
        List<String> productNumbers = request.getProductNumbers();
        List<Product> products = productRepository.findAllByProductNumberIn(productNumbers);

        Order order = Order.creat(products, registeredAt);
        Order savedOrder = orderRepository.save(order);
        return OrderResponse.of(savedOrder);
    }

}
