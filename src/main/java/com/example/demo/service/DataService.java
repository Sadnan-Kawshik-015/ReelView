package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
@Service
public class DataService extends BaseService{

    @Autowired
    @Qualifier("restTemplateWithLoadBalance")
    protected RestTemplate restTemplate;

    protected <T> ResponseEntity<T> apiCall(String url, Class<T> responseType, boolean withAuth,String apiKey) {
        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            if (withAuth) {
                httpHeaders.add("Authorization", "Bearer " + apiKey);
            }
            httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            HttpEntity<String> entity = new HttpEntity<>(httpHeaders);
            return restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    responseType
            );
        } catch (HttpStatusCodeException e) {
            // Handle HTTP status code exceptions
            System.err.println("HTTP Status Code Exception: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
            return ResponseEntity.status(e.getStatusCode()).body(null);
        } catch (RestClientException e) {
            // Handle other RestClient exceptions
            System.err.println("Rest Client Exception: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        } catch (Exception e) {
            // Handle any other exceptions
            System.err.println("General Exception: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


}
