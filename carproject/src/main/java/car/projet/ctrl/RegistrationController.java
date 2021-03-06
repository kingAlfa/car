package car.projet.ctrl;

import car.projet.dao.UserDaoImp;
import car.projet.entites.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
public class RegistrationController
{
    @Autowired
    private UserDaoImp userdao;

    @RequestMapping(value = "/register",method = RequestMethod.GET)
    public ModelAndView showRegister(HttpServletRequest request, HttpServletResponse response){
        ModelAndView mv = new ModelAndView("register");
        mv.addObject("user",new Users());

        return mv;
    }

    @RequestMapping(value = "/registerProcess",method = RequestMethod.POST)
    public String addUser(HttpServletRequest request, HttpServletResponse response,
                                @ModelAttribute("user") Users user){
        user.setId();
        userdao.register(user);
        return "redirect:/list";
    }
}
