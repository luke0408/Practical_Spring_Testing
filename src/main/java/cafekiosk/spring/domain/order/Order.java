package cafekiosk.spring.domain.order;

import cafekiosk.spring.domain.BaseEntity;
import cafekiosk.spring.domain.orderProduct.OrderProduct;
import cafekiosk.spring.domain.product.Product;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders")
@Entity
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private int totalPrice;

    @Column(name = "registered_at")
    private LocalDateTime registeredAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderProduct> orderProducts = new ArrayList<>();

    @Builder
    private Order(List<Product> products, LocalDateTime registeredAt, OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
        this.totalPrice = calculateTotalPrice(products);
        this.registeredAt = registeredAt;
        this.orderProducts = createOrderProducts(products);
    }

    private List<OrderProduct> createOrderProducts(List<Product> products) {
        return products.stream()
                .map(product -> new OrderProduct(this, product))
                .collect(Collectors.toList());
    }

    private int calculateTotalPrice(List<Product> products) {
        return products.stream()
                .mapToInt(Product::getPrice)
                .sum();
    }

    public static Order create(List<Product> products, LocalDateTime registeredAt) {
        return Order.builder()
                .products(products)
                .registeredAt(registeredAt)
                .orderStatus(OrderStatus.INIT)
                .build();
    }

}
