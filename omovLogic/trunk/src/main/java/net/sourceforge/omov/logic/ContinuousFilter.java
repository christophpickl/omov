package net.sourceforge.omov.logic;

import net.sourceforge.omov.core.bo.Movie;

public class ContinuousFilter {

	private final String searchTerm;
	
	private final ContinuousFilterField filterField;

	private final String toString;

	public ContinuousFilter(String searchTerm, ContinuousFilterField filterField) {
		this.searchTerm = searchTerm;
		this.filterField = filterField;
		
		this.toString = "ContinuousFilter[searchTerm="+searchTerm+";filterField="+filterField+"]";
	}
	
	public boolean isMatching(Movie movie) {
		return this.getFilterField().isMatching(movie, this.searchTerm);
	}


	public String getSearchTerm() {
		return searchTerm;
	}


	public ContinuousFilterField getFilterField() {
		return filterField;
	}
	
	
	@Override
	public String toString() {
		return toString;
	}
	
	
	
	
	
	
	
	
	
	private static abstract class FilterLogic {
		abstract boolean isMatching(Movie movie, String searchTerm);
	}
	
	
	
	
	public enum ContinuousFilterField {
		ALL(new FilterLogic() {
			@Override
			boolean isMatching(Movie movie, String searchTerm) {
				if(TITLE.isMatching(movie, searchTerm) ||
				   PEOPLE.isMatching(movie, searchTerm) ||
				   COMMENT.isMatching(movie, searchTerm)) {
					return true;
				}
				return false;
			}
		}),
		TITLE(new FilterLogic() {
			@Override
			boolean isMatching(Movie movie, String searchTerm) {
				return movie.getTitle().toLowerCase().contains(searchTerm.toLowerCase());
			}
		}),
		PEOPLE(new FilterLogic() {
			@Override
			boolean isMatching(Movie movie, String searchTerm) {
				final String searchTermLowered = searchTerm.toLowerCase();
				if(movie.getDirector().toLowerCase().contains(searchTermLowered)) {
					return true;
				}
				
				for(String actor : movie.getActors()) {
					if(actor.toLowerCase().contains(searchTermLowered)) {
						return true;
					}
				}
				
				return false;
			}
		}),
		COMMENT(new FilterLogic() {
			@Override
			boolean isMatching(Movie movie, String searchTerm) {
				return movie.getComment().toLowerCase().contains(searchTerm.toLowerCase());
			}
		});
		
		private final FilterLogic logic;
		ContinuousFilterField(FilterLogic logic) {
			this.logic = logic;
		}
		
		boolean isMatching(Movie movie, String searchTerm) {
			return this.logic.isMatching(movie, searchTerm);
		}
	}
}
