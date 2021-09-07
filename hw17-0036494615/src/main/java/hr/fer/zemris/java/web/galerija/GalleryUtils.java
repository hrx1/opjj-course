package hr.fer.zemris.java.web.galerija;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.servlet.ServletContext;

/**
 * 
 * Methods used by Servlets used for Gallery servlets.
 * 
 * @author Hrvoje
 *
 */
public class GalleryUtils {
	/** Name of a file which stores informations about images */
	public static final String imageDefinitionsFN = "/WEB-INF/opisnik.txt";
	/** Root Path to Images */
	public static final String imagesRoot = "/WEB-INF/slike";
	/** Root Path to Thumbnails */
	public static final String thumbnailsRoot = "/WEB-INF/thumbnails";

	
	/**
	 * Loads tags from imagesRoot path resolved with application resource.
	 * 
	 * @param application to resolve with
	 * @return loaded tags
	 * @throws IOException thrown when imagesRoot is not readable
	 */
	static Set<String> loadTags(ServletContext application) throws IOException {
		//ako result vec postoji kao globalna varijabla, nemoj loadat novo
		Set<String> result = new HashSet<>();
		
		loadImageInfo(application).forEach(o -> o.tags.forEach(e -> result.add(e)));
		
		return result;
	}
	
	/**
	 * Loads image info from images stored in application
	 * 
	 * @param application Application
	 * @return Set of ImageInfo
	 * 
	 * @throws IOException if imageDefinitionsFN is not found
	 */
	static Set<ImageInfo> loadImageInfo(ServletContext application) throws IOException {

		Set<ImageInfo> result = new HashSet<>();
				
				BufferedReader bis = new BufferedReader(
						new InputStreamReader(application.getResourceAsStream(imageDefinitionsFN)));
				
				while(true) {
					
					String path = bis.readLine();
					if(path == null) break;
					
					String title = bis.readLine();
					if(title == null) break;
					
					String tagsRaw = bis.readLine();
					if(tagsRaw == null) break;
					
					List<String> tags = parseTags(tagsRaw);
					
					result.add(new ImageInfo(path.trim(), title.trim(), tags));
				}
				
				try {
					bis.close();
				} catch (IOException ignorable) {
				}
				
				return result;
	}
	
	
	/**
	 * Loads image info 
	 * 
	 * @param application Application
	 * @param imageName image name
	 * @return Image Info 
	 * @throws IOException if root directory is not found
	 */
	public static ImageInfo loadImageInfo(ServletContext application, String imageName) throws IOException {
		
		BufferedReader bis = new BufferedReader(
				new InputStreamReader(application.getResourceAsStream(imageDefinitionsFN)));
		
		String path = null;
		String title = null;
		String tagsRaw = null;
		List<String> tags = null;
		
		while(true) {
			
			path = bis.readLine();
			if(path == null) break;
			
			title = bis.readLine();
			if(title == null) break;
			
			tagsRaw = bis.readLine();
			if(tagsRaw == null) break;
			
			tags = parseTags(tagsRaw);
			
			if(path.equals(imageName)) {
				break;
			}
		}
		
		try {
			bis.close();
		} catch (IOException ignorable) {
		}
		
		if(path == null || title == null || tags == null) return null;
		
		return new ImageInfo(path, title, tags);
	}

	/**
	 * Parses tags from string
	 * 
	 * @param tagsRaw string
	 * @return list of tags
	 */
	private static List<String> parseTags(String tagsRaw) {
		String[] splitted = tagsRaw.split(",");
		List<String> result = new ArrayList<>(splitted.length);
		
		for(int i = 0; i < splitted.length; ++i) {
			result.add(splitted[i].trim().toLowerCase());
		}
		
		return result;
	}

	/**
	 * Class which stores information about images
	 * 
	 * @author Hrvoje
	 *
	 */
	static class ImageInfo {
		String path, title;
		List<String> tags;

		/**
		 * Constructor for image info
		 * @param path of image
		 * @param title of image
		 * @param tags of image
		 */
		public ImageInfo(String path, String title, List<String> tags) {
			super();
			this.path = path;
			this.title = title;
			this.tags = tags;
		}
		
	}

	/**
	 * Returns File from path with name
	 * 
	 * @param path Path
	 * @param name Name
	 * @return Path to file
	 * @throws IOException if path is not a readable directory
	 */
	public static Path getFile(Path path, String name) throws IOException {
		Optional<Path> result = Files.walk(path).filter(p -> p.getName(p.getNameCount() -1).toString().equals(name)).findFirst();
		
		return (result.isPresent())? result.get(): null;
	}

}
