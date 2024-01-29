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
		score.put("movieId", 2L);
		score.put("score", 1.2d);
	}
	
	@Test
	@DisplayName("Save Score Should Return Not Found When Movie Id Does Not Exist")
	public void saveScoreShouldReturnNotFoundWhenMovieIdDoesNotExist() throws Exception {
		score.replace("movieId", 1000L);
		var postBody = new JSONObject(score);

		given()
			.header("Content-type", "application/json")
			.header("Authorization", "Bearer " + token)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.body(postBody)
		.when().put("/scores")
		.then()
			.statusCode(404)
		;
	}
	
	@Test
	@DisplayName("Save Score Should Return Unprocessable Entity When Missing Movie Id")
	public void saveScoreShouldReturnUnprocessableEntityWhenMissingMovieId() throws Exception {
		score.replace("movieId", null);
		var postBody = new JSONObject(score);

		given()
			.header("Content-type", "application/json")
			.header("Authorization", "Bearer " + token)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.body(postBody)
		.when().put("/scores")
		.then()
			.statusCode(422)
		;
	}
	
	@Test
	@DisplayName("Save Score Should Return Unprocessable Entity When Score Is Less Then Zero")
	public void saveScoreShouldReturnUnprocessableEntityWhenScoreIsLessThanZero() throws Exception {
		score.replace("score", -10.5d);
		var postBody = new JSONObject(score);

		given()
			.header("Content-type", "application/json")
			.header("Authorization", "Bearer " + token)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.body(postBody)
		.when().put("/scores")
		.then()
			.statusCode(422)
		;
	}

}
