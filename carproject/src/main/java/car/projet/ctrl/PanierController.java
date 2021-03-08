package car.projet.ctrl;

import car.projet.dao.PanierRepository;
import car.projet.dao.ProductRepository;
import car.projet.dao.UserRepository;
import car.projet.entites.Panier;
import car.projet.entites.Products;
import car.projet.entites.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class PanierController
{
    @Autowired
    private ProductRepository dao;
    @Autowired
    private PanierRepository panierDao ;

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(path = "/ajouterPanier/{id}",method = RequestMethod.POST)
    public String ajoutPanier(HttpServletRequest request,Model model, @PathVariable int id){
        Users user = (Users) request.getSession().getAttribute("userSession");

        try{

            Optional<Panier> eltPanier = panierDao.findById(id);
            Panier elt = eltPanier.get();
            elt.updateQuantite(1);
            panierDao.save(elt);
            model.addAttribute("panier",elt);

        } catch (Exception e) {

            Panier panier = new Panier(dao.findById(id).getId(),1);
            panier.setId_User(user.getId());
            Products prod = dao.findById(id);
            panierDao.save(panier);
            model.addAttribute("panier",panier);

        }
        int total = panierDao.totalQuantityStock(user.getId());
        model.addAttribute("total",total);
        
        return "redirect:/list";
    }

    @RequestMapping("/panier")
    public String panier(Model model,HttpServletRequest request){
        Users user = (Users) request.getSession().getAttribute("userSession");
        List<Products> listDesProduit = new ArrayList<Products>();
        List<Integer> listId = panierDao.listIdProduit(user.getId());
        for(int id :listId){
            listDesProduit.add(dao.findById(id));
        }

        int total = panierDao.totalQuantityStock(user.getId());
        model.addAttribute("list",listDesProduit);
        model.addAttribute("username",user.getUsername());
        model.addAttribute("total",total);
        model.addAttribute("message","les produits dans le panier");
        return "panier";
    }
}
