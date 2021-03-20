package car.projet.dao;


import car.projet.entites.Stock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

public interface StockRepository extends CrudRepository<Stock, Integer>
{
    @Query("select quantite from Stock where id=?1")
    int getQuantite(int id);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("update Stock s set s.quantite =?1 where s.id =?2")
    int updateQuantite(int quantite,int id);
}
