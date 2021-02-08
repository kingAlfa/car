package fil.car.projet.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ClientController 
{	
	@RequestMapping("/")
	public String client() {
		return "home.jsp";
	}
	
	@RequestMapping("/clients")
	public String addClient() {
		return "home.jsp";
	}

}
