package ch.ti8m.azubi.bch.pizzashop.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Pizza {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("price")
    private double pizzaprice;

    //Konstruktoren für (alles/alles ausser id/leer)
    public Pizza(int personID, String name, double pizzaprice) {
        this.id = personID;
        this.name = name;
        this.pizzaprice = pizzaprice;
    }

    public Pizza(String name, double pizzaprice) {
        this.name = name;
        this.pizzaprice = pizzaprice;
    }

    public Pizza() {
    }

    //getter und setter für id
    public Integer getId() {
        return id;
    }

    public void setId(Integer pizza_ID) {
        this.id = pizza_ID;
    }


    //getter und setter für pizzaname
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    //getter und setter für pizzaprice
    public double getPrice() {
        return pizzaprice;
    }

    public void setPrice(double pizzaprice) {
        this.pizzaprice = pizzaprice;
    }


    //überschriebene toString Methode für die Ausgabe
    @Override
    public String toString() {
        return "#" + id + ": " + name + " " + pizzaprice + " CHF";
    }
}
