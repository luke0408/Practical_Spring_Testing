package cafekiosk.spring.api.service.order.response;

import cafekiosk.spring.api.service.product.response.ProductResponse;
import cafekiosk.spring.domain.order.Order;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class OrderResponse {

    private Long id;
    private int totalPrice;
    private LocalDateTime registeredAt;

    private List<ProductResponse> products;

    @Builder
    public OrderResponse(Long id, int totalPrice, LocalDateTime registeredAt, List<ProductResponse> products) {
        this.id = id;
        this.totalPrice = totalPrice;
        this.registeredAt = registeredAt;
        this.products = products;
    }

    public static OrderResponse of(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .totalPrice(order.getTotalPrice())
                .registeredAt(order.getRegisteredAt())
                .products(order.getOrderProducts().stream()
                        .map(orderProduct -> ProductResponse.of(orderProduct.getProduct()))
                        .collect(Collectors.toList()))
                .build();
    }
}
