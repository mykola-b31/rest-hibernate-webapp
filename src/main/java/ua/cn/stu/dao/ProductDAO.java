package ua.cn.stu.dao;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import ua.cn.stu.domain.Product;

import java.util.List;

public class ProductDAO {

    private Session session;

    public ProductDAO(Session session) {
        this.session = session;
    }

    public Product createProduct(Product product) {
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.persist(product);
            transaction.commit();
            return product;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public Product updateProduct(Product product) {
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.merge(product);
            transaction.commit();
            return product;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public void deleteProduct(Product product) {
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.remove(product);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public void deleteProductById(Long id) {
        Product product = session.find(Product.class, id);
        if (product != null) {
            deleteProduct(product);
        }
    }

    public List<Product> getAllProducts() {
        Query<Product> query = session.createQuery(
                "from Product order by id", Product.class
        );
        return query.getResultList();
    }

    public List<Product> getProductsByName(String name) {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Product> cr = cb.createQuery(Product.class);
        Root<Product> root = cr.from(Product.class);
        cr.select(root).where(cb.equal(root.get("name"), name));

        Query<Product> query = session.createQuery(cr);
        return query.list();
    }

    public Product getProductById(Long id) {
        return session.find(Product.class, id);
    }

}
