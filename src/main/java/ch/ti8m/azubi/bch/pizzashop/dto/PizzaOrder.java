package ch.ti8m.azubi.bch.pizzashop.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PizzaOrder {

    @JsonProperty("amount")
    private int amount;
    @JsonProperty("pizza")
    private Pizza pizza;

    public PizzaOrder(int amount, Pizza pizza) {
        this.pizza = pizza;
        this.amount = amount;
        price();
    }

    public PizzaOrder() {
    }

    //getter und setter für pizza
    public Pizza getPizza() {
        return pizza;
    }

    public void setPizza(Pizza pizza) {
        this.pizza = pizza;
    }

    //getter und setter für amount
    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }


    //setter für price
    public Double price() {
        return amount * pizza.getPrice();
    }


    //überschriebene toString Methode für die Ausgabe
    @Override
    public String toString() {
        return "Pizza: " + amount + " * " + pizza;
    }
}
