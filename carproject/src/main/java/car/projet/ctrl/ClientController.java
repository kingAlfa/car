package car.projet.ctrl;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import car.projet.dao.ProductRepository;
import car.projet.entites.Products;



@Controller
public class ClientController {	
	
	
	@Autowired
	private ProductRepository dao;
	
	@RequestMapping(path = "/produit/{id}")
	public String client(Model  model,@PathVariable int id) {
		Products products = dao.findById(id);
		String mess = "Field dao in car.projet.ctrl.ClientController required a bean named 'entityManagerFactory' that could not be found. ";
		model.addAttribute("produit",mess);
		return "product";
	}

}
