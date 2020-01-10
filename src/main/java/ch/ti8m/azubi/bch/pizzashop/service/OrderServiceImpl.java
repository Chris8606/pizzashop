package ch.ti8m.azubi.bch.pizzashop.service;

import ch.ti8m.azubi.bch.pizzashop.dto.Order;
import ch.ti8m.azubi.bch.pizzashop.persistence.OrderDAO;

import java.sql.Connection;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

public class OrderServiceImpl implements OrderService {

    private final Connection connection = ConnectionUtil.createDBConnection();
    private final OrderDAO orderDAO = new OrderDAO(connection);



    @Override
    public List<Order> list() throws Exception {
        List<Order> orderList = orderDAO.list();
        orderList.sort(Comparator.comparing(Order::getID));
        return orderList;
    }

    @Override
    public Order get(int order_ID) throws Exception {
        Order order = orderDAO.getOrderByID(order_ID);
        if (order == null) {
            throw new NoSuchElementException("No order with id " + order_ID + " exists");
        }
        return order;
    }

    @Override
    public Order create(Order order) throws Exception {
        return orderDAO.create(order);
    }

    @Override
    public void update(Order order) throws Exception {
        orderDAO.update(order);
    }

    @Override
    public void remove(int order_ID) throws Exception {
        orderDAO.delete(order_ID);
    }

}
