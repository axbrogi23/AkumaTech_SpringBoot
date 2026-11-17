package com.akumatech.backend.product;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.*;

i

@RestController
@RequestMapping("/api")
public class ProductController {

    private final ProductRepository products;
    
    public ProductController(ProductRepository products) { 
        this.products = products; }
    
    @GetMapping("/products")
    public List<Product> all() { 
        return products.findAll(); 
    }

    @PostMapping("/admin/products")
    public Product create(@Valid @RequestBody Product product) { 
        return products.save(product); 
    }
    @PutMapping("/admin/products/{id}")
    public Product update(@PathVariable Long id, @Valid @RequestBody Product input) {
        Product product = products.findById(id).orElseThrow();
        product.setNombre(input.getNombre()); product.setPrecio(input.getPrecio());
        product.setImagen(input.getImagen()); product.setDescripcion(input.getDescripcion());
        return products.save(product);
    }
    
    @DeleteMapping("/admin/products/{id}")
    public void delete(@PathVariable Long id) { products.deleteById(id); }
}
