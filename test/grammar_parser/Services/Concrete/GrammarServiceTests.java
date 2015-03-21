package grammar_parser.Services.Concrete;

import grammar_parser.Enums.NodeKind;
import grammar_parser.Models.Grammar;
import grammar_parser.Models.Node;
import grammar_parser.Models.Rule;
import grammar_parser.Services.Abstract.IGrammarService;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class GrammarServiceTests
{
	IGrammarService _grammarService;

	@Test(expected = IllegalArgumentException.class)
	public void getRightRecursiveRules_GrammarIsNull_ThrowsIllegalArgumentException()
	{
		// Arrange
		Grammar grammar = null;

		// Act & Assert
		this._grammarService.getRightRecursiveRules(grammar);
	}

	@Test
	public void getRightRecursiveRules_RecursiveRulesWithCycle_ReturnsSetWithTwoRecursiveRules()
			throws Exception
	{
		// Arrange - create nodes
		Node nodeA = new Node(NodeKind.Nonterminal, "A");
		Node nodeB = new Node(NodeKind.Nonterminal, "B");
		Node nodeC = new Node(NodeKind.Nonterminal, "C");

		// Arrange - create rules
		Rule ruleOne = new Rule(nodeA);

		// A = A, B .
		ruleOne.addNode(nodeA);
		ruleOne.addNode(nodeB);

		Rule ruleTwo = new Rule(nodeB);

		// B = C .
		ruleTwo.addNode(nodeC);

		Rule ruleThree = new Rule(nodeC);

		// C = B .
		ruleThree.addNode(nodeB);

		// Arrange - create grammar
		Grammar grammar = new Grammar();

		grammar.addRule(ruleOne);
		grammar.addRule(ruleTwo);
		grammar.addRule(ruleThree);
		grammar.setHeadRule(ruleOne);

		// Arrange - create rightRecursiveRules list
		Set<Rule> testRightRecursiveRules = new HashSet<Rule>(
			Arrays.asList(new Rule[] {
				ruleTwo, ruleThree
			}));

		// Act
		Set<Rule> rightRecursiveRules = this._grammarService
				.getRightRecursiveRules(grammar);

		// Assert
		Assert.assertEquals(testRightRecursiveRules, rightRecursiveRules);
	}

	@Test
	public void getRightRecursiveRules_RecursiveRuleWithEmptyRightRulesThree_ReturnsSetWithOneRightRecursiveRule()
			throws Exception
	{
		// Arrange - create nodes
		Node nodeA = new Node(NodeKind.Nonterminal, "A");
		Node nodeB = new Node(NodeKind.Nonterminal, "B");
		Node nodeC = new Node(NodeKind.Nonterminal, "C");
		Node nodeD = new Node(NodeKind.Terminal, "d");

		// Arrange - create rules
		Rule ruleOne = new Rule(nodeA);

		// A = "c", A, B, C .
		ruleOne.addNode(nodeC);
		ruleOne.addNode(nodeA);
		ruleOne.addNode(nodeB);
		ruleOne.addNode(nodeC);

		Rule ruleTwo = new Rule(nodeB);

		// B = "d" .
		ruleTwo.addNode(nodeD);

		// B = .
		Rule ruleThree = new Rule(nodeB);

		// C = .
		Rule ruleFour = new Rule(nodeC);

		// Arrange - create grammar
		Grammar grammar = new Grammar();

		grammar.addRule(ruleOne);
		grammar.addRule(ruleTwo);
		grammar.addRule(ruleThree);
		grammar.addRule(ruleFour);
		grammar.setHeadRule(ruleOne);

		// Arrange - create rightRecursiveRules list
		Set<Rule> testRightRecursiveRules = new HashSet<Rule>(
			Arrays.asList(new Rule[] {
				ruleOne
			}));

		// Act
		Set<Rule> rightRecursiveRules = this._grammarService
				.getRightRecursiveRules(grammar);

		// Assert
		Assert.assertEquals(testRightRecursiveRules, rightRecursiveRules);
	}

	@Test
	public void getRightRecursiveRules_RecursiveRuleWithNotEmptyRightRule_ReturnsEmptySet()
			throws Exception
	{
		// Arrange - create nodes
		Node nodeA = new Node(NodeKind.Nonterminal, "A");
		Node nodeB = new Node(NodeKind.Nonterminal, "B");
		Node nodeC = new Node(NodeKind.Terminal, "c");

		// Arrange - create rules
		Rule ruleOne = new Rule(nodeA);

		// A = "c", A, B .
		ruleOne.addNode(nodeC);
		ruleOne.addNode(nodeA);
		ruleOne.addNode(nodeB);

		Rule ruleTwo = new Rule(nodeB);

		// B = "c" .
		ruleTwo.addNode(nodeC);

		// Arrange - create grammar
		Grammar grammar = new Grammar();

		grammar.addRule(ruleOne);
		grammar.addRule(ruleTwo);
		grammar.setHeadRule(ruleOne);

		// Act
		Set<Rule> rightRecursiveRules = this._grammarService
				.getRightRecursiveRules(grammar);

		// Assert
		Assert.assertEquals(0, rightRecursiveRules.size());
	}

	@Test
	public void getRightRecursiveRules_SimpleRecursiveRule_ReturnsEmptySet()
			throws Exception
	{
		// Arrange - create nodes
		Node nodeA = new Node(NodeKind.Nonterminal, "A");
		Node nodeB = new Node(NodeKind.Terminal, "b");

		// Arrange - create rule
		Rule ruleOne = new Rule(nodeA);

		// A = A, "b" .
		ruleOne.addNode(nodeA);
		ruleOne.addNode(nodeB);

		// Arrange - create grammar
		Grammar grammar = new Grammar();

		grammar.addRule(ruleOne);
		grammar.setHeadRule(ruleOne);

		// Act
		Set<Rule> rightRecursiveRules = this._grammarService
				.getRightRecursiveRules(grammar);

		// Assert
		Assert.assertEquals(0, rightRecursiveRules.size());
	}

	@Test
	public void getRightRecursiveRules_SimpleRightRecursiveRule_ReturnsSetWithOneRightRecursiveRule()
			throws Exception
	{
		// Arrange - create nodes
		Node nodeA = new Node(NodeKind.Nonterminal, "A");
		Node nodeB = new Node(NodeKind.Terminal, "b");

		// Arrange - create rule
		Rule ruleOne = new Rule(nodeA);

		// A = "b", A .
		ruleOne.addNode(nodeB);
		ruleOne.addNode(nodeA);

		// Arrange - create grammar
		Grammar grammar = new Grammar();

		grammar.addRule(ruleOne);
		grammar.setHeadRule(ruleOne);

		// Arrange - create rightRecursiveRules list
		Set<Rule> testRightRecursiveRules = new HashSet<Rule>(
			Arrays.asList(new Rule[] {
				ruleOne
			}));

		// Act
		Set<Rule> rightRecursiveRules = this._grammarService
				.getRightRecursiveRules(grammar);

		// Assert
		Assert.assertEquals(testRightRecursiveRules, rightRecursiveRules);
	}

	@Before
	public void setUp() throws Exception
	{
		this._grammarService = new GrammarService();
	}
}
