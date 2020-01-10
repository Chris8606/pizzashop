package ch.ti8m.azubi.bch.pizzashop.service;

import ch.ti8m.azubi.bch.pizzashop.dto.Order;

import java.util.List;

public interface OrderService {

    List<Order> list() throws Exception;

    Order get(int order_ID) throws Exception;

    Order create(Order order) throws Exception;

    void update(Order order) throws Exception;

    void remove(int order_ID) throws Exception;

}
