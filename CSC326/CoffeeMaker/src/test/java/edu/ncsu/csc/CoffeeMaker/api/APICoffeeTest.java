package edu.ncsu.csc.CoffeeMaker.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

import edu.ncsu.csc.CoffeeMaker.common.TestUtils;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

/**
 * Tests coffee API operations
 *
 * @author Sushil Sharma
 * @author Sydney Nguyen
 * @author Shailesh Paudel
 *
 */
@ExtendWith ( SpringExtension.class )
@SpringBootTest
@AutoConfigureMockMvc
public class APICoffeeTest {

    /**
     * model view controller to perform requests
     */
    @Autowired
    private MockMvc          mvc;

    /**
     * instance of recipe service
     */
    @Autowired
    private RecipeService    service;

    /**
     * instance of the inventory
     */
    @Autowired
    private InventoryService iService;

    /**
     * Sets up the tests.
     */
    @BeforeEach
    public void setup () {
        service.deleteAll();
        iService.deleteAll();

        final Inventory ivt = iService.getInventory();

        ivt.addIngredients( 15, 15, 15, 15 );

        iService.save( ivt );

        final Ingredient coffee = new Ingredient( "Coffee", 3 );
        final Ingredient milk = new Ingredient( "Milk", 1 );
        final Ingredient sugar = new Ingredient( "Sugar", 1 );
        final Ingredient chocolate = new Ingredient( "Chocolate", 0 );

        final Recipe recipe = new Recipe();
        recipe.setName( "Coffee" );
        recipe.setPrice( 50 );
        recipe.addIngredient( coffee );
        recipe.addIngredient( milk );
        recipe.addIngredient( sugar );
        recipe.addIngredient( chocolate );
        service.save( recipe );
    }

    /**
     * Test purchasing a beverage
     *
     * @throws Exception
     *             if coffee could not be made
     */
    @Test
    @Transactional
    public void testPurchaseBeverage1 () throws Exception {

        Assertions.assertEquals( 1, service.count() );
        final String name = "Coffee";

        mvc.perform( post( String.format( "/api/v1/makecoffee/%s", name ) ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( 60 ) ) ).andExpect( status().isOk() )
                .andExpect( jsonPath( "$.message" ).value( 10 ) );

        // Still 1 recipe based on the setup() method
        Assertions.assertEquals( 1, service.count() );

    }

    /**
     * Test purchasing beverage when money amount is not sufficient
     *
     * @throws Exception
     *             when not enough money is used
     */
    @Test
    @Transactional
    public void testPurchaseBeverage2 () throws Exception {
        /* Insufficient amount paid */

        Assertions.assertEquals( 1, service.count() );
        final String name = "Coffee";

        mvc.perform( post( String.format( "/api/v1/makecoffee/%s", name ) ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( 40 ) ) ).andExpect( status().is4xxClientError() )
                .andExpect( jsonPath( "$.message" ).value( "Not enough money paid" ) );

        // Still 1 recipe based on the setup() method
        Assertions.assertEquals( 1, service.count() );

    }

    /**
     * Test purchasing coffee when there is not enough inventory
     *
     * @throws Exception
     *             if there is not enough inventory
     */
    @Test
    @Transactional
    public void testPurchaseBeverage3 () throws Exception {
        /* Insufficient inventory */

        final Inventory ivt = iService.getInventory();
        ivt.putIngredient( "Coffee", 0 );
        iService.save( ivt );
        Assertions.assertEquals( 0, ivt.getAmount( "Coffee" ) );

        final String name = "Coffee";

        mvc.perform( post( String.format( "/api/v1/makecoffee/%s", name ) ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( 50 ) ) ).andExpect( status().is4xxClientError() )
                .andExpect( jsonPath( "$.message" ).value( "Not enough inventory" ) );

    }

}
