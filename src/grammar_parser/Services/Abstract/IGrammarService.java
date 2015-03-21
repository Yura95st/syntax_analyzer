package grammar_parser.Services.Abstract;

import grammar_parser.Models.Grammar;
import grammar_parser.Models.Rule;
import grammar_parser.Models.Word;

import java.util.Set;

public interface IGrammarService
{
	/**
	 * Gets the FIRST set of words for the specified rule.
	 *
	 * @param grammar
	 *            the grammar
	 * @param rule
	 *            the rule
	 * @return the FIRST set of words
	 */
	Set<Word> getFirstSet(Grammar grammar, Rule rule) throws Exception;

	/**
	 * Gets the right recursive rules from specified grammar.
	 *
	 * @param grammar
	 *            the grammar
	 * @return the right recursive rules
	 */
	Set<Rule> getRightRecursiveRules(Grammar grammar);
}
