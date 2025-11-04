package ua.cn.stu.dao;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import ua.cn.stu.domain.Goods;
import ua.cn.stu.domain.Supplier;

import java.util.HashSet;
import java.util.List;

public class SupplierDAO {
    private Session session;

    public SupplierDAO(Session session) {
        this.session = session;
    }

    public Supplier createSupplier(Supplier supplier) {
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.persist(supplier);
            transaction.commit();
            return supplier;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public Supplier updateSupplier(Supplier supplier) {
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.merge(supplier);
            transaction.commit();
            return supplier;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public void deleteSupplier(Supplier supplier) {
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            for (Goods goods : new HashSet<>(supplier.getGoodsSet())) {
                goods.setSupplier(null);
            }
            session.remove(supplier);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public void deleteSupplierById(Long supplierId) {
        Supplier supplier = session.find(Supplier.class, supplierId);
        if (supplier != null) {
            deleteSupplier(supplier);
        }
    }

    public List<Supplier> getAllSuppliers() {
        Query<Supplier> query = session.createQuery(
                "from Supplier order by id", Supplier.class
        );
        return query.list();
    }

    public List<Supplier> getSuppliersByName(String name) {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Supplier> cr = cb.createQuery(Supplier.class);
        Root<Supplier> root = cr.from(Supplier.class);
        cr.select(root).where(cb.equal(root.get("name"), name));

        Query<Supplier> query = session.createQuery(cr);
        return query.list();
    }

    public Supplier getSupplierById(Long id) {
        return session.find(Supplier.class, id);
    }

    public List<Supplier> getSuppliersByGoodsName(String goodsName) {
        String hql = "select distinct supp from Supplier supp " +
                "join supp.goodsSet goods " +
                "where lower(goods.name) LIKE lower(:goodsName)";
        Query<Supplier> query = session.createQuery(hql, Supplier.class);
        query.setParameter("goodsName", "%" + goodsName + "%");
        return query.list();
    }

    public List<Supplier> getAllSuppliersSortedBySpecialization() {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Supplier> cr = cb.createQuery(Supplier.class);
        Root<Supplier> root = cr.from(Supplier.class);
        cr.select(root).orderBy(cb.asc(root.get("specialization")));

        Query<Supplier> query = session.createQuery(cr);
        return query.list();
    }

    public List<Supplier> getAllSuppliersSortedByGoodsCount() {
        String hql = "select supp from Supplier supp left join supp.goodsSet goods group by supp order by count(goods) desc";
        Query<Supplier> query = session.createQuery(hql, Supplier.class);
        return query.list();
    }

    public List<Supplier> getAllSuppliersSortedBySpecializationAndGoodsCount() {
        String hql = "select supp from Supplier supp left join supp.goodsSet goods group by supp order by supp.specialization asc, count(goods) desc";
        Query<Supplier> query = session.createQuery(hql, Supplier.class);
        return query.list();
    }

    public List<Supplier> searchSuppliersByAnyField(String searchTerm) {
        String hql = "SELECT DISTINCT s from Supplier s " +
                "LEFT JOIN s.goodsSet g " +
                "WHERE lower(s.name) LIKE lower(:term) " +
                "OR lower(s.contact) LIKE lower(:term) " +
                "OR lower(s.address) LIKE lower(:term) " +
                "OR lower(s.specialization) LIKE lower(:term) " +
                "OR lower(g.name) LIKE lower(:term)";

        Query<Supplier> query = session.createQuery(hql, Supplier.class);
        query.setParameter("term", "%" + searchTerm + "%");
        return query.list();
    }

}
