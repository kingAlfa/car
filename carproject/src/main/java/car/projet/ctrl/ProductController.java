package car.projet.ctrl;



import java.util.ArrayList;
import java.util.List;
import car.projet.dao.PanierRepository;
import car.projet.entites.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import car.projet.dao.ProductRepository;
import car.projet.entites.Products;

import javax.servlet.http.HttpServletRequest;


@Controller
public class ProductController {

	@Autowired
	private ProductRepository dao;
	@Autowired
	private PanierRepository panierDao ;

	//Creation d'un produit et enregistrement
	//Products prod = new Products("Cate 2","Dell","Libelle Dell ","Ordinateur puissant","Dell","...",2.3);
	//dao.save(prod);

	@RequestMapping(path = "/produit/{id}")
	public String client(HttpServletRequest request, Model  model, @PathVariable int id) {

		Users user = (Users) request.getSession().getAttribute("userSession");
		int total=0;
		if(user != null){
			total = panierDao.totalQuantityStock(user.getId());
		}
		model.addAttribute("total",total);

		Products products = dao.findById(id);
		model.addAttribute("products",products);

		return "product";
	}

	@RequestMapping("/list")
	public String list(Model model,HttpServletRequest request){
		Users user = (Users) request.getSession().getAttribute("userSession");
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
		int total;
		if(user != null){
			System.out.println(">>> Product controller : "+user.getId());
			total = panierDao.totalQuantityStock(user.getId());

		}else{
			total=0;
		}
		model.addAttribute("total",total);
		model.addAttribute("list",allProd);

		return "list-produit";

	}

}
