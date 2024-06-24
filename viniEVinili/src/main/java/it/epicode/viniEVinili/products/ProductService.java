package it.epicode.viniEVinili.products;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }

    public Product findById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + productId));
    }

    public Product save(Product product) {
        return productRepository.save(product);
    }

    public Product update(Product product) {
        // Controlla se il prodotto esiste
        if (!productRepository.existsById(product.getId())) {
            throw new EntityNotFoundException("Cannot update product. Product with id " + product.getId() + " not found.");
        }
        return productRepository.save(product);
    }

    public void delete(Long productId) {
        productRepository.deleteById(productId);
    }
}
