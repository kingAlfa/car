package fil.car.alfashop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ClientController {
	
	@RequestMapping("/client")
	public String hello() {
		return "home";
	}

	

	
	

}
