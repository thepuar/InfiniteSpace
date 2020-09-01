package es.thepuar.InfiniteSpace.controller;

import java.io.File;

import es.thepuar.InfiniteSpace.google.client.PhotoClientJava;
import es.thepuar.InfiniteSpace.model.CambioRutaForm;
import es.thepuar.InfiniteSpace.service.impl.FileToPngImplV2;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

import es.thepuar.InfiniteSpace.google.rest.PhotoRestClient;
import es.thepuar.InfiniteSpace.model.Fichero;
import es.thepuar.InfiniteSpace.service.api.FileToPng;

@Controller

public class MainController extends DirectorioController {
    private static final Logger logger = LogManager.getLogger(MainController.class);

    private static final java.io.File DATA_STORE_DIR = new java.io.File("H:\\Documentos\\InfiniteSpace\\zhola.png");
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final int LOCAL_RECEIVER_PORT = 61984;


    private String token = "";


    @Autowired
    FileToPng fileToPngImplV2;


    @Autowired
    PhotoRestClient photoRestClient;

    @Autowired
    PhotoClientJava clienteJava;

    public MainController() {
        this.rutaNavegacion = "";
    }

    @GetMapping("")
    public ModelAndView inicio() {
        logger.info("Bienvenido a InfiniteSpace");
        ModelAndView mav = new ModelAndView("index");
        mav.addObject("ficheros", ficheroService.findAll());
        mav.addObject("fichero", new Fichero());
        this.cargaFicherosDeLaRuta(mav);

        mav.addObject("ruta", this.getRuta());
        mav.addObject("form", new CambioRutaForm());

        return mav;
    }

    @PostMapping("ruta")
    public String accion(String nuevaRuta) {
        if (!StringUtils.isBlank(nuevaRuta)) {
            File f = new File(nuevaRuta);
            if (f.isDirectory())
                this.setRuta(nuevaRuta);
        }
        return "redirect:/";
    }

    @PostMapping("path")
    public String accion(@ModelAttribute(value = "form") CambioRutaForm form) {
        logger.debug("Ruta -> {}", form.getRuta());
        this.setRuta(form.getRuta());
        return "redirect:/";
    }


    @PostMapping("convert")
    public String convert() {
        fileToPngImplV2.convertFile2Png();
        return "index.html";
    }

    @GetMapping("inicio")
    public String iniciar() {
        photoRestClient.sendPost();
        return "index.html";
    }

    @PostMapping("upload")
    public String funciona() {
        logger.debug("Iniciando upload");
        this.clienteJava.uploadFile();

        return "index.html";

    }

    @PostMapping("/download")
    public String descarga() {
        this.clienteJava.downloadImage();
        return "index.html";
    }

    @GetMapping("dashboard")
    public ModelAndView dashBoard() {
        return new ModelAndView("dashboard");
    }

    @Override
    protected void ponRuta() {
        ;
    }
}
