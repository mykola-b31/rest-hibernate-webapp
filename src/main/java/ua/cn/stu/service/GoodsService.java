package ua.cn.stu.service;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import ua.cn.stu.dao.HibernateDAOFactory;
import ua.cn.stu.domain.Goods;
import ua.cn.stu.domain.Supplier;

import java.math.BigDecimal;
import java.util.List;

@Path("goods")
public class GoodsService {

    @GET
    @Path("getAllGoods")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Goods> getAllGoods() {
        return HibernateDAOFactory.getInstance().getGoodsDAO().getAllGoods();
    }

    @PUT
    @Path("addGoods/name/{goodsname}/price/{goodsprice}/quantity/{goodsquantity}/supplier/{goodssupplier}")
    public void addGoods(@PathParam("goodsname") String goodsName, @PathParam("goodsprice") String goodsPrice, @PathParam("goodsquantity") String goodsQuantity, @PathParam("goodssupplier") String supplierId) {
        BigDecimal price = new BigDecimal(goodsPrice);
        int quantity = Integer.parseInt(goodsQuantity);
        Long suppId = Long.parseLong(supplierId);
        Supplier supplier = HibernateDAOFactory.getInstance().getSupplierDAO().getSupplierById(suppId);
        Goods goods = new Goods();
        goods.setName(goodsName);
        goods.setPrice(price);
        goods.setQuantity(quantity);
        goods.setSupplier(supplier);
        HibernateDAOFactory.getInstance().getGoodsDAO().createGoods(goods);
    }

    @DELETE
    @Path("deleteGoods/{goodsid}")
    public void deleteGoods(@PathParam("goodsid") String goodsId)  {
        Long id = Long.parseLong(goodsId);
        HibernateDAOFactory.getInstance().getGoodsDAO().deleteGoodsById(id);
    }

    @POST
    @Path("updateGoods/id/{goodsid}/name/{goodsname}/price/{goodsprice}/quantity/{goodsquantity}/supplier/{goodssupplier}")
    public void updateGoods(@PathParam("goodsid") String goodsId, @PathParam("goodsname") String goodsName, @PathParam("goodsprice") String goodsPrice, @PathParam("goodsquantity") String goodsQuantity, @PathParam("goodssupplier") String supplierId) {
        Long id = Long.parseLong(goodsId);
        BigDecimal price = new BigDecimal(goodsPrice);
        int quantity = Integer.parseInt(goodsQuantity);
        Long suppId = Long.parseLong(supplierId);
        Supplier supplier = HibernateDAOFactory.getInstance().getSupplierDAO().getSupplierById(suppId);
        Goods goods = HibernateDAOFactory.getInstance().getGoodsDAO().getGoodsById(id);
        goods.setName(goodsName);
        goods.setPrice(price);
        goods.setQuantity(quantity);
        goods.setSupplier(supplier);
        HibernateDAOFactory.getInstance().getGoodsDAO().updateGoods(goods);
    }

}
