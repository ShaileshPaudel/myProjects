package edu.ncsu.csc.CoffeeMaker.models;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Inventory for the coffee maker. Inventory is tied to the database using
 * Hibernate libraries. See InventoryRepository and InventoryService for the
 * other two pieces used for database support.
 *
 * @author Kai Presler-Marshall
 * @author Sydney Nguyen
 */
@Entity
public class Inventory extends DomainObject {

    /** id for inventory entry */
    @Id
    @GeneratedValue
    private Long                     id;

    /**
     * Map used to store ingredients and their amounts in the inventory
     */
    private HashMap<String, Integer> ingredientMap;

    /**
     * Empty constructor for Hibernate
     */
    public Inventory () {
        // Intentionally empty so that Hibernate can instantiate
        // Inventory object.
    }

    /**
     * Use this to create inventory with specified amts for original
     * ingredients.
     *
     * @param coffee
     *            amt of coffee
     * @param milk
     *            amt of milk
     * @param sugar
     *            amt of sugar
     * @param chocolate
     *            amt of chocolate
     */
    public Inventory ( final Integer coffee, final Integer milk, final Integer sugar, final Integer chocolate ) {

        ingredientMap = new HashMap<String, Integer>();
        if ( coffee < 0 || milk < 0 || sugar < 0 || chocolate < 0 ) {
            throw new IllegalArgumentException( "Amount cannot be negative" );
        }

        ingredientMap.put( "Chocolate", chocolate );
        ingredientMap.put( "Coffee", coffee );
        ingredientMap.put( "Milk", milk );
        ingredientMap.put( "Sugar", sugar );

    }

    /**
     * Returns the ID of the entry in the DB
     *
     * @return long
     */
    @Override
    public Long getId () {
        return id;
    }

    /**
     * Set the ID of the Inventory (Used by Hibernate)
     *
     * @param id
     *            the ID
     */
    public void setId ( final Long id ) {
        this.id = id;
    }

    /**
     * Gets amount of an ingredient in the inventory
     *
     * @param ingredient
     *            ingredient amount to get
     * @return amount of that ingredient in the inventory, -1 if ingredient not
     *         in inventory
     */
    public Integer getAmount ( final String ingredient ) {
        if ( ingredientMap.containsKey( ingredient ) ) {
            return ingredientMap.get( ingredient );
        }
        return -1;
    }

    /**
     * Gets ingredient map for inventory
     *
     * @return map of inventory
     */
    public HashMap<String, Integer> getIngredientMap () {
        return ingredientMap;
    }

    /**
     * Adds ingredients to the inventory (works for the original 4 ingredients)
     *
     * @param coffee
     *            amt of coffee
     * @param milk
     *            amt of milk
     * @param sugar
     *            amt of sugar
     * @param chocolate
     *            amt of chocolate
     * @return true if successful, false if not
     */
    public boolean addIngredients ( final Integer coffee, final Integer milk, final Integer sugar,
            final Integer chocolate ) {
        if ( coffee < 0 || milk < 0 || sugar < 0 || chocolate < 0 ) {
            throw new IllegalArgumentException( "Amount cannot be negative" );
        }

        if ( ingredientMap.containsKey( "Chocolate" ) ) {
            final Integer origAmt = ingredientMap.get( "Chocolate" );
            ingredientMap.replace( "Chocolate", chocolate + origAmt );
        }
        else {
            ingredientMap.put( "Chocolate", chocolate );
        }

        if ( ingredientMap.containsKey( "Coffee" ) ) {
            final Integer origAmt = ingredientMap.get( "Coffee" );
            ingredientMap.replace( "Coffee", coffee + origAmt );
        }
        else {
            ingredientMap.put( "Coffee", coffee );
        }

        if ( ingredientMap.containsKey( "Milk" ) ) {
            final Integer origAmt = ingredientMap.get( "Milk" );
            ingredientMap.replace( "Milk", milk + origAmt );
        }
        else {
            ingredientMap.put( "Milk", milk );
        }

        if ( ingredientMap.containsKey( "Sugar" ) ) {
            final Integer origAmt = ingredientMap.get( "Sugar" );
            ingredientMap.replace( "Sugar", sugar + origAmt );
        }
        else {
            ingredientMap.put( "Sugar", sugar );
        }

        return true;
    }

    /**
     * Adds ingredient and its inventory to the map if not already in map
     * Updates amount if ingredient is already in the map
     *
     * @param ingredient
     *            ingredient name to add
     * @param amount
     *            initial amount in the inventory
     * @return amount of ingredient in inventory if valid
     */
    public Integer putIngredient ( final String ingredient, final Integer amount ) {
        if ( amount < 0 ) {
            throw new IllegalArgumentException( "Amount must be a positive integer" );
        }

        if ( ingredientMap.containsKey( ingredient ) ) {
            ingredientMap.replace( ingredient, amount );
        }
        else {
            ingredientMap.put( ingredient, amount );
        }
        return amount;

    }

    /**
     * Returns true if there are enough ingredients to make the beverage.
     *
     * @param r
     *            recipe to check recipe to check if there are enough
     *            ingredients
     * @return true if enough ingredients to make the beverage
     */
    public boolean enoughIngredients ( final Recipe r ) {
        boolean isEnough = true;
        final List<Ingredient> ingredients = r.getNames();

        for ( int i = 0; i < ingredients.size(); i++ ) {
            final Integer inventoryAmt = ingredientMap.get( ingredients.get( i ).getName() );
            if ( ingredients.get( i ).getAmount() > inventoryAmt ) {
                isEnough = false;
            }
        }
        return isEnough;
    }

    /**
     * Removes the ingredients used to make the specified recipe. Assumes that
     * the user has checked that there are enough ingredients to make
     *
     * @param r
     *            recipe to make
     * @return true if recipe is made.
     */
    public boolean useIngredients ( final Recipe r ) {
        if ( enoughIngredients( r ) ) {
            // List of all ingredients in the recipe
            final List<Ingredient> ingredients = r.getNames();

            for ( int i = 0; i < ingredients.size(); i++ ) {
                // The ingredient in the recipe to use
                final Ingredient recipeIngredient = ingredients.get( i );
                // The current amount of the ingredient in the inventory
                final Integer inventoryAmt = ingredientMap.get( recipeIngredient.getName() );

                // Update the amount in the inventory
                ingredientMap.replace( recipeIngredient.getName(), inventoryAmt - recipeIngredient.getAmount() );
            }
            return true;
        }
        else { // Not enough ingredients
            return false;
        }

    }

    /**
     * Returns a string describing the current contents of the inventory.
     *
     * @return String
     */
    @Override
    public String toString () {
        final StringBuffer buf = new StringBuffer();

        final Iterator<Map.Entry<String, Integer>> iterator = ingredientMap.entrySet().iterator();
        while ( iterator.hasNext() ) {
            final Map.Entry<String, Integer> entry = iterator.next();
            buf.append( entry.getKey() + ": " + entry.getValue() + "\n" );
        }
        return buf.toString();
    }

}
