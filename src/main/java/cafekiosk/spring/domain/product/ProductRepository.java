package cafekiosk.spring.domain.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {


    List<Product> findAllBySellingStatusIn(List<ProductSellingStatus> sellingStatuses);

    List<Product> findAllByProductNumberIn(List<String> productNumbers);

    @Query("select p.productNumber " +
            "from Product p " +
            "where p.type = :type " +
            "order by p.productNumber desc ")
    String findLastProductNumberByType(@Param("type") ProductType type);
}
