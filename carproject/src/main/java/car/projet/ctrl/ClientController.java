package car.projet.ctrl;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import car.projet.dao.ProductRepository;
import car.projet.entites.Products;



@Controller
public class ClientController 

{	
	@Autowired
	ProductRepository dao;
	
	@GetMapping(path = "/produit/{id}")
	public String client(Model  model,@PathVariable int id) {
		Products products = dao.findById(id);
		
		model.addAttribute("produit",products);
		return "product";
	}

}
