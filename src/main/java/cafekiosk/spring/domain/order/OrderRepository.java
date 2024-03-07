package cafekiosk.spring.domain.order;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

	@Query("SELECT o "
			+ "FROM Order o "
			+ "WHERE o.registeredAt >= :startDate "
			+ "AND o.registeredAt < :endDate "
			+ "AND o.orderStatus = :orderStatus")
	List<Order> findOrdersBy(LocalDateTime startDate, LocalDateTime endDate, OrderStatus orderStatus);
}
