package hr.fer.zemris.java.tecaj_13.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Has following properties:  id, firstName, lastName, nick, email and passwordHash.
 * @author Hrvoje
 *
 */
@NamedQueries({
	@NamedQuery(name="BlogUser.userByNick",query="select u from BlogUser as u where u.nick=:nick"),
	@NamedQuery(name="BlogUser.getAll",query="select u from BlogUser as u")
})
@Entity
@Table(name="blog_user")
public class BlogUser {
	
	private Long id;
	private String firstName;
	private String lastName;
	private String nick;
	private String email;
	private String passwordHash;
	private List<BlogEntry> createdBlogs = new ArrayList<>();
	
	/**
	 * @return the id
	 */
	@Id @GeneratedValue
	public Long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @return the firstName
	 */
	@Column(length=20,nullable=false)
	public String getFirstName() {
		return firstName;
	}
	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	/**
	 * @return the lastName
	 */
	@Column(length=40,nullable=false)
	public String getLastName() {
		return lastName;
	}
	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	/**
	 * @return the nick
	 */
	@Column(length=20,nullable=false, unique=true)
	public String getNick() {
		return nick;
	}
	/**
	 * @param nick the nick to set
	 */
	public void setNick(String nick) {
		this.nick = nick;
	}
	/**
	 * @return the email
	 */
	@Column(length=30,nullable=false)
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return the passwordHash
	 */
	@Column(length=200,nullable=false)
	public String getPasswordHash() {
		return passwordHash;
	}
	/**
	 * @param passwordHash the passwordHash to set
	 */
	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}
	/**
	 * @return the createdBlogs
	 */
	@OneToMany(mappedBy="creator", cascade = CascadeType.PERSIST, orphanRemoval = true, fetch = FetchType.EAGER)
	public List<BlogEntry> getCreatedBlogs() {
		return createdBlogs;
	}
	/**
	 * @param createdBlogs the createdBlogs to set
	 */
	public void setCreatedBlogs(List<BlogEntry> createdBlogs) {
		this.createdBlogs = createdBlogs;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BlogUser other = (BlogUser) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return 	id + "\n" + 
				firstName + "\n" + 
				lastName + "\n" + 
				nick + "\n" + 
				email + "\n" + 
				passwordHash + "\n";
	}
	
	
	/**
	 * Adds blog
	 * @param blog to add
	 */
	public void addCreatedBlog(BlogEntry blog) {
		Objects.requireNonNull(blog);
		
		this.createdBlogs.add(blog);
	}
}
