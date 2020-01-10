import ch.ti8m.azubi.bch.pizzashop.ConnectionFactory;
import ch.ti8m.azubi.bch.pizzashop.dto.Order;
import ch.ti8m.azubi.bch.pizzashop.dto.Pizza;
import ch.ti8m.azubi.bch.pizzashop.dto.PizzaOrder;
import ch.ti8m.azubi.bch.pizzashop.persistence.OrderDAO;
import ch.ti8m.azubi.bch.pizzashop.persistence.PizzaDAO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class OrderDAOTest {
    ConnectionFactory connection = new ConnectionFactory();
    OrderDAO orderDAO = new OrderDAO(connection.testConnection());
    PizzaDAO pizzaDAO = new PizzaDAO(connection.testConnection());

    public OrderDAOTest() throws SQLException {
    }

    //Überprüfe ob die Liste nicht leer ist
    @Test
    public void OrderlistNotEmpty() throws Exception {
        Assert.assertTrue(!orderDAO.list().isEmpty());
    }

    //Vergleiche die bestehende Liste mit der gegebenen Liste
    @Test
    public void listOrderTest() throws Exception {
        Assert.assertEquals("[" +
                "#1: " + getDate() + " Badenerstrasse 709, 077 473 92 20, [Pizza: 3 * #2: Pesto 21.0 CHF], Total: 63.0 CHF, " +
                "#2: " + getDate() + " Buckhauserstrasse 24, 078 392 29 95, [Pizza: 3 * #1: Margherita 20.0 CHF, Pizza: 3 * #3: Napoli 22.0 CHF], Total: 126.0 CHF, " +
                "#3: " + getDate() + " Tüffenwies 33, 079 392 54 29, [Pizza: 5 * #2: Pesto 21.0 CHF], Total: 105.0 CHF]", orderDAO.list().toString());
    }

    //Erstelle eine neue Order und überprüfe ob die Order mit der ID 4 auch identisch ist mit der kreierten
    @Test
    public void creatingOrderTest() throws Exception {
        Order order = new Order("Mühlbachstrasse 78", "078 493 295 38 59", Arrays.asList(new PizzaOrder(2, pizzaDAO.getPizzaByID(1))));
        orderDAO.create(order);
        Assert.assertEquals("#4: " + order.getLocalDate() + " Mühlbachstrasse 78, 078 493 295 38 59, [Pizza: 2 * #1: Margherita 20.0 CHF], Total: 40.0 CHF", orderDAO.getOrderByID(4).toString());
    }

    //Erstelle eine neue Order und update diese mit einer neuen Order
    @Test
    public void updatingOrderTest() throws Exception {
        System.out.println(orderDAO.getOrderByID(1));
        orderDAO.update(new Order(1, "Bödenerstrasse 7009", "077 453 53 28", Arrays.asList(new PizzaOrder(2, pizzaDAO.getPizzaByID(1)))));
        Assert.assertEquals("#1: "+ getDate()+" Bödenerstrasse 7009, 077 453 53 28, [Pizza: 2 * #1: Margherita 20.0 CHF], Total: 40.0 CHF", orderDAO.getOrderByID(1).toString());
    }

    // TODO: 04.07.2019  
    //Update eine Order, die nicht existiert
    @Test(expected = IllegalArgumentException.class)
    public void updatingOrderThatDoesntExists() throws Exception {
        orderDAO.update(new Order(4, "Bödenerstrasse 7009", "077 453 53 28", Arrays.asList(new PizzaOrder(2, pizzaDAO.getPizzaByID(1)))));
        System.out.println(orderDAO.getOrderByID(4));
        Assert.assertEquals("#1: 2123-12-23 Bödenerstrasse 7009, 077 453 53 28, Total: 50.0 CHF", orderDAO.getOrderByID(4).toString());
    }

    //Erstelle eine neue Order und delete diese anschliessend
    @Test
    public void deleteOrderTest() throws Exception {
        orderDAO.create(new Order("Mühlbachstrasse 78", "078 305 93 69", Arrays.asList(new PizzaOrder(2, pizzaDAO.getPizzaByID(1)))));
        System.out.println("Vorher: " + orderDAO.getOrderByID(4));
        Assert.assertTrue(orderDAO.delete(4));
        System.out.println("Nachher: " + orderDAO.getOrderByID(4));
    }


    //Delete eine Pizza die nicht existiert
    @Test
    public void deleteOrderThatDoesntExist() throws Exception {
        Assert.assertFalse(orderDAO.delete(10));
    }


    //Save eine Order ohne ID Eingabe (diese sollte dann erstellt werden (create)), nun save eine neue Order mit der gleichen ID(diese Order wird nun geupdated.
    //Zum Schluss sollte die geuptdatete Order verglichen werden
    @Test
    public void safeOrderTest() throws Exception {
        orderDAO.save(new Order("Mühlbachstrasse 78", "078 383 92 48", Arrays.asList(new PizzaOrder(2, pizzaDAO.getPizzaByID(1)))));
        System.out.println(orderDAO.getOrderByID(4));
        orderDAO.save(new Order(4, "Helloworldstrasse 80", "058 392 54 92", Arrays.asList(new PizzaOrder(3, pizzaDAO.getPizzaByID(1)))));
        System.out.println(orderDAO.getOrderByID(4));
        Assert.assertEquals("#4: "+getDate()+" Helloworldstrasse 80, 058 392 54 92, [Pizza: 3 * #1: Margherita 20.0 CHF], Total: 60.0 CHF", orderDAO.getOrderByID(4).toString());
    }

    //Safe eine Pizza mit einer ID(diese Pizza existiert aber nicht), also soll eine Exception auftreten
    @Test(expected = IllegalArgumentException.class)
    public void safeOrderWithID_ThatDoesntExist() throws Exception {
        orderDAO.save(new Order(4, "Mühlbachstrasse 78", "078 383 92 48", Arrays.asList(new PizzaOrder(2, pizzaDAO.getPizzaByID(1)))));
    }

    //Safe eine Order mit ID(diese Pizza soll bereits existieren), die Pizza wird geupdatet
    @Test
    public void safeOrderWithID() throws Exception {
        System.out.println(orderDAO.getOrderByID(1));
        orderDAO.save(new Order(1, "Helloworldstrasse 80", "058 392 54 92", Arrays.asList(new PizzaOrder(2, pizzaDAO.getPizzaByID(1)))));
        System.out.println(orderDAO.getOrderByID(1));
        Assert.assertEquals("#1: "+getDate()+" Helloworldstrasse 80, 058 392 54 92, [Pizza: 2 * #1: Margherita 20.0 CHF], Total: 40.0 CHF", orderDAO.getOrderByID(1).toString());
    }

    //Safe eine Order ohne ID, diese Order wird created
    @Test
    public void safeOrderWithNoID() throws Exception {
        orderDAO.save(new Order("Helloworldstrasse 80", "058 392 54 92", Arrays.asList(new PizzaOrder(2, pizzaDAO.getPizzaByID(1)))));
        Assert.assertEquals("#4: "+getDate()+" Helloworldstrasse 80, 058 392 54 92, [Pizza: 2 * #1: Margherita 20.0 CHF], Total: 40.0 CHF", orderDAO.getOrderByID(4).toString());
    }


    //Eine Pizza suchen die nicht existiert
    @Test
    public void findOrderThatDoesntExist() throws Exception {
        Assert.assertNull(orderDAO.findOrder(4));
    }

    //Eine Pizza suchen die nicht existiert
    @Test
    public void findOrderThatExists() throws Exception {
        Assert.assertNotNull((orderDAO.findOrder(1)));
    }

    //Erstelle eine Order ohne jeglichen Inhalt
    @Test
    public void checkOrderIsNotEmpty() throws Exception {
        try {
            orderDAO.checkOrderExists(new Order());
            Assert.fail("Expected validation exception");
        } catch (IllegalArgumentException ex) {
            Assert.assertEquals("Order doesnt exist", ex.getMessage());
        }
    }

    //Erstelle eine Order, aber ohne Price
    @Test
    public void validateOrderWithEmptyTotalprice() {
        try {
            Order order = new Order();
            order.setID(4);
            order.setAddress("Helloworldstrasse 80");
            order.setPhonenumber("058 392 54 92");
            System.out.println(order);

            orderDAO.validateOrder(order);

            Assert.fail("Expected validation exception");

        } catch (IllegalArgumentException ex) {
            Assert.assertEquals("Totalprice doesnt exist", ex.getMessage());
        }
    }

    //Erstelle eine Order, aber ohne Phonenumber
    @Test
    public void validateOrderWithEmptyPhonenumber() throws Exception {
        try {
            Order order = new Order();
            order.setID(4);
            order.getLocalDate();
            order.setAddress("Helloworldstrasse 80");
            order.setPizzaOrders(Arrays.asList(new PizzaOrder(2, pizzaDAO.getPizzaByID(1))));
            order.setCalculatedTotalPrice();
            System.out.println(order);

            orderDAO.validateOrder(order);

            Assert.fail("Expected validation exception");

        } catch (IllegalArgumentException ex) {
            Assert.assertEquals("Phonenumber doesnt exist", ex.getMessage());
        }
    }

    //Erstelle eine Order, aber ohne Adresse
    @Test
    public void validateOrderWithEmptyAdress() throws Exception {
        try {
            Order order = new Order();
            order.setID(4);
            order.getLocalDate();
            order.setPhonenumber("058 392 54 92");
            order.setPizzaOrders(Arrays.asList(new PizzaOrder(2, pizzaDAO.getPizzaByID(1))));
            order.setCalculatedTotalPrice();
            System.out.println(order);

            orderDAO.validateOrder(order);

            Assert.fail("Expected validation exception");

        } catch (IllegalArgumentException ex) {
            Assert.assertEquals("Adress doesnt exist", ex.getMessage());
        }
    }

    //Erstelle eine Order & eine Pizza, erstelle eine PizzaOrders List und füge dort pizzaSalami ein & amount 5
    @Test
    public void testCreateOrderWithPizzas() throws Exception {

        Order order = new Order();
        order.setAddress("Helloworldstrasse 80");
        order.setPhonenumber("058 392 54 92");
        order.getLocalDate();

        Pizza pizzaSalami = new Pizza("Salami", 19.50);
        pizzaDAO.save(pizzaSalami);

        List<PizzaOrder> pizzaOrders = new LinkedList<>();
        pizzaOrders.add(new PizzaOrder(5, pizzaSalami));
        order.setPizzaOrders(pizzaOrders);
        order.setCalculatedTotalPrice();
        orderDAO.save(order);

        Assert.assertNotNull(order.getID());

        // load order and check if orders are loaded

        Order loadedOrder = orderDAO.getOrderByID(order.getID());
        List<PizzaOrder> pizzaOrders2 = loadedOrder.getPizzaOrders();
        // check if pizzaOrders are not null, not empty, and contain our pizza orders (5x Salami).
        Assert.assertNotNull(pizzaOrders2);
        Assert.assertTrue(!pizzaOrders2.isEmpty());
        int amount = pizzaOrders2.get(0).getAmount();
        Pizza pizza = pizzaOrders2.get(0).getPizza();
        Assert.assertEquals("Pizza: " + amount + " * " + pizza, pizzaOrders2.get(0).toString());

        System.out.println(pizzaOrders2);
    }

    public Date getDate(){
        return  Date.valueOf(LocalDateTime.now().toLocalDate().toString());
    }

    @Before
    public void setup() throws SQLException {

        // drop/create test database
        Connection connection = ConnectionFactory.testConnection();


        Statement statement1 = connection.createStatement();
        statement1.execute("drop table if exists pizzaOrder");

        Statement statement = connection.createStatement();
        statement.execute("drop table if exists order1");

        Statement statement4 = connection.createStatement();
        statement4.execute("drop table if exists pizza");


        Statement statement2 = connection.createStatement();
        statement2.execute("create table order1(\n" +
                "order_ID int not null primary key auto_increment,\n" +
                "date datetime default current_timestamp not null,\n" +
                "adress VARCHAR(64) not null,\n" +
                "phonenumber VARCHAR(64) not null,\n" +
                "totalprice double not null\n" +
                ");"
        );

        Statement statement5 = connection.createStatement();
        statement5.execute("create table pizza (\n" +
                "pizza_ID int primary key auto_increment not null,\n" +
                "name VARCHAR(64) not null,\n" +
                "pizzaprice double not null\n" +
                ");"
        );
        Statement statement6 = connection.createStatement();
        statement6.execute("create table pizzaOrder(\n" +
                "orderIDFS INT not null,\n" +
                "pizzaIDFS INT not null,\n" +
                "amount INT not null,\n" +
                "foreign key(orderIDFS) references order1(order_ID),\n" +
                "foreign key(pizzaIDFS) references pizza(pizza_ID)\n" +
                ");"
        );

        Statement statement7 = connection.createStatement();
        statement7.execute("insert into pizza (name, pizzaprice) values \n" +
                "('Margherita', '20'), \n" +
                "('Pesto', '21'), \n" +
                "('Napoli', '22')\n" +
                ";"
        );

        Statement statement3 = connection.createStatement();
        statement3.execute("insert into order1 (adress, phonenumber, totalprice) values \n" +
                "('Badenerstrasse 709', '077 473 92 20', 0), \n" +
                "('Buckhauserstrasse 24','078 392 29 95', 0), \n" +
                "('Tüffenwies 33','079 392 54 29', 0)\n"
        );

        Statement statement8 = connection.createStatement();
        statement8.execute("insert into pizzaOrder(orderIDFS, pizzaIDFS, amount) values\n" +
                "('1', '2', '3'), \n" +
                "('2', '1', '3'), \n" +
                "('2', '3', '3'), \n" +
                "('3', '2', '5') \n" +
                ";"
        );
    }
}
