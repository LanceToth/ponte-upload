package hu.ponte.hr.services;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import hu.ponte.hr.controller.ImageMeta;

@Service
public class ImageStore {
	
	public static final String IMAGEFOLDER = "/ponte/imagestore/";
	public static final Integer TARGETSIZE = 100;
	
	public boolean store(MultipartFile uploadedImage, ImageMeta imageMeta) throws FileNotFoundException, IOException  {
		
		File subfolder = getSubfolder(imageMeta.getId());
		subfolder.mkdirs();
		
		File target = new File(subfolder, imageMeta.getName());
		target.createNewFile();
		
		FileOutputStream fileOut = new FileOutputStream(target);
		fileOut.write(uploadedImage.getBytes());
		fileOut.close();
		
		return true;
	}
	
	public boolean generatePreview(ImageMeta imageMeta) throws FileNotFoundException, IOException {
		File subfolder = getSubfolder(imageMeta.getId());
		File originalImage = new File(subfolder, imageMeta.getName());
		
		try(FileInputStream fileIn = new FileInputStream(originalImage)){
			BufferedImage originalBuffered = ImageIO.read(fileIn);
			int imageType = ((originalBuffered.getType() == 0) ? BufferedImage.TYPE_INT_ARGB : originalBuffered.getType());
			
			int resizeRatio = getResizeRatio(originalBuffered.getWidth(), originalBuffered.getHeight());
			
			int widthScaled = originalBuffered.getWidth() / resizeRatio;
			int heightScaled = originalBuffered.getHeight() / resizeRatio;
			
	        BufferedImage previewBuffered = new BufferedImage(widthScaled, heightScaled, imageType);
	        
	        Graphics2D g2d = previewBuffered.createGraphics();
	        g2d.drawImage(originalBuffered, 0, 0, widthScaled, heightScaled, null);
	        g2d.dispose();
	 
	        return ImageIO.write(previewBuffered, "jpg", new File(subfolder, "/preview.jpg"));
		}
	}
	
	private static File getSubfolder(String metaId) {
		return new File(IMAGEFOLDER + metaId + "/");
	}
	
	private static int getResizeRatio(int width, int height) {
		int larger = width;
		if(height > larger) {
			larger = height;
		}
		return larger / TARGETSIZE;
	}

}
