package fil.car.alfashop.dao;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import org.springframework.data.repository.Repository;

import fil.car.alfashop.entite.Produit;

public interface ProduitRepository extends Repository<Produit,Integer>
{
	Produit findById(Integer id_prod) ;
	
	Page<Produit> findAll(PageRequest request);
	
	
}
