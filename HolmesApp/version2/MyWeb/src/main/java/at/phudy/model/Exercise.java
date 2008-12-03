package at.phudy.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Exercise {

	@Id
	@GeneratedValue
	private Long id;
	
	private String title;

	@ManyToOne(targetEntity=at.phudy.model.Catalog.class, fetch=FetchType.EAGER)
	private Catalog catalog;

	

	@Override
	public boolean equals(final Object other) {
		if(this == other) {
			return true;
		}
		if((other instanceof Exercise) == false) {
			return false;
		}
		final Exercise that = (Exercise) other;
		return this.id.longValue() == that.id.longValue();
	}
	
	@Override
	public int hashCode() {
		return this.id.intValue() * 7 + this.title.hashCode();
	}
	@Override
	public String toString() {
		return "Exercise[id=" + this.id + "; title=" + this.title+ "; catalog=" + this.catalog + "]";
	}
	
	
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}


	public Catalog getCatalog() {
		return this.catalog;
	}

	public void setCatalog(Catalog catalog) {
		this.catalog = catalog;
	}
	
	
}
