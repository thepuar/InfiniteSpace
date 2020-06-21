package es.thepuar.InfiniteSpace.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import es.thepuar.InfiniteSpace.service.api.FileToPng;

@Controller
public class MainController {
	
	@Autowired
	FileToPng converter;
	

	@GetMapping("")
	public String inicio() {
		return "index.html";
	}

	@PostMapping("action")
	public String accion() {
		System.out.println("Has pulsado accion");
		converter.test();
		return "index.html";
	}
}
