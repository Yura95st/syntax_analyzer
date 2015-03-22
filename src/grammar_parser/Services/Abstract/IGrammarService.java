package grammar_parser.Services.Abstract;

import grammar_parser.Models.Grammar;
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
	Map<Rule, Set<Word>> getFirstSetDictionary(Grammar grammar) throws Exception;

	/**
	 * Gets the right recursive rules from specified grammar.
	 *
	 * @param grammar
	 *            the grammar
	 * @return the right recursive rules
	 */
	Set<Rule> getRightRecursiveRules(Grammar grammar);
}