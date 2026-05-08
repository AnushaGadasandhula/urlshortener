package org.jsp.urlshortener.exception;

public class DuplicateShortCodeException extends UrlShortenerException {
    
    public DuplicateShortCodeException(String shortCode) {
        super("System collision detected for code: '" + shortCode + "'. Generating new code...");
    }
}