package com.currencyxchange.controller;


import com.currencyxchange.dto.GeoLocationDTO;
import com.currencyxchange.service.GeoLocationService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
public class GeoLocationController {
    private final GeoLocationService geoLocationService;

    public GeoLocationController(GeoLocationService geoLocationService) {
        this.geoLocationService = geoLocationService;
    }

    @GetMapping("/location")
    public Mono<GeoLocationDTO> getUserLocation(HttpServletRequest request) {
        return geoLocationService.getUserLocation();
    }
}
