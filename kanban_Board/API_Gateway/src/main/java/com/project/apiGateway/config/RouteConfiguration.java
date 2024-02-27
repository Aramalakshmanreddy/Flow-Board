package com.project.apiGateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfiguration
{
    //this class will help you to provide Single Entry point for all microservices
    @Bean
    public RouteLocator ApiRoutingMethod(RouteLocatorBuilder routeLocatorBuilder)
    {
        return routeLocatorBuilder.routes()
                .route(predicateSpec -> predicateSpec.path("/api/v1/**").uri("lb://user-authentication-service"))
                .route(predicateSpec -> predicateSpec.path("/api/v2/**").uri("lb://kanban-service"))
                .route(predicateSpec -> predicateSpec.path("/api/v3/**").uri("lb://notification-service"))
                .build();
    }

}
