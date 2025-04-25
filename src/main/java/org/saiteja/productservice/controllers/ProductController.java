package org.saiteja.productservice.controllers;

import lombok.NonNull;
import org.saiteja.productservice.commons.AuthenticationCommons;
import org.saiteja.productservice.dtos.CreateProductRequestDto;
import org.saiteja.productservice.dtos.Role;
import org.saiteja.productservice.dtos.UserDTO;
import org.saiteja.productservice.exceptions.ProductNotFoundException;
import org.saiteja.productservice.models.Product;
import org.saiteja.productservice.services.ProductService;
import org.saiteja.productservice.services.SelfProductService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RestController;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ProductController {

    public ProductService productService;
    AuthenticationCommons authenticationCommons;
    private RestTemplate restTemplate;
    private SelfProductService selfProductService;

    public ProductController(@Qualifier("selfProductService") ProductService productService,SelfProductService selfProductService, AuthenticationCommons authenticationCommons,RestTemplate restTemplate){
        this.productService = productService;
        this.authenticationCommons = authenticationCommons;
        this.restTemplate = restTemplate;
        this.selfProductService=selfProductService;

    }
    /*
    at the end of the day
    api = method in my controller
     */

    /*
    GET /products
     */

    // this is for Authentation service calling user service.

    @GetMapping("/productsAuth/{token}")
    public ResponseEntity<List<Product> >getAllProducts( @PathVariable("token") @NonNull String token){
        // validate the token before calling the service.
      UserDTO userDTO=authenticationCommons.validateToken(token);
      if(userDTO==null){
          // the token is invalid, this is forbiden request.
          return new ResponseEntity<>(HttpStatus.FORBIDDEN);
      }

      // only admins are allowed to this /products end point handle authorization part here.
        boolean isAdmin=false;
        for( Role role : userDTO.getRoles()){
            if(role.getName().equals("ADMIN")){

            }
        }
        if(isAdmin==false){
            // not allowed to further , not access.
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        }

      List<Product> productList=productService.getAllProducts();
      ResponseEntity<List<Product>> finalResult = new ResponseEntity<>(productList, HttpStatus.ACCEPTED);
        return finalResult;
    }


    /*
    GET /products/{id}
     */
    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getSingleProduct(@PathVariable("id") long id) throws ProductNotFoundException {

        Product p = productService.getSingleProduct(id);
        ResponseEntity<Product> responseEntity;
        if(p == null){
            responseEntity = new ResponseEntity<>(p, HttpStatus.NOT_FOUND);
        }else{
            System.out.println("\n\nfound\n\n");
            responseEntity = new ResponseEntity<>(p, HttpStatus.OK);
        }

        return responseEntity;
    }

    /*
    Create a product
    {
        title :
        description:
        price:
        category:
    } => payload / request body
    POST /products
     */



    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts() throws ProductNotFoundException {

        //I want to call user service
        ////Client side load balancing
        UserDTO userDTO = restTemplate.getForObject("http://UserServiceAuthentication/users/2",
                UserDTO.class);
        //we have the user from the user service


        List<Product> products = new ArrayList<>();

//        List<Product> productListSample = new ArrayList<>();
//        Product extraProd = new Product();
//        extraProd.setPrice(100);
//        extraProd.setTitle("pen");
//        extraProd.setDescription("utility");
//
//        productListSample.add(extraProd);

//        throw new ProductNotFoundException("Bla bla bla");

        ResponseEntity<List<Product>> response = new ResponseEntity<>(products, HttpStatus.OK);
        return response;
    }

    @PostMapping("/products")

    public Product createProduct(@RequestBody CreateProductRequestDto createProductRequestDto){
        return productService.createProduct(createProductRequestDto.getTitle(),
                createProductRequestDto.getDescription(),
                createProductRequestDto.getPrice(),
                createProductRequestDto.getImage(),
                createProductRequestDto.getCategory());
    }




    @GetMapping("/product/cache")
    public ResponseEntity<List<Product>> getAllUsers(){
        List<Product> userList=selfProductService.getListOfUsers();
        return userList!= null ? ResponseEntity.ok(userList) : ResponseEntity.notFound().build();
    }
    @GetMapping("/product/cache/{id}")
    public ResponseEntity<Product> getUserById(@PathVariable Long id){
        Product user=selfProductService.getUserById(id);
        return user!=null ?   ResponseEntity.ok(user) :ResponseEntity.notFound().build() ;

    }

    @PostMapping("/product//cache")
    public ResponseEntity<Product> createUser(@RequestBody Product user){
        Product saveduser=selfProductService.saveUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(saveduser);
    }

    @DeleteMapping("/product/cache/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long id){
        selfProductService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }


//    @ExceptionHandler(ProductNotFoundException.class)
//    public ResponseEntity<ErrorDTO> handleProductNotFoundException(ProductNotFoundException productNotFoundException){
//        ErrorDTO errorDTO = new ErrorDTO();
//
//        errorDTO.setMessage(productNotFoundException.getMessage());
//
//        ResponseEntity<ErrorDTO> responseEntity = new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
//
//        return responseEntity;
//    }
}