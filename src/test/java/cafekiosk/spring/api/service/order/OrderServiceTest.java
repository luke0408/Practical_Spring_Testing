package cafekiosk.spring.api.service.order;

import cafekiosk.spring.api.controller.order.request.OrderCreateRequest;
import cafekiosk.spring.api.service.order.response.OrderResponse;
import cafekiosk.spring.domain.product.Product;
import cafekiosk.spring.domain.product.ProductRepository;
import cafekiosk.spring.domain.product.ProductType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static cafekiosk.spring.domain.product.ProductSellingStatus.SELLING;
import static cafekiosk.spring.domain.product.ProductType.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@ActiveProfiles("test")
@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductRepository productRepository;

    @DisplayName("주문번호 리스트를 받아 주문을 생성한다.")
    @Test
    void createOrder() {
        // given
        LocalDateTime registeredAt = LocalDateTime.now();

        Product product1 = createProduct(HANDMADE, "020",  3000);
        Product product2 = createProduct(BOTTLE, "021", 4000);
        Product product3 = createProduct(BEVERAGE, "022", 5000);
        productRepository.saveAll(List.of(product1, product2, product3));

        OrderCreateRequest request = OrderCreateRequest.builder()
                .productNumbers(List.of("020", "021", "022"))
                .build();

        // when
        OrderResponse orderResponse = orderService.createOrder(request, registeredAt);

        // then
        assertThat(orderResponse.getId()).isNotNull();
        assertThat(orderResponse)
                .extracting("registeredAt", "totalPrice")
                .contains(registeredAt, 12000);
        assertThat(orderResponse.getProducts()).hasSize(3)
                .extracting("productNumber", "price")
                .containsExactlyInAnyOrder(
                        tuple("020", 3000),
                        tuple("021", 4000),
                        tuple("022", 5000)
                );
    }

    private Product createProduct (ProductType type, String productNumber, int price) {
        return Product.builder()
                .productNumber(productNumber)
                .type(type)
                .sellingStatus(SELLING)
                .name("test 메뉴")
                .price(price)
                .build();
    }
}