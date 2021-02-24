package car.projet.ctrl;


import java.awt.desktop.SystemEventListener;

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

		//Creation d'un produit et enregistrement
		Products prod = new Products("Cate 2","Dell","Libelle Dell ","Ordinateur puissant","Dell","...",2.3);
		dao.save(prod);
		
		Products products = dao.findById(id);
		String mess = "Test du dao";
		
		model.addAttribute("products",products);
		model.addAttribute("mess",mess);
		
		return "product";
	}

}
