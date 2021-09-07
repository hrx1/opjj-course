package hr.fer.zemris.java.p12.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Class describes Vote Option.
 * Every Vote Option has it's ID, name and song
 * 
 * @author Hrvoje
 *
 */
public class VoteOption {
	/* Vote option parameters */
	public String ID, name, description;

	/**
	 * Constructor for Vote Option
	 * 
	 * @param ID of Vote option
	 * @param name of vote option
	 * @param song of vote option
	 */
	public VoteOption(String ID, String name, String description) {
		super();
		this.ID = ID;
		this.name = name;
		this.description = description;
	}

	/**
	 * Getter for ID
	 * 
	 * @return ID
	 */
	public String getID() {
		return ID;
	}

	/**
	 * Getter for Name
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Getter for Song
	 * 
	 * @return song
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Parses Vote Options from file.
	 * ID, name and description should be separated with '\t' character.
	 * 
	 * @param fileName of file
	 * @return List of Vote Options parsed from a File
	 * @throws IOException if File cannot be read.
	 */
	public static List<VoteOption> parseVoteOptions(String fileName) throws IOException {
		List<String> lines = Files.readAllLines(Paths.get(fileName));
		List<VoteOption> voteOptions = new ArrayList<>(lines.size());
		
		for(String line : lines) {
			String[] parsed = line.split("\t");
			VoteOption tmp = new VoteOption(parsed[0], parsed[1], parsed[2]);
			voteOptions.add(tmp);
		}
		
		voteOptions.sort((o1, o2) -> o1.ID.compareTo(o2.ID));
		
		return voteOptions;
	}

}
