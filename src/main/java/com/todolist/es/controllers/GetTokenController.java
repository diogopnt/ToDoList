package com.todolist.es.controllers;

import com.todolist.es.services.JwtUtilService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/token")
@Tag(name = "Token API", description = "Handles token retrieval and caching for the application.")
public class GetTokenController {

    @Value("${external.auth.token.url}")
    private String tokenUrl;

    @Value("${external.auth.client.credentials}")
    private String clientCredentials;

    private final RestTemplate restTemplate;
    private final JwtUtilService jwtUtilService;

    private final Map<String, String> tokenCache = new ConcurrentHashMap<>();
    private final Map<String, Lock> lockMap = new ConcurrentHashMap<>();

    public GetTokenController(RestTemplate restTemplate, JwtUtilService jwtUtilService) {
        this.restTemplate = restTemplate;
        this.jwtUtilService = jwtUtilService;
    }

    @Operation(
            summary = "Retrieve a token using an authorization code",
            description = """
            This endpoint retrieves a token by sending an authorization code to an external authentication server.
            If the token is already cached, it is returned from the cache. 
            Otherwise, it will make a request to the external server to obtain the token.
        """,
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Token retrieved successfully",
                            content = @Content(schema = @Schema(example = "{\"token\":\"<your_token_here>\"}"))
                    ),
                    @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @Parameters({
            @Parameter(
                    name = "code",
                    description = "Authorization code received from the authentication server",
                    required = true,
                    example = "abc123xyz"
            )
    })
    @GetMapping("/get")
    public ResponseEntity<Map<String, Object>> getToken(
            @RequestParam String code) throws Exception {

        System.out.println("Code: " + code);

        if (tokenCache.containsKey(code)) {
            System.out.println("Returning cached token for code: " + code);

            String jwtToken = tokenCache.get(code);
            JsonObject jsonObject = new JsonParser().parse(jwtToken).getAsJsonObject();
            String id_token = jsonObject.get("id_token").getAsString();

            return ResponseEntity.ok(getTokenResponse(jwtToken));
        }

        Lock lock = lockMap.computeIfAbsent(code, k -> new ReentrantLock());
        lock.lock();

        try {
            if (tokenCache.containsKey(code)) {
                System.out.println("Returning cached token for code after acquiring lock: " + code);

                String jwtToken = tokenCache.get(code);
                JsonObject jsonObject = new JsonParser().parse(jwtToken).getAsJsonObject();
                String id_token = jsonObject.get("id_token").getAsString();

                return ResponseEntity.ok(getTokenResponse(jwtToken));
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED);
            headers.setAccept(Collections.singletonList(org.springframework.http.MediaType.APPLICATION_JSON));
            headers.set("Authorization", "Basic " + clientCredentials);

            String body = "grant_type=authorization_code" +
                    "&code=" + code +
                    "&redirect_uri=https://es-ua.ddns.net/todo";

            HttpEntity<String> entity = new HttpEntity<>(body, headers);

            System.out.println("Headers: " + headers);
            System.out.println("Body: " + body);

            ResponseEntity<String> response = restTemplate.exchange(tokenUrl, HttpMethod.POST, entity, String.class);

            String jwtToken = response.getBody();
            tokenCache.put(code, jwtToken);

            JsonObject jsonObject = new JsonParser().parse(jwtToken).getAsJsonObject();
            String id_token = jsonObject.get("id_token").getAsString();

            System.out.println(jwtToken);
            return ResponseEntity.ok(getTokenResponse(jwtToken));

        } finally {
            lock.unlock();
            lockMap.remove(code);
        }
    }

    private Map<String, Object> getTokenResponse(String token) {
        return Map.of("token", token);
    }
}

