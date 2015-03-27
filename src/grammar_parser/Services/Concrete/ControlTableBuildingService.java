package grammar_parser.Services.Concrete;

import grammar_parser.Exceptions.GrammarIsNotLLOneGrammarException;
import grammar_parser.Models.ControlTableItem;
import grammar_parser.Models.Grammar;
import grammar_parser.Models.Node;
import grammar_parser.Models.Rule;
import grammar_parser.Models.Word;
import grammar_parser.Services.Abstract.IControlTableBuildingService;
import grammar_parser.Services.Abstract.IGrammarService;
import grammar_parser.Utils.Guard;
import grammar_parser.Utils.SetUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class ControlTableBuildingService implements
		IControlTableBuildingService
{
	private final IGrammarService _grammarService;

	public ControlTableBuildingService(IGrammarService grammarService)
	{
		Guard.notNull(grammarService, "grammarService");

		this._grammarService = grammarService;
	}

	@Override
	public Map<ControlTableItem, Rule> buildControlTable(Grammar grammar)
		throws Exception
	{
		Guard.notNull(grammar, "grammar");

		Map<ControlTableItem, Rule> controlTable =
			new HashMap<ControlTableItem, Rule>();

		if (grammar.getRulesDictionary().size() > 0)
		{
			Map<Node, Set<Word>> firstSetDictionary =
				this._grammarService.getFirstSetDictionary(grammar);
			Map<Node, Set<Word>> followSetDictionary =
				this._grammarService.getFollowSetDictionary(grammar);

			if (!this._grammarService.isLLOneGrammar(grammar, firstSetDictionary,
				followSetDictionary))
			{
				throw new GrammarIsNotLLOneGrammarException(
					String.format("Grammar does not satisfy the conditions of the LL(1)-grammar."));
			}
		}

		return controlTable;
	}
}
