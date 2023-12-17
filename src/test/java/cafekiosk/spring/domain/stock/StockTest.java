package cafekiosk.spring.domain.stock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StockTest {

    @DisplayName("재고의 수량이 제공해야하는 수량보다 작은지 확인한다.")
    @Test
    void isQuantityLessThan() {
        // given
        Stock stock = Stock.create("020", 10);
        int givenQuantity = 20;

        // when
        boolean result = stock.isQuantityLessThan(givenQuantity);

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("재고를 주어진 만큼 차감할 수 있다.")
    @Test
    void deductQuantity() {
        // given
        Stock stock = Stock.create("020", 10);
        int givenQuantity = 5;

        // when
        stock.deductQuantity(givenQuantity);

        // then
        assertThat(stock.getQuantity()).isEqualTo(5);
    }

    @DisplayName("재고를 이상의 수량을 차감 시도하면 예외가 발생한다.")
    @Test
    void deductQuantityWithException() {
        // given
        Stock stock = Stock.create("020", 10);
        int givenQuantity = 20;

        // when // then
        assertThatThrownBy(() -> stock.deductQuantity(givenQuantity))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("재고가 부족합니다.");
    }
}