package edu.ncsu.csc.CoffeeMaker.unit;

import java.util.List;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc.CoffeeMaker.TestConfig;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

/**
 * Tests functionality of Recipe, such as add, delete, edit, create
 *
 * @author stnguye3
 * @author Shailesh Paudel
 * @author ssharm37
 *
 */
@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class RecipeTest {

    /**
     * Service object for testing
     */
    @Autowired
    private RecipeService service;

    /**
     * Sets up the service by deleting all recipes
     */
    @BeforeEach
    public void setup () {
        service.deleteAll();
    }

    /**
     * Tests adding 2 recipes
     */
    @Test
    @Transactional
    public void testAddRecipe () {

        final Recipe r1 = new Recipe();
        r1.setName( "Black Coffee" );
        r1.setPrice( 1 );
        r1.addIngredient( new Ingredient( "Coffee", 1 ) );
        r1.addIngredient( new Ingredient( "Milk", 0 ) );
        r1.addIngredient( new Ingredient( "Sugar", 0 ) );
        r1.addIngredient( new Ingredient( "Chocolate", 0 ) );

        service.save( r1 );

        // All the ingredients should not be 0
        Assertions.assertTrue( r1.checkRecipe() );

        final Recipe r2 = new Recipe();
        r2.setName( "Mocha" );
        r2.setPrice( 1 );
        r2.addIngredient( new Ingredient( "Coffee", 1 ) );
        r2.addIngredient( new Ingredient( "Milk", 1 ) );
        r2.addIngredient( new Ingredient( "Sugar", 1 ) );
        r2.addIngredient( new Ingredient( "Chocolate", 1 ) );

        service.save( r2 );

        final List<Recipe> recipes = service.findAll();
        Assertions.assertEquals( 2, recipes.size(),
                "Creating two recipes should result in two recipes in the database" );

        Assertions.assertEquals( r1, recipes.get( 0 ), "The retrieved recipe should match the created one" );
    }

    /**
     * Test adding 2 recipes where one field for one recipe is invalid Should
     * not save either recipe
     */
    @Test
    @Transactional
    public void testNoRecipes () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = new Recipe();
        r1.setName( "Tasty Drink" );
        r1.setPrice( 12 );
        r1.addIngredient( new Ingredient( "Coffee", -12 ) );
        r1.addIngredient( new Ingredient( "Milk", 0 ) );
        r1.addIngredient( new Ingredient( "Sugar", 0 ) );
        r1.addIngredient( new Ingredient( "Chocolate", 0 ) );

        final Recipe r2 = new Recipe();
        r2.setName( "Mocha" );
        r2.setPrice( 1 );
        r2.addIngredient( new Ingredient( "Coffee", 1 ) );
        r2.addIngredient( new Ingredient( "Milk", 1 ) );
        r2.addIngredient( new Ingredient( "Sugar", 1 ) );
        r2.addIngredient( new Ingredient( "Chocolate", 1 ) );

        final List<Recipe> recipes = List.of( r1, r2 );

        try {
            service.saveAll( recipes );
            Assertions.assertEquals( 0, service.count(),
                    "Trying to save a collection of elements where one is invalid should result in neither getting saved" );
        }
        catch ( final Exception e ) {
            Assertions.assertTrue( e instanceof ConstraintViolationException );
        }

    }

    /**
     * Test adding one recipe
     */
    @Test
    @Transactional
    public void testAddRecipe1 () {

        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
        final String name = "Coffee";
        final Recipe r1 = createRecipe( name, 50, 3, 1, 1, 0 );

        service.save( r1 );

        Assertions.assertEquals( 1, service.findAll().size(), "There should only one recipe in the CoffeeMaker" );
        Assertions.assertNotNull( service.findByName( name ) );

    }

    /* Test2 is done via the API for different validation */

    /**
     * Test adding a recipe with an invalid price
     */
    @Test
    @Transactional
    public void testAddRecipe3 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
        final String name = "Coffee";
        final Recipe r1 = createRecipe( name, -50, 3, 1, 1, 0 );

        try {
            service.save( r1 );

            Assertions.assertNull( service.findByName( name ),
                    "A recipe was able to be created with a negative price" );
        }
        catch ( final ConstraintViolationException cvee ) {
            // Expected
        }

    }

    /**
     * Test adding recipe with invalid coffee amount
     */
    @Test
    @Transactional
    public void testAddRecipe4 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
        final String name = "Coffee";
        final Recipe r1 = createRecipe( name, 50, -3, 1, 1, 2 );

        try {
            service.save( r1 );

            Assertions.assertNull( service.findByName( name ),
                    "A recipe was able to be created with a negative amount of coffee" );
        }
        catch ( final ConstraintViolationException cvee ) {
            // Expected
        }

    }

    /**
     * Test adding recipe with invalid milk amount
     */
    @Test
    @Transactional
    public void testAddRecipe5 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
        final String name = "Coffee";
        final Recipe r1 = createRecipe( name, 50, 3, -1, 1, 2 );

        try {
            service.save( r1 );

            Assertions.assertNull( service.findByName( name ),
                    "A recipe was able to be created with a negative amount of milk" );
        }
        catch ( final ConstraintViolationException cvee ) {
            // Expected
        }

    }

    /**
     * Test adding recipe with invalid sugar amount
     */
    @Test
    @Transactional
    public void testAddRecipe6 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
        final String name = "Coffee";
        final Recipe r1 = createRecipe( name, 50, 3, 1, -1, 2 );

        try {
            service.save( r1 );

            Assertions.assertNull( service.findByName( name ),
                    "A recipe was able to be created with a negative amount of sugar" );
        }
        catch ( final ConstraintViolationException cvee ) {
            // Expected
        }

    }

    /**
     * Test adding recipe with invalid chocolate amount
     */
    @Test
    @Transactional
    public void testAddRecipe7 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
        final String name = "Coffee";
        final Recipe r1 = createRecipe( name, 50, 3, 1, 1, -2 );

        try {
            service.save( r1 );

            Assertions.assertNull( service.findByName( name ),
                    "A recipe was able to be created with a negative amount of chocolate" );
        }
        catch ( final ConstraintViolationException cvee ) {
            // Expected
        }

    }

    /**
     * Test adding 2 recipes
     */
    @Test
    @Transactional
    public void testAddRecipe13 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
        service.save( r1 );
        final Recipe r2 = createRecipe( "Mocha", 50, 3, 1, 1, 2 );
        service.save( r2 );

        Assertions.assertEquals( 2, service.count(),
                "Creating two recipes should result in two recipes in the database" );

    }

    /**
     * Test adding 3 recipes
     */
    @Test
    @Transactional
    public void testAddRecipe14 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
        service.save( r1 );
        final Recipe r2 = createRecipe( "Mocha", 50, 3, 1, 1, 2 );
        service.save( r2 );
        final Recipe r3 = createRecipe( "Latte", 60, 3, 2, 2, 0 );
        service.save( r3 );

        Assertions.assertEquals( 3, service.count(),
                "Creating three recipes should result in three recipes in the database" );

    }

    /**
     * Test adding recipe with all zero field and all non zero field.
     */
    @Test
    @Transactional
    public void testAddRecipe15 () {
        // Check recipe count is 0 initially
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        // Create a new recipe with all zero field
        final Recipe r1 = createRecipe( "Coffee", 0, 0, 0, 0, 0 );
        service.save( r1 );

        // All the ingredients should not be 0 (should be false)
        Assertions.assertFalse( r1.checkRecipe() );

        // Create a new recipe with all non-zero field
        final Recipe r2 = createRecipe( "Coffee", 1, 2, 3, 4, 5 );
        service.save( r2 );

        // All the ingredients should not be 0 (should be true)
        Assertions.assertTrue( r2.checkRecipe() );

        Assertions.assertEquals( 2, service.count(),
                "Creating two recipes should result in two recipes in the database" );

    }

    /**
     * Tests toString() method.
     */
    @Test
    @Transactional
    public void testToString () {

        // Check recipe count is 0 initially
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        // Make a recipe
        final Recipe r1 = new Recipe();
        r1.setName( "Black Coffee" );
        r1.setPrice( 1 );
        r1.addIngredient( new Ingredient( "Coffee", 1 ) );
        r1.addIngredient( new Ingredient( "Milk", 0 ) );
        r1.addIngredient( new Ingredient( "Sugar", 0 ) );
        r1.addIngredient( new Ingredient( "Chocolate", 0 ) );
        service.save( r1 );

        // Check recipe count
        Assertions.assertEquals( 1, service.findAll().size(), "There should be 1 Recipes in the CoffeeMaker" );

        // Make a expected string
        final String expectedString = "Black Coffee";

        // Check if the toString() produces expected string name
        Assertions.assertTrue( r1.toString().contains( expectedString ) );

    }

    /**
     * Test deleting one recipe when there is just one in the database
     */
    @Test
    @Transactional
    public void testDeleteRecipe1 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
        service.save( r1 );

        Assertions.assertEquals( 1, service.count(), "There should be one recipe in the database" );

        service.delete( r1 );
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
    }

    /**
     * Test deleting all recipes at once
     */
    @Test
    @Transactional
    public void testDeleteRecipe2 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
        service.save( r1 );
        final Recipe r2 = createRecipe( "Mocha", 50, 3, 1, 1, 2 );
        service.save( r2 );
        final Recipe r3 = createRecipe( "Latte", 60, 3, 2, 2, 0 );
        service.save( r3 );

        Assertions.assertEquals( 3, service.count(), "There should be three recipes in the database" );

        service.deleteAll();

        Assertions.assertEquals( 0, service.count(), "`service.deleteAll()` should remove everything" );

    }

    /**
     * Test deleting one recipe at a time when there are multiple
     */
    @Test
    @Transactional
    public void testDeleteRecipe3 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
        final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
        service.save( r1 );
        final Recipe r2 = createRecipe( "Mocha", 50, 3, 1, 1, 2 );
        service.save( r2 );
        final Recipe r3 = createRecipe( "Latte", 60, 3, 2, 2, 0 );
        service.save( r3 );

        Assertions.assertEquals( 3, service.count(), "There should be three recipes in the database" );

        // Test deleting just one recipe at a time
        service.delete( r3 );
        Assertions.assertEquals( 2, service.count(), "There should be two recipes in the database" );

        // Delete another one
        service.delete( r1 );
        Assertions.assertEquals( 1, service.count(), "There should be one recipe in the database" );

        // Make sure the correct recipe is left in database
        Assertions.assertEquals( r2, service.findByName( "Mocha" ) );

    }

    /**
     * Test deleting a recipe that is not in the database
     */
    @Test
    @Transactional
    public void testDeleteRecipe4 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        // Recipe r1 is not saved in database
        final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
        final Recipe r2 = createRecipe( "Mocha", 50, 3, 1, 1, 2 );
        service.save( r2 );
        final Recipe r3 = createRecipe( "Latte", 60, 3, 2, 2, 0 );
        service.save( r3 );
        Assertions.assertEquals( 2, service.findAll().size(), "There should be 2 Recipes in the CoffeeMaker" );

        // Delete a recipe that's not in the database
        service.delete( r1 );
        Assertions.assertEquals( 2, service.findAll().size(), "There should be 2 Recipes in the CoffeeMaker" );

    }

    /**
     * Checks to make sure the service.findAll() list does not contain a deleted
     * recipe, and also checks that the correct exception is thrown.
     */
    @Test
    @Transactional
    public void testDeleteRecipe5 () {
        // Check to make sure the database is empty
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
        Assertions.assertEquals( 0, service.count() );

        // Create 4 recipes
        final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
        service.save( r1 );
        final Recipe r2 = createRecipe( "Mocha", 50, 3, 1, 1, 2 );
        service.save( r2 );
        final Recipe r3 = createRecipe( "Latte", 60, 3, 2, 2, 0 );
        service.save( r3 );
        final Recipe r4 = createRecipe( "Hot Chocolate", 20, 1, 5, 6, 4 );
        service.save( r4 );

        Assertions.assertEquals( 4, service.findAll().size(), "There should be 4 Recipes in the CoffeeMaker" );
        Assertions.assertEquals( 4, service.count(), "There should be four recipes in the database" );

        // Delete a recipe
        service.delete( r2 );

        // Check that there are 3 recipes now
        Assertions.assertEquals( 3, service.count() );
        Assertions.assertEquals( 3, service.findAll().size() );

        // Check each item in the ArrayList to make sure a recipe is not still
        // there
        final List<Recipe> recipeList = service.findAll();
        Assertions.assertEquals( 3, recipeList.size() ); // Same as above

        Assertions.assertEquals( r1, recipeList.get( 0 ) );
        Assertions.assertEquals( r3, recipeList.get( 1 ) );
        Assertions.assertEquals( r4, recipeList.get( 2 ) );
        Assertions.assertFalse( recipeList.contains( r2 ) ); // "r2" should not
                                                             // be in the List

        // Check the names (in added order)
        Assertions.assertEquals( "Coffee", recipeList.get( 0 ).getName() );
        Assertions.assertEquals( "Latte", recipeList.get( 1 ).getName() );
        Assertions.assertEquals( "Hot Chocolate", recipeList.get( 2 ).getName() );

        // Check for the correct exception when trying to get the deleted recipe
        final IndexOutOfBoundsException e1 = Assertions.assertThrows( IndexOutOfBoundsException.class, () -> {
            recipeList.get( 3 );
        }, "Should throw an IndexOutOfBoundsException" );

        // Check the exception message
        Assertions.assertEquals( "Index 3 out of bounds for length 3", e1.getMessage() );

    }

    /**
     * Test editing a recipe after it has already been saved
     */
    @Test
    @Transactional
    public void testEditRecipe1 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
        service.save( r1 );

        r1.setPrice( 70 );

        service.save( r1 );

        final Recipe retrieved = service.findByName( "Coffee" );

        Assertions.assertEquals( 70, (int) retrieved.getPrice() );
        Assertions.assertEquals( 3, (int) retrieved.getNames().get( 0 ).getAmount() );
        Assertions.assertEquals( 1, (int) retrieved.getNames().get( 1 ).getAmount() );
        Assertions.assertEquals( 1, (int) retrieved.getNames().get( 2 ).getAmount() );
        Assertions.assertEquals( 0, (int) retrieved.getNames().get( 3 ).getAmount() );

        Assertions.assertEquals( 1, service.count(), "Editing a recipe shouldn't duplicate it" );

    }

    /**
     * Tests updating a recipe using the updateRecipe() method.
     */
    @Test
    @Transactional
    public void testUpdateRecipe () {
        // Check for no recipes in the database initially
        Assertions.assertEquals( 0, service.findAll().size() );
        Assertions.assertEquals( 0, service.count() );

        // Create a new recipe first and save it
        final Recipe recipe1 = createRecipe( "Orange Juice", 20, 1, 6, 8, 7 );
        service.save( recipe1 );

        // Check its contents
        Assertions.assertEquals( 20, (int) recipe1.getPrice() );
        Assertions.assertEquals( 1, (int) recipe1.getNames().get( 0 ).getAmount() );
        Assertions.assertEquals( 6, (int) recipe1.getNames().get( 1 ).getAmount() );
        Assertions.assertEquals( 8, (int) recipe1.getNames().get( 2 ).getAmount() );
        Assertions.assertEquals( 7, (int) recipe1.getNames().get( 3 ).getAmount() );
        Assertions.assertEquals( "Orange Juice", recipe1.getName() );
        Assertions.assertEquals( 1, service.count() );
        Assertions.assertEquals( 1, service.findAll().size() );

    }

    /**
     * Tests the hashCode() method for Recipe.java.
     */
    @Test
    @Transactional
    public void testHashCode () {
        // Create multiple Recipe objects for testing
        final Recipe r1 = createRecipe( "Mocha", 3, 5, 9, 8, 1 );
        final Recipe r2 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
        final Recipe r3 = createRecipe( "Latte", 60, 3, 2, 2, 0 );
        final Recipe r4 = createRecipe( "Latte", 60, 3, 2, 2, 0 ); // Same as r3
        final Recipe r5 = createRecipe( "Mocha", 3, 5, 9, 8, 1 ); // Same as r1

        // Test for the same hash code when objects are equal
        Assertions.assertEquals( r1.hashCode(), r5.hashCode() );
        Assertions.assertEquals( r3.hashCode(), r4.hashCode() );

        // Test other objects that are different, for each Recipe
        Assertions.assertNotEquals( r1.hashCode(), r2.hashCode() );
        Assertions.assertNotEquals( r1.hashCode(), r3.hashCode() );
        Assertions.assertNotEquals( r1.hashCode(), r4.hashCode() );

        Assertions.assertNotEquals( r2.hashCode(), r3.hashCode() );
        Assertions.assertNotEquals( r2.hashCode(), r4.hashCode() );
        Assertions.assertNotEquals( r2.hashCode(), r5.hashCode() );

        Assertions.assertNotEquals( r3.hashCode(), r5.hashCode() );

        Assertions.assertNotEquals( r4.hashCode(), r5.hashCode() );

        // Check that the same object returns the exact hash code
        Assertions.assertEquals( r5.hashCode(), r5.hashCode() );
        Assertions.assertEquals( r1.hashCode(), r1.hashCode() );

        // Check when the Recipe's name is null, which is checked by the
        // hashCode() method
        final Recipe nullRecipe1 = createRecipe( null, 1, 1, 1, 1, 1 );
        final Recipe nullRecipe2 = createRecipe( null, 5, 4, 3, 2, 1 );

        Assertions.assertEquals( nullRecipe1.hashCode(), nullRecipe2.hashCode() ); // Different
        Assertions.assertEquals( nullRecipe1.hashCode(), nullRecipe1.hashCode() ); // Same

        Assertions.assertNotEquals( nullRecipe1.hashCode(), r3.hashCode() );
        Assertions.assertNotEquals( r5, nullRecipe2.hashCode() );

        // Check when only the name is the same (should still be true)
        final Recipe sameName = createRecipe( "Mocha", 4, 4, 4, 4, 4 );
        Assertions.assertEquals( sameName.hashCode(), r1.hashCode() );
        Assertions.assertEquals( sameName.hashCode(), r5.hashCode() );

        // Save some recipes to the database and check again
        service.save( r3 );
        service.save( r2 );
        service.save( nullRecipe1 );
        Assertions.assertEquals( 3, service.count() );

        Assertions.assertNotEquals( nullRecipe1.hashCode(), r3.hashCode() );
        Assertions.assertNotEquals( nullRecipe1.hashCode(), r2.hashCode() );
        Assertions.assertNotEquals( r2.hashCode(), r3.hashCode() );
    }

    /**
     * Tests the equals() method for Recipe.java.
     */
    @Test
    @Transactional
    public void testEquals () {
        // Create multiple Recipe objects for testing
        final Recipe r1 = createRecipe( "Mocha", 3, 5, 9, 8, 1 );
        final Recipe r2 = createRecipe( "Coffee", 50, 3, 1, 1, 0 );
        final Recipe r3 = createRecipe( "Latte", 60, 3, 2, 2, 0 );
        final Recipe r4 = createRecipe( "Latte", 60, 3, 2, 2, 0 ); // Same as r3
        final Recipe r5 = createRecipe( "Mocha", 3, 5, 9, 8, 1 ); // Same as r1

        // Test for the equality for the same object (in both directions)
        Assertions.assertTrue( r1.equals( r5 ) );
        Assertions.assertTrue( r5.equals( r1 ) );
        Assertions.assertTrue( r3.equals( r4 ) );
        Assertions.assertTrue( r4.equals( r3 ) );

        // Test for non-matching objects
        Assertions.assertFalse( r1.equals( r2 ) );
        Assertions.assertFalse( r1.equals( r3 ) );
        Assertions.assertFalse( r1.equals( r4 ) );

        Assertions.assertFalse( r2.equals( r3 ) );
        Assertions.assertFalse( r2.equals( r4 ) );
        Assertions.assertFalse( r2.equals( r5 ) );

        Assertions.assertFalse( r3.equals( r5 ) );

        Assertions.assertFalse( r4.equals( r5 ) );

        // Check that the same object matches to itself
        Assertions.assertTrue( r1.equals( r1 ) );
        Assertions.assertTrue( r5.equals( r5 ) );
        Assertions.assertTrue( r3.equals( r3 ) );

        // Check when the Recipe's name is null
        final Recipe nullRecipe1 = createRecipe( null, 1, 1, 1, 1, 1 );
        final Recipe nullRecipe2 = createRecipe( null, 5, 4, 3, 2, 1 );

        Assertions.assertTrue( nullRecipe1.equals( nullRecipe2 ) ); // Different
        Assertions.assertTrue( nullRecipe1.equals( nullRecipe1 ) ); // Same

        Assertions.assertFalse( nullRecipe1.equals( r3 ) );
        Assertions.assertFalse( nullRecipe2.equals( r5 ) );

        // Check when only the name is the same (should still be true)
        final Recipe sameName = createRecipe( "Mocha", 4, 4, 4, 4, 4 );
        Assertions.assertTrue( sameName.equals( r1 ) );
        Assertions.assertTrue( sameName.equals( r5 ) );
        Assertions.assertTrue( r1.equals( sameName ) );
        Assertions.assertTrue( r5.equals( sameName ) );

        // Save some recipes to the database and check again
        service.save( r3 );
        service.save( r2 );
        service.save( nullRecipe1 );
        Assertions.assertEquals( 3, service.count() );

        Assertions.assertFalse( nullRecipe1.equals( r3 ) );
        Assertions.assertFalse( nullRecipe1.equals( r2 ) );
        Assertions.assertFalse( r2.equals( r3 ) );

    }

    /**
     * Creates a Recipe with the given amount of ingredients
     *
     * @param name
     *            name of recipe
     * @param price
     *            price of recipe
     * @param coffee
     *            amount of coffee
     * @param milk
     *            amount of milk
     * @param sugar
     *            amount of sugar
     * @param chocolate
     *            amount of chocolate
     * @return Recipe object with given values
     */
    private Recipe createRecipe ( final String name, final Integer price, final Integer coffee, final Integer milk,
            final Integer sugar, final Integer chocolate ) {
        final Recipe recipe = new Recipe();

        if ( coffee >= 0 && milk >= 0 && sugar >= 0 && chocolate >= 0 ) {
            recipe.setName( name );
            recipe.setPrice( price );
            recipe.addIngredient( new Ingredient( "Coffee", coffee ) );
            recipe.addIngredient( new Ingredient( "Milk", milk ) );
            recipe.addIngredient( new Ingredient( "Sugar", sugar ) );
            recipe.addIngredient( new Ingredient( "Chocolate", chocolate ) );
        }

        return recipe;
    }

}
