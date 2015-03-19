package syntax_analyzer.Parsers.Concrete;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import syntax_analyzer.Enums.NodeKind;
import syntax_analyzer.Enums.SpecialNodeKind;
import syntax_analyzer.Exceptions.InvalidGrammarException;
import syntax_analyzer.Exceptions.SpecialNodeIsNotDefinedException;
import syntax_analyzer.Models.Grammar;
import syntax_analyzer.Models.Node;
import syntax_analyzer.Models.Rule;
import syntax_analyzer.Parsers.Abstract.IGrammarParser;

public class GrammarParserTest
{
	private Map<SpecialNodeKind, Node> _specialNodesDictionary;

	IGrammarParser _grammarParser;

	@Test
	public void parse_GrammarRuleWithAlternatives_SplitsAlternativesToDifferentRules()
			throws Exception
	{
		// Arrange - create nodes
		Node nodeA = new Node(NodeKind.Nonterminal, "A");
		Node nodeB = new Node(NodeKind.Nonterminal, "B");
		Node nodeC = new Node(NodeKind.Terminal, "c");

		Node[] nodes = {
			nodeA,
			this._specialNodesDictionary.get(SpecialNodeKind.Definition),
			nodeB,
			this._specialNodesDictionary.get(SpecialNodeKind.Alternation),
			nodeC,
			this._specialNodesDictionary.get(SpecialNodeKind.Termination),
		}; // A = B | "c" .

		// Arrange - create rules
		Rule firstRule = new Rule(nodeA);

		firstRule.addNode(nodeB);

		Rule secondRule = new Rule(nodeA);

		secondRule.addNode(nodeC);

		// Arrange - create test grammar
		Grammar testGrammar = new Grammar();

		testGrammar.addRule(firstRule);
		testGrammar.addRule(secondRule);

		testGrammar.setHeadRule(firstRule);

		this._grammarParser.setNodes(Arrays.asList(nodes));

		// Act
		Grammar grammar = this._grammarParser.parse();

		// Assert
		Assert.assertEquals(testGrammar, grammar);
	}

	@Test(expected = InvalidGrammarException.class)
	public void parse_GrammarRuleWithInvalidAlternationSpecialNode_ThrowsInvalidGrammarException()
			throws Exception
	{
		// Arrange - create nodes
		Node nodeA = new Node(NodeKind.Nonterminal, "A");

		Node[] nodes = {
			nodeA,
			this._specialNodesDictionary.get(SpecialNodeKind.Definition),
			this._specialNodesDictionary.get(SpecialNodeKind.Alternation),
			this._specialNodesDictionary.get(SpecialNodeKind.Termination)

		}; // A = | .

		this._grammarParser.setNodes(Arrays.asList(nodes));

		// Act & Assert
		this._grammarParser.parse();
	}

	@Test(expected = InvalidGrammarException.class)
	public void parse_GrammarRuleWithInvalidConcatenationSpecialNode_ThrowsInvalidGrammarException()
			throws Exception
	{
		// Arrange - create nodes
		Node nodeA = new Node(NodeKind.Nonterminal, "A");
		Node nodeB = new Node(NodeKind.Nonterminal, "B");

		Node[] nodes = {
			nodeA,
			this._specialNodesDictionary.get(SpecialNodeKind.Definition),
			nodeB,
			this._specialNodesDictionary.get(SpecialNodeKind.Concatenation),
			this._specialNodesDictionary.get(SpecialNodeKind.Alternation),
			this._specialNodesDictionary.get(SpecialNodeKind.Termination)

		}; // A = B , | .

		this._grammarParser.setNodes(Arrays.asList(nodes));

		// Act & Assert
		this._grammarParser.parse();
	}

	@Test(expected = InvalidGrammarException.class)
	public void parse_GrammarRuleWithMissingConcatenationOrAlternationSpecialNode_ThrowsInvalidGrammarException()
			throws Exception
	{
		// Arrange - create nodes
		Node nodeA = new Node(NodeKind.Nonterminal, "A");
		Node nodeB = new Node(NodeKind.Nonterminal, "B");
		Node nodeC = new Node(NodeKind.Terminal, "c");

		Node[] nodes = {
			nodeA,
			this._specialNodesDictionary.get(SpecialNodeKind.Definition),
			nodeB, nodeC,
			this._specialNodesDictionary.get(SpecialNodeKind.Termination)

		}; // A = B "c" .

		this._grammarParser.setNodes(Arrays.asList(nodes));

		// Act & Assert
		this._grammarParser.parse();
	}

	@Test(expected = InvalidGrammarException.class)
	public void parse_GrammarRuleWithMissingDefinitionSpecialNode_ThrowsInvalidGrammarException()
			throws Exception
	{
		// Arrange - create nodes
		Node nodeA = new Node(NodeKind.Nonterminal, "A");

		Node[] nodes = {
			nodeA,
			this._specialNodesDictionary.get(SpecialNodeKind.Termination),
		}; // A .

		this._grammarParser.setNodes(Arrays.asList(nodes));

		// Act & Assert
		this._grammarParser.parse();
	}

	@Test(expected = InvalidGrammarException.class)
	public void parse_GrammarRuleWithMissingTerminationSpecialNode_ThrowsInvalidGrammarException()
			throws Exception
	{
		// Arrange - create nodes
		Node nodeA = new Node(NodeKind.Nonterminal, "A");

		Node[] nodes = {
			nodeA, this._specialNodesDictionary.get(SpecialNodeKind.Definition)
		}; // A = B

		this._grammarParser.setNodes(Arrays.asList(nodes));

		// Act & Assert
		this._grammarParser.parse();
	}

