package syntax_analyzer.Analyzers.Concrete;

import grammar_parser.Enums.NodeKind;
import grammar_parser.Exceptions.GrammarIsNotLLOneGrammarException;
import grammar_parser.Exceptions.NodeIsNotNonterminalException;
import grammar_parser.Exceptions.NodeIsNotTerminalException;
import grammar_parser.Models.ControlTableItem;
import grammar_parser.Models.Grammar;
import grammar_parser.Models.Node;
import grammar_parser.Models.Rule;
import grammar_parser.Models.Word;
import grammar_parser.Services.Abstract.IControlTableBuildingService;
import grammar_parser.Utils.Guard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import syntax_analyzer.Analyzers.Abstract.ISyntaxAnalyzer;
import syntax_analyzer.Exceptions.GrammarIsInvalidException;
import syntax_analyzer.Exceptions.GrammarIsNotSetException;
import syntax_analyzer.Exceptions.SyntaxAnalyzerErrorException;
import syntax_analyzer.Models.SyntaxAnalyzerResult;
import syntax_analyzer.Models.Token;

public class SyntaxAnalyzer implements ISyntaxAnalyzer
{
	private final IControlTableBuildingService _controlTableBuildingService;

	private Grammar _grammar;

	private List<Token> _tokens;

	public SyntaxAnalyzer(
		IControlTableBuildingService controlTableBuildingService)
	{
		Guard.notNull(controlTableBuildingService,
			"controlTableBuildingService");

		this._controlTableBuildingService = controlTableBuildingService;

		this._grammar = null;
		this._tokens = new ArrayList<Token>();
	}

	@Override
	public Grammar getGrammar()
	{
		return this._grammar;
	}

	@Override
	public List<Token> getTokens()
	{
		return this._tokens;
	}

	@Override
	public SyntaxAnalyzerResult run() throws Exception
	{
		this.checkGrammar();

		SyntaxAnalyzerResult result = new SyntaxAnalyzerResult(true, null);

		int tokensCount = this._tokens.size();

		int currentTokenId = 0;

		Map<ControlTableItem, Rule> controlTable = this.getControlTable();

		Stack<Node> nodesStack = new Stack<Node>();

		nodesStack.push(this._grammar.getHeadRule().getHeadNode());

		while (!nodesStack.isEmpty())
		{
			Node topNode = nodesStack.peek();

			NodeKind topNodeKind = topNode.getKind();

			if (topNodeKind == NodeKind.Nonterminal)
			{
				Rule nextRule = null;

				if (currentTokenId < tokensCount)
				{
					// Try to get nextRule based on current token.
					nextRule =
						this.getNextRule(topNode, this
								.getWordFromToken(this._tokens
										.get(currentTokenId)), controlTable);
				}

				if (nextRule == null)
				{
					// Try to get nextRule based on empty token.
					nextRule =
						this.getNextRule(topNode, Word.getEmptyWord(),
							controlTable);
				}

				if (nextRule != null)
				{
					nodesStack.pop();

					nodesStack.addAll(this.getNodesToStack(nextRule));
				}
				else
				{
					break;
				}
			}
			else if (topNodeKind == NodeKind.Terminal)
			{
				if (currentTokenId < tokensCount
					&& topNode.getText().equals(
						this._tokens.get(currentTokenId).getValue()))
				{
					nodesStack.pop();

					currentTokenId++;
				}
				else
				{
					break;
				}
			}
		}

		if (currentTokenId < tokensCount)
		{
			result =
				new SyntaxAnalyzerResult(false,
					this._tokens.get(currentTokenId));
		}
		else if (!nodesStack.isEmpty())
		{
			result = new SyntaxAnalyzerResult(false, null);
		}

		return result;
	}

	@Override
	public void setGrammar(Grammar grammar) throws GrammarIsInvalidException
	{
		Guard.notNull(grammar, "grammar");

		if (grammar.getRulesDictionary().size() == 0)
		{
			throw new GrammarIsInvalidException("Grammar can't be empty.");
		}

		if (grammar.getHeadRule() == null)
		{
			throw new GrammarIsInvalidException(
				String.format("Grammar's headRule can't be null."));
		}

		this._grammar = grammar;
	}

	@Override
	public void setTokens(List<Token> tokens)
	{
		Guard.notNull(tokens, "tokens");

		this._tokens = new ArrayList<Token>(tokens);
	}

	private void checkGrammar() throws GrammarIsNotSetException
	{
		if (this._grammar == null)
		{
			throw new GrammarIsNotSetException("Grammar can't be null.");
		}
	}

	private Map<ControlTableItem, Rule> getControlTable() throws Exception
	{
		Map<ControlTableItem, Rule> controlTable = null;

		try
		{
			controlTable =
				this._controlTableBuildingService
						.buildControlTable(this._grammar);
		}
		catch (GrammarIsNotLLOneGrammarException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			throw new SyntaxAnalyzerErrorException(
				"Error occured in ControlTableBuildingService.", e);
		}

		return controlTable;
	}

	private Rule getNextRule(Node node, Word word,
		Map<ControlTableItem, Rule> controlTable)
		throws NodeIsNotNonterminalException
	{
		ControlTableItem controlTableItem = new ControlTableItem(node, word);

		Rule nextRule = controlTable.get(controlTableItem);

		return nextRule;
	}

	private List<Node> getNodesToStack(Rule rule)
	{
		List<Node> nodes = rule.getNodes();

		Collections.reverse(nodes);

		return nodes;
	}

	private Word getWordFromToken(Token token)
		throws NodeIsNotTerminalException
	{
		Node node = new Node(NodeKind.Terminal, token.getValue());

		Word word = new Word(node);

		return word;
	}

}
