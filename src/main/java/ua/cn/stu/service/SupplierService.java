package ua.cn.stu.service;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import ua.cn.stu.dao.HibernateDAOFactory;
import ua.cn.stu.domain.Supplier;

import java.util.List;

@Path("supplier")
public class SupplierService {

    @GET
    @Path("getAllSuppliers")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Supplier> getAllSuppliers() {
        return HibernateDAOFactory.getInstance().getSupplierDAO().getAllSuppliers();
    }

    @PUT
    @Path("addSupplier/name/{suppliername}/contact/{suppliercontact}/address/{supplieraddress}/specialization/{supplierspecialization}")
    public void addSupplier(@PathParam("suppliername") String supplierName, @PathParam("suppliercontact") String supplierContact, @PathParam("supplieraddress") String supplierAddress, @PathParam("supplierspecialization") String supplierSpecialization) {
        Supplier supplier = new Supplier();
        supplier.setName(supplierName);
        supplier.setContact(supplierContact);
        supplier.setAddress(supplierAddress);
        supplier.setSpecialization(supplierSpecialization);
        HibernateDAOFactory.getInstance().getSupplierDAO().createSupplier(supplier);
    }

    @DELETE
    @Path("deleteSupplier/{supplierid}")
    public void deleteSupplier(@PathParam("supplierid") String supplierId) {
        Long id = Long.parseLong(supplierId);
        HibernateDAOFactory.getInstance().getSupplierDAO().deleteSupplierById(id);
    }

    @POST
    @Path("updateSupplier/id/{supplierid}/name/{suppliername}/contact/{suppliercontact}/address/{supplieraddress}/specialization/{supplierspecialization}")
    public void updateSupplier(@PathParam("supplierid") String supplierId, @PathParam("suppliername") String supplierName, @PathParam("suppliercontact") String supplierContact, @PathParam("supplieraddress") String supplierAddress, @PathParam("supplierspecialization") String supplierSpecialization) {
        Long id = Long.parseLong(supplierId);
        Supplier supplier = HibernateDAOFactory.getInstance().getSupplierDAO().getSupplierById(id);
        supplier.setName(supplierName);
        supplier.setContact(supplierContact);
        supplier.setAddress(supplierAddress);
        supplier.setSpecialization(supplierSpecialization);
        HibernateDAOFactory.getInstance().getSupplierDAO().updateSupplier(supplier);
    }

}
