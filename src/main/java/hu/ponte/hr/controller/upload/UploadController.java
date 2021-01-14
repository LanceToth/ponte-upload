package hu.ponte.hr.controller.upload;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import hu.ponte.hr.Utils;
import hu.ponte.hr.controller.ImageMeta;
import hu.ponte.hr.controller.ImageMetaRepository;
import hu.ponte.hr.services.ImageStore;
import hu.ponte.hr.services.SignService;
@Component
@RequestMapping("api/file")
public class UploadController
{
	
	@Autowired
    private ImageStore imageStore;
	
	@Autowired
    private SignService signService;
    
    @Autowired
    private ImageMetaRepository imageMetaRepository;
    
    @RequestMapping(value = "post", method = RequestMethod.POST)
    @ResponseBody
    public String handleFormUpload(@RequestParam("file") MultipartFile file) throws IOException {
    	String method = "handleFormUpload";
    	Utils.log(method, "started");
    	
    	//Kitöltjük az adatbázis tábla tartalmát
    	ImageMeta imageMeta = new ImageMeta(file.getOriginalFilename(), file.getContentType(), file.getSize());
    	
    	try {
    		//aláírás generálás
    		String sign = signService.generateSignature(file.getBytes());
    		
    		//Utils.log(method, "generateSignature result: " + sign);
    		
    		imageMeta.setDigitalSign(sign);
    	}catch(Exception ex) {
    		Utils.log(method, "generateSignature failed due to the following:");
    		ex.printStackTrace();
    	}
    	
    	//adatbázisba tároljuk (id-t kapunk)
    	imageMeta = imageMetaRepository.save(imageMeta);
    	
    	//eltároljuk a képet a lemezen
    	imageStore.store(file, imageMeta);
    	
        return "ok";
    }
}
