package com.yqg.pay.util;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * Created by huanhuan on 2018/12/17.
 */
@Service
public class CustomRestTemplate extends RestTemplate {
    public <T> T getForObject(String url, HttpHeaders headers, Class<T> responseType, Object... uriVariables) throws RestClientException {
        ResponseEntity<T> response = this.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), responseType, uriVariables);
        return response.getBody();
    }

}
