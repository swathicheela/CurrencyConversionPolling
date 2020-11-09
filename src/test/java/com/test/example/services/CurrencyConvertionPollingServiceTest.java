package com.test.example.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.test.example.pojo.BuildData;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CurrencyConvertionPollingServiceTest {
	
	@Autowired
	CurrencyConvertionPollingService cccpn;
	
	BuildData buildData = new BuildData();
	
	@Test
	public void pollTest() throws FileNotFoundException, IOException, JSONException {
		buildData.setFileName("20201010-0000.txt");
		String currencyDataDtls=cccpn.fetchCurrencyData(buildData);
		assertNotNull(currencyDataDtls);
		cccpn.addCurrencyDetailsInMemory(currencyDataDtls);
		assertEquals(cccpn.currencydata.get("FR_USD"),"0.91");
		assertEquals(cccpn.currencydata.get("CAD_USD"),"0.99");
		assertEquals(cccpn.currencydata.get("FR_IND"),"0.11");
	}
}
