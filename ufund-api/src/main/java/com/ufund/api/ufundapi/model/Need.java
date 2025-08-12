package com.ufund.api.ufundapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a need in the cupboard
 * 
 * @author Brock Weiler
 */
public class Need {

    // Package private for tests
    static final String STRING_FORMAT = "Need [id=%d, type=%s, cost=%.2f, quantity=%d, category=%s]";

    @JsonProperty("id")
    private int id;
    @JsonProperty("type")
    private String type;
    @JsonProperty("cost")
    private double cost;
    @JsonProperty("quantity")
    private int quantity;
    @JsonProperty("category")
    private Category category;

    /**
     * Create a hero with the given id and type
     * 
     * @param id       the id of the current need
     * @param type     The type of the need
     * @param cost     The amount the need costs, in dollars
     * @param quantity how many for the need
     * @param category The category of food
     * 
     *                 {@literal @}JsonProperty is used in serialization and
     *                 deserialization
     *                 of the JSON object to the Java object in mapping the fields.
     *                 If a field
     *                 is not provided in the JSON object, the Java field gets the
     *                 default Java
     *                 value, i.e. 0 for int
     */
    public Need(@JsonProperty("id") int id, @JsonProperty("type") String type, @JsonProperty("cost") double cost,
            @JsonProperty("quantity") int quantity, @JsonProperty("category") Category category) {
        this.id = id;
        this.type = type;
        this.cost = cost;
        this.quantity = quantity;
        this.category = category;
    }

    /**
     * Retrieves the id of the need
     * 
     * @return The id of the need
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the type of need - necessary for JSON object to Java object
     * deserialization
     * 
     * @param type The type of need
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Retrieves the type of need
     * 
     * @return The type of need
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the cost of a need - necessary for JSON object to Java object
     * deserialization
     * 
     * @param cost The cost of a need
     */
    public void setCost(double cost) {
        this.cost = cost;
    }

    /**
     * Retrieves the cost of a need
     * 
     * @return The cost of a need
     */
    public double getCost() {
        return cost;
    }

    /**
     * Sets the quantity of a need - necessary for JSON object to Java object
     * deserialization
     * 
     * @param cost The quantity of a need
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Retrieves the quantity of a need
     * 
     * @return The quantity of a need
     */
    public int getQuantity() {
        return quantity;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format(STRING_FORMAT, id, type, cost, quantity, category);
    }

    public enum Category {
        PRODUCE,
        PERISHABLES,
        NON_PERISHABLES,
        SEMI_PERISHABLES,
        BEVERAGES;

        @Override
        public String toString() {
            switch (this) {
                case NON_PERISHABLES:
                    return "Non-Perishables";
                default:
                    // Capitalize the first letter and make the rest lowercase for user-friendly
                    return this.name().charAt(0) + this.name().substring(1).toLowerCase();
            }
        }
    }
}
