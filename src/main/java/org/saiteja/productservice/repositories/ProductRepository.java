package org.saiteja.productservice.repositories;

import org.saiteja.productservice.models.Category;
import org.saiteja.productservice.models.Product;
import org.saiteja.productservice.projections.ProductProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Product save(Product p);


    @Override
    List<Product> findAll();

    @Override
    Optional<Product> findById(Long id);

    //Product findByIdIs(Long id);

    List<Product> findByCategory(Category category);

    List<Product> findAllByCategory_Title(String categoryTitle);

    List<Product> findAllByCategory_Id(Long categoryId);

    List<Product> findByTitleStartingWithAndIdEqualsAndPriceLessThan(String startingWith, Long id, double priceLessThan);

    /*
    title, id of products
    HQL
     */


    @Query("select p.title as title, p.id as id from Product p where p.category.title = :categoryName")
    List<ProductProjection> getTitlesAndIdOfAllProductsWithGivenCategoryName(@Param("categoryName") String categoryName);

    @Query(value = "select * from products p where p.id = 1 and p.title = :productTitle", nativeQuery = true)
    List<ProductProjection> getTitlesAndIdOfAllProductsWithCategoryNameEquals(@Param("productTitle") String productTitle);

}