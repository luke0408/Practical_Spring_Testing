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
        Product product1 = createProduct("020", HANDMADE, SELLING, "test 수제 음료", 3000);
        Product product2 = createProduct("021", BOTTLE, HOLD, "test 병음료", 4000);
        Product product3 = createProduct("022", BEVERAGE, STOP_SELLING, "test 음료", 5000);
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
        Product product1 = createProduct("020", HANDMADE, SELLING, "test 수제 음료", 3000);
        Product product2 = createProduct("021", BOTTLE, HOLD, "test 병음료", 4000);
        Product product3 = createProduct("022", BEVERAGE, STOP_SELLING, "test 음료", 5000);
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

    @Test
    @DisplayName("상품타입에 따른 마지막 상품번호를 조회한다.")
    void findLastProductNumberByType() {
        // given
        String targetProductNumber = "020";
        Product product1 = createProduct(targetProductNumber, HANDMADE, SELLING, "test 수제 음료", 3000);
        Product product2 = createProduct("021", BOTTLE, HOLD, "test 병음료", 4000);
        Product product3 = createProduct("022", BEVERAGE, STOP_SELLING, "test 음료", 5000);
        productRepository.saveAll(List.of(product1, product2, product3));

        // when
        String lastProductNumber = productRepository.findLastProductNumberByType(HANDMADE);

        // then
        assertThat(lastProductNumber).isEqualTo(targetProductNumber);
    }

    @Test
    @DisplayName("상품타입에 따른 마지막 상품번호를 조회할떄, 해당 타입의 상품이 없으면 null을 반환한다.")
    void findLastProductNumberByTypeIsEmpty() {
        // when
        String lastProductNumber = productRepository.findLastProductNumberByType(HANDMADE);

        // then
        assertThat(lastProductNumber).isNull();
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
