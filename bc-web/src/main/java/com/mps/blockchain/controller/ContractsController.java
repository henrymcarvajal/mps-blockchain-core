package com.mps.blockchain.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mps.blockchain.contracts.exceptions.MissingInputException;
import com.mps.blockchain.controller.request.TransactionResponse;
import com.mps.blockchain.service.contracts.ContractsHandler;

@RestController
@RequestMapping("/contracts")
public class ContractsController {

	@Autowired
	private ContractsHandler contractsHandler;

	@PostMapping
	public TransactionResponse transact(@RequestBody Map<String, String> parameters) throws MissingInputException {
		return contractsHandler.runTransaction(parameters);
	}

	@GetMapping("/ping")
	public String getPing() {
		return "Ping there!";
	}
}
