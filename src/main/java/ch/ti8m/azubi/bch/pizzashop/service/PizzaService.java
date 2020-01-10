package ch.ti8m.azubi.bch.pizzashop.service;

import ch.ti8m.azubi.bch.pizzashop.dto.Pizza;

import java.util.List;

public interface PizzaService {

    List<Pizza> list() throws Exception;

    Pizza get(int pizza_ID) throws Exception;

    Pizza create(Pizza pizza) throws Exception;

    void update(Pizza pizza) throws Exception;

    void remove(int pizza_ID) throws Exception;
}
