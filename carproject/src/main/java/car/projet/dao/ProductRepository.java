package car.projet.dao;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;


import car.projet.entites.Products;


public interface ProductRepository  extends CrudRepository<Products, Integer>{
	
	//Search by the product id
	
	Products findById(int id);

	//public Page<Products> findAll(PageRequest pageRequest);

	
	

	

}
