package com.test.example.services;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.example.pojo.BuildData;

@Component
public class CurrencyConvertionPollingService {
	
	final static Logger logger = Logger.getLogger(CurrencyConvertionPollingService.class);
	
	@Autowired
	FileManager fileManager;
	
	@Value(value= "${input.files.path}")
	String filepath;
	
	@Value(value="${input.build.files.name}")
	String buildFileName;
	
	int previousBuildId;
	
	ObjectMapper mapper = new ObjectMapper();
	
	HashMap<String,String> currencydata = new HashMap<>();

	/**
	 * Adding currency details into hashmap for storing in memory and they get replaced with updated values
	 * 
	 * @param data
	 * @throws JSONException
	 */
	protected void addCurrencyDetailsInMemory(String data) throws JSONException {
		JSONObject jsonObj = new JSONObject(data);
		Iterator<String> keys = jsonObj.keys();
		while(keys.hasNext()) {
			String key=keys.next();
			currencydata.put(key, jsonObj.getString(key));
		}
		logger.debug("CurrencyData"+currencydata);
	}

	/**
	 * Poll the temp directory and parse the build and currency conversion file
	 */
	public void poll() {
		logger.debug("Fetch build data.");
		try {
			BuildData buildData = fetchBuildData();

			if (previousBuildId != buildData.getBuildId()) {
				logger.info("New build detected. Processing currency conversion.");
				String currencyConversionData = fetchCurrencyData(buildData);
				addCurrencyDetailsInMemory(currencyConversionData);
				previousBuildId = buildData.getBuildId();
			} else
				logger.info("Build is unchanged. Skipped processing currency conversion.");
		} catch (FileNotFoundException e) {
			logger.error("Error while polling. Unable to find the file ", e);
		} catch (IOException e) {
			logger.error("Error while polling. Unable to find the file", e);
		} catch (JSONException e) {
			logger.error("Error while polling. Unable to parse the string", e);
		}
	}

	/**
	 * Fetch currency conversion rates from the file
	 * 
	 * @param buildData
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	protected String fetchCurrencyData(BuildData buildData) throws FileNotFoundException, IOException {
		return fileManager.readFile(filepath + buildData.getFileName());
	}
	
	/**
	 * Get the build data from build file and return the build data object
	 * 
	 * @return
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	protected BuildData fetchBuildData() throws FileNotFoundException, IOException {
		String buildDataString = fileManager.readFile(filepath + buildFileName);
		
		return mapper.readValue(buildDataString, BuildData.class);
	}
}
