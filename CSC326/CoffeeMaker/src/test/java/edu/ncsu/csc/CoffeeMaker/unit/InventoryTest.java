package edu.ncsu.csc.CoffeeMaker.unit;

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
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;

/**
 * Tests Inventory functionality
 *
 * @author Sydney Nguyen
 * @author Shailesh Paudel
 *
 */
@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class InventoryTest {

    /**
     * Inventory used for testing
     */
    @Autowired
    private InventoryService inventoryService;

    /** coffee ingredient */
    final Ingredient         coffee    = new Ingredient( "Coffee", 1 );

    /** milk ingredient */
    final Ingredient         milk      = new Ingredient( "Milk", 20 );

    /** sugar ingredient */
    final Ingredient         sugar     = new Ingredient( "Sugar", 5 );

    /** chocolate ingredient */
    final Ingredient         chocolate = new Ingredient( "Chocolate", 10 );

    /**
     * Sets up the Inventory before each test Each ingredient is set to 500
     */
    @BeforeEach
    public void setup () {
        final Inventory ivt = inventoryService.getInventory();
        ivt.addIngredients( 500, 500, 500, 500 );
        inventoryService.save( ivt );
    }

    /**
     * Test using ingredients from Inventory to make sure they get updated
     */
    @Test
    @Transactional
    public void testConsumeInventory () {
        final Inventory i = inventoryService.getInventory();

        final Recipe recipe = new Recipe();
        recipe.setName( "Delicious Not-Coffee" );
        recipe.addIngredient( chocolate );
        recipe.addIngredient( milk );
        recipe.addIngredient( sugar );
        recipe.addIngredient( coffee );

        recipe.setPrice( 5 );

        i.useIngredients( recipe );

        /*
         * Make sure that all of the inventory fields are now properly updated
         */

        Assertions.assertEquals( 490, (int) i.getAmount( "Chocolate" ) );
        Assertions.assertEquals( 480, (int) i.getAmount( "Milk" ) );
        Assertions.assertEquals( 495, (int) i.getAmount( "Sugar" ) );
        Assertions.assertEquals( 499, (int) i.getAmount( "Coffee" ) );
    }

    /**
     * Test adding ingredients to the inventory
     */
    @Test
    @Transactional
    public void testAddInventory1 () {
        Inventory ivt = inventoryService.getInventory();

        ivt.addIngredients( 5, 3, 7, 2 );

        /* Save and retrieve again to update with DB */
        inventoryService.save( ivt );

        ivt = inventoryService.getInventory();

        Assertions.assertEquals( 505, (int) ivt.getAmount( "Coffee" ),
                "Adding to the inventory should result in correctly-updated values for coffee" );
        Assertions.assertEquals( 503, (int) ivt.getAmount( "Milk" ),
                "Adding to the inventory should result in correctly-updated values for milk" );
        Assertions.assertEquals( 507, (int) ivt.getAmount( "Sugar" ),
                "Adding to the inventory should result in correctly-updated values sugar" );
        Assertions.assertEquals( 502, (int) ivt.getAmount( "Chocolate" ),
                "Adding to the inventory should result in correctly-updated values chocolate" );

    }

    /**
     * Test adding invalid amount of coffee to Inventory
     */
    @Test
    @Transactional
    public void testAddInventory2 () {
        final Inventory ivt = inventoryService.getInventory();

        try {
            ivt.addIngredients( -5, 3, 7, 2 );
        }
        catch ( final IllegalArgumentException iae ) {
            Assertions.assertEquals( 500, (int) ivt.getAmount( "Coffee" ),
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- coffee" );
            Assertions.assertEquals( 500, (int) ivt.getAmount( "Milk" ),
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- milk" );
            Assertions.assertEquals( 500, (int) ivt.getAmount( "Sugar" ),
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- sugar" );
            Assertions.assertEquals( 500, (int) ivt.getAmount( "Chocolate" ),
                    "Trying to update the Inventory with an invalid value for coffee should result in no changes -- chocolate" );
        }
    }

    /**
     * Test adding invalid amount of milk to Inventory
     */
    @Test
    @Transactional
    public void testAddInventory3 () {
        final Inventory ivt = inventoryService.getInventory();

        try {
            ivt.addIngredients( 5, -3, 7, 2 );
        }
        catch ( final IllegalArgumentException iae ) {
            Assertions.assertEquals( 500, (int) ivt.getAmount( "Coffee" ),
                    "Trying to update the Inventory with an invalid value for milk should result in no changes -- coffee" );
            Assertions.assertEquals( 500, (int) ivt.getAmount( "Milk" ),
                    "Trying to update the Inventory with an invalid value for milk should result in no changes -- milk" );
            Assertions.assertEquals( 500, (int) ivt.getAmount( "Sugar" ),
                    "Trying to update the Inventory with an invalid value for milk should result in no changes -- sugar" );
            Assertions.assertEquals( 500, (int) ivt.getAmount( "Chocolate" ),
                    "Trying to update the Inventory with an invalid value for milk should result in no changes -- chocolate" );

        }

    }

    /**
     * Test adding invalid amount of sugar to Inventory
     */
    @Test
    @Transactional
    public void testAddInventory4 () {
        final Inventory ivt = inventoryService.getInventory();

        try {
            ivt.addIngredients( 5, 3, -7, 2 );
        }
        catch ( final IllegalArgumentException iae ) {
            Assertions.assertEquals( 500, (int) ivt.getAmount( "Coffee" ),
                    "Trying to update the Inventory with an invalid value for sugar should result in no changes -- coffee" );
            Assertions.assertEquals( 500, (int) ivt.getAmount( "Milk" ),
                    "Trying to update the Inventory with an invalid value for sugar should result in no changes -- milk" );
            Assertions.assertEquals( 500, (int) ivt.getAmount( "Sugar" ),
                    "Trying to update the Inventory with an invalid value for sugar should result in no changes -- sugar" );
            Assertions.assertEquals( 500, (int) ivt.getAmount( "Chocolate" ),
                    "Trying to update the Inventory with an invalid value for sugar should result in no changes -- chocolate" );

        }

    }

    /**
     * Test adding invalid amount of chocolate to Inventory
     */
    @Test
    @Transactional
    public void testAddInventory5 () {
        final Inventory ivt = inventoryService.getInventory();

        try {
            ivt.addIngredients( 5, 3, 7, -2 );
        }
        catch ( final IllegalArgumentException iae ) {
            Assertions.assertEquals( 500, (int) ivt.getAmount( "Coffee" ),
                    "Trying to update the Inventory with an invalid value for chocolate should result in no changes -- coffee" );
            Assertions.assertEquals( 500, (int) ivt.getAmount( "Milk" ),
                    "Trying to update the Inventory with an invalid value for chocolate should result in no changes -- milk" );
            Assertions.assertEquals( 500, (int) ivt.getAmount( "Sugar" ),
                    "Trying to update the Inventory with an invalid value for chocolate should result in no changes -- sugar" );
            Assertions.assertEquals( 500, (int) ivt.getAmount( "Chocolate" ),
                    "Trying to update the Inventory with an invalid value for chocolate should result in no changes -- chocolate" );

        }

    }

    /**
     * Test adding 0 amount of everything to Inventory
     */
    @Test
    @Transactional
    public void testAddInventory6 () {
        final Inventory ivt = inventoryService.getInventory();

        // Trying adding 0 to all the fields
        ivt.addIngredients( 0, 0, 0, 0 );

        // Check the amount for each of the fields
        Assertions.assertEquals( 500, (int) ivt.getAmount( "Coffee" ),
                "Trying to update the Inventory with an 0 value for chocolate should result in no changes -- coffee" );
        Assertions.assertEquals( 500, (int) ivt.getAmount( "Milk" ),
                "Trying to update the Inventory with an 0 value for chocolate should result in no changes -- milk" );
        Assertions.assertEquals( 500, (int) ivt.getAmount( "Sugar" ),
                "Trying to update the Inventory with an 0 value for chocolate should result in no changes -- sugar" );
        Assertions.assertEquals( 500, (int) ivt.getAmount( "Chocolate" ),
                "Trying to update the Inventory with an 0 value for chocolate should result in no changes -- chocolate" );

    }

    /**
     * Test consuming inventory when there is not enough of a certain ingredient
     */
    @Test
    @Transactional
    public void testNotEnoughInventory () {
        final Inventory i = inventoryService.getInventory();

        // ------------------ Create recipe requiring a lot of chocolate
        final Recipe recipe1 = new Recipe();
        recipe1.setName( "Super Chocolate" );
        recipe1.addIngredient( new Ingredient( "Chocolate", 501 ) );
        recipe1.addIngredient( new Ingredient( "Milk", 20 ) );
        recipe1.addIngredient( new Ingredient( "Sugar", 5 ) );
        recipe1.addIngredient( new Ingredient( "Coffee", 1 ) );
        recipe1.setPrice( 5 );

        // Not enough chocolate in Inventory, so no inventory will be used
        i.useIngredients( recipe1 );

        // Make sure that all of the inventory fields are still the same since
        // drink was not made
        Assertions.assertEquals( 500, (int) i.getAmount( "Chocolate" ) );
        Assertions.assertEquals( 500, (int) i.getAmount( "Milk" ) );
        Assertions.assertEquals( 500, (int) i.getAmount( "Sugar" ) );
        Assertions.assertEquals( 500, (int) i.getAmount( "Coffee" ) );

        // ------------------ Create recipe requiring a lot of milk
        final Recipe recipe2 = new Recipe();
        recipe2.setName( "Just Milk" );
        recipe2.addIngredient( new Ingredient( "Milk", 600 ) );
        recipe2.setPrice( 12 );

        // Not enough milk in Inventory, so no inventory will be used
        i.useIngredients( recipe2 );

        // Make sure that all of the inventory fields are still the same since
        // drink was not made
        Assertions.assertEquals( 500, (int) i.getAmount( "Chocolate" ) );
        Assertions.assertEquals( 500, (int) i.getAmount( "Milk" ) );
        Assertions.assertEquals( 500, (int) i.getAmount( "Sugar" ) );
        Assertions.assertEquals( 500, (int) i.getAmount( "Coffee" ) );

        // ------------------ Create recipe requiring a lot of sugar
        final Recipe recipe3 = new Recipe();
        recipe3.setName( "Super sugar" );
        recipe3.addIngredient( new Ingredient( "Chocolate", 2 ) );
        recipe3.addIngredient( new Ingredient( "Milk", 20 ) );
        recipe3.addIngredient( new Ingredient( "Sugar", 506 ) );
        recipe3.addIngredient( new Ingredient( "Coffee", 23 ) );
        recipe3.setPrice( 5 );

        // Not enough sugar in Inventory, so no inventory will be used
        i.useIngredients( recipe3 );

        // Make sure that all of the inventory fields are still the same since
        // drink was not made
        Assertions.assertEquals( 500, (int) i.getAmount( "Chocolate" ) );
        Assertions.assertEquals( 500, (int) i.getAmount( "Milk" ) );
        Assertions.assertEquals( 500, (int) i.getAmount( "Sugar" ) );
        Assertions.assertEquals( 500, (int) i.getAmount( "Coffee" ) );

        // ------------------ Create recipe requiring a lot of coffee
        final Recipe recipe4 = new Recipe();
        recipe4.setName( "Black Coffee" );
        recipe4.addIngredient( new Ingredient( "Chocolate", 0 ) );
        recipe4.addIngredient( new Ingredient( "Milk", 0 ) );
        recipe4.addIngredient( new Ingredient( "Sugar", 0 ) );
        recipe4.addIngredient( new Ingredient( "Coffee", 638 ) );
        recipe4.setPrice( 3 );

        // Not enough coffee in Inventory, so no inventory will be used
        i.useIngredients( recipe4 );

        // Make sure that all of the inventory fields are still the same since
        // drink was not made
        Assertions.assertEquals( 500, (int) i.getAmount( "Chocolate" ) );
        Assertions.assertEquals( 500, (int) i.getAmount( "Milk" ) );
        Assertions.assertEquals( 500, (int) i.getAmount( "Sugar" ) );
        Assertions.assertEquals( 500, (int) i.getAmount( "Coffee" ) );

    }

    /**
     * Tests checking for valid/invalid amounts of chocolate
     */
    @Test
    @Transactional
    public void testPutChocolate () {
        final Inventory i = inventoryService.getInventory();
        // Should return amount of ingredient as int if amount is valid
        // Check valid chocolate amounts
        Integer ingredientAmt = i.putIngredient( "Chocolate", 32 );
        Assertions.assertEquals( 32, ingredientAmt );

        ingredientAmt = i.putIngredient( "Chocolate", 0 );
        Assertions.assertEquals( 0, ingredientAmt );

        // Check invalid chocolate amount
        try {
            i.putIngredient( "Chocolate", -4 );
        }
        catch ( final Exception e ) {
            Assertions.assertTrue( e instanceof IllegalArgumentException );
            Assertions.assertEquals( "Amount must be a positive integer", e.getMessage() );
        }

    }

    /**
     * Tests checking for valid/invalid amounts of coffee
     */
    @Test
    @Transactional
    public void testPutCoffee () {
        final Inventory i = inventoryService.getInventory();
        int ingredientAmt;

        // Should return amount of ingredient as int if amount is valid
        // Check valid coffee amounts
        ingredientAmt = i.putIngredient( "Coffee", 17 );
        Assertions.assertEquals( 17, ingredientAmt );

        ingredientAmt = i.putIngredient( "Coffee", 0 );
        Assertions.assertEquals( 0, ingredientAmt );

        // Check invalid coffee amounts
        try { // Try a negative amount
            i.putIngredient( "Coffee", -10 );
        }
        catch ( final Exception e ) {
            Assertions.assertTrue( e instanceof IllegalArgumentException );
            Assertions.assertEquals( "Amount must be a positive integer", e.getMessage() );
        }

    }

    /**
     * Tests checking for valid/invalid amounts of milk
     */
    @Test
    @Transactional
    public void testPutMilk () {
        final Inventory i = inventoryService.getInventory();
        int ingredientAmt;

        // Should return amount of ingredient as int if amount is valid
        // Check valid milk amounts
        ingredientAmt = i.putIngredient( "Milk", 204 );
        Assertions.assertEquals( 204, ingredientAmt );

        ingredientAmt = i.putIngredient( "Milk", 0 );
        Assertions.assertEquals( 0, ingredientAmt );

        // Check invalid milk amounts
        try { // Try a negative amount
            i.putIngredient( "Milk", -3 );
        }
        catch ( final Exception e ) {
            Assertions.assertTrue( e instanceof IllegalArgumentException );
            Assertions.assertEquals( "Amount must be a positive integer", e.getMessage() );
        }

    }

    /**
     * Tests checking for valid/invalid amounts of sugar
     */
    @Test
    @Transactional
    public void testPutSugar () {
        final Inventory i = inventoryService.getInventory();
        int ingredientAmt;

        // Should return amount of ingredient as int if amount is valid
        // Check valid sugar amounts
        ingredientAmt = i.putIngredient( "Sugar", 123 );
        Assertions.assertEquals( 123, ingredientAmt );

        ingredientAmt = i.putIngredient( "Sugar", 0 );
        Assertions.assertEquals( 0, ingredientAmt );

        // Check invalid sugar amounts
        try { // Try a negative amount
            i.putIngredient( "Sugar", -29 );
        }
        catch ( final Exception e ) {
            Assertions.assertTrue( e instanceof IllegalArgumentException );
            Assertions.assertEquals( "Amount must be a positive integer", e.getMessage() );
        }

    }

    /**
     * Test toString functionality of Inventory
     */
    @Test
    @Transactional
    public void testToString () {
        final Inventory i = inventoryService.getInventory();

        // Test initial values in inventory as string representation
        Assertions.assertTrue( i.toString().contains( "Coffee: 500\n" ) );
        Assertions.assertTrue( i.toString().contains( "Milk: 500\n" ) );
        Assertions.assertTrue( i.toString().contains( "Sugar: 500\n" ) );
        Assertions.assertTrue( i.toString().contains( "Chocolate: 500\n" ) );

        // Add recipe and make it, to use up some of the inventory
        final Recipe recipe = new Recipe();
        recipe.setName( "Iced Coffee" );
        recipe.addIngredient( new Ingredient( "Chocolate", 2 ) );
        recipe.addIngredient( new Ingredient( "Milk", 12 ) );
        recipe.addIngredient( new Ingredient( "Sugar", 4 ) );
        recipe.addIngredient( new Ingredient( "Coffee", 3 ) );
        recipe.setPrice( 7 );

        i.useIngredients( recipe );

        // Check that new inventory string is updated
        // Test initial values in inventory as string representation
        Assertions.assertTrue( i.toString().contains( "Coffee: 497\n" ) );
        Assertions.assertTrue( i.toString().contains( "Milk: 488\n" ) );
        Assertions.assertTrue( i.toString().contains( "Sugar: 496\n" ) );
        Assertions.assertTrue( i.toString().contains( "Chocolate: 498\n" ) );

    }

    /**
     * Test adding new ingredient to inventory
     */
    @Test
    @Transactional
    public void testAddArbitraryIngredient () {
        final Inventory i = inventoryService.getInventory();
        i.putIngredient( "Caramel", 9 );
        Assertions.assertEquals( 9, i.getAmount( "Caramel" ) );
    }

    /**
     * Test getting amount of ingredient that is not in inventory
     */
    @Test
    @Transactional
    public void testGetAmount () {
        final Inventory i = inventoryService.getInventory();
        final Integer amt = i.getAmount( "NotInInventory" );
        Assertions.assertEquals( -1, amt );
    }

    /**
     * Test creating new Inventory with invalid amount
     */
    @Test
    @Transactional
    public void testInvalidConstruct () {
        try {
            final Inventory i = new Inventory( -3, 0, 2, 2 );
            Assertions.assertNull( i );
        }
        catch ( final Exception e ) {
            Assertions.assertTrue( e instanceof IllegalArgumentException );
            Assertions.assertEquals( e.getMessage(), "Amount cannot be negative" );
        }

    }

}
