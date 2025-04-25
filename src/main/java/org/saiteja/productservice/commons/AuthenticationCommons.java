package org.saiteja.productservice.commons;

import org.saiteja.productservice.dtos.UserDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthenticationCommons {

   private RestTemplate restTemplate ;

   public AuthenticationCommons(RestTemplate restTemplate) {
       this.restTemplate = restTemplate;
   }
    public UserDTO validateToken(String token){

        // what is type of request am i going to make to  call user service .
       ResponseEntity<UserDTO> userDTOResponseEntity=  restTemplate.
               postForEntity("http://localhost:8080/users/validate/"+token,null,UserDTO.class);

       return userDTOResponseEntity.getBody();
    }
}
