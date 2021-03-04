package car.projet.dao;

import car.projet.entites.Panier;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;

import java.util.Optional;


public interface PanierRepository extends Repository<Panier,Integer> {

    void save(Panier panier);

    Optional<Panier> findById(int id);
}
