package syntax_analyzer.Services.Abstract;

import java.util.Set;

import syntax_analyzer.Models.Grammar;
import syntax_analyzer.Models.Rule;

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
