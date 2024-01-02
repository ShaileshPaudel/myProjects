package edu.ncsu.csc.CoffeeMaker;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

/**
 * This is a test class for the test database interaction.
 *
 * @author shaileshpaudel
 * @author Sushil Sharma
 *
 */
@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class TestDatabaseInteraction {

    /**
     * Keeps track of recipes
     */
    @Autowired
    private RecipeService recipeService;

    /**
     * Sets up the tests.
     */
    @BeforeEach
    public void setup () {
        recipeService.deleteAll();
    }

    /**
     * Tests the RecipeService class
     */
    @Test
    @Transactional
    public void testRecipes () {
        final Recipe r = new Recipe();

        r.setName( "Mocha" );

        r.addIngredient( new Ingredient( "Chocolate", 2 ) );
        r.addIngredient( new Ingredient( "Coffee", 5 ) );
        r.addIngredient( new Ingredient( "Milk", 1 ) );
        r.addIngredient( new Ingredient( "Sugar", 6 ) );

        r.setPrice( 10 );

        recipeService.save( r );

        final List<Recipe> dbRecipes = recipeService.findAll();

        assertEquals( 1, dbRecipes.size() );

        final Recipe dbRecipe = dbRecipes.get( 0 );

        assertEquals( r.getName(), dbRecipe.getName() );
        assertEquals( r.getNames().get( 0 ), dbRecipe.getNames().get( 0 ) );
        assertEquals( r.getNames().get( 1 ), dbRecipe.getNames().get( 1 ) );
        assertEquals( r.getNames().get( 2 ), dbRecipe.getNames().get( 2 ) );
        assertEquals( r.getNames().get( 3 ), dbRecipe.getNames().get( 3 ) );

        assertEquals( r.getPrice(), dbRecipe.getPrice() );

        final Recipe dbRecipeByName = recipeService.findByName( "Mocha" );

        assertEquals( r.getName(), dbRecipeByName.getName() );
        assertEquals( r.getNames().get( 0 ), dbRecipeByName.getNames().get( 0 ) );
        assertEquals( r.getNames().get( 1 ), dbRecipeByName.getNames().get( 1 ) );
        assertEquals( r.getNames().get( 2 ), dbRecipeByName.getNames().get( 2 ) );
        assertEquals( r.getNames().get( 3 ), dbRecipeByName.getNames().get( 3 ) );

        dbRecipe.setPrice( 20 );

        recipeService.save( dbRecipe );

        assertEquals( r.getPrice(), dbRecipe.getPrice() );

        assertEquals( 1, recipeService.count() );

        assertEquals( 20, (int) recipeService.findAll().get( 0 ).getPrice() );
    }
}
