package com.mango.url_shortener.controller;

import com.mango.url_shortener.model.Url;
import com.mango.url_shortener.service.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/api/url")
public class UrlController {
    @Autowired
    private UrlService urlService;

    // Shorten a url
    @PostMapping("shorten")
    public ResponseEntity<String> shortenUrl(@RequestBody String originalUrl){
        String shortUrl = urlService.shortenUrl(originalUrl);
        return ResponseEntity.ok("Shortened URL: http://localhost:8080/api/url" + shortUrl);
    }

    // Redirect to original url
    @GetMapping("/{shortUrl}")
    public ResponseEntity<String> getOriginalUrl(@PathVariable String shortUrl) {
        Optional<Url> url = urlService.getOriginalUrl(shortUrl);

        return url.map(value -> ResponseEntity.ok(value.getOriginalUrl())) // Return URL as JSON
                .orElseGet(() -> ResponseEntity.notFound().build()); // 404 if not found
    }
}
