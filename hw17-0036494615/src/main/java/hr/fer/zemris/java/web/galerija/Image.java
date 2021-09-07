package hr.fer.zemris.java.web.galerija;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.json.JSONArray;
import org.json.JSONObject;

import hr.fer.zemris.java.web.galerija.GalleryUtils.ImageInfo;

import static hr.fer.zemris.java.web.galerija.GalleryUtils.imagesRoot;

/**
 * Servlet manages Image operations
 * 
 * @author Hrvoje
 *
 */
@javax.ws.rs.Path("/image")
public class Image {
	@Context
	private ServletContext application;
	
	/**
	 * Returns JPEG of a given image
	 * 
	 * @param imageName image name
	 * @return JPEG of a given image
	 */
	@GET
	@Produces("image/jpeg")
	public Response getImage(@QueryParam("image") String imageName) {
		Path image = Paths.get(application.getRealPath(imagesRoot)).resolve(imageName);
		
		if(!Files.isReadable(image)) {
			return Response.noContent().build();
		}
		
	    BufferedImage imageBuffer;
		try {
			imageBuffer = ImageIO.read(image.toFile());
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
	 * Returns Info of a given image
	 * 
	 * @param imageName image name
	 * @return Info of a given image
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response getImageInfo(@QueryParam("image") String imageName) {
		System.out.println("dosao " + imageName);
		ImageInfo infos;
		try {
			infos = GalleryUtils.loadImageInfo(application, imageName);
		} catch (IOException e) {
			System.out.println("padam");
			return Response.noContent().build();
		}
		System.out.println("je li null12?");
		if(infos == null) return Response.noContent().build();
		System.out.println("nije");
		JSONObject info = new JSONObject();
		
		info.put("title", infos.title);
		info.put("path", infos.title);
		
		JSONArray tags = new JSONArray(infos.tags);
		info.put("tags", tags);
		System.out.println(info.toString());
		return Response.status(Status.OK).entity(info.toString()).build();
	}

}
