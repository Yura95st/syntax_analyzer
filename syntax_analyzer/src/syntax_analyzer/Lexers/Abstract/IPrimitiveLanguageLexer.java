package syntax_analyzer.Lexers.Abstract;

import grammar_parser.Lexers.Abstract.ILexer;
import syntax_analyzer.Models.Token;
import syntax_analyzer.Models.TokenDefinition;

public interface IPrimitiveLanguageLexer extends ILexer<Token>
{
	/**
	 * Gets the keywords.
	 *
	 * @return the keywords
	 */
	Iterable<String> getKeywords();

	/**
	 * Gets the token definitions.
	 *
	 * @return the token definitions
	 */
	Iterable<TokenDefinition> getTokenDefinitions();

	/**
	 * Sets the keywords.
	 *
	 * @param keywords
	 *            the new keywords
	 */
	void setKeywords(Iterable<String> keywords);

	/**
	 * Sets the token definitions.
	 *
	 * @param tokenDefinitions
	 *            the new token definitions
	 */
	void setTokenDefinitions(Iterable<TokenDefinition> tokenDefinitions);
}
