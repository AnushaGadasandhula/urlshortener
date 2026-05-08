package org.jsp.urlshortener.exception;

public class UrlNotFoundException extends UrlShortenerException {
    
    public UrlNotFoundException(String shortCode) {
        super("Short URL code '" + shortCode + "' does not exist or has expired");
    }
    
    public UrlNotFoundException() {
        super("The requested short URL was not found");
    }
}