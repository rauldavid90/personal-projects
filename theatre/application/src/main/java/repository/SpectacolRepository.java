package repository;

import domain.Loc;
import domain.Spectacol;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class SpectacolRepository implements ISpectacolRepository {
    static SessionFactory sessionFactory;

    public SpectacolRepository(HibernateUtil hibernateUtil) {
        this.sessionFactory = hibernateUtil.getSessionFactory();
    }

    public Spectacol findOne(Long id) {
        Spectacol spectacol = null;
        try {
            try (Session session = sessionFactory.openSession()) {
                Transaction tx = null;

                try {
                    tx = session.beginTransaction();
                    spectacol = (Spectacol) session.createQuery("from Spectacol where id = :id")
                            .setParameter("id", id)
                            .list().get(0);
                    tx.commit();
                } catch (RuntimeException ex) {
                    if (tx != null)
                        tx.rollback();
                }
            }
        } catch (Exception e) {
            System.err.println("Exception " + e);
            e.printStackTrace();
        } finally {
            //sessionFactory.close();
        }
        return spectacol;
    }

    public List<Spectacol> findAll() {
        try {
            try (Session session = sessionFactory.openSession()) {
                Transaction tx = null;

                try {
                    tx = session.beginTransaction();
                    List<Spectacol> spectacole = session.createQuery("from Spectacol ", Spectacol.class)
                            .list();
                    tx.commit();
                    return spectacole;
                } catch (RuntimeException ex) {
                    if (tx != null)
                        tx.rollback();
                }
            }
        } catch (Exception e) {
            System.err.println("Exception " + e);
            e.printStackTrace();
        } finally {
            //sessionFactory.close();
        }
        return null;
    }
}
