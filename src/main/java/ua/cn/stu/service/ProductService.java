package ua.cn.stu.service;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import ua.cn.stu.dao.HibernateDAOFactory;
import ua.cn.stu.domain.Product;

import java.util.List;

@Path("product")
public class ProductService {

    @GET
    @Path("getAllProducts")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Product> getAllProducts() {
        return HibernateDAOFactory.getInstance().getProductDAO().getAllProducts();
    }

    @PUT
    @Path("addProduct/name/{productname}/description/{productdescription}")
    public void addProduct(@PathParam("productname") String productName, @PathParam("productdescription") String productDescription) {
        Product product = new Product();
        product.setName(productName);
        product.setDescription(productDescription);
        HibernateDAOFactory.getInstance().getProductDAO().createProduct(product);
    }

    @DELETE
    @Path("deleteProduct/{productid}")
    public void deleteProduct(@PathParam("productid") String productId) {
        Long id = Long.parseLong(productId);
        HibernateDAOFactory.getInstance().getProductDAO().deleteProductById(id);
    }

    @POST
    @Path("updateProduct/id/{productid}/name/{productname}/description/{productdescription}")
    public void updateProduct(@PathParam("productid") String productId, @PathParam("productname") String productName, @PathParam("productdescription") String productDescription) {
        Long id = Long.parseLong(productId);
        Product product = HibernateDAOFactory.getInstance().getProductDAO().getProductById(id);
        product.setName(productName);
        product.setDescription(productDescription);
        HibernateDAOFactory.getInstance().getProductDAO().updateProduct(product);
    }

}
