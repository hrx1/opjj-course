package hr.fer.zemris.java.web.galerija;


import java.io.IOException;

import static hr.fer.zemris.java.web.galerija.GalleryUtils.*;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Servlet which manages Tags
 * 
 * @author Hrvoje
 *
 */
@Path("/tags")
public class TagsServlet {
		
	@Context
	private ServletContext application;
	
	/**
	 * Returns tags loaded from file
	 * 
	 * @return Response
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTags() {
		JSONArray tags = new JSONArray();
		try {
			for(String tag : loadTags(application)) {
				tags.put(tag);
			}
		} catch (JSONException e) {
			return Response.status(Status.PARTIAL_CONTENT).build();
		} catch(IOException e2) {
			return Response.status(Status.NOT_FOUND).build();
		}
		
		JSONObject result = new JSONObject();
		result.put("tags", tags);

		return Response.status(Status.OK).entity(result.toString()).build();
	}
	
	
}
