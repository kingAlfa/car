package car.projet.dao;


import car.projet.entites.Stock;
import org.springframework.data.repository.CrudRepository;

public interface StockRepository extends CrudRepository<Stock, Integer>
{

}
