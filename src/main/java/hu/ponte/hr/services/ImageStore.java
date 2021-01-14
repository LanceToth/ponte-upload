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
	
	/**
	 * A képeket az adatbázis azonosító alapján helyezzük el a lemezen,
	 * hogy a fájlnév ismétlése ne jelentsen gondot és a meta adatok alapján könnyen meg lehessen találni
	 * 
	 * @param uploadedImage a feltöltendő kép
	 * @param imageMeta az adatbázisban táolt metaadatok
	 * @return sikeres tárolás
	 * @throws FileNotFoundException ha nem sikeült létrehozni a fájlt
	 * @throws IOException írási hiba esetén
	 */
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
	
	/**
	 * Létrehozza az előnézeti képet, amikor szükség van rá és eltárolja a lemezen fix névvel,
	 * hogy csak egyszer kelljen létrehozni és ne kelljen táolni az elnevezését.
	 * A megegyező név nem gond, mert minden kép külön mappába kerül.
	 * 
	 * @param imageMeta adatbázisban tárolt metaadatok
	 * @return sikeres tárolás
	 * @throws FileNotFoundException ha nem sikeült létrehozni a fájlt
	 * @throws IOException írási hiba esetén
	 */
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
	
	/**
	 * A metaadat adatbázis azonosítója és a megadott könyvtá struktúra alapján visszaadja az adott kép helyét a lemezen
	 */
	private static File getSubfolder(String metaId) {
		return new File(IMAGEFOLDER + metaId + "/");
	}
	
	/**
	 * Kiszámolja a kép nagyobbik dimenziója alapján, mennyivel kell osztani az oldalakat,
	 * hogy beleférjen a kép a megadott előnézeti méretbe
	 */
	private static int getResizeRatio(int width, int height) {
		int larger = width;
		if(height > larger) {
			larger = height;
		}
		return larger / TARGETSIZE;
	}

}
