package ch.ti8m.azubi.bch.pizzashop.web;

import ch.ti8m.azubi.bch.pizzashop.dto.Pizza;
import ch.ti8m.azubi.bch.pizzashop.service.FreemarkerConfig;
import ch.ti8m.azubi.bch.pizzashop.service.PizzaService;
import ch.ti8m.azubi.bch.pizzashop.service.ServiceRegistry;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@WebServlet("/pizza")
public class PizzaServlet extends HttpServlet {

    private PizzaService pizzaService;
    private Template template;
    private Logger logger = LoggerFactory.getLogger(getClass().getName());

    @Override
    public void init() throws ServletException {
        pizzaService = ServiceRegistry.getInstance().get(PizzaService.class);
        template = new FreemarkerConfig().loadTemplate("pizza.ftl");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        List<Pizza> pizzas = null;

        try {
            pizzas = pizzaService.list();
        } catch (Exception e) {
            e.printStackTrace();
        }

        PrintWriter writer = resp.getWriter();
        Map<String, Object> model = new HashMap<>();
        model.put("pizzas", pizzas);
      //  logger.info(pizzas.toString());

        try {
            template.process(model, writer);
        } catch (TemplateException ex) {
            throw new IOException("Could not process template: " + ex.getMessage());
        }
    }
}
