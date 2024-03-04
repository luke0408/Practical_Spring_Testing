package cafekiosk.spring.domain.product;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public enum ProductType {

    HANDMADE("제조 음료", 0),
    BOTTLE("병 음료", 1),
    BAKERY("베이커리", 2),
    BEVERAGE("음료", 3),
    ;

    private final String text;
    private final int prefix;

    public static boolean containsStockType(ProductType type) {
        return List.of(BOTTLE, BAKERY, BEVERAGE).contains(type);
    }
}
