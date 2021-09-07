package hr.fer.zemris.java.web.galerija;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import hr.fer.zemris.java.web.galerija.GalleryUtils.ImageInfo;

import static hr.fer.zemris.java.web.galerija.GalleryUtils.*;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;

/**
 * Servlet manages tagImages operations
 * 
 * @author Hrvoje
 *
 */

@Path("/tag")
public class TagImages {
	
	@Context
	private ServletContext application;
	
	/**
	 * Returns images which contain tag
	 * 
	 * @param tagName tag
	 * @return images which contain tag
	 */
	@GET
	@Path("{tagName}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTagImages(@PathParam("tagName") String tagName) {
		
		JSONObject result = new JSONObject();
		result.put("tag", tagName);
		JSONArray names = new JSONArray();

		try {
			for(ImageInfo imageInfo : loadImageInfo(application)) {
				if(imageInfo.tags.contains(tagName)) {
					names.put(imageInfo.path);
				}
			}
		} catch (JSONException e) {
			return Response.status(Status.PARTIAL_CONTENT).build();
		} catch(IOException e2) {
			return Response.status(Status.NOT_FOUND).build();
		}
		
		result.put("images", names);
		
		return Response.status(Status.OK).entity(result.toString()).build();
	}

}
