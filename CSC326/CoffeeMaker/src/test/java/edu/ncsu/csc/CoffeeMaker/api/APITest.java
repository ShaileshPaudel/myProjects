package edu.ncsu.csc.CoffeeMaker.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import edu.ncsu.csc.CoffeeMaker.common.TestUtils;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;

/**
 * A test class for testing the API functionality/REST API endpoints, including
 * various HTTP requests such as GET, POST, PUT, etc.
 *
 * @author Sushil Sharma
 * @author Sydney Nguyen
 * @author Shailesh Paudel
 *
 */
@ExtendWith ( SpringExtension.class )
@SpringBootTest
@AutoConfigureMockMvc
public class APITest {

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

    /** coffee ingredient */
    final Ingredient              coffee    = new Ingredient( "Coffee", 5 );

    /** milk ingredient */
    final Ingredient              milk      = new Ingredient( "Milk", 8 );

    /** sugar ingredient */
    final Ingredient              sugar     = new Ingredient( "Sugar", 3 );

    /** chocolate ingredient */
    final Ingredient              chocolate = new Ingredient( "Chocolate", 4 );

    /**
     * Sets up the tests.
     */
    @BeforeEach
    public void setup () {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();

    }

    /**
     * The test method for checking the REST API endpoints. Creates a recipe and
     * ensures it is actually present in the database.
     *
     * @throws UnsupportedEncodingException
     *             if there is an error during the encoding process
     * @throws Exception
     *             if there is another general error in the recipe creation
     *             process
     */
    @Test
    @Transactional
    public void apiTestRecipe () throws UnsupportedEncodingException, Exception {

        String recipe = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();

        // Figure out if the recipe is present
        if ( !recipe.contains( "Mocha" ) ) {
            // Create a new Mocha recipe
            final Recipe r = new Recipe();
            r.addIngredient( chocolate );
            r.addIngredient( coffee );
            r.addIngredient( milk );
            r.setName( "Mocha" );
            r.addIngredient( sugar );
            r.setPrice( 7 );

            mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                    .content( TestUtils.asJsonString( r ) ) ).andExpect( status().isOk() );

        }

        recipe = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();

        // Test to make sure recipe is present
        assertTrue( recipe.contains( "Mocha" ) );
        System.out.println( "\n\nMocha String = " + recipe + "\n\n" );
        assertTrue( recipe.contains( "\"price\":7" ) );

        // Test adding inventory
        final Inventory i = new Inventory( 50, 50, 50, 50 );

        mvc.perform( put( "/api/v1/inventory" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( i ) ) ).andExpect( status().isOk() );

        // Check for correct amount of ingredients in inventory
        assertEquals( (Integer) 50, i.getAmount( "Chocolate" ) );
        assertEquals( i.getAmount( "Sugar" ), (Integer) 50 );
        assertEquals( i.getAmount( "Milk" ), (Integer) 50 );
        assertEquals( i.getAmount( "Coffee" ), (Integer) 50 );

        // Make Coffee
        mvc.perform( post( String.format( "/api/v1/makecoffee/%s", "Mocha" ) ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( 100 ) ) ).andExpect( status().isOk() ).andDo( print() );

    }

}
