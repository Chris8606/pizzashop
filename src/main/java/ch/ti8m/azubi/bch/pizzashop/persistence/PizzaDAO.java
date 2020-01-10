package ch.ti8m.azubi.bch.pizzashop.persistence;

import ch.ti8m.azubi.bch.pizzashop.dto.Pizza;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PizzaDAO {

    private Connection connection;

    public PizzaDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Return a list of all pizzas.
     */
    public List<Pizza> list() throws Exception {
        List<Pizza> pizzaList = new ArrayList<>();

        // select all
        PreparedStatement statement = connection.prepareStatement("select * from pizza");
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            pizzaList.add(new Pizza(resultSet.getInt("pizza_id"), resultSet.getString("name"), resultSet.getDouble("pizzaprice")));
        }
        return pizzaList;
    }


    /**
     * Get the pizza with the given id. If no such pizza is found, null is returned.
     */
    public Pizza getPizzaByID(int pizzaID) throws Exception {
        PreparedStatement statement = connection.prepareStatement("select * from pizza");
        ResultSet resultSet = statement.executeQuery();
        Pizza pizza;
        while (resultSet.next()) {
            if (pizzaID == resultSet.getInt("pizza_ID")) {
                pizza = new Pizza(pizzaID, resultSet.getString("name"), resultSet.getDouble("pizzaprice"));
                return pizza;
            }
        }
        return null;
    }

    /**
     * Create a new pizza.
     */
    public Pizza create(Pizza pizza) throws Exception {
        validatePizza(pizza);
        PreparedStatement preparedStatement = connection.prepareStatement("insert into pizza (name, pizzaprice) values (?,?)", Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, pizza.getName());
        preparedStatement.setDouble(2, pizza.getPrice());


        preparedStatement.executeUpdate();
        ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
        generatedKeys.next();
        int pizza_ID = generatedKeys.getInt(1);
        pizza.setId(pizza_ID);
        return (getPizzaByID(pizza_ID));

    }

    /**
     * Update a pizza
     */
    public void update(Pizza pizza) throws Exception {
        doesPizzaExist(pizza);
        PreparedStatement preparedStatement = connection.prepareStatement("update pizza set name = (?), pizzaprice = (?) where pizza_ID =" + pizza.getId());
        preparedStatement.setString(1, pizza.getName());
        preparedStatement.setDouble(2, pizza.getPrice());


        preparedStatement.executeUpdate();


    }


    public void save(Pizza pizza) throws Exception {
        if (pizza.getId() == null) {
            create(pizza);
        } else {
            update(pizza);
        }
    }

    public Pizza findPizza(int pizza_ID) throws Exception {
        PreparedStatement statement = connection.prepareStatement("select * from pizza where pizza_ID=" + pizza_ID);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return getPizzaByID(pizza_ID);
        }
        return null;

    }

    /**
     * Delete a pizza by id.
     */
    public boolean delete(int pizza_ID) throws Exception {
        String sql = String.format("delete from pizza where pizza_ID=" + pizza_ID);
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            int numberOfDeletedRecords = preparedStatement.executeUpdate();
            if (numberOfDeletedRecords > 0) {
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean doesPizzaExist(Pizza pizza) throws Exception {
        if (pizza.getId() == null) {
            return false;
        }
        PreparedStatement preparedStatement = connection.prepareStatement("select * from pizza where pizza_ID = " + pizza.getId());
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return true;
        }
        throw new IllegalArgumentException("Pizza doesnt exist");
    }

    public void validatePizza(Pizza pizza) {

        if (pizza.getName() == null) {
            throw new IllegalArgumentException("Name doesnt exist");
        }
        if (pizza.getPrice() == 0) {
            throw new IllegalArgumentException("Price has no price");
        }
        if (pizza.getPrice() < 0) {
            throw new IllegalArgumentException("Price is negative");
        }
    }
}

