package com.devsuperior.dsmovie.controllers;

import com.devsuperior.dsmovie.tests.TokenUtil;
import io.restassured.http.ContentType;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class MovieControllerRA {

	private String clientUsername, clientPassword, adminUsername, adminPassword;
	private String clientToken, adminToken, invalidToken;

	private Long existingId, nonExistingId;
	private String baseImgUri;

	private Map<String, Object> move;

	@BeforeEach
	void setUp() throws Exception {
		baseURI = "http://localhost:8080";

		// Authenticated users (client/admin)
		clientUsername = "joaquim@gmail.com";
		clientPassword = "123456";
		adminUsername = "maria@gmail.com";
		adminPassword = "123456";
		clientToken = TokenUtil.obtainAccessToken(clientUsername, clientPassword);
		adminToken = TokenUtil.obtainAccessToken(adminUsername, adminPassword);
		invalidToken = "invalid-token";

		existingId = 2L;
		nonExistingId = 5000L;
		baseImgUri = "https://www.themoviedb.org/t/p/w533_and_h300_bestv2";

		move = new HashMap<>();
		move.put("title", "New Movie");
		move.put("score", 0.0d);
		move.put("count", 0);
		move.put("image", baseImgUri + "/vIgyYkXkg6NC2whRbYjBD7eb3Er.jpg");
	}

	@Test
	@DisplayName("Find All Should Return Ok When Movie No Arguments Given")
	public void findAllShouldReturnOkWhenMovieNoArgumentsGiven() throws Exception {
		given()
			.header("Content-type", "application/json")
			.contentType(ContentType.JSON)
		.when().get("/movies")
		.then()
			.statusCode(200)
			.body("content.id", hasItems(1, 2, 3))
			.body("content.title",
				hasItems(
					"The Witcher",
					"Venom: Tempo de Carnificina",
					"O Espetacular Homem-Aranha 2: A Ameaça de Electro"
				)
			)
			.body("content.score", hasItems(4.5f, 3.3f, 0.0f))
			.body("content.count", hasItems(2, 3, 0))
			.body("content.image",
				hasItems(
					baseImgUri + "/jBJWaqoSCiARWtfV0GlqHrcdidd.jpg",
					baseImgUri + "/vIgyYkXkg6NC2whRbYjBD7eb3Er.jpg",
					baseImgUri + "/u7SeO6Y42P7VCTWLhpnL96cyOqd.jpg"
				)
			)
			.body("pageable.paged", equalTo(true))
			.body("numberOfElements", is(20))
			.body("empty", equalTo(false))
		;
	}
	
	@Test
	@DisplayName("Find All Should Return Page Movies When Movie Title Param Is Not Empty")
	public void findAllShouldReturnPagedMoviesWhenMovieTitleParamIsNotEmpty() throws Exception {
		var movieTitle = "Venom";

		given()
			.header("Content-type", "application/json")
			.contentType(ContentType.JSON)
		.when().get("/movies?title={movieTitle}", movieTitle)
		.then()
			.statusCode(200)
			.body("content.id", hasItems(2))
			.body("content.title", hasItems("Venom: Tempo de Carnificina"))
			.body("content.score", hasItems(3.3f))
			.body("content.count", hasItems(3))
			.body("content.image", hasItems(baseImgUri + "/vIgyYkXkg6NC2whRbYjBD7eb3Er.jpg"))
			.body("pageable.paged", equalTo(true))
			.body("numberOfElements", is(1))
			.body("empty", equalTo(false))
		;
	}
	
	@Test
	@DisplayName("Find By Id Should Return Movie When Id Exists")
	public void findByIdShouldReturnMovieWhenIdExists() throws Exception {
		given()
			.header("Content-type", "application/json")
			.contentType(ContentType.JSON)
		.when().get("/movies/{id}", existingId)
		.then()
			.statusCode(200)
			.body("id", is(2))
			.body("title", equalTo("Venom: Tempo de Carnificina"))
			.body("score", is(3.3f))
			.body("count", is(3))
			.body("image", equalTo(baseImgUri + "/vIgyYkXkg6NC2whRbYjBD7eb3Er.jpg"))
		;
	}
	
	@Test
	@DisplayName("Find By Is Should Return Not Found When Id Does Not Exist")
	public void findByIdShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
		given()
			.header("Content-type", "application/json")
			.contentType(ContentType.JSON)
		.when().get("/movies/{id}", nonExistingId)
		.then()
			.statusCode(404)
		;
	}
	
	@Test
	@DisplayName("Insert Should Return Unprocessable Entity When Admin Logged And Blank Title")
	public void insertShouldReturnUnprocessableEntityWhenAdminLoggedAndBlankTitle() throws JSONException {
		move.replace("title", "");
		var postBody = new JSONObject(move);

		given()
			.header("Content-type", "application/json")
			.header("Authorization", "Bearer " + adminToken)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.body(postBody)
		.when().post("/movies")
		.then()
			.statusCode(422)
		;
	}
	
	@Test
	@DisplayName("Insert Should Return Forbidden When Client Logged")
	public void insertShouldReturnForbiddenWhenClientLogged() throws Exception {
		var postBody = new JSONObject(move);

		given()
			.header("Content-type", "application/json")
			.header("Authorization", "Bearer " + clientToken)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.body(postBody)
		.when().post("/movies")
		.then()
			.statusCode(403)
		;
	}
	
	@Test
	@DisplayName("Insert Should Return Unauthorized When Invalid Token")
	public void insertShouldReturnUnauthorizedWhenInvalidToken() throws Exception {
		var postBody = new JSONObject(move);

		given()
			.header("Content-type", "application/json")
			.header("Authorization", "Bearer " + invalidToken)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
			.body(postBody)
		.when().post("/movies")
		.then()
			.statusCode(401)
		;
	}

}
