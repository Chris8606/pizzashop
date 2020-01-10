package ch.ti8m.azubi.bch.pizzashop.web;

import ch.ti8m.azubi.bch.pizzashop.dto.Order;
import ch.ti8m.azubi.bch.pizzashop.dto.PizzaOrder;
import ch.ti8m.azubi.bch.pizzashop.service.FreemarkerConfig;
import freemarker.template.Template;
import freemarker.template.TemplateException;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/pizzaOrder")
public class PizzaOrderServlet extends HttpServlet {

    private Template template;
    private Logger logger = LoggerFactory.getLogger(getClass().getName());

    @Override
    public void init() throws ServletException {

        template = new FreemarkerConfig().loadTemplate("pizzaOrder.ftl");
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        HttpSession session = req.getSession();
        Order order = (Order) session.getAttribute("order");
        List<PizzaOrder> pizzaOrderList = null;

        try {
            pizzaOrderList = order.getPizzaOrders();
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info(order.toString());
        logger.info(pizzaOrderList.toString());

        PrintWriter writer = resp.getWriter();
        Map<String, Object> model = new HashMap<>();
        model.put("pizzaOrderList", pizzaOrderList);
        model.put("order", order);

        try {
            template.process(model, writer);
        } catch (TemplateException ex) {
            throw new IOException("Could not process template: " + ex.getMessage());
        }
    }
}
