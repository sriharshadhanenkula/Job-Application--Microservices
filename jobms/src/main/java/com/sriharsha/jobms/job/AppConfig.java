package com.sriharsha.jobms.job;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

// There is no need of below class when we are using openfeign, If we use Rest template
// Then below class should be declared to use services name and also Ribbon is used for load balancing
// by using feign there is no need of ribbon. feign automatically balance the load
@Configuration
public class AppConfig {

    @LoadBalanced
    @Bean
    public RestTemplate restTemplate(){
        return  new RestTemplate();
    }
}
