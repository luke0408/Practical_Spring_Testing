package cafekiosk.spring.api.controller.product.dto.request;

import cafekiosk.spring.domain.product.Product;
import cafekiosk.spring.domain.product.ProductSellingStatus;
import cafekiosk.spring.domain.product.ProductType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductCreatRequest {

    private ProductType type;
    private ProductSellingStatus sellingStatus;
    private String name;
    private int price;

    public Product toEntity(String nextProductNumber) {
        return Product.builder()
                .productNumber(nextProductNumber)
                .type(type)
                .sellingStatus(sellingStatus)
                .name(name)
                .price(price)
                .build();
    }
}
