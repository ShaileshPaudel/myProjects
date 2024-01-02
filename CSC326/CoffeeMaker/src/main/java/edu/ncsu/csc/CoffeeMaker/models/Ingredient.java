package edu.ncsu.csc.CoffeeMaker.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Min;

/**
 * Class represents an Ingredient object, which contains a name and amount
 *
 * @author Sushil Sharma
 * @author Sydney Nguyen
 * @author Shailesh Paudel
 *
 */
@Entity
public class Ingredient extends DomainObject {

    /** Primary key for database */
    @Id
    @GeneratedValue
    private Long    id;

    /** Ingredient name */
    private String  name;

    /** Amount of the ingredient */
    @Min ( 0 )
    private Integer amount;

    /**
     * Creates Ingredient object with a name and amount
     *
     * @param name
     *            the name of ingredient
     * @param amount
     *            the initial amount
     */
    public Ingredient ( final String name, final Integer amount ) {

        this.name = name;
        this.amount = amount;
    }

    /**
     * Constructs Ingredient object with no parameters
     */
    public Ingredient () {
        id = null;
        name = null;
        amount = null;
    }

    /**
     * Returns the ingredient name
     *
     * @return name of ingredient
     */
    public String getName () {
        return this.name;
    }

    /**
     * Sets the name of ingredient
     *
     * @param name
     *            the name of the ingredient
     */
    public void setName ( final String name ) {
        this.name = name;
    }

    /**
     * Gets the amount of the ingredient
     *
     * @return amount of ingredient
     */
    public Integer getAmount () {
        return this.amount;
    }

    /**
     * Sets the initial amount of an ingredient
     *
     * @param amount
     *            amount of ingredient to set
     */
    public void setAmount ( final Integer amount ) {
        this.amount = amount;
    }

    /**
     * Sets the id of the ingredient
     *
     * @param id
     *            id to set
     */
    @SuppressWarnings ( "unused" )
    private void setId ( final Long id ) {
        this.id = id;
    }

    /**
     * Get the ID of the Ingredient
     *
     * @return the ID
     */
    @Override
    public Long getId () {
        return this.id;
    }

    /**
     * A String representation of the Ingredient object.
     */
    @Override
    public String toString () {
        return "Ingredient Name - " + this.name + ", Amount = " + this.amount;
    }

    /**
     * Generates a hashCode for an Ingredient object
     *
     * @return hash code for Ingredient object
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
        final Ingredient other = (Ingredient) obj;
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

}
