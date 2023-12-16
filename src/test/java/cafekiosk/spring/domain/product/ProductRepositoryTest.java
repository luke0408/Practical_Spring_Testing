package cafekiosk.spring.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static cafekiosk.spring.domain.product.ProductSellingStatus.*;
import static cafekiosk.spring.domain.product.ProductType.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

//@SpringBootTest // 전체 설정을 로드하여 테스트
@ActiveProfiles("test")
@DataJpaTest    // JPA 관련 설정만 로드하여 테스트
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @DisplayName("원하는 판매상태를 가진 상품들을 조회한다.")
    @Test
    void findAllBySellingStatusIn() {
        // given
        Product product1 = Product.builder()
                .productNumber("020")
                .type(HANDMADE)
                .sellingStatus(SELLING)
                .name("test 수제 음료")
                .price(3000)
                .build();
        Product product2 = Product.builder()
                .productNumber("021")
                .type(BOTTLE)
                .sellingStatus(HOLD)
                .name("test 병음료")
                .price(4000)
                .build();
        Product product3 = Product.builder()
                .productNumber("022")
                .type(BEVERAGE)
                .sellingStatus(STOP_SELLING)
                .name("test 음료")
                .price(5000)
                .build();
        productRepository.saveAll(List.of(product1, product2, product3));

        // when
        List<Product> products = productRepository.findAllBySellingStatusIn(List.of(SELLING, HOLD));

        // then
        assertThat(products).hasSize(2)
                .extracting("productNumber", "name", "sellingStatus")
                .containsExactlyInAnyOrder(
                        tuple("020", "test 수제 음료", SELLING),
                        tuple("021", "test 병음료", HOLD)
                );
    }

    @DisplayName("상품번호로 상품들을 조회한다.")
    @Test
    void findAllByProductNumberIn() {
        // given
        Product product1 = Product.builder()
                .productNumber("020")
                .type(HANDMADE)
                .sellingStatus(SELLING)
                .name("test 수제 음료")
                .price(3000)
                .build();
        Product product2 = Product.builder()
                .productNumber("021")
                .type(BOTTLE)
                .sellingStatus(HOLD)
                .name("test 병음료")
                .price(4000)
                .build();
        Product product3 = Product.builder()
                .productNumber("022")
                .type(BEVERAGE)
                .sellingStatus(STOP_SELLING)
                .name("test 음료")
                .price(5000)
                .build();
        productRepository.saveAll(List.of(product1, product2, product3));

        // when
        List<Product> products = productRepository.findAllByProductNumberIn(List.of("020", "021"));

        // then
        assertThat(products).hasSize(2)
                .extracting("productNumber", "name", "sellingStatus")
                .containsExactlyInAnyOrder(
                        tuple("020", "test 수제 음료", SELLING),
                        tuple("021", "test 병음료", HOLD)
                );
    }

}