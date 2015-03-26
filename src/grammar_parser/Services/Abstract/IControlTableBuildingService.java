package grammar_parser.Services.Abstract;

import grammar_parser.Exceptions.GrammarIsNotLLOneGrammarException;
import grammar_parser.Models.ControlTableItem;
import grammar_parser.Models.Grammar;
import grammar_parser.Models.Rule;

import java.util.Map;

public interface IControlTableBuildingService
{

	/**
	 * Builds the control table for the specified grammar.
	 *
	 * @param grammar
	 *            the grammar
	 * @return the control table
	 * @throws GrammarIsNotLLOneGrammarException
	 *             the grammar is not LL(1)-grammar exception
	 */
	Map<ControlTableItem, Rule> buildControlTable(Grammar grammar)
			 throws Exception;
}
