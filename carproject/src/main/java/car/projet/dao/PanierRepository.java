package car.projet.dao;

import car.projet.entites.Panier;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;


public interface PanierRepository extends Repository<Panier,Integer> {

    void save(Panier panier);

    Optional<Panier> findById(int id);

    @Query("select id from Panier where id_User =?1")
    List<Integer> listIdProduit(int idUser);

    @Query("select sum(quantite) from Panier where id_User =?1")
    int totalQuantityStock(int id_user);
}