	@Test
	public void parse_GrammarWithDuplicateRules_RemoveDuplicateRules()
		throws Exception
	{
		// Arrange - create nodes
		Node nodeA = new Node(NodeKind.Nonterminal, "A");
		Node nodeB = new Node(NodeKind.Nonterminal, "B");

		Node[] nodes = {
			nodeA,
			this._specialNodesDictionary.get(SpecialNodeKind.Definition),
			nodeB,
			this._specialNodesDictionary.get(SpecialNodeKind.Alternation),
			nodeB,
			this._specialNodesDictionary.get(SpecialNodeKind.Termination),
		}; // A = B | B .

		// Arrange - create rule
		Rule rule = new Rule(nodeA);

		rule.addNode(nodeB);

		// Arrange - create test grammar
		Grammar testGrammar = new Grammar();

		testGrammar.addRule(rule);

		testGrammar.setHeadRule(rule);

		this._grammarParser.setNodes(Arrays.asList(nodes));

		// Act
		Grammar grammar = this._grammarParser.parse();

		// Assert
		Assert.assertEquals(testGrammar, grammar);
	}

	@Test
	public void parse_GrammarWithEmptyRule_ReturnsValidGrammar()
			throws Exception
	{
		// Arrange - create nodes
		Node nodeA = new Node(NodeKind.Nonterminal, "A");

		Node[] nodes = {
			nodeA,
			this._specialNodesDictionary.get(SpecialNodeKind.Definition),
			this._specialNodesDictionary.get(SpecialNodeKind.Termination),
		}; // A = .

		// Arrange - create rule
		Rule rule = new Rule(nodeA);

		// Arrange - create test grammar
		Grammar testGrammar = new Grammar();

		testGrammar.addRule(rule);
		testGrammar.setHeadRule(rule);

		this._grammarParser.setNodes(Arrays.asList(nodes));

		// Act
		Grammar grammar = this._grammarParser.parse();

		// Assert
		Assert.assertEquals(testGrammar, grammar);
	}

	@Test
	public void parse_GrammarWithSimpleRules_ReturnsValidGrammar()
			throws Exception
	{
		// Arrange - create nodes
		Node nodeA = new Node(NodeKind.Nonterminal, "A");
		Node nodeB = new Node(NodeKind.Nonterminal, "B");
		Node nodeC = new Node(NodeKind.Terminal, "c");
		Node nodeD = new Node(NodeKind.Terminal, "d");

		Node[] nodes = {
			nodeA,
			this._specialNodesDictionary.get(SpecialNodeKind.Definition),
			nodeB,
			this._specialNodesDictionary.get(SpecialNodeKind.Concatenation),
			nodeC,
			this._specialNodesDictionary.get(SpecialNodeKind.Termination),
			nodeB,
			this._specialNodesDictionary.get(SpecialNodeKind.Definition),
			nodeD,
			this._specialNodesDictionary.get(SpecialNodeKind.Termination)
		}; // A = B , "c" . B = "d" .

		// Arrange - create rules
		Rule firstRule = new Rule(nodeA);

		firstRule.addNode(nodeB);
		firstRule.addNode(nodeC);

		Rule secondRule = new Rule(nodeB);

		secondRule.addNode(nodeD);

		// Arrange - create test grammar
		Grammar testGrammar = new Grammar();

		testGrammar.addRule(firstRule);
		testGrammar.addRule(secondRule);
		testGrammar.setHeadRule(firstRule);

		this._grammarParser.setNodes(Arrays.asList(nodes));

		// Act
		Grammar grammar = this._grammarParser.parse();

		// Assert
		Assert.assertEquals(testGrammar, grammar);
	}

	@Test
	public void parse_MethodIsIdempotent() throws Exception
	{
		// Arrange
		Node[] nodes = {
			new Node(NodeKind.Nonterminal, "A"),
			this._specialNodesDictionary.get(SpecialNodeKind.Definition),
			new Node(NodeKind.Nonterminal, "B"),
			this._specialNodesDictionary.get(SpecialNodeKind.Termination),
		}; // A = B .

		this._grammarParser.setNodes(Arrays.asList(nodes));

		// Act
		Grammar grammarOne = this._grammarParser.parse();
		Grammar grammarTwo = this._grammarParser.parse();

		// Assert
		Assert.assertEquals(grammarOne, grammarTwo);
	}

	@Test
	public void parse_NodesListIsEmpty_ReturnsEmptyGrammar() throws Exception
	{
		// Arrange
		this._grammarParser.setNodes(new ArrayList<Node>());

		// Act
		Grammar grammar = this._grammarParser.parse();

		// Assert
		Assert.assertEquals(null, grammar.getHeadRule());
		Assert.assertEquals(0, grammar.getRulesDictionary().size());
	}

	@Test(expected = SpecialNodeIsNotDefinedException.class)
	public void setSpecialNodesDictionary_NotAllSpecialNodesAreDefined_ThrowsSpecialNodeIsNotDefinedException()
			throws Exception
	{
		// Arrange
		Map<SpecialNodeKind, Node> specialNodesDictionary = new HashMap<SpecialNodeKind, Node>() {
			{
				this.put(SpecialNodeKind.Alternation, new Node(
					NodeKind.Delimiter, "node"));

				this.put(SpecialNodeKind.Concatenation, new Node(
					NodeKind.Delimiter, "node"));

				this.put(SpecialNodeKind.Definition, null);

				this.put(SpecialNodeKind.Termination, null);
			}
		};

		// Act & Assert
		this._grammarParser.setSpecialNodesDictionary(specialNodesDictionary);
	}

	@Before
	public void setUp() throws Exception
	{
		this._grammarParser = new GrammarParser();

		this._specialNodesDictionary = this._grammarParser
				.getSpecialNodesDictionary();
	}

}
