package hu.ponte.hr.controller.upload;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import hu.ponte.hr.controller.ImageMeta;
import hu.ponte.hr.controller.ImageMetaRepository;
import hu.ponte.hr.services.ImageStore;
@Component
@RequestMapping("api/file")
public class UploadController
{
	@Autowired
    private ImageStore imageStore;
    
    @Autowired
    private ImageMetaRepository imageMetaRepository;
    
    @RequestMapping(value = "post", method = RequestMethod.POST)
    @ResponseBody
    public String handleFormUpload(@RequestParam("file") MultipartFile file) throws IOException {
    	ImageMeta imageMeta = new ImageMeta(file.getOriginalFilename(), file.getContentType(), file.getSize());
    	
    	//getsign
    	//imageMeta.setSign
    	
    	imageMeta = imageMetaRepository.save(imageMeta);
    	
    	imageStore.store(file, imageMeta);
    	
        return "ok";
    }
}
