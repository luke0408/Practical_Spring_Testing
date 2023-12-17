package cafekiosk.spring.api.service.order;

import cafekiosk.spring.api.controller.order.request.OrderCreateRequest;
import cafekiosk.spring.api.service.order.response.OrderResponse;
import cafekiosk.spring.domain.order.OrderRepository;
import cafekiosk.spring.domain.orderProduct.OrderProductRepository;
import cafekiosk.spring.domain.product.Product;
import cafekiosk.spring.domain.product.ProductRepository;
import cafekiosk.spring.domain.product.ProductType;
import cafekiosk.spring.domain.stock.Stock;
import cafekiosk.spring.domain.stock.StockRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static cafekiosk.spring.domain.product.ProductSellingStatus.SELLING;
import static cafekiosk.spring.domain.product.ProductType.*;
import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderProductRepository orderProductRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StockRepository stockRepository;

    @AfterEach
    void tearDown() {
//        productRepository.deleteAll();
        orderProductRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
        orderRepository.deleteAllInBatch();
        stockRepository.deleteAllInBatch();
    }

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

    @DisplayName("중복되는 상품번호 리스트로 주문을 생성할 수 있다.")
    @Test
    void createOrderWithDuplicateProductNumbers() {
        // given
        LocalDateTime registeredAt = LocalDateTime.now();

        Product product1 = createProduct(HANDMADE, "020",  3000);
        Product product2 = createProduct(BOTTLE, "021", 4000);
        Product product3 = createProduct(BEVERAGE, "022", 5000);
        productRepository.saveAll(List.of(product1, product2, product3));

        OrderCreateRequest request = OrderCreateRequest.builder()
                .productNumbers(List.of("020", "020"))
                .build();

        // when
        OrderResponse orderResponse = orderService.createOrder(request, registeredAt);

        // then
        assertThat(orderResponse.getId()).isNotNull();
        assertThat(orderResponse)
                .extracting("registeredAt", "totalPrice")
                .contains(registeredAt, 6000);
        assertThat(orderResponse.getProducts()).hasSize(2)
                .extracting("productNumber", "price")
                .containsExactlyInAnyOrder(
                        tuple("020", 3000),
                        tuple("020", 3000)
                );
    }

    @DisplayName("재고와 관련된 상품이 포함되어 있는 주문번호 리스트를 받아 주문을 생성한다.")
    @Test
    void createOrderWithStock() {
        // given
        LocalDateTime registeredAt = LocalDateTime.now();

        Product product1 = createProduct(BEVERAGE, "020",  3000);
        Product product2 = createProduct(BOTTLE, "021", 4000);
        Product product3 = createProduct(HANDMADE, "022", 5000);
        productRepository.saveAll(List.of(product1, product2, product3));

        Stock stock1 = Stock.create("020", 2);
        Stock stock2 = Stock.create("021", 2);
        stockRepository.saveAll(List.of(stock1, stock2));

        OrderCreateRequest request = OrderCreateRequest.builder()
                .productNumbers(List.of("020", "020", "021", "022"))
                .build();

        // when
        OrderResponse orderResponse = orderService.createOrder(request, registeredAt);

        // then
        assertThat(orderResponse.getId()).isNotNull();
        assertThat(orderResponse)
                .extracting("registeredAt", "totalPrice")
                .contains(registeredAt, 15000);
        assertThat(orderResponse.getProducts()).hasSize(4)
                .extracting("productNumber", "price")
                .containsExactlyInAnyOrder(
                        tuple("020", 3000),
                        tuple("020", 3000),
                        tuple("021", 4000),
                        tuple("022", 5000)
                );

        List<Stock> stocks = stockRepository.findAll();
        assertThat(stocks).hasSize(2)
                .extracting("productNumber", "quantity")
                .containsExactlyInAnyOrder(
                        tuple("020", 0),
                        tuple("021", 1)
                );
    }

    @DisplayName("재고가 부족한 상품으로 주문을 생성하려고 하는 경우 예외가 발생한다.")
    @Test
    void createOrderWithNoStock() {
        // given
        LocalDateTime registeredAt = LocalDateTime.now();

        Product product1 = createProduct(BEVERAGE, "020",  3000);
        Product product2 = createProduct(BOTTLE, "021", 4000);
        Product product3 = createProduct(HANDMADE, "022", 5000);
        productRepository.saveAll(List.of(product1, product2, product3));

        Stock stock1 = Stock.create("020", 1);
        Stock stock2 = Stock.create("021", 1);
        stockRepository.saveAll(List.of(stock1, stock2));

        OrderCreateRequest request = OrderCreateRequest.builder()
                .productNumbers(List.of("020", "020", "021", "022"))
                .build();

        // when // then
        assertThatThrownBy(() -> orderService.createOrder(request, registeredAt))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("제고가 부족한 상품이 있습니다.");
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