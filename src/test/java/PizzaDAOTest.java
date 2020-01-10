import ch.ti8m.azubi.bch.pizzashop.ConnectionFactory;
import ch.ti8m.azubi.bch.pizzashop.dto.Pizza;
import ch.ti8m.azubi.bch.pizzashop.persistence.PizzaDAO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class PizzaDAOTest {
    ConnectionFactory connection = new ConnectionFactory();
    PizzaDAO pizzaDAO = new PizzaDAO(connection.testConnection());

    public PizzaDAOTest() throws SQLException {
    }

    //Überprüfe ob die Liste nicht leer ist
    @Test
    public void PizzalistNotEmpty() throws Exception {
        Assert.assertTrue(!pizzaDAO.list().isEmpty());
    }

    //Vergleiche die bestehende Liste mit der gegebenen Liste
    @Test
    public void listPizzasTest() throws Exception {
        Assert.assertEquals("[#1: Margherita 20.0 CHF, #2: Pesto 21.0 CHF, #3: Napoli 22.0 CHF]", pizzaDAO.list().toString());
    }

    //Erstelle eine neue Pizza und überprüfe ob die Pizza mit der ID 4 auch identisch ist mit der kreierten
    @Test
    public void creatingPizzaTest() throws Exception {
        pizzaDAO.create(new Pizza("Hwaii", 19));
        Assert.assertEquals("#4: Hwaii 19.0 CHF", pizzaDAO.getPizzaByID(4).toString());
    }

    //Erstelle eine neue Pizza und update diese mit einer neuen Pizza
    @Test
    public void updatingPizzaTest() throws Exception {
        System.out.println(pizzaDAO.getPizzaByID(1));
        pizzaDAO.update(new Pizza(1, "Banane", 18.50));
        System.out.println(pizzaDAO.getPizzaByID(1));
        Assert.assertEquals("#1: Banane 18.5 CHF", pizzaDAO.getPizzaByID(1).toString());
    }


    //Update eine Pizza, die nicht existiert
    @Test(expected = IllegalArgumentException.class)
    public void updatingPizzaThatDoesntExists() throws Exception {
        pizzaDAO.update(new Pizza(4, "Banane", 18.50));
        Assert.assertEquals("#1: Banane 18.5 CHF", pizzaDAO.getPizzaByID(4).toString());
    }


    //Erstelle eine neue Pizza und delete diese anschliessend
    @Test
    public void deletePizzaTest() throws Exception {
        pizzaDAO.create(new Pizza(4, "Funghi", 30));
        Assert.assertTrue(pizzaDAO.delete(4));
    }

    //Delete eine Pizza die nicht existiert
    @Test
    public void deletePizzaThatDoesntExists() throws Exception {
        Assert.assertFalse(pizzaDAO.delete(10));
    }


    //Save eine Pizza ohne ID Eingabe, diese sollte dann erstellt werden (create), nun save ich eine neue Pizza mit der gleichen ID(diese Pizza wird nun geupdated)
    //Zum Schluss sollte die geuptdatete Pizza verglichen werden
    @Test
    public void safePizzaTest() throws Exception {
        pizzaDAO.save(new Pizza("Margherita", 20));
        System.out.println(pizzaDAO.getPizzaByID(4));
        pizzaDAO.save(new Pizza(4, "Funghi", 23));
        System.out.println(pizzaDAO.getPizzaByID(4));
        Assert.assertEquals("#4: Funghi 23.0 CHF", pizzaDAO.getPizzaByID(4).toString());
    }

    //Ich Safe eine Pizza mit ID (diese Pizza existiert aber nicht), also soll eine Exception auftreten
    @Test(expected = IllegalArgumentException.class)
    public void safePizzaWithID_ThatDoesntExist() throws Exception {
        pizzaDAO.save(new Pizza(4, "Hwaii", 24));
    }

    //Safe eine Pizza mit ID(diese Pizza soll bereits existieren), die Pizza wird geupdatet
    @Test
    public void safePizzaThatExist() throws Exception {
        pizzaDAO.create(new Pizza("Funghi", 23));
        System.out.println(pizzaDAO.getPizzaByID(4));
        pizzaDAO.save(new Pizza(4, "Hwaii", 24));
        System.out.println(pizzaDAO.getPizzaByID(4));
        Assert.assertEquals("#4: Hwaii 24.0 CHF", pizzaDAO.getPizzaByID(4).toString());
    }

    //Safe eine Pizza ohne ID, diese Pizza wird created
    @Test
    public void safePizzaWithNoID() throws Exception {
        pizzaDAO.save(new Pizza("Margherita", 23));
        Assert.assertEquals("#4: Margherita 23.0 CHF", pizzaDAO.getPizzaByID(4).toString());
    }

    //Eine Pizza suchen die nicht existiert
    @Test
    public void findPizzaThatDoesntExist() throws Exception {
        Assert.assertNull(pizzaDAO.findPizza(4));
    }

    //Eine Pizza suchen die existiert
    @Test
    public void findPizzaThatExists() throws Exception {
        Assert.assertNotNull((pizzaDAO.findPizza(1)));
    }

    //Erstelle eine Pizza mit keinem Inhalt und erwarte eine Exception
    @Test(expected = IllegalArgumentException.class)
    public void validateEmptyPizza() {
        pizzaDAO.validatePizza(new Pizza());
    }

    @Test(expected = IllegalArgumentException.class)
    public void validateNoPizzaPrice() {
        pizzaDAO.validatePizza(new Pizza("Margherita", 0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void validateNegativePizzaPrice() {
        pizzaDAO.validatePizza(new Pizza("Margherita", -5));
    }

    @Test(expected = IllegalArgumentException.class)
    public void validatePizzaWithEmptyName() {
        pizzaDAO.validatePizza(new Pizza(null, 0));
    }

    @Before
    public void setup() throws SQLException {


        // drop/create test database
        Connection connection = ConnectionFactory.testConnection();

        Statement statement = connection.createStatement();
        statement.execute("drop table if exists order1");


        Statement statement2 = connection.createStatement();
        statement2.execute("create table order1(\n" +
                "order_ID int not null primary key auto_increment,\n" +
                "date datetime not null,\n" +
                "adress VARCHAR(64) not null,\n" +
                "phonenumber VARCHAR(64) not null,\n" +
                "totalprice double not null,\n" +
                ");"
        );

        Statement statement3 = connection.createStatement();
        statement3.execute("insert into order1 (date, adress, phonenumber, totalprice) values \n" +
                "( '2019-07-01', 'Badenerstrasse 709', '077 473 92 20', 0), \n" +
                "( '2019-07-02', 'Buckhauserstrasse 24','078 392 29 95', 0), \n" +
                "( '2019-07-03', 'Tüffenwies 33','079 392 54 29', 0)\n"
        );
        Statement statement4 = connection.createStatement();
        statement4.execute("drop table if exists pizza");


        Statement statement5 = connection.createStatement();
        statement5.execute("create table pizza (\n" +
                "pizza_ID int primary key auto_increment not null,\n" +
                "name VARCHAR(64) not null,\n" +
                "pizzaprice double not null\n" +
                ");"
        );

        Statement statement6 = connection.createStatement();
        statement6.execute("insert into pizza (name, pizzaprice) values \n" +
                "('Margherita', '20'), \n" +
                "('Pesto', '21'), \n" +
                "('Napoli', '22')\n" +
                ";"
        );
    }
}
