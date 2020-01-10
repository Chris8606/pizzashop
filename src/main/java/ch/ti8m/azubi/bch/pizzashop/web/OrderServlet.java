package ch.ti8m.azubi.bch.pizzashop.web;

import ch.ti8m.azubi.bch.pizzashop.dto.Order;
import ch.ti8m.azubi.bch.pizzashop.dto.Pizza;
import ch.ti8m.azubi.bch.pizzashop.dto.PizzaOrder;
import ch.ti8m.azubi.bch.pizzashop.service.FreemarkerConfig;
import ch.ti8m.azubi.bch.pizzashop.service.OrderService;
import ch.ti8m.azubi.bch.pizzashop.service.PizzaService;
import ch.ti8m.azubi.bch.pizzashop.service.ServiceRegistry;
import freemarker.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@WebServlet("/order")
public class OrderServlet extends HttpServlet {
    private OrderService orderService;
    private PizzaService pizzaService;
    private Order order;
    private Template template;
    private Logger logger = LoggerFactory.getLogger(getClass().getName());

    @Override
    public void init() throws ServletException {
        orderService = ServiceRegistry.getInstance().get(OrderService.class);
        pizzaService = ServiceRegistry.getInstance().get(PizzaService.class);
        template = new FreemarkerConfig().loadTemplate("order.ftl");
    }

    /**
     * Get the order HTML page, with a list of orders and a form to submit new orders.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        try {
            List<Pizza> pizzas = pizzaService.list();

            PrintWriter writer = resp.getWriter();
            Map<String, Object> model = new HashMap<>();
            model.put("pizzas", pizzas);
            //  logger.info(orders.toString());

            template.process(model, writer);
        } catch (Exception ex) {
            throw new IOException("Could not process template: " + ex.getMessage(), ex);
        }
    }

    /**
     * Post a new order (parameter: text), and redirect to GET afterwards.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        try {
            String address = req.getParameter("address");
            String phonenumber = req.getParameter("phonenumber");

            logger.info("Creating Order");
            logger.info("Address: " + address);
            logger.info("Phone: " + phonenumber);
            if (!address.isEmpty() && !phonenumber.isEmpty()) {
                List<Pizza> pizzas = pizzaService.list();
                List<PizzaOrder> pizzaOrderList = new ArrayList<>();
                for (int i = 0; i < pizzas.size(); i++) {
                    String parameterName = "amount-" + pizzas.get(i).getId();
                    String parameterValue = req.getParameter(parameterName);
                    logger.info(parameterName+" = "+parameterValue);
                 //   Integer amount = parameterValue!=null? Integer.parseInt(parameterValue) : null;
                    Integer amount = Integer.parseInt(parameterValue);
                    if (amount != null && amount != 0) {
                        PizzaOrder pizzaOrder = new PizzaOrder(amount, pizzas.get(i));
                        pizzaOrderList.add(pizzaOrder);
                    }
                }
                if (!pizzaOrderList.isEmpty()) {
                    Order order = new Order();
                    order.setAddress(address);
                    order.setPhonenumber(phonenumber);
                    order.setPizzaOrders(pizzaOrderList);
                    order.setCalculatedTotalPrice();

                    HttpSession session = req.getSession();
                    session.setAttribute("order", order);
                    logger.info(order.toString());
                    logger.info("Creating Order in Datebase");
                    orderService.create(order);
                    resp.sendRedirect("pizzaOrder");
                }
            }
        } catch (Exception ex) {
            throw new IOException("Request failed", ex);
        }
    }
}
