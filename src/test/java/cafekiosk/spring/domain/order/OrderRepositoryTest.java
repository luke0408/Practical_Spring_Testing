package cafekiosk.spring.domain.order;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import cafekiosk.spring.domain.product.Product;
import cafekiosk.spring.domain.product.ProductRepository;
import cafekiosk.spring.domain.product.ProductSellingStatus;
import cafekiosk.spring.domain.product.ProductType;

@ActiveProfiles("test")
@DataJpaTest
class OrderRepositoryTest {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private ProductRepository productRepository;

	@Test
	@DisplayName("주문일자와 주문상태로 주문을 조회한다.")
	void findOrdersByInOrderDateAndOrderStatus() {
		// given
		LocalDateTime orderDate = LocalDateTime.now();

		Product product1 = createProduct("001", ProductType.HANDMADE, ProductSellingStatus.SELLING, "test 수제 음료", 3000);
		Product product2 = createProduct("002", ProductType.BOTTLE, ProductSellingStatus.HOLD, "test 병음료", 4000);
		Product product3 = createProduct("003", ProductType.BEVERAGE, ProductSellingStatus.STOP_SELLING, "test 음료", 5000);
		List<Product> products = List.of(product1, product2, product3);
		productRepository.saveAll(products);

		Order order1 = createOrder(products, orderDate.plusHours(1), OrderStatus.PAYMENT_COMPLETED);
		Order order2 = createOrder(products, orderDate.plusHours(11), OrderStatus.PAYMENT_FAILED);
		Order order3 = createOrder(products, orderDate.plusHours(21), OrderStatus.PAYMENT_COMPLETED);
		orderRepository.saveAll(List.of(order1, order2, order3));

		// when
		List<Order> orders = orderRepository.findOrdersBy(orderDate, orderDate.plusDays(1), OrderStatus.PAYMENT_COMPLETED);

		// then
		assertThat(orders).hasSize(2)
				.extracting("registeredAt", "orderStatus")
				.containsExactlyInAnyOrder(
						tuple(orderDate.plusHours(1), OrderStatus.PAYMENT_COMPLETED),
						tuple(orderDate.plusHours(21), OrderStatus.PAYMENT_COMPLETED)
				);

	}

	private Order createOrder(
			List<Product> products,
			LocalDateTime registeredAt,
			OrderStatus orderStatus
	) {
		return Order.builder()
				.products(products)
				.registeredAt(registeredAt)
				.orderStatus(orderStatus)
				.build();
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
