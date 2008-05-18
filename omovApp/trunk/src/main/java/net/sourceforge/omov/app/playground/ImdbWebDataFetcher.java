package net.sourceforge.omov.app.playground;

import java.util.Arrays;
import java.util.List;

import net.sourceforge.omov.webApi.IWebDataFetcher;
import net.sourceforge.omov.webApi.WebDataFetcherFactory;
import net.sourceforge.omov.webApi.WebSearchResult;

public class ImdbWebDataFetcher  {
	
	public static void main(String[] args) throws Exception {
		IWebDataFetcher fetcher = WebDataFetcherFactory.newWebDataFetcher();
		List<WebSearchResult> os = fetcher.search("Matrix");
		System.out.println(Arrays.toString(os.toArray()));
	}
	
}
