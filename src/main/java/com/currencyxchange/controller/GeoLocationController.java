package com.currencyxchange.controller;

import com.currencyxchange.dto.GeoLocationDTO;
import com.currencyxchange.service.GeoLocationService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/location")
public class GeoLocationController {

    private final GeoLocationService geoLocationService;

    public GeoLocationController(GeoLocationService geoLocationService) {
        this.geoLocationService = geoLocationService;
    }

    @GetMapping
    public Mono<GeoLocationDTO> getUserLocation(HttpServletRequest request) {
        String ip = null;

        // Extract real client IP from Azure-provided header
        String forwardedFor = request.getHeader("X-Forwarded-For");
        if (forwardedFor != null && !forwardedFor.isBlank()) {
            ip = forwardedFor.split(",")[0].trim(); // take first IP in chain
        }

        return geoLocationService.getUserLocation(ip);
    }
}
