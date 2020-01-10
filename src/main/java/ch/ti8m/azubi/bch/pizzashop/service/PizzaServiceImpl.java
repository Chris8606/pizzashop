package ch.ti8m.azubi.bch.pizzashop.service;

import ch.ti8m.azubi.bch.pizzashop.dto.Pizza;
import ch.ti8m.azubi.bch.pizzashop.persistence.PizzaDAO;

import java.sql.Connection;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

public class PizzaServiceImpl implements PizzaService{

    private final Connection connection = ConnectionUtil.createDBConnection();
    private final PizzaDAO pizzaDAO = new PizzaDAO(connection);



    @Override
    public List<Pizza> list() throws Exception {
        List<Pizza> pizzasList = pizzaDAO.list();
        pizzasList.sort(Comparator.comparing(Pizza::getId));
        return pizzasList;
    }

    @Override
    public Pizza get(int pizza_ID) throws Exception {
        Pizza pizza = pizzaDAO.getPizzaByID(pizza_ID);
        if (pizza == null) {
            throw new NoSuchElementException("No pizza with id " + pizza_ID + " exists");
        }
        return pizza;
    }

    @Override
    public Pizza create(Pizza pizza) throws Exception {
        return pizzaDAO.create(pizza);
    }

    @Override
    public void update(Pizza pizza) throws Exception {
        pizzaDAO.update(pizza);
    }

    @Override
    public void remove(int pizza_ID) throws Exception {
        pizzaDAO.delete(pizza_ID);
    }
}
