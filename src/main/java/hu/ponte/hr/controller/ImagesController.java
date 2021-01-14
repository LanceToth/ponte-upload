package hu.ponte.hr.controller;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hu.ponte.hr.Utils;
import hu.ponte.hr.services.ImageStore;

@RestController()
@RequestMapping("api/images")
public class ImagesController {

    @Autowired
    private ImageStore imageStore;
    
    @Autowired
    private ImageMetaRepository imageMetaRepository;

    @GetMapping("meta")
    public List<ImageMeta> listImages() {
		return imageMetaRepository.findAll();
    }

    /**
     * Visszatér az előnézeti képpel, illetve létrehozza, ha nem létezik
     * 
     * @param id kép adatbázis metaadat azonosítója
     * @param response válasz a képpel
     * @throws IllegalArgumentException ha helytelen az id
     * @throws FileNotFoundException ha nem sikeül létrehozni az előnézetet
     * @throws IOException ha nem sikerül kiolvasni a lemezről
     */
    @GetMapping("preview/{id}")
    public void getImage(@PathVariable("id") String id, HttpServletResponse response) throws IllegalArgumentException, FileNotFoundException, IOException {
    	String method = "getImage";
    	Utils.log(method, "searching for " + id);
    	ImageMeta imageMeta = imageMetaRepository.findById(id)
    			.orElseThrow(() -> new IllegalArgumentException("Image " + id + " not found"));
    	
    	Utils.log(method, "imageMeta is " + imageMeta);
    	
    	File preview = new File(ImageStore.IMAGEFOLDER + imageMeta.getId() + "/preview.jpg");
    	
    	if (!preview.exists()) {
    		imageStore.generatePreview(imageMeta);
    	}
    	
		try(FileInputStream previewInput = new FileInputStream(preview)){
			response.getOutputStream().write(previewInput.readAllBytes());
		}
	}

}
