/*
 *
 */
package grammar_parser.Helpers.TableBuilder;

import grammar_parser.Helpers.TableBuilder.Models.TableBuilderSettings;

import java.util.List;

public interface ITableBuilder
{
	/**
	 * Builds the table.
	 *
	 * @return the string representation of the table
	 */
	String buildTable();

	/**
	 * Gets the table's content.
	 */
	List<List<String>> getContent();

	/**
	 * Gets the table header's captions.
	 */
	List<String> getHeaderCaptions();

	/**
	 * Sets the table's content.
	 *
	 * @param content
	 *            the content
	 */
	void setContent(Iterable<Iterable<String>> content);

	/**
	 * Sets the table header's captions.
	 *
	 * @param headerCaptions
	 *            the new header captions
	 */
	void setHeaderCaptions(Iterable<String> headerCaptions);

	/**
	 * Sets the settings.
	 *
	 * @param settings
	 *            the new settings
	 */
	void setSettings(TableBuilderSettings settings);
}
