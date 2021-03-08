package car.projet.ctrl;


import car.projet.dao.UserDaoImp;
import car.projet.dao.UserRepository;
import car.projet.entites.Login;
import car.projet.entites.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class LoginController
{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserDaoImp userDaoImp;




    @RequestMapping(value = "/login",method = RequestMethod.GET)
    public ModelAndView showLogin(HttpServletRequest request, HttpServletResponse response){

        ModelAndView mv = new ModelAndView("login");
        mv.addObject("login",new Login());
        return mv;
    }


    @RequestMapping(value = "/loginProcess",method = RequestMethod.POST)
    public String loginProcess(Model model, HttpServletRequest request, HttpServletResponse response, @ModelAttribute("login") Login login){
        Users user = userDaoImp.validateUser(login);
        HttpSession session = request.getSession();
        session.setAttribute("userSession",user);
       // System.out.println(session.getAttribute("userSession"));
        String path ="";

        if (user != null){
            path="redirect:/list";
        }
        else{
            path="login";
            model.addAttribute("message","Username or password is wrong");
        }

     return path;
    }
}
