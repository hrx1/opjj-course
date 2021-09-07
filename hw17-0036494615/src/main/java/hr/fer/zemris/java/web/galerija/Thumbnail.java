package hr.fer.zemris.java.web.galerija;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import static hr.fer.zemris.java.web.galerija.GalleryUtils.thumbnailsRoot;

/**
 * Thumbnail servlet which manages thumbnails.
 * 
 * @author Hrvoje
 *
 */
@javax.ws.rs.Path("/thumbnail")
public class Thumbnail {
	/** Servlet Context */
	@Context
	private ServletContext application;
	
	/**
	 * Returns thumbnail with image name
	 * 
	 * @param imageName image name
	 * @return JPEG 
	 */
	@GET
	@Produces("image/jpeg")
	public Response getThumbnail(@QueryParam("image") String imageName) {
		
		Path thumbnailsPath = Paths.get(application.getRealPath(thumbnailsRoot));

		if(!Files.isDirectory(thumbnailsPath)) {
			try {
				Files.createDirectory(thumbnailsPath);
			} catch (IOException e) {
				return Response.noContent().build();
			}
		}
		
		Path thumbnail;
		try {
			thumbnail = GalleryUtils.getFile(thumbnailsPath, imageName);
		} catch (IOException ignorable) {
			return Response.noContent().build();
		}

		if(thumbnail == null) {
			try {
				Path src = Paths.get(application.getRealPath(GalleryUtils.imagesRoot)).resolve(imageName);
				thumbnail = generateThumbnail(src, thumbnailsPath.resolve(imageName));
			} catch (IOException e) {
				return Response.noContent().build();
			}
		}
		
	    BufferedImage imageBuffer;
		try {
			imageBuffer = ImageIO.read(thumbnail.toFile());
		} catch (IOException ignorable) {
			return Response.noContent().build();
		}
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    try {
			ImageIO.write(imageBuffer, "jpg", baos);
		} catch (IOException e) {
			return Response.noContent().build();
		}
	    byte[] imageData = baos.toByteArray();

	    return Response.ok(imageData).build();
	}

	/**
	 * Generates thumbnail from source to destination
	 * 
	 * @param source of original
	 * @param destination of resized
	 * @return Path to resized
	 * 
	 * @throws IOException if source or destination is not readable
	 */
	private Path generateThumbnail(Path source, Path destination) throws IOException{
		BufferedImage srcImage = ImageIO.read(source.toFile());
		
		Image scaled = srcImage.getScaledInstance(150, 150, Image.SCALE_SMOOTH);
		BufferedImage result = new BufferedImage(150, 150, BufferedImage.TYPE_INT_RGB);
		
		Graphics2D graphics = result.createGraphics();
		graphics.drawImage(scaled, 0, 0, null);
		graphics.dispose();
		
		ImageIO.write(result, "jpg", destination.toFile());
		
		return destination;
	}

	
	
}
