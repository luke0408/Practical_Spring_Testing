package cafekiosk.unit;

import cafekiosk.unit.beverage.Americano;
import cafekiosk.unit.beverage.Latte;
import cafekiosk.unit.order.Order;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class CafeKioskTest {

    @Test
    void add_manual_test() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        cafeKiosk.add(new Americano());

        System.out.println(">>> 담긴 음료 수: " + cafeKiosk.getBeverages().size());
        System.out.println(">>> 담긴 음료: " + cafeKiosk.getBeverages().get(0).getName());


    }

    @DisplayName("음료 1개를 추가하면 음료 리스트에 추가된다.")
    @Test
    void add() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        cafeKiosk.add(new Americano());

        assertThat(cafeKiosk.getBeverages()).hasSize(1);
        assertThat(cafeKiosk.getBeverages().get(0).getName()).isEqualTo("아메리카노");
    }

    @Test
    void add_seval_beverages() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();

        cafeKiosk.add(americano, 3);

        assertThat(cafeKiosk.getBeverages().get(0).getName()).isEqualTo("아메리카노");
        assertThat(cafeKiosk.getBeverages().get(1).getName()).isEqualTo("아메리카노");
        assertThat(cafeKiosk.getBeverages().get(2).getName()).isEqualTo("아메리카노");
    }

    @Test
    void add_zero_beverages() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();

        assertThatThrownBy(() -> cafeKiosk.add(americano, 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("음료의 개수는 1개 이상이어야 합니다.");
    }

        @Test
    void remove() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();

        cafeKiosk.add(americano);
        assertThat(cafeKiosk.getBeverages()).hasSize(1);

        cafeKiosk.remove(americano);
        assertThat(cafeKiosk.getBeverages()).hasSize(0);
    }

    @Test
    void clear() {
        CafeKiosk cafeKiosk = new CafeKiosk();

        cafeKiosk.add(new Americano());
        cafeKiosk.add(new Americano());
        cafeKiosk.add(new Americano());
        assertThat(cafeKiosk.getBeverages()).hasSize(3);

        cafeKiosk.clear();
        assertThat(cafeKiosk.getBeverages()).hasSize(0);
    }

    @Test
    void calculateTotalPrice() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();
        Latte latte = new Latte();

        cafeKiosk.add(americano);
        cafeKiosk.add(latte);

        int totalPrice = cafeKiosk.calculateTotalPrice();

        assertThat(totalPrice).isEqualTo(8500);
    }

    @Test
    void createOrder() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();

        cafeKiosk.add(americano);

        Order order = cafeKiosk.createOrder();
        assertThat(order.getBeverages()).hasSize(1);
        assertThat(order.getBeverages().get(0).getName()).isEqualTo("아메리카노");
    }

    @Test
    void createOrderWithDateTime() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();

        cafeKiosk.add(americano);

        Order order = cafeKiosk.createOrder(LocalDateTime.of(2023, 1, 17, 10,0));

        assertThat(order.getBeverages()).hasSize(1);
        assertThat(order.getBeverages().get(0).getName()).isEqualTo("아메리카노");
    }

    @Test
    void createOrderWithOutsideOpenTime() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();
        cafeKiosk.add(americano);

        assertThatThrownBy(() -> cafeKiosk.createOrder(LocalDateTime.of(2023, 1, 17, 9,0)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 시간이 아닙니다. 관리자에게 문의하세요.");
    }
}