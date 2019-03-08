package uk.ac.ed.plasmo.utility;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Utility class used to carry out a number of image processing functions
 * @author ctindal
 *
 */
public class ImageUtility {
	
	/**
	 * determines the new dimensions of an image based on a single dimension. Tries to
	 * constrain the proportions between height and width
	 * @param image
	 * @param dimension
	 * @return
	 */
	public static int[] getTargetDimensions(BufferedImage image, int dimension) {
		
		int [] dimensions = new int [2];
		
		int w = image.getWidth();
		int h = image.getHeight();
		int targetWidth;
		int targetHeight;
		
		//if the image height is > image width set the value of image
		//width to the standard dimension and proportionately alter the width size
		if(h > w) {
			
			//only set the standard dimension if the image height is > the standard
			if(h < dimension) {
				targetHeight = h;
				targetWidth = w;
			}
			else {
				targetHeight = dimension;
				targetWidth = (int) (targetHeight / (double) h * w);
				if(targetWidth < 1){
					targetWidth = 1;
				}
			}
		}
		//otherwise, vice versa
		else {
			
			//only set the standard dimension if the image width is > the standard
			if(w < dimension) {
				targetHeight = h;
				targetWidth = w;
			}
			else {
				targetWidth = dimension;
				targetHeight = (int) (targetWidth / (double) w * h);
				if(targetHeight < 1) {
					targetHeight = 1;
				}
			}
		}
		
		dimensions[0] = targetWidth;
		dimensions[1] = targetHeight;
		
		return dimensions;
	}
	
	/**
	 * resize an image to the specified dimensions
	 * @param image
	 * @param targetWidth
	 * @param targetHeight
	 * @param higherQuality
	 * @return
	 */
	public static BufferedImage resize(BufferedImage image, int targetWidth, int targetHeight, boolean higherQuality) {
		
		
		//constrain proportions (roughly)
		if(targetWidth < 10 || targetHeight < 10) {
			return null;
		}
		
		int type = (image.getTransparency() == Transparency.OPAQUE) ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
		
		BufferedImage ret = (BufferedImage) image;
		int w, h;
		if(higherQuality) {
			//use multi-step technique: start with original size, then
			//scale down in multiple passes with drawImage()
			//until target size is reached
			w = image.getWidth();
			h = image.getHeight();
		} else {
			w = targetWidth;
			h = targetHeight;
		}
		
		do {
			if(higherQuality && w > targetWidth) {
				w /= 2;
				if(w < targetWidth) {
					w = targetWidth;
				}
			}
			
			if(higherQuality && h > targetHeight) {
				h /= 2;
				if(h < targetHeight) {
					h = targetHeight;
				}
			}
			
			BufferedImage tmp = new BufferedImage(w, h, type);
			Graphics2D g2 = tmp.createGraphics();
			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g2.drawImage(ret, 0, 0, w, h, null);
			g2.dispose();
			
			ret = tmp;
		}
		while (w != targetWidth || h != targetHeight);
		
		return ret;
		
	}
	
	public static BufferedImage convertFileToImage(File file) {
		
		try {
			BufferedImage image = ImageIO.read(file);
			return image;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * write an image to a file on the file system
	 * @param image
	 * @param format
	 * @param filepath
	 * @param filename
	 * @return
	 * @throws IOException
	 */
	public static boolean writeImageToFile(BufferedImage image, String format, String filepath, String filename) throws IOException {
		return ImageIO.write(image, format, new File(filepath, filename));
	}
	
}
