
package edu.ncsu.csc.CoffeeMaker.datageneration;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import edu.ncsu.csc.CoffeeMaker.TestConfig;
import edu.ncsu.csc.CoffeeMaker.models.DomainObject;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

/**
 * Test generating recipe with ingredients database implementation
 *
 * @author Sushil Sharma
 * @author Sydney Nguyen
 * @author shaileshpaudel
 *
 */
@RunWith ( SpringRunner.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class GenerateRecipeWithIngredients {

    /** instance of the recipe service */
    @Autowired
    private RecipeService recipeService;

    /**
     * Sets up tests by deleting any existing recipes
     */
    @Before
    public void setup () {
        recipeService.deleteAll();
    }

    /**
     * Creates a recipe with ingredients
     */
    @Test
    @Transactional
    public void createRecipe () {
        final Recipe r1 = new Recipe();
        r1.setName( "Delicious Coffee" );
        r1.setPrice( 50 );
        Assertions.assertEquals( "Delicious Coffee", r1.getName() );
        Assertions.assertEquals( 50, r1.getPrice() );

        r1.addIngredient( new Ingredient( "Coffee", 10 ) );
        r1.addIngredient( new Ingredient( "Pumpkin Spice", 3 ) );
        r1.addIngredient( new Ingredient( "Milk", 2 ) );
        Assertions.assertEquals( 3, r1.getNames().size() );

        recipeService.save( r1 );

        printRecipes();
    }

    /**
     * Print the recipes in the service
     */
    private void printRecipes () {
        for ( final DomainObject r : recipeService.findAll() ) {
            System.out.println( r );
        }
    }

}
