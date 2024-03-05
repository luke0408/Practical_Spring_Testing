package cafekiosk.spring.api.service.product.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import cafekiosk.spring.domain.product.Product;
import cafekiosk.spring.domain.product.ProductSellingStatus;
import cafekiosk.spring.domain.product.ProductType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor  // 기본 생성자 - 역직렬화를 위해 필요
@AllArgsConstructor // 모든 필드를 인자로 받는 생성자
public class ProductCreatServiceRequest {

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
