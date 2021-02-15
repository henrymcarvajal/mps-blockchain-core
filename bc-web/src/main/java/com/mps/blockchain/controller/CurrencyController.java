package com.mps.blockchain.controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mps.blockchain.service.currencies.CurrencyConvertionService;
import com.mps.blockchain.service.currencies.MissingCurrencyConversionException;

@RestController
@RequestMapping("/currency")
public class CurrencyController {
    
    @Autowired
    private CurrencyConvertionService currencyConvertionService;
    
    @GetMapping("/{from}/{to}/{amount}")
    public String convert(@PathVariable String from, @PathVariable String to, @PathVariable BigDecimal amount)
            throws MissingCurrencyConversionException {
        return currencyConvertionService.convert(from, to, amount).stripTrailingZeros().toPlainString();
    }
    
    @GetMapping("/ping")
    public String getPing() {
        return "CurrencyController there!";
    }
}
