package com.currencyxchange.controller;


import com.currencyxchange.dto.GeoLocationDTO;
import com.currencyxchange.service.GeoLocationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Mono;

@Controller
public class HomeController {

    private final GeoLocationService geoLocationService;

    public HomeController(GeoLocationService geoLocationService) {
        this.geoLocationService = geoLocationService;
    }

    @GetMapping("/")
    public String home(Model model) {
        Mono<GeoLocationDTO> geoLocationMono = geoLocationService.getUserLocation();

        geoLocationMono.subscribe(geoLocation -> {
            model.addAttribute("country", geoLocation.getCountry());
            model.addAttribute("currency", geoLocation.getCurrency());
        });

        return "index"; // Thymeleaf template: index.html
    }
}
