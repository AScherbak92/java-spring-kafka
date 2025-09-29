package by.gsu.scherbak.orderService.security;

import by.gsu.scherbak.orderService.service.ApiKeyService;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
 * Filter configuration
 *
 * @version 1.0 21.09.2025
 * @author Scherbak Andrey
 * */
@Configuration
public class FilterConfig {
    @Bean
    public FilterRegistrationBean<AuthenticationFilter> filter(ApiKeyService apiKeyService) {
        FilterRegistrationBean<AuthenticationFilter> bean = new FilterRegistrationBean<>();

        bean.setFilter(new AuthenticationFilter(apiKeyService));
        bean.addUrlPatterns("/api/orders/*");
        bean.setOrder(1);

        return bean;
    }
}
