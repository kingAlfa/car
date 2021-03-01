package car.projet.dao;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;


import car.projet.entites.Products;

import java.util.List;


public interface ProductRepository  extends CrudRepository<Products, Integer>{
	
	//Search by the product id
	
	Products findById(int id);

	@Query("select p.id from Products p join Stock s on s.id = p.id where s.quantite >0 ")
	List<String> selectProductsInStock();

	//public Page<Products> findAll(PageRequest pageRequest);

	
	

	

}
