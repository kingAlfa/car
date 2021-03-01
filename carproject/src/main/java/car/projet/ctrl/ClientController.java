package car.projet.ctrl;


import java.awt.desktop.SystemEventListener;
import java.io.IOException;
import java.util.List;

import car.projet.entites.UseData;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import car.projet.dao.ProductRepository;
import car.projet.entites.Products;
import org.springframework.web.context.support.ServletContextResource;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;


@Controller
public class ClientController {
	@PersistenceUnit
	private EntityManagerFactory emf;
	
	@Autowired
	private ProductRepository dao;



	@RequestMapping(path = "/produit/{id}")
	public String client(Model  model,@PathVariable int id) {

		//Creation d'un produit et enregistrement
		//Products prod = new Products("Cate 2","Dell","Libelle Dell ","Ordinateur puissant","Dell","...",2.3);
		//dao.save(prod);
		Products products = dao.findById(id);
		model.addAttribute("products",products);
		return "product";
	}

	@RequestMapping("/list")
	public String list(Model model){
		//UseData use = new UseData();
		//List<Products> l = use.getAllInStock();
		List<Products> list = (List<Products>) dao.findAll();

		model.addAttribute("list",list);

		return "list-produit";

	}

}
