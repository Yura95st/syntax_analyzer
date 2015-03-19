package grammar_parser.Services.Abstract;

import java.util.Set;

import grammar_parser.Models.Grammar;
import grammar_parser.Models.Rule;

public interface IGrammarService
{
	/**
	 * Gets the right recursive rules from specified grammar.
	 *
	 * @param grammar
	 *            the grammar
	 * @return the right recursive rules
	 */
	Set<Rule> getRightRecursiveRules(Grammar grammar);
}
