package cafekiosk.unit.beverage;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class AmericanoTest {


    @Test
    void getName() {
        Americano americano = new Americano();
//        assertEquals("아메리카노", americano.getName()); junit의 assert
        assertThat(americano.getName()).isEqualTo("아메리카노"); // assertj
    }

    @Test
    void getPrice() {
        Americano americano = new Americano();
        assertThat(americano.getPrice()).isEqualTo(4000);
    }

}
