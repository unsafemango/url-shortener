package com.mango.url_shortener.service;

import com.mango.url_shortener.model.Url;
import com.mango.url_shortener.repository.UrlRepository;
import org.hashids.Hashids;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UrlService {

    @Autowired
    private UrlRepository urlRepository;
    private final Hashids hashids = new Hashids("the_salt_is_salty", 8); // 8-character short URLs

    // save a new url and return the shortened version
    public String shortenUrl(String originalUrl){
        // Save the entity first to generate an ID
        Url url = Url.builder()
                .originalUrl(originalUrl)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusDays(30)) // Expire in 30 days
                .build();

        url = urlRepository.save(url); // Save and get assigned ID

        // Generate a unique short URL using the assigned ID
        String shortUrl = hashids.encode(url.getId());

        // Update the entity with the generated short URL
        url.setShortUrl(shortUrl);
        urlRepository.save(url); // Save again with updated short URL

        return shortUrl;
    }

    // Retrieve the original URL from short URL
    public Optional<Url> getOriginalUrl(String shortUrl){
        return urlRepository.findByShortUrl(shortUrl);
    }

}
