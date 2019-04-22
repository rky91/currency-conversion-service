package com.rky.microservices.proxy;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.rky.microservices.bean.CurrencyConversionBean;


//@FeignClient(name="currency-exchange-service", url="localhost:8000") /* mention URL when you don't use load balance and using only single instance*/

//@FeignClient(name="currency-exchange-service")

@FeignClient(name="netflix-zuul-api-gateway-server")
@RibbonClient(name="currency-exchange-service")
public interface CurrencyExchangeServiceProxy {
	
	//@GetMapping("/currency-exchange/from/{from}/to/{to}")
	@GetMapping("currency-exchange-service/currency-exchange/from/{from}/to/{to}") //The application name has been added before the URI as the request will come via Zuul API gateway
	public CurrencyConversionBean retriveExchangeValue(@PathVariable String from, @PathVariable String to);

}
