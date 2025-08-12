package com.ufund.api.ufundapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * Represents a need in the cupboard
 * 
 * @author Danny Catorcini, Matt Spring
 */
public class User {

    // Package private for tests
    static final String STRING_FORMAT = "User [id=%d, name=%s, needList=%s, quantity=%s, spent=%.2f]";

    @JsonProperty("id")
    private int id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("needList")
    private ArrayList<Integer> needList = new ArrayList<>();
    @JsonProperty("quantity")
    private ArrayList<Integer> quantity = new ArrayList<>();
    @JsonProperty("spent")
    private double spent;

    /**
     * Create a hero with the given id and type
     * 
     * @param id       the id of the current user
     * @param name     The name of the current user
     * @param needList The needs they have in their basket
     * @param quantity How many of each need
     * @param spent    how much money they have spent
     * 
     *                 {@literal @}JsonProperty is used in serialization and
     *                 deserialization
     *                 of the JSON object to the Java object in mapping the fields.
     *                 If a field
     *                 is not provided in the JSON object, the Java field gets the
     *                 default Java
     *                 value, i.e. 0 for int
     */
    public User(@JsonProperty("id") int id, @JsonProperty("name") String name, @JsonProperty("needList") ArrayList<Integer> needList,
        @JsonProperty("quantity") ArrayList<Integer> quantity, @JsonProperty("spent") double spent) {
        this.id = id;
        this.name = name;
        this.needList = needList;
        this.quantity = quantity;
        this.spent = spent;
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
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieves the type of need
     * 
     * @return The type of need
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the cost of a need - necessary for JSON object to Java object
     * deserialization
     * 
     * @return The cost of a need
     */
    public void resetSpent() {
        this.spent = 0;
    }

     /**
     * Sets the cost of a need - necessary for JSON object to Java object
     * deserialization
     * 
     * @param cost The cost of a need
     */
    public void addSpent(double spent) {
        this.spent += spent;
    }

    /**
     * Retrieves the cost of a need
     * 
     * @return The cost of a need
     */
    public double getSpent() {
        return spent;
    }

    public ArrayList<Integer> getNeedList() {
        return needList;
    }

    public void addNeed(Need need) {
        needList.add(need.getId());
    }

    public void removeNeed(Need need) {
        needList.remove(Integer.valueOf(need.getId()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        String needs = arrListToString(needList);
        String quant = arrListToString(quantity);
        return String.format(STRING_FORMAT, id, name, needs, quant, spent);
    }

    private String arrListToString(ArrayList<Integer> arrL) {
        String string = "[";
        for (int i : arrL) {
            if (!string.equals("[")) {
                string += ",";
            }
            string += i;
        }
        return string + "]";
    }
}