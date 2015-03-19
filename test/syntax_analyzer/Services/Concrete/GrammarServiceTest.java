package syntax_analyzer.Services.Concrete;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import syntax_analyzer.Enums.NodeKind;
import syntax_analyzer.Models.Grammar;
import syntax_analyzer.Models.Node;
import syntax_analyzer.Models.Rule;
import syntax_analyzer.Services.Abstract.IGrammarService;

public class GrammarServiceTest
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
	public void getRightRecursiveRules_RecursiveRuleWithCicle_ReturnsListWithTwoRecursiveRules()
			throws Exception
	{
		// Arrange - create nodes
		Node nodeA = new Node(NodeKind.Nonterminal, "A");
		Node nodeB = new Node(NodeKind.Terminal, "b");
		Node nodeC = new Node(NodeKind.Nonterminal, "C");
		Node nodeD = new Node(NodeKind.Terminal, "d");
		Node nodeE = new Node(NodeKind.Nonterminal, "E");
		Node nodeF = new Node(NodeKind.Terminal, "f");

		// Arrange - create rules
		Rule ruleOne = new Rule(nodeA);

		// A = "b", C .
		ruleOne.addNode(nodeB);
		ruleOne.addNode(nodeC);

		Rule ruleTwo = new Rule(nodeC);

		// C = "d", E .
		ruleTwo.addNode(nodeD);
		ruleTwo.addNode(nodeE);

		Rule ruleThree = new Rule(nodeE);

		// E = "f", C .
		ruleThree.addNode(nodeF);
		ruleThree.addNode(nodeC);

		// Arrange - create grammar
		Grammar grammar = new Grammar();

		grammar.addRule(ruleOne);
		grammar.addRule(ruleTwo);
		grammar.addRule(ruleThree);
		grammar.setHeadRule(ruleOne);

		// Arrange - create rightRecursiveRules list
		Set<Rule> testRightRecursiveRules = new HashSet<Rule>(
			Arrays.asList(new Rule[] {
				ruleThree, ruleTwo
			}));

		// Act
		Set<Rule> rightRecursiveRules = this._grammarService
				.getRightRecursiveRules(grammar);

		// Assert
		Assert.assertEquals(testRightRecursiveRules, rightRecursiveRules);
	}

	@Test
	public void getRightRecursiveRules_RecursiveRuleWithEmptyRightRulesThree_ReturnsListWithOneRightRecursiveRule()
			throws Exception
	{
		// Arrange - create nodes
		Node nodeA = new Node(NodeKind.Nonterminal, "A");
		Node nodeB = new Node(NodeKind.Nonterminal, "B");
		Node nodeC = new Node(NodeKind.Terminal, "c");
		Node nodeD = new Node(NodeKind.Nonterminal, "D");

		// Arrange - create rules
		Rule ruleOne = new Rule(nodeA);

		// A = "c", A, B .
		ruleOne.addNode(nodeC);
		ruleOne.addNode(nodeA);
		ruleOne.addNode(nodeB);

		Rule ruleTwo = new Rule(nodeB);

		// B = D .
		ruleTwo.addNode(nodeD);

		Rule ruleThree = new Rule(nodeD); // D = .

		// Arrange - create grammar
		Grammar grammar = new Grammar();

		grammar.addRule(ruleOne);
		grammar.addRule(ruleTwo);
		grammar.addRule(ruleThree);
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
	public void getRightRecursiveRules_RecursiveRuleWithNotEmptyRightRule_ReturnsEmptyList()
			throws Exception
	{
		// Arrange - create nodes
		Node nodeA = new Node(NodeKind.Nonterminal, "A");
		Node nodeB = new Node(NodeKind.Nonterminal, "B");
		Node nodeC = new Node(NodeKind.Terminal, "c");
		Node nodeD = new Node(NodeKind.Terminal, "d");

		// Arrange - create rules
		Rule ruleOne = new Rule(nodeA);

		// A = "c", A, B .
		ruleOne.addNode(nodeC);
		ruleOne.addNode(nodeA);
		ruleOne.addNode(nodeB);

		Rule ruleTwo = new Rule(nodeB);

		// B = "d" .
		ruleTwo.addNode(nodeD);

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
	public void getRightRecursiveRules_SimpleRightRecursiveRule_ReturnsListWithOneRightRecursiveRule()
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
