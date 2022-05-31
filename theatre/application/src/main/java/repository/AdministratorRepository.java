package repository;

import domain.Administrator;
import domain.Client;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class AdministratorRepository implements IAdministratorRepository{
    static SessionFactory sessionFactory;

    public AdministratorRepository(HibernateUtil hibernateUtil) {
        this.sessionFactory = hibernateUtil.getSessionFactory();
    }

    @Override
    public Administrator login(String username, String parola) {
        Administrator administrator = null;
        try {
            try (Session session = sessionFactory.openSession()) {
                Transaction tx = null;

                try {
                    tx = session.beginTransaction();
                    administrator = (Administrator) session.createQuery("from Administrator where username = :u and parola = :p")
                            .setParameter("u", username)
                            .setParameter("p", parola)
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

        }
        return administrator;
    }
}
