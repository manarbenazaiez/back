package tn.esprit.spring.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.spring.services.CryptoService;

import java.io.IOException;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/crypto")
public class CryptoController {

    private final CryptoService cryptoService;

    public CryptoController(CryptoService cryptoService) {
        this.cryptoService = cryptoService;
    }

    @GetMapping("/prices")
    public ResponseEntity<Map<String, Double>> getRealTimePrices(@RequestParam(required = false) String[] symbols) {
        try {
            if (symbols == null || symbols.length == 0) {
                // If symbols are not provided, return prices for all available symbols
                symbols = new String[]{"bitcoin", "ethereum", "litecoin"};
            }
            Map<String, Double> prices = cryptoService.getRealTimePrices(symbols);
            return ResponseEntity.ok(prices);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
