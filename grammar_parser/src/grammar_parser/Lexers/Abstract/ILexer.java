package grammar_parser.Lexers.Abstract;

import java.util.List;

public interface ILexer<T>
{
	/**
	 * Gets the source.
	 *
	 * @return the source
	 */
	String getSource();

	/**
	 * Gets the space characters.
	 *
	 * @return the space characters
	 */
	Iterable<Character> getSpaceCharacters();

	/**
	 * Parses the source.
	 *
	 * @return the list of tokens, parsed from the source
	 */
	List<T> parse();

	/**
	 * Sets the source.
	 *
	 * @param source
	 *            the new source
	 */
	void setSource(String source);

	/**
	 * Sets the space characters.
	 *
	 * @param spaceCharacters
	 *            the new space characters
	 */
	void setSpaceCharacters(Iterable<Character> spaceCharacters);
}
