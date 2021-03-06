package car.projet.ctrl;



import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import car.projet.dao.PanierRepository;
import car.projet.entites.Panier;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import car.projet.dao.ProductRepository;
import car.projet.entites.Products;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.servlet.http.HttpServletRequest;


@Controller
public class ClientController {
	@PersistenceUnit
	private EntityManagerFactory emf;
	
	@Autowired
	private ProductRepository dao;
	@Autowired
	private PanierRepository panierDao ;


	@RequestMapping(path = "/produit/{id}")
	public String client(Model  model,@PathVariable int id) {

		//Creation d'un produit et enregistrement
		//Products prod = new Products("Cate 2","Dell","Libelle Dell ","Ordinateur puissant","Dell","...",2.3);
		//dao.save(prod);
		Products products = dao.findById(id);
		model.addAttribute("products",products);
		int total = panierDao.totalQuantityStock();
		model.addAttribute("total",total);
		return "product";
	}

	@RequestMapping("/list")
	public String list(Model model){
		//la liste des id des produits avec une quantite superieur à 0
		List<String> inStock = dao.selectProductsInStock();

		//La list de tous les produits dont la quantite est superieur à 0
		List<Products> allProd = new ArrayList<>();
		int taille = inStock.size();
		for (int j=0;j< taille;j++)
		{
			allProd.add(dao.findById(Integer.parseInt(inStock.get(j))));
		}

		//List<Products> list = (List<Products>) dao.findAll();
		int total = panierDao.totalQuantityStock();
		model.addAttribute("total",total);

		model.addAttribute("list",allProd);

		return "list-produit";

	}

	@RequestMapping(path = "/ajouterPanier/{id}",method = RequestMethod.POST)
	public String ajoutPanier(Model model, @PathVariable int id){

		try{

			Optional<Panier> eltPanier = panierDao.findById(id);
			Panier elt = eltPanier.get();
			elt.updateQuantite(1);
			panierDao.save(elt);
			model.addAttribute("panier",elt);

		} catch (Exception e) {

			Panier panier = new Panier(dao.findById(id).getId(),1);
			Products prod = dao.findById(id);
			//panier.setProduct(prod);
			panierDao.save(panier);
			model.addAttribute("panier",panier);
			//e.printStackTrace();
		}
		int total = panierDao.totalQuantityStock();
		model.addAttribute("total",total);


		//return "panier";
		return "product";
	}

	@RequestMapping("/login")
	public String login(Model model){
		model.addAttribute("message","La page login");
		return "login";
	}

}
