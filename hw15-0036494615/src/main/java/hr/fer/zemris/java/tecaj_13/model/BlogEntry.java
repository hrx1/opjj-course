package hr.fer.zemris.java.tecaj_13.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Class defines Blog Entry.
 * 
 * @author Hrvoje
 *
 */
@NamedQueries({
	@NamedQuery(name="BlogEntry.upit1",query="select b from BlogComment as b where b.blogEntry=:be and b.postedOn>:when")
})
@Entity
@Table(name="blog_entries")
@Cacheable(true)
public class BlogEntry {
	/** Blog ID */
	private Long id;
	/** Blog comments */
	private List<BlogComment> comments = new ArrayList<>();
	/** Time of Creation */
	private Date createdAt;
	/** Time of last modification */
	private Date lastModifiedAt;
	/** Title */
	private String title;
	/** Text */
	private String text;
	/** Creator of blog entry */
	private BlogUser creator;
	
	/**
	 * ID getter
	 * 
	 * @return ID
	 */
	@Id @GeneratedValue
	public Long getId() {
		return id;
	}
	
	/**
	 * ID Setter
	 * @param id ID
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Comments getter
	 * @return comments
	 */
	/* fetch=FetchType.LAZY, TODO */
	@OneToMany(mappedBy="blogEntry", fetch=FetchType.EAGER, cascade=CascadeType.PERSIST, orphanRemoval=true)
	@OrderBy("postedOn")
	public List<BlogComment> getComments() {
		return comments;
	}
	
	/**
	 * Comments setter
	 * @param comments Comments
	 */
	public void setComments(List<BlogComment> comments) {
		this.comments = comments;
	}

	/**
	 * Time of creation getter
	 * @return time of creation
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable=false)
	public Date getCreatedAt() {
		return createdAt;
	}

	/**
	 * Sets time of last modification
	 * @param createdAt time of last modification
	 */
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	/**
	 * Getter for time of last modification
	 * @return time of last modification
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable=true)
	public Date getLastModifiedAt() {
		return lastModifiedAt;
	}

	/**
	 * Setter for last modification
	 * @param lastModifiedAt time of last modification
	 */
	public void setLastModifiedAt(Date lastModifiedAt) {
		this.lastModifiedAt = lastModifiedAt;
	}

	/**
	 * Blog title getter
	 * @return title
	 */
	@Column(length=200,nullable=false)
	public String getTitle() {
		return title;
	}

	/**
	 * Blog title setter
	 * @param title Title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Title getter
	 * @return title
	 */
	@Column(length=4096,nullable=false)
	public String getText() {
		return text;
	}

	/**
	 * Text setter
	 * @param text Text
	 */
	public void setText(String text) {
		this.text = text;
	}
	
	/**
	 * Creator getter
	 * @return creator
	 */
	@ManyToOne
	@JoinColumn(nullable = false)
	public BlogUser getCreator() {
		return creator;
	}
	
	/**
	 * Creator setter
	 * @param user Creator
	 */
	public void setCreator(BlogUser user) {
		this.creator = user;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BlogEntry other = (BlogEntry) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}