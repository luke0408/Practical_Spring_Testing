package cafekiosk.spring.domain.order;

import cafekiosk.spring.domain.product.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static cafekiosk.spring.domain.product.ProductSellingStatus.SELLING;
import static cafekiosk.spring.domain.product.ProductType.HANDMADE;
import static org.assertj.core.api.Assertions.assertThat;

class OrderTest {

    @DisplayName("주문 생성 시 상품 리스트에서 주문의 총 금액을 계산한다.")
    @Test
    void calculateTotalPrice() {
        // given
        List<Product> products = List.of (
                createProduct("020", 3000),
                createProduct("021", 4000),
                createProduct("022", 5000)
        );

        // when
        Order order = Order.create(products, LocalDateTime.now());

        // then
        assertThat(order.getTotalPrice()).isEqualTo(12000);
    }

    @DisplayName("주문 생성 시 주문의 상태는 INIT이다.")
    @Test
    void init() {
        // given
        List<Product> products = List.of (
                createProduct("020", 3000),
                createProduct("021", 4000),
                createProduct("022", 5000)
        );

        // when
        Order order = Order.create(products, LocalDateTime.now());

        // then
        assertThat(order.getOrderStatus()).isEqualByComparingTo(OrderStatus.INIT);
    }

    @DisplayName("주문 생성 시 주문의 등록 시간을 저장한다.")
    @Test
    void registeredDateTime() {
        // given
        LocalDateTime registeredAt = LocalDateTime.now();
        List<Product> products = List.of (
                createProduct("020", 3000),
                createProduct("021", 4000),
                createProduct("022", 5000)
        );

        // when
        Order order = Order.create(products, registeredAt);

        // then
        assertThat(order.getRegisteredAt()).isEqualTo(registeredAt);
    }

    private Product createProduct (String productNumber, int price) {
        return Product.builder()
                .productNumber(productNumber)
                .type(HANDMADE)
                .sellingStatus(SELLING)
                .name("test 메뉴")
                .price(price)
                .build();
    }
}
