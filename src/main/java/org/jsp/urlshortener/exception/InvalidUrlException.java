package org.jsp.urlshortener.exception;

public class InvalidUrlException extends UrlShortenerException {
    
    public InvalidUrlException(String url) {
        super("Invalid URL format: '" + url + "'. Please include http:// or https://");
    }
    
    public InvalidUrlException() {
        super("Please provide a valid URL (e.g., https://www.example.com)");
    }
}