package car.projet.dao;


import org.springframework.data.repository.Repository;


import car.projet.entites.Products;


public interface ProductRepository  extends Repository<Products, Integer>{
	
	//Search by the product id
	
	Products findById(int id);
	

	

}
