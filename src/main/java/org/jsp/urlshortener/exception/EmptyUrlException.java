package org.jsp.urlshortener.exception;

public class EmptyUrlException extends UrlShortenerException {
    
    public EmptyUrlException() {
        super("URL cannot be empty. Please enter a valid URL");
    }
}