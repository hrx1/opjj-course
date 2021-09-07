package hr.fer.zemris.java.p12.model;

/**
 * Class abstracts Vote Result
 * @author Hrvoje
 *
 */
public class VoteResult extends VoteOption implements Comparable<VoteResult> {
	/** Number of votes */
	public int voteNumber;

	/**
	 * Constructor for Vote Option
	 * 
	 * @param ID of Vote option
	 * @param name of vote option
	 * @param song of vote option
	 */
	public VoteResult(String ID, String name, String description, int voteNumber) {
		super(ID, name, description);
		this.voteNumber = voteNumber;
	}
	
	/**
	 * Getter for Vote Number
	 * @return
	 */
	public int getVoteNumber() {
		return voteNumber;
	}

	@Override
	public int compareTo(VoteResult other) {
		return other.voteNumber - this.voteNumber;
	}
	
}
