package ch.ti8m.azubi.bch.pizzashop.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

public class Order {

    @JsonProperty("id")
    private Integer ID;
    @JsonProperty("date")
    private Date date;
    @JsonProperty("address")
    private String address;
    @JsonProperty("phone")
    private String phonenumber;
    @JsonProperty("total")
    private Double totalprice;
    @JsonProperty("pizzaOrders")
    private List<PizzaOrder> pizzaOrder;


    public Order() {
    }

    //Konstruktoren  mit ID & pizza Orders

    public Order(int ID, String address, String phonenumber, List<PizzaOrder> pizzaOrders) {
        this.ID = ID;
        this.date = getLocalDate();
        this.address = address;
        this.phonenumber = phonenumber;
        this.pizzaOrder = pizzaOrders;
        setCalculatedTotalPrice();
        calculatedVAT();
    }

    //Konstruktoren  ohne ID & mit pizza Orders
    public Order(String address, String phonenumber, List<PizzaOrder> pizzaOrders) {
        this.date = getLocalDate();
        this.address = address;
        this.phonenumber = phonenumber;
        this.pizzaOrder = pizzaOrders;
        setCalculatedTotalPrice();
        calculatedVAT();
    }

    //getter und setter für PizzaOrder
    public List<PizzaOrder> getPizzaOrders() {
        return pizzaOrder;
    }

    public void setPizzaOrders(List<PizzaOrder> pizzaOrder) {
        this.pizzaOrder = pizzaOrder;
    }

    //getter und setter für ID
    public Integer getID() {
        return ID;
    }

    public void setID(Integer order_ID) {
        this.ID = order_ID;
    }

    //getter und setter für address
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    //getter und setter für phonenumber
    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    //getter und setter für totalprice
    public Double getTotalprice() {
        return totalprice;
    }

    public void setCalculatedTotalPrice(){
        double totalPrice = 0;
        for (int i = 0; i < pizzaOrder.size(); i++){
            totalPrice += pizzaOrder.get(i).getAmount() * pizzaOrder.get(i).getPizza().getPrice();
        }
        this.totalprice = totalPrice;
    }

    @JsonIgnore
    public double calculatedVAT(){
        return (totalprice/100)*7.7;
    }

    @JsonIgnore
    public Date getLocalDate(){
        return  Date.valueOf(LocalDateTime.now().toLocalDate().toString());
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    //überschriebene toString Methode für die Ausgabe
    @Override
    public String toString() {
        return "#" + ID + ": " + date + " " + address + ", " + phonenumber + ", " + pizzaOrder + ", Total: " + totalprice + " CHF";
    }
}
