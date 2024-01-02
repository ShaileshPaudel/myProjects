package edu.ncsu.csc.CoffeeMaker.unit;

import java.util.List;

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
import edu.ncsu.csc.CoffeeMaker.services.IngredientService;

/**
 * Tests functionality of Ingredient objects, such as add, delete, edit, etc.
 *
 * A lot of this code is based off of the other test files in the unit folder.
 *
 * @author ssharm37
 * @author stnguye3
 * @author spaudel4
 *
 */
@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class IngredientTest {

    /**
     * Service object for testing
     */
    @Autowired
    private IngredientService service;

    /**
     * Sets up the service by deleting all existing ingredients
     */
    @BeforeEach
    public void setup () {
        service.deleteAll();
    }

    /**
     * Tests adding 2 different ingredients.
     */
    @Test
    @Transactional
    public void testAddIngredient () {
        Assertions.assertEquals( 0, service.count() );

        final Ingredient i1 = new Ingredient();
        i1.setName( "Honey" );
        i1.setAmount( 4 );

        service.save( i1 );

        final Ingredient i2 = new Ingredient();
        i2.setName( "Caramel" );
        i2.setAmount( 1 );

        service.save( i2 );

        final List<Ingredient> ingredients = service.findAll();
        Assertions.assertEquals( 2, ingredients.size(),
                "Creating two ingredients should result in two ingredients in the database" );

        Assertions.assertEquals( "Honey", i1.getName() );
        Assertions.assertEquals( 4, i1.getAmount() );

        Assertions.assertEquals( "Caramel", i2.getName() );
        Assertions.assertEquals( 1, i2.getAmount() );

        Assertions.assertEquals( i1, ingredients.get( 0 ), "The retrieved ingredient should match the created one" );
        Assertions.assertEquals( i2, ingredients.get( 1 ), "The retrieved ingredient should match the created one" );

    }

    /**
     * Test deleting one Ingredient when there is just one in the database.
     */
    @Test
    @Transactional
    public void testDeleteIngredient1 () {

        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Ingredients in the CoffeeMaker" );

        final Ingredient i1 = new Ingredient();
        i1.setName( "Honey" );
        i1.setAmount( 4 );

        service.save( i1 );

        Assertions.assertEquals( "Honey", i1.getName() );
        Assertions.assertEquals( 4, i1.getAmount() );

        Assertions.assertEquals( 1, service.count(), "There should be one ingredient in the database" );

        service.delete( i1 );

        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Ingredients in the CoffeeMaker" );

    }

    /**
     * Test editing an Ingredient after it has already been saved.
     */
    @Test
    @Transactional
    public void testEditIngredient1 () {

        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Ingredients in the CoffeeMaker" );

        final Ingredient i1 = new Ingredient();
        i1.setName( "Honey" );
        i1.setAmount( 4 );

        service.save( i1 );

        Assertions.assertEquals( 1, service.count(), "Editing an ingredient shouldn't duplicate it" );

        Assertions.assertEquals( "Honey", i1.getName() );
        Assertions.assertEquals( 4, i1.getAmount() );

        // Edit and re-save the Ingredient object
        i1.setAmount( 15 );

        service.save( i1 );

        Assertions.assertEquals( "Honey", i1.getName() );
        Assertions.assertEquals( 15, i1.getAmount() ); // Should change

        Assertions.assertEquals( 1, service.count(), "Editing an ingredient shouldn't duplicate it" );

    }

    /**
     * Tests the hashCode() method for Ingredient.java.
     */
    @Test
    @Transactional
    public void testHashCode () {
        // Create multiple Ingredient objects for testing (i1 <=> i5, i3 <=> i4)
        final Ingredient i1 = createIngredient( "Honey", 3 );
        final Ingredient i2 = createIngredient( "Caramel", 50 );
        final Ingredient i3 = createIngredient( "Strawberry", 1 );
        final Ingredient i4 = createIngredient( "Strawberry", 1 );
        final Ingredient i5 = createIngredient( "Honey", 3 );

        // Test for the same hash code when objects are equal
        Assertions.assertEquals( i1.hashCode(), i5.hashCode() );
        Assertions.assertEquals( i3.hashCode(), i4.hashCode() );

        // Test other objects that are different, for each Ingredient
        Assertions.assertNotEquals( i1.hashCode(), i2.hashCode() );
        Assertions.assertNotEquals( i1.hashCode(), i3.hashCode() );
        Assertions.assertNotEquals( i1.hashCode(), i4.hashCode() );

        Assertions.assertNotEquals( i2.hashCode(), i3.hashCode() );
        Assertions.assertNotEquals( i2.hashCode(), i4.hashCode() );
        Assertions.assertNotEquals( i2.hashCode(), i5.hashCode() );

        Assertions.assertNotEquals( i3.hashCode(), i5.hashCode() );

        Assertions.assertNotEquals( i4.hashCode(), i5.hashCode() );

        // Check that the same object returns the exact hash code
        Assertions.assertEquals( i5.hashCode(), i5.hashCode() );
        Assertions.assertEquals( i1.hashCode(), i1.hashCode() );

        // Check when the Ingredient's name is null, which is checked by the
        // hashCode() method
        final Ingredient nullIngredient1 = createIngredient( null, 1 );
        final Ingredient nullIngredient2 = createIngredient( null, 5 );

        Assertions.assertEquals( nullIngredient1.hashCode(), nullIngredient2.hashCode() ); // Different
        Assertions.assertEquals( nullIngredient2.hashCode(), nullIngredient1.hashCode() ); // Same

        Assertions.assertNotEquals( nullIngredient1.hashCode(), i3.hashCode() );
        Assertions.assertNotEquals( i5, nullIngredient2.hashCode() );

        // Save some ingredients to the database and check again
        service.save( i3 );
        service.save( i2 );
        service.save( nullIngredient1 );
        Assertions.assertEquals( 3, service.count() );

        Assertions.assertNotEquals( nullIngredient1.hashCode(), i3.hashCode() );
        Assertions.assertNotEquals( nullIngredient1.hashCode(), i2.hashCode() );
        Assertions.assertNotEquals( i2.hashCode(), i3.hashCode() );
    }

    /**
     * Tests the equals() method for Ingredient.java.
     */
    @Test
    @Transactional
    public void testEquals () {
        // Create multiple Ingredient objects for testing (i1 <=> i5, i3 <=> i4)
        final Ingredient i1 = createIngredient( "Honey", 3 );
        final Ingredient i2 = createIngredient( "Caramel", 50 );
        final Ingredient i3 = createIngredient( "Strawberry", 1 );
        final Ingredient i4 = createIngredient( "Strawberry", 1 );
        final Ingredient i5 = createIngredient( "Honey", 3 );

        // Test for the equality for the same object (in both directions)
        Assertions.assertTrue( i1.equals( i5 ) );
        Assertions.assertTrue( i5.equals( i1 ) );
        Assertions.assertTrue( i3.equals( i4 ) );
        Assertions.assertTrue( i4.equals( i3 ) );

        // Test for non-matching objects
        Assertions.assertFalse( i1.equals( i2 ) );
        Assertions.assertFalse( i1.equals( i3 ) );
        Assertions.assertFalse( i1.equals( i4 ) );

        Assertions.assertFalse( i2.equals( i3 ) );
        Assertions.assertFalse( i2.equals( i4 ) );
        Assertions.assertFalse( i2.equals( i5 ) );

        Assertions.assertFalse( i3.equals( i5 ) );

        Assertions.assertFalse( i4.equals( i5 ) );

        // Check that the same object matches to itself
        Assertions.assertTrue( i1.equals( i1 ) );
        Assertions.assertTrue( i5.equals( i5 ) );
        Assertions.assertTrue( i3.equals( i3 ) );

        // Check when the Ingredient name is null
        final Ingredient nullIngredient1 = createIngredient( null, 1 );
        final Ingredient nullIngredient2 = createIngredient( null, 5 );

        // Different
        Assertions.assertTrue( nullIngredient1.equals( nullIngredient2 ) ); //
        // Same
        Assertions.assertTrue( nullIngredient2.equals( nullIngredient1 ) ); //

        Assertions.assertFalse( nullIngredient1.equals( i3 ) );
        Assertions.assertFalse( nullIngredient2.equals( i5 ) );

        // Save some ingredients to the database and check again
        service.save( i3 );
        service.save( i2 );
        service.save( nullIngredient1 );
        Assertions.assertEquals( 3, service.count() );

        Assertions.assertFalse( nullIngredient1.equals( i3 ) );
        Assertions.assertFalse( nullIngredient1.equals( i2 ) );
        Assertions.assertFalse( i2.equals( i3 ) );

    }

    /**
     * A private helper method to create an Ingredient with the given
     * parameters.
     *
     * @param name
     *            type of the ingredient (i.e. coffee)
     * @param amount
     *            initial amount of ingredient
     * @return Ingredient object with given values
     */
    private Ingredient createIngredient ( final String name, final Integer amount ) {
        final Ingredient createdIngredient = new Ingredient( name, amount );
        createdIngredient.setName( name );
        createdIngredient.setAmount( amount );

        return createdIngredient;
    }

}
