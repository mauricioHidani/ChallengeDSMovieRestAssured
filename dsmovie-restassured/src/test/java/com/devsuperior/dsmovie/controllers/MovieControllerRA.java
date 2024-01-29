package com.devsuperior.dsmovie.controllers;

import io.restassured.http.ContentType;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class MovieControllerRA {
	
	private String baseImgUri;
	@BeforeEach
	void setUp() throws Exception {
		baseURI = "http://localhost:8080";
		baseImgUri = "https://www.themoviedb.org/t/p/w533_and_h300_bestv2";
	}

	@Test
	public void findAllShouldReturnOkWhenMovieNoArgumentsGiven() {
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
	public void findAllShouldReturnPagedMoviesWhenMovieTitleParamIsNotEmpty() {		
	}
	
	@Test
	public void findByIdShouldReturnMovieWhenIdExists() {		
	}
	
	@Test
	public void findByIdShouldReturnNotFoundWhenIdDoesNotExist() {	
	}
	
	@Test
	public void insertShouldReturnUnprocessableEntityWhenAdminLoggedAndBlankTitle() throws JSONException {		
	}
	
	@Test
	public void insertShouldReturnForbiddenWhenClientLogged() throws Exception {
	}
	
	@Test
	public void insertShouldReturnUnauthorizedWhenInvalidToken() throws Exception {
	}
}
