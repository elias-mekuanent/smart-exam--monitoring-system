package com.senpare.apigateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.config.GatewayAutoConfiguration;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(GatewayAutoConfiguration.class)
public class RouteConfig {

    private static final String BASE_URL = "/api/v1";


    @Value("${app.base-url}")
    private String apiGatewayBaseUrl;
    @Value("${services.uri.eureka-server}")
    private String eurekaServerUri;
    @Value("${services.uri.audio-processing-service}")
    private String audioProcessorUrl;
    @Value("${services.uri.exam-service}")
    private String examServiceUrl;
    @Value("${services.uri.payment-service}")
    private String paymentServiceUrl;
    @Value("${services.uri.auth-service}")
    private String authServiceUrl;
    @Value("${services.uri.test-service-url}")
    private String testServiceUrl;

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("openapi", r ->
                        r.path("/v3/api-docs/**")
                        .filters(f -> f.rewritePath("/v3/api-docs/(?<path>.*)", "/$\\{path}/v3/api-docs"))
                        .uri(apiGatewayBaseUrl)
                )
                .route("test-service", r ->
                        r.path(BASE_URL + "/test-service/**")
                        .uri(testServiceUrl)
                )
                .route("audio-processing-service", r ->
                        r.path(BASE_URL +  "/audio-processing-service/**")
                        .uri(audioProcessorUrl)
                )
                .route("audio-processing-service-docs", r ->
                        r.path("/audio-processing-service/**")
                                .uri(audioProcessorUrl)
                )
                .route("exam-service", r ->
                        r.path(BASE_URL +  "/exam-service/**")
                                .uri(examServiceUrl)
                )
                .route("exam-service-docs", r ->
                        r.path("/exam-service/**")
                                .uri(examServiceUrl)
                )
                .route("payment-service", r ->
                        r.path(BASE_URL +  "/payment-service/**")
                                .uri(paymentServiceUrl)
                )
                .route("payment-service-docs", r ->
                        r.path("/payment-service/**")
                                .uri(paymentServiceUrl)
                )
                .route("auth-service", r ->
                        r.path(BASE_URL +  "/auth-service/**")
                                .uri(authServiceUrl)
                )
                .route("auth-service-docs", r ->
                        r.path("/auth-service/**")
                                .uri(authServiceUrl)
                )
                .route("eureka-server", r ->
                         r.path("/eureka/web")
                        .filters(f -> f.setPath("/"))
                        .uri(eurekaServerUri)
                )
                .route("eureka-server-static", r ->
                        r.path("/eureka/**")
                        .uri(eurekaServerUri)
                ).build();


    }
}

//    @Autowired
//    private TokenRelayGatewayFilterFactory filterFactory;

//                        .filters(f -> f.filter(filterFactory.apply()))
//                        .filters(f -> f.oauth2ResourceServer(
//                                        oauth2 -> oauth2.jwt(jwt -> jwt.jwkSetUri("<keycloak server url>/auth/realms/<realm name>/protocol/openid-connect/certs"))
//                                )