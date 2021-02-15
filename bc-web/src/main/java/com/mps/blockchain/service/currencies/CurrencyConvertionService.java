package com.mps.blockchain.service.currencies;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mps.blockchain.model.CurrenciesConversion;
import com.mps.blockchain.persistence.repository.CurrenciesConverterRepository;

@Service
public class CurrencyConvertionService {
    
    private static final String PIVOT_FIAT = Currency.FIAT.US_DOLLAR;
    
    @Autowired
    private CurrenciesConverterRepository currenciesConverterRepository;
    
    public BigDecimal convert(String sourceCurrency, String targetCurrency, BigDecimal amount) throws MissingCurrencyConversionException {
        if (sourceCurrency.equals(targetCurrency)) {
            return amount;
        }
        
        BigDecimal fromCurrencyToPivot = convertSourceToPivot(sourceCurrency, amount);
        return convertPivotToTarget(targetCurrency, fromCurrencyToPivot);
    }
    
    private BigDecimal convertSourceToPivot(String sourceCurrency, BigDecimal amount) throws MissingCurrencyConversionException {
        return convertCurrency(sourceCurrency, PIVOT_FIAT, amount);
    }
    
    private BigDecimal convertPivotToTarget(String targetCurrency, BigDecimal amount) throws MissingCurrencyConversionException {
        return convertCurrency(PIVOT_FIAT, targetCurrency, amount);
    }
    
    private Optional<CurrenciesConversion> getConverter(String from, String to) throws MissingCurrencyConversionException {
        Optional<CurrenciesConversion> converterOptionalFromPivot = currenciesConverterRepository
                .findByFromUnitAndToUnit(to, from);
        if (converterOptionalFromPivot.isPresent()) {
            return converterOptionalFromPivot;
        }
        
        converterOptionalFromPivot = currenciesConverterRepository.findByFromUnitAndToUnit(from, to);
        if (converterOptionalFromPivot.isPresent()) {
            return converterOptionalFromPivot;
        }
        
        throw new MissingCurrencyConversionException(String.format("No conversion available for %s and %s", to, from));
    }
    
    private BigDecimal convertCurrency(String sourceCurrency, String targetCurrency, BigDecimal amount) throws MissingCurrencyConversionException {
        
        BigDecimal convertedCurrency = amount;
        
        if (!sourceCurrency.equalsIgnoreCase(targetCurrency)) {
            Optional<CurrenciesConversion> conversionOptional = getConverter(sourceCurrency, targetCurrency);            
            CurrenciesConversion currenciesConversion = conversionOptional.get();
            
            BigDecimal factor;
            BigDecimal divisor;
            
            if (sourceCurrency.equalsIgnoreCase(currenciesConversion.getFromUnit())) {
                factor = BigDecimal.valueOf(currenciesConversion.getToValue());
                divisor = BigDecimal.valueOf(currenciesConversion.getFromValue());
            } else {
                factor = BigDecimal.valueOf(currenciesConversion.getFromValue());
                divisor = BigDecimal.valueOf(currenciesConversion.getToValue());
            }
            
            convertedCurrency = factor.divide(divisor, MathContext.DECIMAL32).multiply(amount);
        }
        
        return convertedCurrency;
    }
}
