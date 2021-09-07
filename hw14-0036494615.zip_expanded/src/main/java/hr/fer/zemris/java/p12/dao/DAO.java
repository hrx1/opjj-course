package hr.fer.zemris.java.p12.dao;


import java.util.List;

import hr.fer.zemris.java.p12.model.Poll;
import hr.fer.zemris.java.p12.model.VoteOption;
import hr.fer.zemris.java.p12.model.VoteResult;

/**
 * Interface towards Persistance Data Structures.
 * 
 * @author Hrvoje
 *
 */
public interface DAO {
	
	/**
	 * Returns Vote options for Poll. 
	 * 
	 * @param pollID Poll ID
	 * @return Vote Options
	 * @throws DAOException
	 */
	public List<VoteOption> getVoteOptions(int pollID) throws DAOException;
	
	/**
	 * Returns Polls
	 * 
	 * @return polls
	 * @throws DAOException
	 */
	public List<Poll> getPolls() throws DAOException;

	/**
	 * 
	 * @param voteID
	 * @throws DAOException
	 */
	public void addVote(int voteID) throws DAOException;

	/**
	 * Returns Voting Results for Poll.
	 * 
	 * @param pollID of Poll
	 * @return Voting Results for Poll
	 * @throws DAOException thrown when underlying structure fails
	 */
	public List<VoteResult> getVotingResults(int pollID) throws DAOException;

	/**
	 * Returns meta data about Poll in format 'title:message'
	 * 
	 * @param pollID of poll
	 * @return meta data about Poll in format 'title:message'
	 * @throws DAOException thrown when underlying structure fails 
	 */
	public String[] getPollMeta(Integer pollID) throws DAOException;
}
