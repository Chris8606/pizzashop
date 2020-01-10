package ch.ti8m.azubi.bch.pizzashop.persistence;

import ch.ti8m.azubi.bch.pizzashop.dto.Order;
import ch.ti8m.azubi.bch.pizzashop.dto.PizzaOrder;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {
    private Connection connection;
    public int numberOfDeletedRecords;
    private PizzaDAO pizzaDAO;

    public OrderDAO(Connection connection) {
        this.connection = connection;
        pizzaDAO = new PizzaDAO(connection);
    }

    /**
     * Return a list of all orders.
     */
    public List<Order> list() throws Exception {
        List<Order> orderList = new ArrayList<>();

        // select all
        PreparedStatement statement = connection.prepareStatement("select * from order1");
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            orderList.add(new Order(resultSet.getInt("order_ID"), resultSet.getString("adress"), resultSet.getString("phonenumber"), getPizzaOrders(resultSet.getInt("order_ID"))));
        }
        return orderList;
    }


    /**
     * Get the oder with the given id. If no such order is found, null is returned.
     */
    public Order getOrderByID(int order_ID) throws Exception {
        PreparedStatement statement = connection.prepareStatement("select * from order1 where order_ID=" + order_ID);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            if (order_ID == resultSet.getInt("order_ID")) {
                return new Order(order_ID, resultSet.getString("adress"), resultSet.getString("phonenumber"), getPizzaOrders(order_ID));
            }
        }
        return null;
    }

    /**
     * Create a new order.
     */
    public Order create(Order order) throws Exception {
        validateOrder(order);
        PreparedStatement preparedStatement = connection.prepareStatement("insert into order1 (date, adress, phonenumber, totalprice) values (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setDate(1, order.getLocalDate());
        preparedStatement.setString(2, order.getAddress());
        preparedStatement.setString(3, order.getPhonenumber());
        preparedStatement.setDouble(4, order.getTotalprice());

        preparedStatement.executeUpdate();
        ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
        generatedKeys.next();
        int order_ID = generatedKeys.getInt(1);
        order.setID(order_ID);


        PreparedStatement preparedStatement1 = connection.prepareStatement("insert into pizzaOrder (orderIDFS, pizzaIDFS, amount) values (?,?,?)", Statement.RETURN_GENERATED_KEYS);
        List<PizzaOrder> pizzaOrders = order.getPizzaOrders();
        for (int i = 0; i < pizzaOrders.size(); i++) {
            preparedStatement1.setInt(1, order.getID());
            preparedStatement1.setInt(2, pizzaOrders.get(i).getPizza().getId());
            preparedStatement1.setInt(3, pizzaOrders.get(i).getAmount());
            preparedStatement1.executeUpdate();
        }

        return order;

    }

    /**
     * Update a order
     */
    public void update(Order order) throws Exception {
        validateOrder(order);
        checkOrderExists(order);

        PreparedStatement preparedStatement = connection.prepareStatement("update order1 set date = (?), adress = (?), phonenumber = (?), totalprice = (?) where order_ID=" + order.getID());
        preparedStatement.setDate(1, order.getLocalDate());
        preparedStatement.setString(2, order.getAddress());
        preparedStatement.setString(3, order.getPhonenumber());
        preparedStatement.setDouble(4, order.getTotalprice());

        preparedStatement.executeUpdate();
        PreparedStatement preparedStatement1 = connection.prepareStatement("delete pizzaOrder where orderIDFS=" + order.getID());
        preparedStatement1.executeUpdate();

        PreparedStatement preparedStatement2 = connection.prepareStatement("insert into pizzaOrder (orderIDFS, pizzaIDFS, amount) values (?,?,?)");
        List<PizzaOrder> pizzaOrders = order.getPizzaOrders();
        for (int i = 0; i < pizzaOrders.size(); i++) {
            preparedStatement2.setInt(1, order.getID());
            preparedStatement2.setInt(2, pizzaOrders.get(i).getPizza().getId());
            preparedStatement2.setInt(3, pizzaOrders.get(i).getAmount());
            preparedStatement2.executeUpdate();
        }
    }

    public void save(Order order) throws Exception {
        if (order.getID() == null) {
            create(order);
        } else {
            update(order);
        }
    }

    public Order findOrder(int order_ID) throws Exception {
        PreparedStatement statement = connection.prepareStatement("select * from order1 where order_ID=" + order_ID);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return getOrderByID(order_ID);
        }
        return null;

    }

    /**
     * Delete a order by id.
     */
    public boolean delete(int order_ID) throws Exception {


        PreparedStatement preparedStatement = connection.prepareStatement("delete from pizzaOrder where orderIDFS=" + order_ID);
        numberOfDeletedRecords = preparedStatement.executeUpdate();

        PreparedStatement preparedStatement2 = connection.prepareStatement("delete from order1 where order_ID=" + order_ID);
        int numberOfDeletedRecords2 = preparedStatement2.executeUpdate();

        if (numberOfDeletedRecords > 0 && numberOfDeletedRecords2 > 0) {
            return true;
        } else {
            return false;
        }
    }


    public void checkOrderExists(Order order) throws Exception {
        Integer id = order.getID();
        if (id == null || getOrderByID(id) == null) {
            throw new IllegalArgumentException("Order doesnt exist");
        }
    }

    public void validateOrder(Order order) {
        if (order.getLocalDate() == null) {
            throw new IllegalArgumentException("Date doesnt exist");
        }
        if (order.getAddress() == null) {
            throw new IllegalArgumentException("Adress doesnt exist");
        }
        if (order.getPhonenumber() == null) {
            throw new IllegalArgumentException("Phonenumber doesnt exist");
        }
        if (order.getTotalprice() == null) {
            throw new IllegalArgumentException("Totalprice doesnt exist");
        }
    }

    private static Connection createDBConnection() throws SQLException {
        return createDBConnection("localhost", 3306, "realpizzashop", "root", "");
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

    public List<PizzaOrder> getPizzaOrders(int orderID) throws Exception {
        PreparedStatement preparedStatement = connection.prepareStatement("select * from pizzaOrder where orderIDFS=" + orderID);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<PizzaOrder> pizzaOrders = new ArrayList<>();
        while (resultSet.next()) {
            pizzaOrders.add(new PizzaOrder(resultSet.getInt("amount"), pizzaDAO.getPizzaByID(resultSet.getInt("pizzaIDFS"))));
        }
        return pizzaOrders;
    }
}
