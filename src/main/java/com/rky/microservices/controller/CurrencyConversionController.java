package com.rky.microservices.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.rky.microservices.bean.CurrencyConversionBean;
import com.rky.microservices.proxy.CurrencyExchangeServiceProxy;

@RestController
public class CurrencyConversionController {
	
	
	@Autowired
	private CurrencyExchangeServiceProxy currencyExchangeServiceProxy;
	
	@Value("${rky.test}")
	private String test;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@GetMapping("/currency-converter/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConversionBean convertCurrency(@PathVariable String from, @PathVariable String to, @PathVariable BigDecimal quantity) {
		
		
		//create URI variable map
		Map<String, String> uriVariables = new HashMap<>();
		uriVariables.put("from", from);
		uriVariables.put("to", to);
		
		ResponseEntity<CurrencyConversionBean> responceEntity = new RestTemplate().getForEntity(
				"http://localhost:8000/currency-exchange/from/{from}/to/{to}", 
				CurrencyConversionBean.class, 
				uriVariables);
		
		CurrencyConversionBean responce = responceEntity.getBody();
		
		
		return new CurrencyConversionBean(responce.getId(), from, to, responce.getConversionMultiple(), quantity, quantity.multiply(responce.getConversionMultiple()), responce.getPort());
		
	}
	
	@GetMapping("/currency-converter-f/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConversionBean convertCurrencyFeign(@PathVariable String from, @PathVariable String to, @PathVariable BigDecimal quantity) {
		
		System.out.println("Inside convertCurrencyFeign-----------------------------> "+test);
		
		CurrencyConversionBean responce = currencyExchangeServiceProxy.retriveExchangeValue(from, to);
		
		logger.info("Response > {}",responce);
		return new CurrencyConversionBean(responce.getId(), from, to, responce.getConversionMultiple(), quantity, quantity.multiply(responce.getConversionMultiple()), responce.getPort());
		
	}

}
