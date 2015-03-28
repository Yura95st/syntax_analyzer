package grammar_parser.Parsers.Concrete;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import grammar_parser.Enums.NodeKind;
import grammar_parser.Enums.SpecialNodeKind;
import grammar_parser.Exceptions.InvalidGrammarException;
import grammar_parser.Exceptions.SpecialNodeIsNotDefinedException;
import grammar_parser.Models.Grammar;
import grammar_parser.Models.Node;
import grammar_parser.Models.Rule;
import grammar_parser.Parsers.Abstract.IGrammarParser;
import grammar_parser.Utils.Guard;

public class GrammarParser implements IGrammarParser
{
	private int _currentNodeId;

	private Grammar _grammar;

	private List<Node> _nodes;

	private Stack<Rule> _rules;

	private Map<SpecialNodeKind, Node> _specialNodesDictionary;

	private int _state;

	public GrammarParser() throws SpecialNodeIsNotDefinedException
	{
		this._nodes = new ArrayList<Node>();

		this._currentNodeId = 0;

		this.setSpecialNodesDictionary(new HashMap<SpecialNodeKind, Node>() {
			{
				this.put(SpecialNodeKind.Alternation, new Node(
					NodeKind.Delimiter, "|"));

				this.put(SpecialNodeKind.Concatenation, new Node(
					NodeKind.Delimiter, ","));

				this.put(SpecialNodeKind.Definition, new Node(
					NodeKind.Delimiter, "="));

				this.put(SpecialNodeKind.Termination, new Node(
					NodeKind.Delimiter, "."));
			}
		});
	}

	@Override
	public List<Node> getNodes()
	{
		return this._nodes;
	}

	@Override
	public Map<SpecialNodeKind, Node> getSpecialNodesDictionary()
	{
		return this._specialNodesDictionary;
	}

	@Override
	public Grammar parse() throws Exception
	{
		this.initialize();

		this.processRules();

		this.processGrammar();

		return this._grammar;
	}

	@Override
	public void setNodes(List<Node> nodes)
	{
		Guard.notNull(nodes, "nodes");

		this._nodes = new ArrayList<Node>(nodes);

		this._currentNodeId = 0;
	}

	@Override
	public void setSpecialNodesDictionary(
		Map<SpecialNodeKind, Node> specialNodesDictionary)
				throws SpecialNodeIsNotDefinedException
	{
		Guard.notNull(specialNodesDictionary, "specialNodesDictionary");

		this._specialNodesDictionary = new HashMap<SpecialNodeKind, Node>(
				specialNodesDictionary);

		for (SpecialNodeKind kind : SpecialNodeKind.values())
		{
			if (this._specialNodesDictionary.get(kind) == null)
			{
				throw new SpecialNodeIsNotDefinedException(String.format(
					"Special node with kind '%1$s' must be defined.", kind));
			}
		}
	}

	private void complexRuleStep() throws InvalidGrammarException
	{
		Node currentNode = this.getCurrentNode();

		if (currentNode.equals(this._specialNodesDictionary
			.get(SpecialNodeKind.Termination)))
		{
			this._state = 0;
		}
		else if (currentNode.equals(this._specialNodesDictionary
			.get(SpecialNodeKind.Concatenation)))
		{
			this._state = 4;
		}
		else if (currentNode.equals(this._specialNodesDictionary
			.get(SpecialNodeKind.Alternation)))
		{
			Rule lastRule = this._rules.peek();

			this._rules.push(new Rule(lastRule.getHeadNode()));

			this._state = 2;
		}
		else
		{
			throw new InvalidGrammarException(
				String.format(
					"Invalid grammar at node '%1$s'. Expected alternation, concatenation or termination node.",
					currentNode.getText()));
		}

	}

	private void createRuleStep() throws InvalidGrammarException
	{
		Node currentNode = this.getCurrentNode();

		if (currentNode.getKind() != NodeKind.Nonterminal)
		{
			throw new InvalidGrammarException(
				String.format(
					"Invalid grammar at node '%1$s'. Rule must start only from nonterminal.",
					currentNode.getText()));
		}

		this._rules.push(new Rule(currentNode));

		this._state = 1;
	}

	private void defineRuleStep() throws InvalidGrammarException
	{
		Node currentNode = this.getCurrentNode();

		if (!currentNode.equals(this._specialNodesDictionary
				.get(SpecialNodeKind.Definition)))
		{
			throw new InvalidGrammarException(String.format(
				"Invalid grammar at node '%1$s'. Invalid rule definition.",
				currentNode.getText()));
		}

		this._state = 2;
	}

	private Node getCurrentNode()
	{
		Node currentNode = this._nodes.get(this._currentNodeId);

		return currentNode;
	}

	private void initialize()
	{
		this._grammar = new Grammar();

		this._rules = new Stack<Rule>();

		this._currentNodeId = 0;

		this._state = 0;
	}

	private boolean isInBounds()
	{
		return this._currentNodeId < this._nodes.size();
	}

	private void multipleNodesStep() throws InvalidGrammarException
	{
		Node currentNode = this.getCurrentNode();

		if (currentNode.getKind() != NodeKind.Nonterminal
			&& currentNode.getKind() != NodeKind.Terminal)
		{
			throw new InvalidGrammarException(
				String.format(
					"Invalid grammar at node '%1$s'. Expected terminal or nonterminal node.",
					currentNode.getText()));
		}

		Rule lastRule = this._rules.peek();

		lastRule.addNode(currentNode);

		this._state = 3;
	}

	private void processGrammar() throws Exception
	{
		for (Rule rule : this._rules)
		{
			// Add only unique rules to the grammar
			if (!this._grammar.getRules(rule.getHeadNode()).contains(rule))
			{
				this._grammar.addRule(rule);
			}
		}

		// Set head rule
		if (this._rules.size() > 0)
		{
			this._grammar.setHeadRule(this._rules.get(0));
		}
	}

	private void processRules() throws InvalidGrammarException
	{
		while (this.isInBounds())
		{
			switch (this._state)
			{
				case 0:
				{
					this.createRuleStep();
					break;
				}

				case 1:
				{
					this.defineRuleStep();
					break;
				}
				case 2:
				{
					this.simpleRuleStep();
					break;
				}

				case 3:
				{
					this.complexRuleStep();
					break;
				}

				case 4:
				{
					this.multipleNodesStep();
					break;
				}
			}

			this._currentNodeId++;
		}

		if (this._state != 0)
		{
			Rule lastRule = this._rules.peek();

			throw new InvalidGrammarException(String.format(
				"Invalid grammar at rule with head node '%1$s'.", lastRule
				.getHeadNode().getText()));
		}
	}

	private void simpleRuleStep() throws InvalidGrammarException
	{
		Node currentNode = this.getCurrentNode();

		if (currentNode.equals(this._specialNodesDictionary
			.get(SpecialNodeKind.Termination)))
		{
			this._state = 0;
		}
		else if (currentNode.getKind() == NodeKind.Nonterminal
				|| currentNode.getKind() == NodeKind.Terminal)
		{
			Rule lastRule = this._rules.peek();

			lastRule.addNode(currentNode);

			this._state = 3;
		}
		else
		{
			throw new InvalidGrammarException(
				String.format(
					"Invalid grammar at node '%1$s'. Expected terminal, nonterminal or termination node.",
					currentNode.getText()));
		}
	}
}