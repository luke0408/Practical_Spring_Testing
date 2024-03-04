package cafekiosk.spring.api.service.product;

import cafekiosk.spring.api.controller.product.dto.request.ProductCreatRequest;
import cafekiosk.spring.api.service.product.response.ProductResponse;
import cafekiosk.spring.domain.product.Product;
import cafekiosk.spring.domain.product.ProductRepository;
import cafekiosk.spring.domain.product.ProductSellingStatus;
import cafekiosk.spring.domain.product.ProductType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;

    // 동시성 이슈가 발생할 수 있는 메서드
    // UUID를 사용하거나, DB의 sequence를 사용하거나, Redis를 사용하여 해결할 수 있다.
    @Transactional
    public ProductResponse createProduct(ProductCreatRequest request) {
        String nextProductNumber = createNextProductNumber(request.getType());
        return ProductResponse.of(productRepository.save(request.toEntity(nextProductNumber)));
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> getSellingProducts() {
        List<Product> products = productRepository.findAllBySellingStatusIn(ProductSellingStatus.forDisplay());

        return products.stream()
                .map(product -> ProductResponse.of(product))
                .collect(Collectors.toList());
    }

    private String createNextProductNumber(ProductType type) {
        String lastProductNumber = productRepository.findLastProductNumberByType(type);

        if (lastProductNumber == null) {
            return type.getPrefix() + "01";
        }

        String lastProductNumberWithoutPrefix = lastProductNumber.substring(1);
        int nextProductNumber = Integer.parseInt(lastProductNumberWithoutPrefix) + 1;
        return type.getPrefix() + String.format("%03d", nextProductNumber);
    }

}
