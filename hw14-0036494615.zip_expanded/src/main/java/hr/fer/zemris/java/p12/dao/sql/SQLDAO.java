package hr.fer.zemris.java.p12.dao.sql;

import hr.fer.zemris.java.p12.Inicijalizacija;
import hr.fer.zemris.java.p12.dao.DAO;
import hr.fer.zemris.java.p12.dao.DAOException;
import hr.fer.zemris.java.p12.model.Poll;
import hr.fer.zemris.java.p12.model.VoteOption;
import hr.fer.zemris.java.p12.model.VoteResult;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * Ovo je implementacija podsustava DAO uporabom tehnologije SQL. Ova
 * konkretna implementacija očekuje da joj veza stoji na raspolaganju
 * preko {@link SQLConnectionProvider} razreda, što znači da bi netko
 * prije no što izvođenje dođe do ove točke to trebao tamo postaviti.
 * U web-aplikacijama tipično rješenje je konfigurirati jedan filter 
 * koji će presresti pozive servleta i prije toga ovdje ubaciti jednu
 * vezu iz connection-poola, a po zavrsetku obrade je maknuti.
 *  
 * @author marcupic
 */
public class SQLDAO implements DAO {

	@Override
	public List<Poll> getPolls() throws DAOException {
		List<Poll> polls = new LinkedList<>();
		
		Connection con = SQLConnectionProvider.getConnection();
		PreparedStatement pst = null;
		
		try {
			pst = con.prepareStatement("select id, title, message " +
					"from " + Inicijalizacija.pollsTable
					);
			try {
				ResultSet rs = pst.executeQuery();
				try {
					while(rs!=null && rs.next()) {
						int id = rs.getInt(1);
						String title = rs.getString(2);
						String message = rs.getString(3);
						polls.add(new Poll(id, title, message));
					}
				} finally {
					try { rs.close(); } catch(Exception ignorable) {}
				}
			} finally {
				try { pst.close(); } catch(Exception ignorable) {}
			}
		} catch(Exception ex) {
			throw new DAOException("Pogreška prilikom dohvata liste korisnika.", ex);
		}

		return polls;
	}

	
	@Override
	public List<VoteOption> getVoteOptions(int pollID) throws DAOException {
		List<VoteOption> voteOptions = new LinkedList<>();
		
		Connection con = SQLConnectionProvider.getConnection();
		PreparedStatement pst = null;
		
		try {
			pst = con.prepareStatement("select id, optionTitle, optionLink " +
					"from " + Inicijalizacija.pollOptions +
					" where pollID=" + pollID
					);
			try {
				ResultSet rs = pst.executeQuery();
				try {
					while(rs!=null && rs.next()) {
						int id = rs.getInt(1);
						String title = rs.getString(2);
						String link = rs.getString(3);
						VoteOption tmp = new VoteOption(String.valueOf(id), title, link);
						voteOptions.add(tmp);
					}
				} finally {
					try { rs.close(); } catch(Exception ignorable) {}
				}
			} finally {
				try { pst.close(); } catch(Exception ignorable) {}
			}
		} catch(Exception ex) {
			throw new DAOException("Pogreška prilikom dohvata liste korisnika.", ex);
		}

		return voteOptions;
	}


	@Override
	public void addVote(int voteID) throws DAOException {
		Connection con = SQLConnectionProvider.getConnection();
		PreparedStatement pst = null;
		
			try {
				pst = con.prepareStatement(
						"UPDATE " + Inicijalizacija.pollOptions + 
						" SET votesCount = votesCount + 1 " +
						"WHERE id=" + voteID);
				pst.executeUpdate();
			}catch(SQLException e) {throw new DAOException(e);}
			finally {
				try { pst.close(); } catch(Exception ignorable) {}
			}
	}


	@Override
	public List<VoteResult> getVotingResults(int pollID) throws DAOException {
		
		List<VoteResult> results = new LinkedList<>();
		
		Connection con = SQLConnectionProvider.getConnection();
		PreparedStatement pst = null;
		
		try {
			pst = con.prepareStatement(
					"SELECT ID, optionTitle, optionLink, votesCount"
					+ " from " + Inicijalizacija.pollOptions
					+ " where pollID=" + pollID);
			ResultSet rs = pst.executeQuery();
			while(rs.next()) {
				int ID = rs.getInt(1);
				String title = rs.getString(2);
				String link = rs.getString(3);
				int counter = rs.getInt(4);
				results.add(new VoteResult(String.valueOf(ID), title, link, counter));
			}
		} catch(Exception e) { 
			throw new DAOException(e);
		}
		
		return results;
	}


	@Override
	/**
	 * Returns title and message of poll with pollID, separated with ':'.
	 * Returns <code>null</code> if no meta is found
	 */
	public String[] getPollMeta(Integer pollID) throws DAOException {
		Connection con = SQLConnectionProvider.getConnection();
		PreparedStatement pst = null;
		
		try {
			pst = con.prepareStatement(
					"SELECT title, message"
					+ " from " + Inicijalizacija.pollsTable
					+ " where id =" + pollID);
			
			ResultSet rs = pst.executeQuery();
			if(rs.next()) {
				String[] result = new String[2];
				result[0] = rs.getString(1);
				result[1] = rs.getString(2);
				return result;
			}
		} catch(Exception e) { 
			throw new DAOException(e);
		}
		
		return null;
	}

}
