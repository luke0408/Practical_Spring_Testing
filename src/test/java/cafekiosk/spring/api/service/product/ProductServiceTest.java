package cafekiosk.spring.api.service.product;

import cafekiosk.spring.api.controller.product.dto.request.ProductCreatRequest;
import cafekiosk.spring.api.service.product.response.ProductResponse;
import cafekiosk.spring.domain.product.Product;
import cafekiosk.spring.domain.product.ProductRepository;
import cafekiosk.spring.domain.product.ProductSellingStatus;
import cafekiosk.spring.domain.product.ProductType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static cafekiosk.spring.domain.product.ProductSellingStatus.*;
import static cafekiosk.spring.domain.product.ProductType.*;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @AfterEach
    void tearDown() {
        productRepository.deleteAll();
    }

    @Test
    @DisplayName("신규 상품을 등록한다. 상품번호는 같은 타입의 상품번호 중 가장 큰 번호에 1을 더한 값이어야 한다.")
    void createProduct() {
        // given
        Product product1 = createProduct("001", HANDMADE, SELLING, "test 수제 음료", 3000);
        Product product2 = createProduct("101", BOTTLE, HOLD, "test 병음료", 4000);
        Product product3 = createProduct("201", BEVERAGE, STOP_SELLING, "test 음료", 5000);
        productRepository.saveAll(List.of(product1, product2, product3));

        ProductCreatRequest request = ProductCreatRequest.builder()
                .type(HANDMADE)
                .sellingStatus(SELLING)
                .name("test 수제 음료 2")
                .price(4000)
                .build();

        // when
        ProductResponse productResponse = productService.createProduct(request);

        // then
        assertThat(productResponse)
                .extracting("productNumber", "type", "sellingStatus", "name", "price")
                .containsExactly("002", HANDMADE, SELLING, "test 수제 음료 2", 4000);
    }

    @Test
    @DisplayName("새로운 타입의 상품을 등록한다. 상품번호는 {타입 prefix + 01} 이어야 한다.")
    void createProductWhenProductTypeIsEmpty() {
        ProductCreatRequest request = ProductCreatRequest.builder()
                .type(HANDMADE)
                .sellingStatus(SELLING)
                .name("test 수제 음료 2")
                .price(4000)
                .build();

        // when
        ProductResponse productResponse = productService.createProduct(request);

        // then
        assertThat(productResponse)
                .extracting("productNumber", "type", "sellingStatus", "name", "price")
                .containsExactly("001", HANDMADE, SELLING, "test 수제 음료 2", 4000);
    }

    private Product createProduct(
            String productNumber,
            ProductType productType,
            ProductSellingStatus sellingStatus,
            String name,
            int price
    ) {
        return Product.builder()
                .productNumber(productNumber)
                .type(productType)
                .sellingStatus(sellingStatus)
                .name(name)
                .price(price)
                .build();
    }
}
