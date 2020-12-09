package com.mps.blockchain.service.currencies;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mps.blockchain.model.CurrencyConversion;
import com.mps.blockchain.persistence.repository.CurrenciesConverterRepository;

@Service
public class CurrencyConvertionService {

	private static final String PIVOT_FIAT = "USD";

	@Autowired
	private CurrenciesConverterRepository currenciesConverterRepository;

	public enum FiatCurrency {
		COLOMBIAN_PESO, UNITED_STATES_DOLLAR;
	}

	public enum CryptoCurrency {
		XDAI;
	}

	public BigDecimal convert(String from, String to, BigDecimal amount) {
		if (from.equalsIgnoreCase(to)) {
			return amount;
		}

		BigDecimal fromCurrencyToPivot;
		if (from.equalsIgnoreCase(PIVOT_FIAT)) {
			fromCurrencyToPivot = amount.multiply(BigDecimal.ONE);
		} else {
			Optional<CurrencyConversion> converterOptionalToPivot1 = currenciesConverterRepository
					.findByFromUnitAndToUnit(from, PIVOT_FIAT);

			if (converterOptionalToPivot1.isPresent()) {
				fromCurrencyToPivot = amount.multiply(BigDecimal.valueOf(converterOptionalToPivot1.get().getToValue()));
			} else {
				Optional<CurrencyConversion> converterOptionalToPivot2 = currenciesConverterRepository
						.findByFromUnitAndToUnit(PIVOT_FIAT, from);

				if (converterOptionalToPivot2.isPresent()) {
					fromCurrencyToPivot = amount
							.divide(BigDecimal.valueOf(converterOptionalToPivot2.get().getToValue()));
				} else {
					return BigDecimal.ZERO;
				}
			}
		}

		BigDecimal fromPivotToCurrency;
		if (to.equalsIgnoreCase(PIVOT_FIAT)) {
			fromPivotToCurrency = fromCurrencyToPivot.multiply(BigDecimal.ONE);
		} else {
			Optional<CurrencyConversion> converterOptionalFromPivot1 = currenciesConverterRepository
					.findByFromUnitAndToUnit(PIVOT_FIAT, to);

			if (converterOptionalFromPivot1.isPresent()) {
				fromPivotToCurrency = fromCurrencyToPivot
						.multiply(BigDecimal.valueOf(converterOptionalFromPivot1.get().getToValue()));
			} else {
				Optional<CurrencyConversion> converterOptionalFromPivot2 = currenciesConverterRepository
						.findByFromUnitAndToUnit(to, PIVOT_FIAT);

				if (converterOptionalFromPivot2.isPresent()) {
					fromPivotToCurrency = fromCurrencyToPivot
							.divide(BigDecimal.valueOf(converterOptionalFromPivot2.get().getToValue()));
				} else {
					return BigDecimal.ZERO;
				}
			}

		}

		return fromPivotToCurrency;
	}
}
