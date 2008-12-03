package at.phudy.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;



@Entity
public class Catalog {

	@Id
	@GeneratedValue
	private Long id;
	
	private String title;
	
	@OneToMany(targetEntity=at.phudy.model.Exercise.class, fetch=FetchType.EAGER)
	private List<Exercise> exercises;

	
	@Override
	public boolean equals(final Object other) {
		if(this == other) {
			return true;
		}
		if((other instanceof Catalog) == false) {
			return false;
		}
		final Catalog that = (Catalog) other;
		return this.id.longValue() == that.id.longValue();
	}
	
	@Override
	public int hashCode() {
		return this.id.intValue() * 7 + this.title.hashCode();
	}
	
	@Override
	public String toString() {
//		final StringBuilder sb = new StringBuilder();
//		for (final Exercise ex : this.exercises) {
//			sb.append(ex.toString());
//		}
		return "Catalog[" +
				"id=" + this.id + "; " +
				"title=" + this.title+ "; " +
				"exercises=" + this.exercises.size() + "]";
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

	public List<Exercise> getExercises() {
		return this.exercises;
	}

	public void setExercises(List<Exercise> exercises) {
		this.exercises = exercises;
	}
	
	
}
