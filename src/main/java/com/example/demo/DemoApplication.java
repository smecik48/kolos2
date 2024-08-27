package com.example.demo;

import com.fasterxml.jackson.databind.util.JSONPObject;
import org.apache.commons.io.FileUtils;
import org.json.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Scanner;
import java.lang.Object;
import java.util.UUID;


@SpringBootApplication
@RestController
public class DemoApplication {
	ArrayList<JSONObject> tokenList = new ArrayList<>();
	ArrayList<JSONObject> activeTokens = new ArrayList<>();

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@PostMapping("/register")
	private String postToken(){
		String token = UUID.randomUUID().toString();
		DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String time = LocalDateTime.now().format(df);
		String jsonFile = String.format("{\"token\":\"%s\", \"time\":\"%s\"}" ,token ,time);
		tokenList.add(new JSONObject(jsonFile));
		return jsonFile;
	}

	@GetMapping("/tokens")
	private ArrayList<JSONObject> getTokens(){
		ArrayList<JSONObject> tokens = new ArrayList<>();
		String tempJson;
		String tempToken;
		LocalDateTime tempTime;
		String tempValidation;
		DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		for(JSONObject i: tokenList){
			tempToken = i.getString("token");
			tempTime = LocalDateTime.parse(i.getString("time"), df);
			if(LocalDateTime.now().getMinute()-tempTime.getMinute()>=5){
				tempValidation = "nieaktywny";
			}
			else
				tempValidation = "aktywny";
			tempJson = String.format("{\"token\":\"%s\", \"time\":\"%s\", \"validation\":\"%s\"}",
					tempToken ,i.getString("time"), tempValidation);
			tokens.add(new JSONObject(tempJson));
			activeTokens.add(new JSONObject(tempJson));
			System.out.println(tempJson);
		}
		return tokens;
	}

	@GetMapping("/image")
	private String getImage(){
		try {
			byte[] fileContent = FileUtils.readFileToByteArray(new File("image.png"));
			String encodedString = Base64.getEncoder().encodeToString(fileContent);
			String imgCode = String.format("data:image/png;base64, %s", encodedString);
			System.out.println(imgCode);
			String html = String.format("<html><img src='%s' th:width='512' th:height='512'></html> ", imgCode);
			return html;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
