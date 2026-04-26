package drinkshop.ut.service;

import drinkshop.domain.CategorieBautura;
import drinkshop.domain.Product;
import drinkshop.domain.TipBautura;
import drinkshop.repository.file.FileProductRepository;
import drinkshop.service.ProductService;
import drinkshop.service.validator.ProductValidator;
import drinkshop.service.validator.ValidationException;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Step 3: Integrare R (Repository)
 * Se testează S (ProductService) împreună cu V (ProductValidator) și R (FileProductRepository).
 * Toate componentele sunt reale (integrare incrementală completă).
 */
public class ProductServiceIntegrationStep3Test {

    private static final String TEST_FILE = "target/test_products.csv";
    private FileProductRepository repository;
    private ProductValidator validator;
    private ProductService productService;

    @BeforeEach
    void setUp() throws IOException {
        // Ștergem fișierul de test dacă există pentru a avea un mediu curat
        Files.deleteIfExists(new File(TEST_FILE).toPath());
        
        // R este real (FileProductRepository)
        repository = new FileProductRepository(TEST_FILE);
        // V este real
        validator = new ProductValidator();
        // S este real, injectăm V real și R real
        productService = new ProductService(repository, validator);
    }

    @AfterAll
    static void tearDownAll() throws IOException {
        Files.deleteIfExists(new File(TEST_FILE).toPath());
    }

    @Test
    @DisplayName("Step 3: integrare completă S+V+R - update reușit")
    void testUpdateProductCompleteIntegration() {
        // Arrange: Adăugăm un produs în repo pentru a-l putea updata
        Product p = new Product(1, "Original", 5.0, CategorieBautura.ALL, TipBautura.ALL);
        repository.save(p);

        // Act
        productService.updateProduct(1, "Updated", 10.0, CategorieBautura.JUICE, TipBautura.BASIC);

        // Assert: Verificăm că datele s-au schimbat în repo-ul real
        Product updated = repository.findOne(1);
        assertNotNull(updated);
        assertEquals("Updated", updated.getNume());
        assertEquals(10.0, updated.getPret());
        assertEquals(CategorieBautura.JUICE, updated.getCategorie());
    }

    @Test
    @DisplayName("Step 3: integrare completă S+V+R - validare eșuată")
    void testUpdateProductValidationFailsIntegration() {
        // Arrange
        Product p = new Product(2, "Original", 5.0, CategorieBautura.ALL, TipBautura.ALL);
        repository.save(p);

        // Act & Assert: V aruncă ValidationException pentru preț negativ
        assertThrows(ValidationException.class, () -> {
            productService.updateProduct(2, "Fail", -1.0, CategorieBautura.ALL, TipBautura.ALL);
        });

        // Verificăm că în R datele NU s-au schimbat
        Product notUpdated = repository.findOne(2);
        assertEquals("Original", notUpdated.getNume());
        assertEquals(5.0, notUpdated.getPret());
    }
}
