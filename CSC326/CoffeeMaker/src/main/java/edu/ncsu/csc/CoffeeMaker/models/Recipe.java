package edu.ncsu.csc.CoffeeMaker.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Min;

/**
 * Recipe for the coffee maker. Recipe is tied to the database using Hibernate
 * libraries. See RecipeRepository and RecipeService for the other two pieces
 * used for database support.
 *
 * @author Kai Presler-Marshall
 */
@Entity
public class Recipe extends DomainObject {

    /** Recipe id */
    @Id
    @GeneratedValue
    private Long                   id;

    /** Recipe name */
    private String                 name;

    /** Recipe price */
    @Min ( 0 )
    private Integer                price;

    /** Ingredients the recipe may have */
    @OneToMany ( cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    private final List<Ingredient> ingredients;

    /**
     * Creates a default recipe for the coffee maker.
     */
    public Recipe () {
        this.name = "";
        this.ingredients = new ArrayList<Ingredient>();
        this.price = 0;
    }

    /**
     * Adds ingredient to the recipe
     *
     * @param ingredient
     *            ingredient to add to the recipe
     */
    public void addIngredient ( final Ingredient ingredient ) {
        ingredients.add( ingredient );
    }

    /**
     * Gets list of ingredients in the recipe
     *
     * @return list of ingredients
     */
    public List<Ingredient> getNames () {
        return ingredients;
    }

    /**
     * Get the ID of the Recipe
     *
     * @return the ID
     */
    @Override
    public Long getId () {
        return id;
    }

    /**
     * Set the ID of the Recipe (Used by Hibernate)
     *
     * @param id
     *            the ID
     */
    @SuppressWarnings ( "unused" )
    private void setId ( final Long id ) {
        this.id = id;
    }

    /**
     * Returns name of the recipe.
     *
     * @return Returns the name.
     */
    public String getName () {
        return name;
    }

    /**
     * Sets the recipe name.
     *
     * @param name
     *            The name to set.
     */
    public void setName ( final String name ) {
        this.name = name;
    }

    /**
     * Returns the price of the recipe.
     *
     * @return Returns the price.
     */
    public Integer getPrice () {
        return price;
    }

    /**
     * Sets the recipe price.
     *
     * @param price
     *            The price to set.
     */
    public void setPrice ( final Integer price ) {
        this.price = price;
    }

    /**
     * toString representation of Recipe that prints out all the ingredients.
     *
     * @return String
     */
    @Override
    public String toString () {
        return "Recipe Name: " + this.name + " / Recipe Price: " + this.price + " / " + ingredients.toString();
    }

    /**
     * Generates a hashCode for a Recipe object
     *
     * @return hash code for Recipe object
     */
    @Override
    public int hashCode () {
        final int prime = 31;
        Integer result = 1;
        result = prime * result + ( ( name == null ) ? 0 : name.hashCode() );
        return result;
    }

    /**
     * Compares a given object to this object for equality.
     *
     * @param obj
     *            the Object to compare with
     * @return true if the objects are the same
     */
    @Override
    public boolean equals ( final Object obj ) {
        if ( this == obj ) {
            return true;
        }
        if ( obj == null ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        final Recipe other = (Recipe) obj;
        if ( name == null ) {
            if ( other.name != null ) {
                return false;
            }
        }
        else if ( !name.equals( other.name ) ) {
            return false;
        }
        return true;
    }

    /**
     * Checks that the recipe is valid Checks that recipe has at least one
     * ingredient with amount of at least 1
     *
     * @return true if recipe is valid, false otherwise
     */
    public boolean checkRecipe () {
        boolean valid = false;
        for ( int i = 0; i < ingredients.size(); i++ ) { // Go thru all
                                                         // ingredients in
                                                         // recipe
            if ( ingredients.get( i ).getAmount() > 0 ) { // Check if at least
                                                          // one ingredient has
                                                          // an amount of 1 or
                                                          // more
                valid = true;
            }
        }
        return valid;
    }

}
