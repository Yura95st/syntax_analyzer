package grammar_parser.Services.Abstract;

import grammar_parser.Models.Grammar;
import grammar_parser.Models.Node;
import grammar_parser.Models.Rule;
import grammar_parser.Models.Word;

import java.util.Map;
import java.util.Set;

public interface IGrammarService
{
	/**
	 * Gets the First set dictionary for the specified grammar.
	 *
	 * @param grammar
	 *            the grammar
	 * @return the First set dictionary
	 * @throws Exception
	 */
	Map<Node, Set<Word>> getFirstSetDictionary(Grammar grammar)
		throws Exception;

	/**
	 * Gets the nonterminal nodes for the specified grammar.
	 *
	 * @param grammar
	 *            the grammar
	 * @return the nonterminal nodes
	 */
	Set<Node> getNonterminalNodes(Grammar grammar);

	/**
	 * Gets the right recursive rules from specified grammar.
	 *
	 * @param grammar
	 *            the grammar
	 * @return the right recursive rules
	 */
	Set<Rule> getRightRecursiveRules(Grammar grammar);

	/**
	 * Gets the terminal nodes for the specified grammar.
	 *
	 * @param grammar
	 *            the grammar
	 * @return the terminal nodes
	 */
	Set<Node> getTerminalNodes(Grammar grammar);
}