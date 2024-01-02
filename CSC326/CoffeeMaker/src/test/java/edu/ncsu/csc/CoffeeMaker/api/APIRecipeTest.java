package edu.ncsu.csc.CoffeeMaker.api;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Assertions;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.context.WebApplicationContext;

import edu.ncsu.csc.CoffeeMaker.common.TestUtils;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

/**
 * Tests the API functionality associated with recipes, including adding and
 * deleting recipes through the mock MVC.
 *
 * @author Sushil Sharma
 * @author Sydney Nguyen
 * @author Shailesh Paudel
 *
 */
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith ( SpringExtension.class )
public class APIRecipeTest {

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
     * The RecipeService object used for saving recipes to the database.
     */
    @Autowired
    private RecipeService         service;

    /**
     * Sets up the tests.
     */
    @BeforeEach
    public void setup () {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();

        service.deleteAll();
    }

    /**
     * Tests adding multiple recipes to the database, including by using the
     * private helper method.
     *
     * @throws Exception
     *             if there is an error during the test process
     */
    @Test
    @Transactional
    public void testAddRecipeMultiple () throws Exception {

        service.deleteAll();

        // Create a recipe using the setter methods and check contents
        final Recipe recipe = new Recipe();
        recipe.setName( "Delicious Not-Coffee" );
        recipe.addIngredient( new Ingredient( "Chocolate", 10 ) );
        recipe.addIngredient( new Ingredient( "Milk", 20 ) );
        recipe.addIngredient( new Ingredient( "Sugar", 5 ) );
        recipe.addIngredient( new Ingredient( "Coffee", 1 ) );

        recipe.setPrice( 5 );

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( recipe ) ) );

        Assertions.assertEquals( 1, (int) service.count() );

        // Create a recipe using the private helper method and check contents
        final Recipe r2 = new Recipe();
        r2.setName( "Lemonade" );
        r2.setPrice( 1 );
        r2.addIngredient( new Ingredient( "Chocolate", 4 ) );
        r2.addIngredient( new Ingredient( "Coffee", 6 ) );
        r2.addIngredient( new Ingredient( "Milk", 8 ) );
        r2.addIngredient( new Ingredient( "Sugar", 5 ) );

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r2 ) ) );

        final String recipeString = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();

        Assertions.assertEquals( 2, (int) service.count() );
        Assertions.assertEquals( 2, service.findAll().size() );
        assertTrue( recipeString.contains( "Lemonade" ) );
        assertTrue( recipeString.contains( "\"price\":1" ) );

        // Check the first recipe too
        assertTrue( recipeString.contains( "Delicious Not-Coffee" ) );

    }

    /**
     * Tests adding a recipe with a duplicate name, which shouldn't be allowed.
     *
     * @throws Exception
     *             if there is an error during the test process
     */
    @Test
    @Transactional
    public void testAddRecipeDuplicate () throws Exception {

        /* Tests a recipe with a duplicate name to make sure it's rejected */

        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
        final String name = "Coffee";
        final Recipe r1 = new Recipe();

        r1.setName( name );
        r1.setPrice( 0 );
        r1.addIngredient( new Ingredient( "Chocolate", 50 ) );
        r1.addIngredient( new Ingredient( "Coffee", 3 ) );
        r1.addIngredient( new Ingredient( "Milk", 1 ) );
        r1.addIngredient( new Ingredient( "Sugar", 1 ) );

        service.save( r1 );

        final Recipe r2 = new Recipe();

        r2.setName( name );
        r2.setPrice( 0 );
        r2.addIngredient( new Ingredient( "Chocolate", 50 ) );
        r2.addIngredient( new Ingredient( "Coffee", 3 ) );
        r2.addIngredient( new Ingredient( "Milk", 1 ) );
        r2.addIngredient( new Ingredient( "Sugar", 1 ) );

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r2 ) ) ).andExpect( status().is4xxClientError() );

        Assertions.assertEquals( 1, service.findAll().size(), "There should only one recipe in the CoffeeMaker" );
    }

    /**
     * Tests adding more than 3 recipes to the database, which is the maximum
     * amount allowed.
     *
     * @throws Exception
     *             if there is an error during the test process
     */
    @Test
    @Transactional
    public void testAddRecipeMax () throws Exception {

        /* Tests to make sure that our cap of 3 recipes is enforced */

        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = new Recipe();
        r1.setName( "Coffee" );
        r1.setPrice( 0 );
        r1.addIngredient( new Ingredient( "Chocolate", 50 ) );
        r1.addIngredient( new Ingredient( "Coffee", 3 ) );
        r1.addIngredient( new Ingredient( "Milk", 1 ) );
        r1.addIngredient( new Ingredient( "Sugar", 1 ) );
        service.save( r1 );

        final Recipe r2 = new Recipe();
        r2.setName( "Mocha" );
        r2.setPrice( 2 );
        r2.addIngredient( new Ingredient( "Chocolate", 50 ) );
        r2.addIngredient( new Ingredient( "Coffee", 3 ) );
        r2.addIngredient( new Ingredient( "Milk", 1 ) );
        r2.addIngredient( new Ingredient( "Sugar", 1 ) );
        service.save( r2 );

        final Recipe r3 = new Recipe();
        r3.setName( "Latte" );
        r3.setPrice( 0 );
        r3.addIngredient( new Ingredient( "Chocolate", 60 ) );
        r3.addIngredient( new Ingredient( "Coffee", 3 ) );
        r3.addIngredient( new Ingredient( "Milk", 2 ) );
        r3.addIngredient( new Ingredient( "Sugar", 2 ) );
        service.save( r3 );

        Assertions.assertEquals( 3, service.count(),
                "Creating three recipes should result in three recipes in the database" );

        final Recipe r4 = new Recipe();
        r4.setName( "Hot Chocolate" );
        r4.setPrice( 2 );
        r4.addIngredient( new Ingredient( "Chocolate", 75 ) );
        r4.addIngredient( new Ingredient( "Coffee", 0 ) );
        r4.addIngredient( new Ingredient( "Milk", 2 ) );
        r4.addIngredient( new Ingredient( "Sugar", 1 ) );

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r4 ) ) ).andExpect( status().isInsufficientStorage() );

        Assertions.assertEquals( 3, service.count(), "Creating a fourth recipe should not get saved" );
    }

    /**
     * Tests the API delete recipe functionality using the mock MVC (for a
     * single delete).
     *
     * @throws UnsupportedEncodingException
     *             if there is an error during the encoding process
     * @throws Exception
     *             if there is another general error in the test process
     *
     */
    @Test
    @Transactional
    @DeleteMapping ( "/api/v1/recipes" )
    public void testDeleteRecipeSingle () throws UnsupportedEncodingException, Exception {
        String recipe = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();

        // Create a new Masala Chai recipe
        final Recipe r = new Recipe();

        // Figure out if the recipe is present
        if ( !recipe.contains( "Masala Chai" ) ) {
            r.addIngredient( new Ingredient( "Chocolate", 4 ) );
            r.addIngredient( new Ingredient( "Coffee", 5 ) );
            r.addIngredient( new Ingredient( "Milk", 8 ) );
            r.setName( "Masala Chai" );
            r.setPrice( 7 );
            r.addIngredient( new Ingredient( "Sugar", 3 ) );

            // Post the created recipe
            mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                    .content( TestUtils.asJsonString( r ) ) ).andExpect( status().isOk() );

        }

        // Get/update the recipe string
        recipe = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();

        assertTrue( recipe.contains( "Masala Chai" ) );
        Assertions.assertEquals( 1, service.count(), "Only one recipe added" );

        // Delete the recipe
        mvc.perform( delete( "/api/v1/recipes/Masala Chai" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r ) ) ).andExpect( status().isOk() );

        // Update the recipe string
        recipe = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();

        // Now check the contents of the database to make sure the recipe has
        // been deleted
        assertFalse( recipe.contains( "Masala Chai" ) );
        assertFalse( recipe.contains( "\"price\":7" ) );
        assertFalse( recipe.contains( "\"milk\":8" ) );
        assertFalse( recipe.contains( "\"chocolate\":4" ) );
        Assertions.assertEquals( 0, service.count() );
        Assertions.assertEquals( 0, service.findAll().size() );

    }

    /**
     * Tests the API delete recipe functionality using the mock MVC Tests to
     * delete a recipe that is not in database
     *
     * @throws UnsupportedEncodingException
     *             if there is an error during the encoding process
     * @throws Exception
     *             if there is another general error in the test process
     *
     */
    @Test
    @Transactional
    @DeleteMapping ( "/api/v1/recipes" )
    public void testDeleteRecipeError () throws UnsupportedEncodingException, Exception {
        String recipe = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();

        // Create a new Masala Chai recipe
        final Recipe r = new Recipe();

        // Figure out if the recipe is present
        if ( !recipe.contains( "Masala Chai" ) ) {
            r.addIngredient( new Ingredient( "Chocolate", 4 ) );
            r.addIngredient( new Ingredient( "Coffee", 5 ) );
            r.addIngredient( new Ingredient( "Milk", 8 ) );
            r.setName( "Masala Chai" );
            r.setPrice( 7 );
            r.addIngredient( new Ingredient( "Sugar", 3 ) );

            // Post the created recipe
            mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                    .content( TestUtils.asJsonString( r ) ) ).andExpect( status().isOk() );

        }

        // Get/update the recipe string
        recipe = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();

        // Database now has one recipe, Masala Chai
        assertTrue( recipe.contains( "Masala Chai" ) );
        Assertions.assertEquals( 1, service.count(), "Only one recipe added" );

        // Attempt to delete recipe not in database ( status().isNotFound() )
        mvc.perform( delete( "/api/v1/recipes/LatteNotInRecipeDatabase" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r ) ) ).andExpect( status().isNotFound() );

        // Make sure Masala Chai was not deleted
        Assertions.assertEquals( 1, service.count(), "Masala Chai still in recipe database" );

    }

    /**
     * Test getting recipes
     *
     * @throws Exception
     *             if error occurs when testing
     */
    @Test
    @Transactional
    public void testGetRecipes () throws UnsupportedEncodingException, Exception {
        String recipe = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();

        // Create a new Masala Chai recipe
        final Recipe r = new Recipe();

        // Figure out if the recipe is present
        if ( !recipe.contains( "Masala Chai" ) ) {
            r.addIngredient( new Ingredient( "Chocolate", 4 ) );
            r.addIngredient( new Ingredient( "Coffee", 5 ) );
            r.addIngredient( new Ingredient( "Milk", 8 ) );
            r.setName( "Masala Chai" );
            r.setPrice( 7 );
            r.addIngredient( new Ingredient( "Sugar", 3 ) );

            // Post the created recipe
            mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                    .content( TestUtils.asJsonString( r ) ) ).andExpect( status().isOk() );

        }

        recipe = mvc.perform( get( "/api/v1/recipes/Masala Chai" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();
        Assertions.assertTrue( recipe.contains( "Masala Chai" ) );
    }

}
