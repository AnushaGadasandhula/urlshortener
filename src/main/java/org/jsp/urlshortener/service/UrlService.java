package org.jsp.urlshortener.service;

import org.springframework.stereotype.Service;
import org.jsp.urlshortener.model.LinkData;
import org.jsp.urlshortener.exception.UrlNotFoundException;
import org.jsp.urlshortener.exception.DuplicateShortCodeException;
import java.util.Map;
import java.util.HashMap;
import java.util.Random;

@Service
public class UrlService {
    
    private final Map<String, LinkData> urlDatabase = new HashMap<>();
    private final Random random = new Random();
    
   
    private String generateRandomCode() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            int index = random.nextInt(characters.length());
            code.append(characters.charAt(index));
        }
        return code.toString();
    }
    
    private String getUniqueShortCode() {
        int maxAttempts = 10;  
        int attempts = 0;
        String code;
        
        do {
            code = generateRandomCode();
            attempts++;
            
            if (urlDatabase.containsKey(code)) {
                System.out.println("⚠️ Collision detected for code: " + code + ". Retrying... (Attempt " + attempts + ")");
            }
            
            if (attempts >= maxAttempts) {
                System.out.println("⚠️ Too many collisions! Generating longer code...");
                code = generateLongerCode();
                break;
            }
            
        } while (urlDatabase.containsKey(code));
        
        return code;
    }
    
    private String generateLongerCode() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(characters.length());
            code.append(characters.charAt(index));
        }
        return code.toString();
    }
    
    public String shortenUrl(String longUrl) {
        for (Map.Entry<String, LinkData> entry : urlDatabase.entrySet()) {
            if (entry.getValue().getLongUrl().equals(longUrl)) {
                System.out.println("✅ URL already exists with code: " + entry.getKey());
                return entry.getKey(); 
            }
        }
        
        String shortCode = getUniqueShortCode();
        urlDatabase.put(shortCode, new LinkData(longUrl));
        System.out.println("🆕 New short URL created: " + shortCode + " → " + longUrl);
        System.out.println("📊 Total URLs in database: " + urlDatabase.size());
        
        return shortCode;
    }
    
    public String getOriginalUrl(String shortCode) {
        LinkData linkData = urlDatabase.get(shortCode);
        
        if (linkData == null) {
            System.out.println("❌ Short code not found: " + shortCode);
            throw new UrlNotFoundException(shortCode);
        }
        
        linkData.incrementClicks();
        urlDatabase.put(shortCode, linkData);
        
        System.out.println("🔗 Redirect: " + shortCode + " → " + linkData.getLongUrl() + " (Total clicks: " + linkData.getClicks() + ")");
        
        return linkData.getLongUrl();
    }
    
    public LinkData getStats(String shortCode) {
        LinkData linkData = urlDatabase.get(shortCode);
        
        if (linkData == null) {
            throw new UrlNotFoundException(shortCode);
        }
        
        return linkData;
    }
    
    public Map<String, LinkData> getAllUrls() {
        return urlDatabase;
    }
    
    public int getDatabaseSize() {
        return urlDatabase.size();
    }
}