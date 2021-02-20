package car.projet.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.Repository;


import car.projet.entites.Products;


public interface ProductRepository  extends Repository<Products, Integer>{
	
	//Search by the product id
	Products findById(Integer id);
	
	//Get All the products
	Page<Products> findAll(PageRequest request);
	

}
