package ch.ti8m.azubi.bch.pizzashop;

import ch.ti8m.azubi.bch.pizzashop.dto.Order;
import ch.ti8m.azubi.bch.pizzashop.dto.Pizza;
import ch.ti8m.azubi.bch.pizzashop.dto.PizzaOrder;
import ch.ti8m.azubi.bch.pizzashop.persistence.OrderDAO;
import ch.ti8m.azubi.bch.pizzashop.persistence.PizzaDAO;
import ch.ti8m.azubi.bch.pizzashop.service.ObjectMapperFactory;

import java.sql.*;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws Exception {
        PizzaDAO pizzaDAO = new PizzaDAO(createDBConnection());
        OrderDAO orderDAO = new OrderDAO(createDBConnection());

        Order order = new Order();
        order.setID(1);
        order.setAddress("Badenerstrasse 670");
        order.setPhonenumber("077 843 28 43");
        order.setPizzaOrders(Arrays.asList(new PizzaOrder(1, pizzaDAO.getPizzaByID(1))));
        order.setDate(order.getDate());
        //order.setCalculatedTotalPrice();
        //order.calculatedVAT();

        String json = ObjectMapperFactory.objectMapper().writeValueAsString(orderDAO.getOrderByID(1));

        System.out.println(json);

        Order restoredMovie = ObjectMapperFactory.objectMapper().readValue(json, Order.class);



       /* OrderDAO orderDAO = new OrderDAO(createDBConnection());
        Main main = new Main();
        main.setup();


        orderDAO.create(new Order(4,"Badenerstrasse 709", "078 483 94 29", Arrays.asList(new PizzaOrder(2, pizzaDAO.getPizzaByID(1)))));
        System.out.println(orderDAO.list());
*/
    }

    private static Connection createDBConnection() throws SQLException {
        return createDBConnection("localhost", 3306, "realpizzashop", "root", null);
    }

    private static Connection createDBConnection(String host, int port, String dbName, String user, String password) throws SQLException {

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL JDBC Driver not found");
        }
        String connectionURL = String.format("jdbc:mysql://%s:%d/%s", host, port, dbName);
        return DriverManager.getConnection(connectionURL, user, password);
    }

    public void setup() throws Exception {

        // drop/create test database
        Connection connection = createDBConnection();
        PizzaDAO pizzaDAO = new PizzaDAO(connection);

        //drop table if exists

        PreparedStatement preparedStatement2 = connection.prepareStatement("drop table if exists pizzaorder");
        preparedStatement2.executeUpdate();

        PreparedStatement preparedStatement = connection.prepareStatement("drop table if exists pizza");
        preparedStatement.executeUpdate();

        PreparedStatement preparedStatement1 = connection.prepareStatement("drop table if exists order1");
        preparedStatement1.executeUpdate();


        //table pizza
        Statement statement2 = connection.createStatement();
        statement2.execute("create table pizza(\n" +
                "pizza_ID int primary key auto_increment not null,\n" +
                "name VARCHAR(64) not null,\n" +
                "pizzaprice double not null\n" +
                ");"
        );

        //table order
        Statement statement3 = connection.createStatement();
        statement3.execute("create table order1(\n" +
                " order_ID int primary key auto_increment not null,\n" +
                " date datetime not null,\n" +
                " adress VARCHAR(255) not null,\n" +
                " phonenumber VARCHAR(255) not null,\n" +
                " totalprice double not null\n" +
                ");"
        );

        Statement statement4 = connection.createStatement();
        statement4.execute("create table pizzaOrder(\n" +
                "orderIDFS INT not null,\n" +
                "pizzaIDFS INT not null,\n" +
                "amount INT not null,\n" +
                "foreign key(orderIDFS) references order1(order_ID),\n" +
                "foreign key(pizzaIDFS) references pizza(pizza_ID)\n" +
                ");"
        );

        Pizza pizza = new Pizza("Margherita", 20);
        pizzaDAO.save(pizza);

        Pizza pizza1 = new Pizza("Pesto", 21);
        pizzaDAO.save(pizza1);

        Pizza pizza2 = new Pizza("Napoli", 22);
        pizzaDAO.save(pizza2);

        Pizza pizza3 = new Pizza("Pizza Prosciutto", 22);
        pizzaDAO.save(pizza3);

        Pizza pizza4 = new Pizza("Pizza Quattro Stagioni", 25);
        pizzaDAO.save(pizza4);

        Pizza pizza5 = new Pizza("Pizza Hwaii", 19);
        pizzaDAO.save(pizza5);

        Pizza pizza6 = new Pizza("Pizza Funghi", 23);
        pizzaDAO.save(pizza6);

        Pizza pizza7 = new Pizza("Pizza Frutti di Mare", 21);
        pizzaDAO.save(pizza7);


    }
}

