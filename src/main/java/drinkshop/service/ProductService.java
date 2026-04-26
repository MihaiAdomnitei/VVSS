package drinkshop.service;

import drinkshop.domain.*;
import drinkshop.repository.Repository;

import java.util.List;
import java.util.stream.Collectors;

public class ProductService {

    private final Repository<Integer, Product> productRepo;
    private final drinkshop.service.validator.ProductValidator productValidator;

    public ProductService(Repository<Integer, Product> productRepo, drinkshop.service.validator.ProductValidator productValidator) {
        this.productRepo = productRepo;
        this.productValidator = productValidator;
    }

    public void addProduct(Product p) {
        productValidator.validate(p);
        productRepo.save(p);
    }

    public void updateProduct(int id, String name, double price, CategorieBautura categorie, TipBautura tip) {
        Product updated = new Product(id, name, price, categorie, tip);
        productValidator.validate(updated);
        productRepo.update(updated);
    }

    public void deleteProduct(int id) {
        productRepo.delete(id);
    }

    public List<Product> getAllProducts() {
//        Iterable<Product> it=productRepo.findAll();
//        ArrayList<Product> products=new ArrayList<>();
//        it.forEach(products::add);
//        return products;

//        return StreamSupport.stream(productRepo.findAll().spliterator(), false)
//                    .collect(Collectors.toList());
        return productRepo.findAll();
    }

    public Product findById(int id) {
        return productRepo.findOne(id);
    }

    public List<Product> filterByCategorie(CategorieBautura categorie) {
        if (categorie == CategorieBautura.ALL) return getAllProducts();
        return getAllProducts().stream()
                .filter(p -> p.getCategorie() == categorie)
                .collect(Collectors.toList());
    }

    public List<Product> filterByTip(TipBautura tip) {
        if (tip == TipBautura.ALL) return getAllProducts();
        return getAllProducts().stream()
                .filter(p -> p.getTip() == tip)
                .collect(Collectors.toList());
    }

    public Product findProductByNameAndBudget(String name, double maxBudget) throws IllegalArgumentException {
        Product found = null;

        for (Product p : productRepo.findAll()) {
            if (p.getNume().equalsIgnoreCase(name)) {
                if (p.getPret() <= maxBudget) {
                    found = p;
                }
            }
        }

        if (found == null) {
            throw new IllegalArgumentException("Produsul nu a fost gasit sau depaseste bugetul!");
        }

        return found;
    }
}