package es.thepuar.InfiniteSpace.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import es.thepuar.InfiniteSpace.google.client.PhotoClientJava;
import es.thepuar.InfiniteSpace.manager.DownloadManager;
import es.thepuar.InfiniteSpace.manager.UploadManager;
import es.thepuar.InfiniteSpace.model.Fichero;
import es.thepuar.InfiniteSpace.model.FicheroDirectorio;
import es.thepuar.InfiniteSpace.model.MapEntryPhoto;
import es.thepuar.InfiniteSpace.model.Referencia;
import es.thepuar.InfiniteSpace.service.api.FileToPng;
import es.thepuar.InfiniteSpace.service.api.MapEntryPhotoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


import es.thepuar.InfiniteSpace.service.api.FicheroService;

@Controller
@RequestMapping("/fichero")
public class FicheroController {

    @Autowired
    Ruta ruta;

    @Autowired
    FicheroService ficheroService;

    @Autowired
    MapEntryPhotoService mapEntryPhotoService;

    @Autowired
    PhotoClientJava photoService;

    @Autowired
    FileToPng fileService;

    @Autowired
    DownloadManager downloadManager;

    @Autowired
    UploadManager uploadManager;

    @GetMapping("{id}")
    public ModelAndView detail(@PathVariable("id") Long id) {
        Fichero fichero = this.ficheroService.findById(id);
        ModelAndView mav = new ModelAndView("fichero_detail");
        mav.addObject("fichero", fichero);
        List<MapEntryPhoto> partes = this.mapEntryPhotoService.findByFichero(fichero);
        mav.addObject("partes", partes);
        return mav;
    }

    @PostMapping("/add")
    public ModelAndView addFile(@ModelAttribute Fichero fichero) {
        this.ficheroService.completeFichero(fichero);
        this.ficheroService.save(fichero);
        this.ficheroService.uploadFile(fichero);

        return new ModelAndView("redirect:/");
    }

    @GetMapping("delete/{id}")
    public ModelAndView removeFile(@PathVariable("id") Long id) {

        this.ficheroService.delete(this.ficheroService.findById(id));
        return new ModelAndView("redirect:/");
    }

    @GetMapping("download/{id}")
    public ModelAndView downloadFile(@PathVariable("id") Long id) {
        Fichero fichero = this.ficheroService.findById(id);
        if (fichero != null)
            this.ficheroService.downloadFile(fichero);
        return new ModelAndView("redirect:/");
    }

    @GetMapping("upload/{id}")
    public ModelAndView uploadFile(@PathVariable("id") Long id) {
        Fichero fichero = this.ficheroService.findById(id);
        fichero.setUploaded(true);
        this.ficheroService.save(fichero);

        return new ModelAndView("redirect:/");
    }

    @GetMapping("directorio")
    public ModelAndView getRuta() {

        return new ModelAndView("redirect:/");

    }

    @GetMapping("split/{id}")
    public ModelAndView split(@PathVariable("id") Long id) {
        File directorio = new File("Z:\\App\\InfiniteSpace\\upload");
        File toSplit = null;
        List<FicheroDirectorio> ficheroDirectorios = new ArrayList<>();

        if (directorio.isDirectory()) {
            int i = 1;
            for (File file : directorio.listFiles()) {
                if (id == i) {
                    toSplit = file;
                }
                ficheroDirectorios.add(new FicheroDirectorio(i++, file));

            }
            if (toSplit != null) {
                Fichero fichero = ficheroService.fileToFichero(toSplit);
                this.fileService.convertFichero2Png(fichero);
            }

        }
        return new ModelAndView("redirect:/");
    }

    @GetMapping("splitAndStore/{id}")
    public ModelAndView splitAndStore(@PathVariable("id") Long id) {
        //File directorio = new File("Z:\\App\\InfiniteSpace\\upload");
        File directorio = new File(ruta.getRuta());
        File toSplit = null;
       // List<FicheroDirectorio> ficheroDirectorios = new ArrayList<>();

        if (directorio.isDirectory()) {
            int i = 1;
            for (File file : directorio.listFiles()) {
                if (id == i) {
                    toSplit = file;
                }
                i++;
               // ficheroDirectorios.add(new FicheroDirectorio(i++, file));

            }
            if (toSplit != null) {
                Fichero fichero = ficheroService.fileToFichero(toSplit);
                List<Referencia> referencias = this.fileService.convertFichero2Png(fichero);
                this.ficheroService.save(fichero);
                this.photoService.uploadFiles(referencias);
                for (Referencia referencia : referencias) {
                    this.mapEntryPhotoService.save(referencia.getEntry());
                    File f = new File(referencia.getRuta());
                    f.delete();
                }

            }
        }
        return new ModelAndView("redirect:/");
    }

    @GetMapping("splitAndStoreMAX/{id}")
    public ModelAndView splitAndStoreMAX(@PathVariable("id") Long id) {
        //File directorio = new File("Z:\\App\\InfiniteSpace\\upload");
        File directorio = new File(ruta.getRuta());
        File toSplit = null;
        // List<FicheroDirectorio> ficheroDirectorios = new ArrayList<>();

        if (directorio.isDirectory()) {
            int i = 1;
            for (File file : directorio.listFiles()) {
                if (id == i) {
                    toSplit = file;
                }
                i++;
                // ficheroDirectorios.add(new FicheroDirectorio(i++, file));

            }
            if (toSplit != null) {
               this.uploadManager.uploadFichero(toSplit);

            }
        }
        return new ModelAndView("redirect:/");
    }

    @GetMapping("compare")
    public ModelAndView compareImages() {
        this.ficheroService.compareFile("","");
        return new ModelAndView("redirect:/");
    }

    @GetMapping("downloadThread/{id}")
    public ModelAndView descargaHilos(@PathVariable("id") Long id){
        Fichero fichero = ficheroService.findById(id);
        downloadManager.downloadFichero(fichero);

        return new ModelAndView("redirect:/");

    }

}
