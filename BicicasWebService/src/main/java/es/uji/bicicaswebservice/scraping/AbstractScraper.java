package es.uji.bicicaswebservice.scraping;

import org.htmlcleaner.TagNode;

import es.uji.geotec.httputils.WebHelper;

/**
 * Abstract scraping template.
 *
 */
public abstract class AbstractScraper<T> {

	public T performScraping() {
		return scrape(WebHelper.cleanHTML(getWebpage()));
	}
	
	protected abstract T scrape(final TagNode root);
	protected abstract String getWebpage();
}