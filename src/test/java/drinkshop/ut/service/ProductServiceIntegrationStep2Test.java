package drinkshop.ut.service;

import drinkshop.domain.CategorieBautura;
import drinkshop.domain.Product;
import drinkshop.domain.TipBautura;
import drinkshop.repository.Repository;
import drinkshop.service.ProductService;
import drinkshop.service.validator.ProductValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Step 2: Integrare V (ProductValidator)
 * Se testează S (ProductService) împreună cu V (ProductValidator).
 * Pentru R (Repository) se folosește un obiect mock.
 */
@ExtendWith(MockitoExtension.class)
public class ProductServiceIntegrationStep2Test {

    @Mock
    private Repository<Integer, Product> mockRepo;

    private ProductValidator validator;
    private ProductService productService;

    @BeforeEach
    void setUp() {
        // V este real (integrare)
        validator = new ProductValidator();
        // S este real, injectăm V real și R mock
        productService = new ProductService(mockRepo, validator);
    }

    @Test
    @DisplayName("Step 2: updateProduct valid - S + V real, R mock")
    void testUpdateProductValid() {
        // Act
        assertDoesNotThrow(() -> {
            productService.updateProduct(1, "Apa Minerala", 2.5, CategorieBautura.ALL, TipBautura.BASIC);
        });

        // Verificăm că s-a ajuns la R.update deoarece V a validat cu succes
        verify(mockRepo, times(1)).update(any(Product.class));
    }

    @Test
    @DisplayName("Step 2: updateProduct invalid - S + V real detectează eroarea, R nu e apelat")
    void testUpdateProductInvalid() {
        // Act & Assert
        // Nume gol -> Validatorul ar trebui să arunce excepție (presupunând logica din ProductValidator)
        assertThrows(RuntimeException.class, () -> {
            productService.updateProduct(1, "", 2.5, CategorieBautura.ALL, TipBautura.BASIC);
        });

        // Verificăm că R.update NU a fost apelat deoarece V a oprit execuția
        verify(mockRepo, never()).update(any(Product.class));
    }
}
