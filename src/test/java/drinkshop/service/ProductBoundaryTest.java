package drinkshop.service;

import drinkshop.domain.CategorieBautura;
import drinkshop.domain.Product;
import drinkshop.domain.TipBautura;
import drinkshop.repository.AbstractRepository;
import drinkshop.service.validator.ProductValidator;
import drinkshop.service.validator.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("P231-98 - ProductBoundaryTest BVA Invalid")
public class ProductBoundaryTest {

    private ProductService productService;

    static class DummyProductRepo extends AbstractRepository<Integer, Product> {
        @Override
        protected Integer getId(Product entity) {
            return entity.getId();
        }
    }

    @BeforeEach
    void setUp() {
        productService = new ProductService(new DummyProductRepo(), new ProductValidator());
    }

    @Test
    @DisplayName("P231-98: BVA - Invalid - Adaugare produs cu pret = 0")
    void addProduct_BVAInvalid() {
        Product p = new Product(103, "Invalid Price", 0.0, CategorieBautura.ALL, TipBautura.ALL);
        assertThrows(ValidationException.class, () -> {
            productService.addProduct(p);
        });
    }
}
