package com.devsuperior.dsmovie.controllers;

import com.devsuperior.dsmovie.tests.TokenUtil;
import io.restassured.http.ContentType;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

public class ScoreControllerRA {

	private String username, userPassword;
	private String token, invalidToken;

	private Map<String, Object> score;

	@BeforeEach
	void setUp() throws Exception {
		baseURI = "http://localhost:8080";

		// Authenticated users (client/admin)
		username = "maria@gmail.com";
		userPassword = "123456";
		token = TokenUtil.obtainAccessToken(username, userPassword);
		invalidToken = "invalid-token";

		score = new HashMap<>();
		score.put("movieId", 2);
		score.put("score", 1.2d);
	}
	
	@Test
	public void saveScoreShouldReturnNotFoundWhenMovieIdDoesNotExist() throws Exception {		
	}
	
	@Test
	public void saveScoreShouldReturnUnprocessableEntityWhenMissingMovieId() throws Exception {
	}
	
	@Test
	public void saveScoreShouldReturnUnprocessableEntityWhenScoreIsLessThanZero() throws Exception {		
	}
}
