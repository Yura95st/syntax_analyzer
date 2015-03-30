package syntax_analyzer.Analyzers.Abstract;

import grammar_parser.Models.Grammar;

import java.util.List;

import syntax_analyzer.Exceptions.GrammarIsInvalidException;
import syntax_analyzer.Models.SyntaxAnalyzerResult;
import syntax_analyzer.Models.Token;

public interface ISyntaxAnalyzer
{
	/**
	 * Gets the grammar.
	 *
	 * @return the grammar
	 */
	Grammar getGrammar();

	/**
	 * Gets the tokens.
	 *
	 * @return the tokens
	 */
	List<Token> getTokens();

	/**
	 * Runs the syntax analyzer.
	 *
	 * @return the syntax analyzer result
	 * @throws Exception
	 */
	SyntaxAnalyzerResult run() throws Exception;

	/**
	 * Sets the grammar.
	 *
	 * @param grammar
	 *            the new grammar
	 * @throws GrammarIsInvalidException
	 */
	void setGrammar(Grammar grammar) throws GrammarIsInvalidException;

	/**
	 * Sets the tokens.
	 *
	 * @param tokens
	 *            the new tokens
	 */
	void setTokens(List<Token> tokens);
}
