package com.mango.url_shortener.service;

import com.mango.url_shortener.model.Url;
import com.mango.url_shortener.repository.UrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;

@Service
public class UrlService {

    @Autowired
    private UrlRepository urlRepository;

    // Generate a short url
    public String shortenUrl(String originalUrl){
        String encoded = Base64.getEncoder()
                .encodeToString(originalUrl.getBytes(StandardCharsets.UTF_8))
                .substring(0,8); // Take only 8 chars for a short link

        Url url = Url.builder()
                .originalUrl(originalUrl)
                .shortUrl(encoded)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusDays(30)) // Expires in 30 days
                .build();

        urlRepository.save(url);

        return encoded;
    }

    // Retrieve the original URL from short URL
    public Optional<Url> getOriginalUrl(String shortUrl){
        return urlRepository.findByShortUrl(shortUrl);
    }
}
