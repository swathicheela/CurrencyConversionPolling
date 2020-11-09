package com.test.example.services;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.springframework.stereotype.Component;

@Component
public class FileManager {

	/**
	 * Read a file by taking the filepath as param
	 * 
	 * @param filePath
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public String readFile(String filePath) throws FileNotFoundException, IOException {
		
		BufferedReader file = new BufferedReader(new FileReader(filePath));
        StringBuffer inputBuffer = new StringBuffer();
        String line2;
        while ((line2 = file.readLine()) != null) {
            inputBuffer.append(line2);
            inputBuffer.append('\n');
        }
        file.close();
        return inputBuffer.toString();
	}
}
