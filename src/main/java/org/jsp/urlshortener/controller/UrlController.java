package org.jsp.urlshortener.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

// Java imports
import java.util.Map;
import java.util.HashMap;

// Your project imports
import org.jsp.urlshortener.service.UrlService;
import org.jsp.urlshortener.model.LinkData;
import org.jsp.urlshortener.exception.UrlNotFoundException;
import org.jsp.urlshortener.exception.InvalidUrlException;
import org.jsp.urlshortener.exception.EmptyUrlException;

@RestController
public class UrlController {
    
    @Autowired
    private UrlService urlService;
    
    @PostMapping("/api/shorten")
    public ResponseEntity<?> shortenUrl(@RequestBody Map<String, String> request) {
        String longUrl = request.get("longUrl");
        
        if (longUrl == null || longUrl.trim().isEmpty()) {
            throw new EmptyUrlException();
        }
        
        longUrl = longUrl.trim();
        if (!longUrl.startsWith("http://") && !longUrl.startsWith("https://")) {
            longUrl = "https://" + longUrl;
        }
        
        try {
            new java.net.URL(longUrl);
        } catch (Exception e) {
            throw new InvalidUrlException(longUrl);
        }
        
        String shortCode = urlService.shortenUrl(longUrl);
        String shortUrl = "http://localhost:8080/r/" + shortCode;
        
     
        LinkData stats = urlService.getStats(shortCode);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("shortUrl", shortUrl);
        response.put("shortCode", shortCode);
        response.put("longUrl", longUrl);
        response.put("clicks", stats.getClicks());
        response.put("message", "URL shortened successfully!");
        
        return ResponseEntity.ok(response);
    }
    
   
    @GetMapping("/r/{shortCode}")
    public ResponseEntity<?> redirectToUrl(@PathVariable String shortCode) {
        if (shortCode.contains(".")) {
            return ResponseEntity.notFound().build();
        }
        
        
        String originalUrl = urlService.getOriginalUrl(shortCode);
        
        if (originalUrl == null) {
            throw new UrlNotFoundException(shortCode);
        }
        
        
        return ResponseEntity.status(HttpStatus.FOUND)
                .header("Location", originalUrl)
                .build();
    }
    
   
    @GetMapping("/api/stats/{shortCode}")
    public ResponseEntity<?> getStats(@PathVariable String shortCode) {
        LinkData stats = urlService.getStats(shortCode);
        
        if (stats == null) {
            throw new UrlNotFoundException(shortCode);
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("shortCode", shortCode);
        response.put("longUrl", stats.getLongUrl());
        response.put("clicks", stats.getClicks());
        response.put("created", "In-memory storage");
        
        return ResponseEntity.ok(response);
    }
    
    @ExceptionHandler(UrlNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleUrlNotFound(UrlNotFoundException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("success", false);
        error.put("error", "NOT_FOUND");
        error.put("message", ex.getMessage());
        error.put("suggestion", "Please check the short URL or create a new one");
        error.put("timestamp", System.currentTimeMillis());
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
    
    
    @ExceptionHandler(InvalidUrlException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidUrl(InvalidUrlException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("success", false);
        error.put("error", "INVALID_URL");
        error.put("message", ex.getMessage());
        error.put("suggestion", "Try: https://www.google.com or http://example.com");
        error.put("timestamp", System.currentTimeMillis());
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
    
   
    @ExceptionHandler(EmptyUrlException.class)
    public ResponseEntity<Map<String, Object>> handleEmptyUrl(EmptyUrlException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("success", false);
        error.put("error", "EMPTY_URL");
        error.put("message", ex.getMessage());
        error.put("suggestion", "Please enter a valid URL in the input field");
        error.put("timestamp", System.currentTimeMillis());
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("success", false);
        error.put("error", "INTERNAL_ERROR");
        error.put("message", "An unexpected error occurred: " + ex.getMessage());
        error.put("suggestion", "Please try again or contact support");
        error.put("timestamp", System.currentTimeMillis());
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}