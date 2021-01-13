package hu.ponte.hr.controller;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hu.ponte.hr.services.ImageStore;

@RestController()
@RequestMapping("api/images")
public class ImagesController {
	
	private static Logger logger = LogManager.getLogger(ImagesController.class);

    @Autowired
    private ImageStore imageStore;
    
    @Autowired
    private ImageMetaRepository imageMetaRepository;

    @GetMapping("meta")
    public List<ImageMeta> listImages() {
		return imageMetaRepository.findAll();
    }

    @GetMapping("preview/{id}")
    public void getImage(@PathVariable("id") String id, HttpServletResponse response) throws IllegalArgumentException, FileNotFoundException, IOException {
    	String method = "getImage";
    	logger.trace("Method {}: searching for {}",method, id);
    	ImageMeta imageMeta = imageMetaRepository.findById(id)
    			.orElseThrow(() -> new IllegalArgumentException("Image " + id + " not found"));
    	
    	logger.trace("Method {}: imageMeta is {}",method, imageMeta);
    	
    	File preview = new File(ImageStore.IMAGEFOLDER + imageMeta.getId() + "/preview.jpg");
    	
    	if (!preview.exists()) {
    		imageStore.generatePreview(imageMeta);
    	}
    	
		try(FileInputStream previewInput = new FileInputStream(preview)){
			response.getOutputStream().write(previewInput.readAllBytes());
		}
	}

}
