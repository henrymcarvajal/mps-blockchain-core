package com.mps.blockchain.persistence.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.mps.blockchain.model.CurrencyConversion;

@Repository
public interface CurrenciesConverterRepository extends CrudRepository<CurrencyConversion, UUID> {
	
	Optional<CurrencyConversion> findByFromUnitAndToUnit(String from, String to);

}
