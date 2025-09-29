package by.gsu.scherbak.orderService.controller;

import by.gsu.scherbak.orderService.service.ApiKeyService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 * Controller for api keys
 *
 * @version 1.0 21.09.2025
 * @author Scherbak Andrey
 * */
@RestController
@RequestMapping("/api/auth")
public class ApiKeyController {
    private final ApiKeyService apiKeyService;

    public ApiKeyController(ApiKeyService apiKeyService) {
        this.apiKeyService = apiKeyService;
    }

    @GetMapping("/token")
    public String getKey() {
        return apiKeyService.generateKey();
    }
}
