package org.saiteja.productservice.services;

import org.saiteja.productservice.dtos.FakeStoreProductDto;
import org.saiteja.productservice.exceptions.ProductNotFoundException;
import org.saiteja.productservice.models.Product;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service("fakeStoreProductService")
public class FakeStoreProductService implements ProductService{
    private RestTemplate restTemplate ; //using this, you will be able to call 3rd party apis

    public FakeStoreProductService(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    @Override
    public List<Product> getAllProducts() {
        FakeStoreProductDto[] fakeStoreProductDtos = restTemplate.getForObject("https://fakestoreapi.com/products",
                FakeStoreProductDto[].class);

        List<Product> products = new ArrayList<>();

        for(FakeStoreProductDto fakeStoreProductDto : fakeStoreProductDtos){
            Product p = fakeStoreProductDto.toProduct();
            products.add(p);
        }

        return products;
    }

    @Override
    public Product getSingleProduct(long id) throws ProductNotFoundException {
        /*
        call the external fakestore product api
        'https://fakestoreapi.com/products/1'
         */
//        FakeStoreProductDto fakeStoreProductDto = restTemplate.getForObject("https://fakestoreapi.com/products/" + id,
//                FakeStoreProductDto.class);

        ResponseEntity<FakeStoreProductDto> fakeStoreProductDtoResponseEntity = restTemplate.getForEntity("https://fakestoreapi.com/products/" + id,
                FakeStoreProductDto.class);


        if(fakeStoreProductDtoResponseEntity.getStatusCode() != HttpStatusCode.valueOf(200)){
            //handle this exception
        }

        //fakeStoreProductDtoResponseEntity.getHeaders();

        FakeStoreProductDto fakeStoreProductDto = fakeStoreProductDtoResponseEntity.getBody();

        if(fakeStoreProductDto == null){
            throw new ProductNotFoundException("Product with id " + id + " is not present with the service. It's an invalid id");
        }

        /*
        if(b != 0) a / b
        b is zero
         */


        return fakeStoreProductDto.toProduct();
    }


    @Override
    public Product createProduct(String title,
                                 String description,
                                 double price,
                                 String imageUrl,
                                 String category) {
        FakeStoreProductDto fakeStoreProductDto = new FakeStoreProductDto();

        fakeStoreProductDto.setCategory(category);
        fakeStoreProductDto.setImage(imageUrl);
        fakeStoreProductDto.setTitle(title);
        fakeStoreProductDto.setPrice(price);
        fakeStoreProductDto.setDescription(description);

        FakeStoreProductDto fakeStoreProductDto1 = restTemplate.postForObject("https://fakestoreapi.com/products",
                fakeStoreProductDto,
                FakeStoreProductDto.class);

        return fakeStoreProductDto1.toProduct();
        /*
        POST /products actually doesn't create a new object in the fakestore
        It's just a dummy api, it does nothing
         */
    }
}