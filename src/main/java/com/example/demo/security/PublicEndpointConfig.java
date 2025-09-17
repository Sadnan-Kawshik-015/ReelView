package com.example.demo.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class PublicEndpointConfig {

    public Map<String, List<HttpMethod>> publicEndPointMap() {
        Map<String, List<HttpMethod>> map = new HashMap<>();
        map.put("/v3/api-docs/**", List.of(HttpMethod.GET));
        map.put("/configuration/**", List.of(HttpMethod.GET));
        map.put("/swagger*/**", List.of(HttpMethod.GET));
        map.put("/webjars/**", List.of(HttpMethod.GET));
        map.put("/swagger-ui/**", List.of(HttpMethod.GET));
        map.put("/api/auth/signin", List.of(HttpMethod.POST));
        map.put("/api/auth/forgot-password", List.of(HttpMethod.POST));
        map.put("/api/auth/reset-password", List.of(HttpMethod.POST));
        map.put("/api/auth/signup", List.of(HttpMethod.POST));
        map.put("/api/movies/search", List.of(HttpMethod.POST));
        return map;
    }

    public String[] getPublicEndpoints(HttpMethod httpMethod) {
        Map<String, List<HttpMethod>> map = publicEndPointMap();
        List<String> endPointList = new ArrayList<>();
        map.forEach((key, value) -> {
            if (value.contains(httpMethod)) {
                endPointList.add(key);
            }
        });
        String[] endPoints = new String[endPointList.size()];
        return endPointList.toArray(endPoints);
    }
}
