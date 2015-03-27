package grammar_parser.Services.Abstract;

import grammar_parser.Models.Grammar;
import grammar_parser.Models.Node;
import grammar_parser.Models.Rule;
import grammar_parser.Models.Word;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IGrammarService
{
	/**
	 * Gets the first plus follow set.
	 *
	 * @param firstSet
	 *            the first set
	 * @param followSet
	 *            the follow set
	 * @return the first plus follow set
	 */
	Set<Word> getFirstPlusFollowSet(Set<Word> firstSet, Set<Word> followSet);

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
	 * Gets the first set for the specified list of nodes.
	 *
	 * @param nodes
	 *            the list of nodes
	 * @param firstSetDictionary
	 *            the first set dictionary
	 * @return the first set for the specified list of nodes
	 * @throws Exception
	 *             the exception
	 */
	Set<Word> getFirstSetForNodesList(List<Node> nodes,
		Map<Node, Set<Word>> firstSetDictionary) throws Exception;

	/**
	 * Gets the Follow set dictionary for the specified grammar.
	 *
	 * @param grammar
	 *            the grammar
	 * @return the Follow set dictionary
	 * @throws Exception
	 */
	Map<Node, Set<Word>> getFollowSetDictionary(Grammar grammar)
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

	/**
	 * Checks if the specified grammar is LL(1)-grammar.
	 *
	 * @param grammar
	 *            the grammar
	 * @param firstSetDictionary
	 *            the first set dictionary
	 * @param followSetDictionary
	 *            the follow set dictionary
	 * @return true, if the specified grammar is LL(1)-grammar, false -
	 *         otherwise
	 * @throws Exception
	 *             the exception
	 */
	boolean isLLOneGrammar(Grammar grammar,
		Map<Node, Set<Word>> firstSetDictionary,
		Map<Node, Set<Word>> followSetDictionary) throws Exception;
}