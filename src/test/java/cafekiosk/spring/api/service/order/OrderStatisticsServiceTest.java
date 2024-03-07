package cafekiosk.spring.api.service.order;

import static cafekiosk.spring.domain.product.ProductSellingStatus.*;
import static cafekiosk.spring.domain.product.ProductType.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import cafekiosk.spring.client.MailSendClient;
import cafekiosk.spring.domain.histotory.mail.MailSendHistory;
import cafekiosk.spring.domain.histotory.mail.MailSendHistoryRepository;
import cafekiosk.spring.domain.order.Order;
import cafekiosk.spring.domain.order.OrderRepository;
import cafekiosk.spring.domain.order.OrderStatus;
import cafekiosk.spring.domain.orderProduct.OrderProductRepository;
import cafekiosk.spring.domain.product.Product;
import cafekiosk.spring.domain.product.ProductRepository;
import cafekiosk.spring.domain.product.ProductType;

@SpringBootTest
class OrderStatisticsServiceTest {

	@Autowired
	private OrderStatisticsService orderStatisticsService;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private OrderProductRepository orderProductRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private MailSendHistoryRepository mailSendHistoryRepository;

	@MockBean
	private MailSendClient mailSendClient;

	@AfterEach
	void tearDown() {
		orderProductRepository.deleteAllInBatch();
		orderRepository.deleteAllInBatch();
		productRepository.deleteAllInBatch();
		mailSendHistoryRepository.deleteAllInBatch();
	}

	@Test
	@DisplayName("결제완료 주문들을 조회하여 매출 통계 메일을 전송한다.")
	void sendOrderStatisticsMail() {
		// given
		LocalDateTime orderDate = LocalDateTime.of(2024, 5, 2, 0, 0);

		Product product1 = createProduct("001", HANDMADE, 3000);
		Product product2 = createProduct("101", BOTTLE, 4000);
		Product product3 = createProduct("201", BEVERAGE, 5000);
		List<Product> products = List.of(product1, product2, product3);

		Order order1 = createCompletedPaymentOrder(products, orderDate.minusMinutes(1));
		Order order2 = createCompletedPaymentOrder(products, orderDate);
		Order order3 = createCompletedPaymentOrder(products, orderDate.plusDays(1).minusMinutes(1));
		Order order4 = createCompletedPaymentOrder(products, orderDate.plusDays(1));

		// stubbing : mock 객체의 특정 메소드의 행위를 지정하는 작업
		when(mailSendClient.sendMail(
				anyString(),
				anyString(),
				anyString(),
				anyString()
				)
		).thenReturn(true);

		// when
		boolean result = orderStatisticsService.sendOrderStatisticsMail(
				orderDate.toLocalDate(),
				"test@test.com"
		);

		// then
		assertThat(result).isTrue();

		List<MailSendHistory> histories = mailSendHistoryRepository.findAll();
		assertThat(histories).hasSize(1)
				.extracting("content")
				.containsExactly("총 매출 합계는 24000원 입니다.");
	}

	private Order createCompletedPaymentOrder(
			List<Product> products,
			LocalDateTime registeredAt
	) {
		Order order = Order.builder()
				.products(products)
				.registeredAt(registeredAt)
				.orderStatus(OrderStatus.PAYMENT_COMPLETED)
				.build();
		return orderRepository.save(order);
	}

	private Product createProduct(
			String productNumber,
			ProductType productType,
			int price
	) {
		Product product = Product.builder()
				.productNumber(productNumber)
				.type(productType)
				.sellingStatus(SELLING)
				.name("test 상품")
				.price(price)
				.build();
		return productRepository.save(product);
	}
}
