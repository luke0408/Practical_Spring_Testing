package cafekiosk.spring.api.controller.product.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import cafekiosk.spring.api.service.product.request.ProductCreatServiceRequest;
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
public class ProductCreatRequest {

	@NotNull(message = "상품 타입은 필수입니다.")
	private ProductType type;

	@NotNull(message = "판매 상태는 필수입니다.")
	private ProductSellingStatus sellingStatus;

	@NotBlank(message = "상품명은 필수입니다.")	// 빈 문자열, null, 공백만 있는 문자열 검증
	private String name;

	@Positive(message = "상품 가격은 양수여야 합니다.")	// 양수 검증
	private int price;

	public ProductCreatServiceRequest toServiceRequest() {
		return ProductCreatServiceRequest.builder()
				.type(type)
				.sellingStatus(sellingStatus)
				.name(name)
				.price(price)
				.build();
	}
}
