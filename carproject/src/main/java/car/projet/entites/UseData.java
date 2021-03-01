package car.projet.entites;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

public class UseData
{
private EntityManagerFactory emf;

public List<Products> getAllInStock(){
    EntityManager em = emf.createEntityManager();
    List<Products> list = (List<Products>) em.createQuery("select p.id from Products  p join Stock s on s.id = p.id where s.quantite >0 ").getResultList();
    return list;

}
}
