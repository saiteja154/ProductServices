package org.saiteja.productservice.services;

import org.saiteja.productservice.exceptions.ProductNotFoundException;
import org.saiteja.productservice.models.Category;
import org.saiteja.productservice.models.Product;
import org.saiteja.productservice.repositories.CategoryRepository;
import org.saiteja.productservice.repositories.ProductRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("selfProductService")
public class SelfProductService implements ProductService{

    private CategoryRepository categoryRepository;
    private ProductRepository productRepository;

    public SelfProductService(CategoryRepository categoryRepository,
                              ProductRepository productRepository){
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getSingleProduct(long id) throws ProductNotFoundException {
        Optional<Product> product = productRepository.findById(id);
        if(product.isEmpty()){
            throw new ProductNotFoundException("Product with id " + id + " is not found in the database");
        }
        return product.get();
    }

    @Override
    public Product createProduct(String title,
                                 String description,
                                 double price,
                                 String imageUrl,
                                 String category) {
        Product p = new Product();
        //p.setId(12);
        p.setTitle(title);
        p.setDescription(description);
        p.setPrice(price);
        p.setImageUrl(imageUrl);
        //p.setCreatedAt();

        Category categoryFromDB = categoryRepository.findByTitle(category);

        if(categoryFromDB == null){
            Category newCategory = new Category();
            newCategory.setTitle(category);
            p.setCategory(newCategory);
        }else{
            p.setCategory(categoryFromDB);
        }

        Product createdProduct = productRepository.save(p);


        return createdProduct;
    }


    @Cacheable(value = "users")
    public List<Product> getListOfUsers(){
        return productRepository.findAll();
    }


    @Cacheable(value = "users",key = "#id")
    public Product getUserById(Long id){
        return productRepository.findById(id).orElse(null);
    }

    @CachePut(value = "users",key = "#user.id")
    public Product saveUser(Product user){
        return productRepository.save(user);
    }

    @CacheEvict(value = "users",key="#id")
    public void deleteUser(Long id){
        productRepository.deleteById(id);
    }


}