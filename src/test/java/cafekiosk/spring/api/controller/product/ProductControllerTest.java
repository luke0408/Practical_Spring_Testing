package cafekiosk.spring.api.controller.product;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import cafekiosk.spring.api.controller.product.request.ProductCreatRequest;
import cafekiosk.spring.api.service.product.ProductService;
import cafekiosk.spring.api.service.product.response.ProductResponse;
import cafekiosk.spring.domain.product.ProductSellingStatus;
import cafekiosk.spring.domain.product.ProductType;

@WebMvcTest(controllers = ProductController.class)
class ProductControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private ProductService productService;

	@Test
	@DisplayName("신규 상품을 등록한다.")
	void createProduct() throws Exception {
		// given
		ProductCreatRequest request = ProductCreatRequest.builder()
				.type(ProductType.HANDMADE)
				.sellingStatus(ProductSellingStatus.SELLING)
				.name("아메리카노")
				.price(3000)
				.build();

		// when & then
		mockMvc.perform(
						post("/api/v1/products/new")
								.content(objectMapper.writeValueAsString(request))
								.contentType(MediaType.APPLICATION_JSON)
				)
				.andDo(print())    // 실행 결과를 콘솔에 출력
				.andExpect(status().isOk());    // HTTP 상태값이 200인지 검증
	}

	@Test
	@DisplayName("신규 상품을 등록할 때 상품 타입은 필수값이다.")
	void createProductWithoutType() throws Exception {
		// given
		ProductCreatRequest request = ProductCreatRequest.builder()
				.sellingStatus(ProductSellingStatus.SELLING)
				.name("아메리카노")
				.price(3000)
				.build();

		// when & then
		mockMvc.perform(
						post("/api/v1/products/new")
								.content(objectMapper.writeValueAsString(request))
								.contentType(MediaType.APPLICATION_JSON)
				)
				.andDo(print())    // 실행 결과를 콘솔에 출력
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.code").value(400))
				.andExpect(jsonPath("$.status").value("BAD_REQUEST"))
				.andExpect(jsonPath("$.message").value("상품 타입은 필수입니다."))
				.andExpect(jsonPath("$.data").isEmpty());
	}

	@Test
	@DisplayName("신규 상품을 등록할 때 상품 판매 상태은 필수 값이다.")
	void createProductWithoutSellingStatus() throws Exception {
		// given
		ProductCreatRequest request = ProductCreatRequest.builder()
				.type(ProductType.HANDMADE)
				.name("아메리카노")
				.price(3000)
				.build();

		// when & then
		mockMvc.perform(
						post("/api/v1/products/new")
								.content(objectMapper.writeValueAsString(request))
								.contentType(MediaType.APPLICATION_JSON)
				)
				.andDo(print())    // 실행 결과를 콘솔에 출력
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.code").value(400))
				.andExpect(jsonPath("$.status").value("BAD_REQUEST"))
				.andExpect(jsonPath("$.message").value("판매 상태는 필수입니다."))
				.andExpect(jsonPath("$.data").isEmpty());
	}

	@Test
	@DisplayName("신규 상품을 등록할 때 상품명은 필수 값이다.")
	void createProductWithoutName() throws Exception {
		// given
		ProductCreatRequest request = ProductCreatRequest.builder()
				.type(ProductType.HANDMADE)
				.sellingStatus(ProductSellingStatus.SELLING)
				.price(3000)
				.build();

		// when & then
		mockMvc.perform(
						post("/api/v1/products/new")
								.content(objectMapper.writeValueAsString(request))
								.contentType(MediaType.APPLICATION_JSON)
				)
				.andDo(print())    // 실행 결과를 콘솔에 출력
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.code").value(400))
				.andExpect(jsonPath("$.status").value("BAD_REQUEST"))
				.andExpect(jsonPath("$.message").value("상품명은 필수입니다."))
				.andExpect(jsonPath("$.data").isEmpty());
	}

	@Test
	@DisplayName("신규 상품을 등록할 때 상품 가격은 양수이다.")
	void createProductWithZeroPrice() throws Exception {
		// given
		ProductCreatRequest request = ProductCreatRequest.builder()
				.type(ProductType.HANDMADE)
				.sellingStatus(ProductSellingStatus.SELLING)
				.name("아메리카노")
				.price(0)
				.build();

		// when & then
		mockMvc.perform(
						post("/api/v1/products/new")
								.content(objectMapper.writeValueAsString(request))
								.contentType(MediaType.APPLICATION_JSON)
				)
				.andDo(print())    // 실행 결과를 콘솔에 출력
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.code").value(400))
				.andExpect(jsonPath("$.status").value("BAD_REQUEST"))
				.andExpect(jsonPath("$.message").value("상품 가격은 양수여야 합니다."))
				.andExpect(jsonPath("$.data").isEmpty());
	}

	@Test
	@DisplayName("판매 상품을 조회한다.")
	void getSellingProducts() throws Exception {
	    // given
		List<ProductResponse> result = List.of();

		when(productService.getSellingProducts()).thenReturn(result);

	    // when & then
		mockMvc.perform(
						get("/api/v1/products/selling")
				)
				.andDo(print())    // 실행 결과를 콘솔에 출력
				.andExpect(status().isOk())    // HTTP 상태값이 200인지 검증
				.andExpect(jsonPath("$.code").value(200))
				.andExpect(jsonPath("$.status").value("OK"))
				.andExpect(jsonPath("$.message").value("OK"))
				.andExpect(jsonPath("$.data").isArray());
	}
}
