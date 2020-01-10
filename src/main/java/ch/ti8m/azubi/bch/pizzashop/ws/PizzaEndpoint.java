package ch.ti8m.azubi.bch.pizzashop.ws;

import ch.ti8m.azubi.bch.pizzashop.dto.Pizza;
import ch.ti8m.azubi.bch.pizzashop.service.PizzaService;
import ch.ti8m.azubi.bch.pizzashop.service.ServiceRegistry;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/pizzas")
public class PizzaEndpoint {

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Pizza> listPizzas() throws Exception {
        return pizzaService().list();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Pizza getPizza(@PathParam("id") int id) throws Exception {
        return pizzaService().get(id);
    }

    @POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Pizza createPizza(Pizza pizza) throws Exception {
        return pizzaService().create(pizza);
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void updatePizza(@PathParam("id") int id, Pizza pizza) throws Exception {
        pizza.setId(id);
        pizzaService().update(pizza);
    }

    @DELETE
    @Path("/{id}")
    public void deleteNote(@PathParam("id") int id) throws Exception {
        pizzaService().remove(id);
    }

    private PizzaService pizzaService() {
        return ServiceRegistry.getInstance().get(PizzaService.class);
    }
}
