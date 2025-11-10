package ua.cn.stu.dao;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import ua.cn.stu.domain.Goods;

import java.math.BigDecimal;
import java.util.List;

public class GoodsDAO {
    private Session session;

    public GoodsDAO(Session session) {
        this.session = session;
    }

    public Goods createGoods(Goods goods) {
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.persist(goods);
            transaction.commit();
            return goods;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public Goods updateGoods(Goods goods) {
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.merge(goods);
            transaction.commit();
            return goods;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public void deleteGoods(Goods goods) {
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            goods.setSupplier(null);
            session.remove(goods);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public void deleteGoodsById(Long id) {
        Goods goods = session.find(Goods.class, id);
        if (goods != null) {
            deleteGoods(goods);
        }
    }

    public List<Goods> getAllGoods() {
        Query<Goods> query = session.createQuery(
                "from Goods g left join fetch g.supplier order by g.id", Goods.class
        );
        return query.getResultList();
    }

    public List<Goods> getGoodsByName(String name) {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Goods> cr = cb.createQuery(Goods.class);
        Root<Goods> root = cr.from(Goods.class);
        cr.select(root).where(cb.equal(root.get("name"), name));

        Query<Goods> query = session.createQuery(cr);
        return query.list();
    }

    public Goods getGoodsById(Long id) {
        return session.find(Goods.class, id);
    }

    public List<Goods> getGoodsBySupplier(Long supplierId) {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Goods> cr = cb.createQuery(Goods.class);
        Root<Goods> root = cr.from(Goods.class);
        cr.select(root).where(cb.equal(root.get("supplier").get("id"), supplierId));

        Query<Goods> query = session.createQuery(cr);
        return query.list();
    }

    public List<Goods> getGoodsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Goods> cr = cb.createQuery(Goods.class);
        Root<Goods> root = cr.from(Goods.class);
        cr.select(root).where(
                cb.between(root.get("price"), minPrice, maxPrice)
        );

        Query<Goods> query = session.createQuery(cr);
        return query.list();
    }

    public List<Goods> getTop10GoodsByQuantityNative() {
        String sql = "SELECT * FROM goods ORDER BY goods_quantity DESC LIMIT 10";

        Query<Goods> query = session.createNativeQuery(sql, Goods.class);
        return query.list();
    }

}