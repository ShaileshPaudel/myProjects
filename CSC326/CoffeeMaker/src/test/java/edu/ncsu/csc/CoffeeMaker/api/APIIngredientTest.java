package edu.ncsu.csc.CoffeeMaker.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.context.WebApplicationContext;

import edu.ncsu.csc.CoffeeMaker.common.TestUtils;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;

/**
 * Tests the API functionality associated with ingredients, including adding and
 * deleting ingredients through the mock MVC.
 *
 * @author Sushil Sharma
 * @author Shailesh Paudel
 * @author Sydney Nguyen
 *
 */
@RunWith ( SpringRunner.class )
@SpringBootTest
@AutoConfigureMockMvc
public class APIIngredientTest {

    /**
     * MockMvc uses Spring's testing framework to handle requests to the REST
     * API
     */
    private MockMvc               mvc;

    /**
     * Used during the setup process for the mvc
     */
    @Autowired
    private WebApplicationContext context;

    /**
     * Sets up the tests.
     */
    @Before
    public void setup () {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();
    }

    /**
     * Tests adding an Ingredient to the database.
     *
     * @throws Exception
     *             if there is an error during the test process
     */
    @Test
    @Transactional
    public void testAddIngredient () throws Exception {

        // Create a new Ingredient
        final Ingredient i1 = new Ingredient();
        i1.setAmount( 4 );
        i1.setName( "Honey" );

        // Post the created ingredient
        mvc.perform( post( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( i1 ) ) ).andExpect( status().isOk() );

        final String ingredient = mvc.perform( get( "/api/v1/ingredients" ) ).andDo( print() )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();

        Assertions.assertTrue( ingredient.contains( "Honey" ) );
        Assertions.assertTrue( ingredient.contains( "4" ) );

        // Test adding duplicate ingredient
        final Ingredient i2 = new Ingredient();
        i2.setAmount( 14 );
        i2.setName( "Honey" );

        mvc.perform( post( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( i2 ) ) ).andExpect( status().is4xxClientError() );

    }

    /**
     * Tests deleting Ingredient
     *
     * @throws Exception
     *             if there is an error
     */
    @Test
    @Transactional
    @DeleteMapping ( "/api/v1/ingredients" )
    public void testDeleteIngredient () throws UnsupportedEncodingException, Exception {
        String ingredient = mvc.perform( get( "/api/v1/ingredients" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();

        // Create a new Ingredient
        final Ingredient i1 = new Ingredient();
        if ( !ingredient.contains( "Honey" ) ) {
            i1.setAmount( 4 );
            i1.setName( "Honey" );
        }

        // Post the created ingredient
        mvc.perform( post( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( i1 ) ) ).andExpect( status().isOk() );

        // Get updated string for all ingredients after adding ingredient
        ingredient = mvc.perform( get( "/api/v1/ingredients" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();
        Assertions.assertTrue( ingredient.contains( "Honey" ) );

        // Get specific ingredient - Honey
        ingredient = mvc.perform( get( "/api/v1/ingredients/Honey" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();
        Assertions.assertTrue( ingredient.contains( "Honey" ) );

        // Delete ingredient
        mvc.perform( delete( "/api/v1/ingredients/Honey" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( i1 ) ) ).andExpect( status().isOk() );

        // Update the recipe string
        ingredient = mvc.perform( get( "/api/v1/ingredients" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();

        Assertions.assertFalse( ingredient.contains( "Honey" ) );

        // Test deleting ingredient not in database
        final Ingredient i3 = new Ingredient();
        i1.setAmount( 43 );
        i1.setName( "Peach" );
        mvc.perform( delete( "/api/v1/ingredients/Peach" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( i3 ) ) ).andExpect( status().is4xxClientError() );

    }

}
