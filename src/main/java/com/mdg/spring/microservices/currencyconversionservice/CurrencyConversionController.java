package com.mdg.spring.microservices.currencyconversionservice;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.mdg.spring.microservices.currencyconversionservice.beans.CurrencyConversionBean;

@RestController
public class CurrencyConversionController {

	@Autowired
	CurrencyExchangeServiceProxy proxy;

	@GetMapping("/conversion-service/from/{from}/to/{to}/quantity/{quantity}")
	CurrencyConversionBean convertAmount(@PathVariable String from, @PathVariable String to,
			@PathVariable BigDecimal quantity) {

		Map<String, String> urlParams = new HashMap<String, String>();
		urlParams.put("from", from);
		urlParams.put("to", to);
		ResponseEntity<CurrencyConversionBean> responseEntity = new RestTemplate().getForEntity(
				"http://localhost:8000/currency-exchange/from/{from}/to/{to}", CurrencyConversionBean.class, urlParams);
		CurrencyConversionBean response = responseEntity.getBody();
		response.setQuantity(quantity);
		response.calculateAmount();
		return response;
	}

	@GetMapping("/conversion-service-feign/from/{from}/to/{to}/quantity/{quantity}")
	CurrencyConversionBean convertAmountFeign(@PathVariable String from, @PathVariable String to,
			@PathVariable BigDecimal quantity) {

		CurrencyConversionBean response = proxy.getExchangeValue(from, to);
		response.setQuantity(quantity);
		response.calculateAmount();
		return response;
	}
}
