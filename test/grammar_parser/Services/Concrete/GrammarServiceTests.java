package grammar_parser.Services.Concrete;

import grammar_parser.Enums.NodeKind;
import grammar_parser.Models.Grammar;
import grammar_parser.Models.Node;
import grammar_parser.Models.Rule;
import grammar_parser.Models.Word;
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
	public void getFirstSet_GrammarIsNull_ThrowsIllegalArgumentException()
		throws Exception
	{
		// Arrange
		Rule rule = new Rule(new Node(NodeKind.Nonterminal, "A"));

		// Act & Assert
		this._grammarService.getFirstSet(null, rule);
	}

	@Test
	public void getFirstSet_RuleIsEmpty_ReturnsSetWithOneEmptyWord()
			throws Exception
	{
		// Arrange - create nodes
		Node nodeA = new Node(NodeKind.Nonterminal, "A");

		// Arrange - create rule

		// A = .
		Rule ruleOne = new Rule(nodeA);

		// Arrange - create grammar
		Grammar grammar = new Grammar();

		grammar.addRule(ruleOne);

		grammar.setHeadRule(ruleOne);

		// Arrange - create words

		// Empty word
		Word wordOne = new Word();

		// Arrange - create testFirstSet
		Set<Word> testFirstSet = new HashSet<Word>();

		testFirstSet.add(wordOne);

		// Act
		Set<Word> firstSet = this._grammarService.getFirstSet(grammar, ruleOne);

		// Assert
		Assert.assertEquals(testFirstSet, firstSet);
	}

	@Test(expected = IllegalArgumentException.class)
	public void getFirstSet_RuleIsNull_ThrowsIllegalArgumentException()
		throws Exception
	{
		// Arrange
		Grammar grammar = new Grammar();

		// Act & Assert
		this._grammarService.getFirstSet(grammar, null);
	}

	@Test
	public void getFirstSet_RuleThatStartsFromTerminal_ReturnsSetWithOneWord()
			throws Exception
	{
		// Arrange - create nodes
		Node nodeA = new Node(NodeKind.Nonterminal, "A");
		Node nodeB = new Node(NodeKind.Nonterminal, "B");
		Node nodeC = new Node(NodeKind.Terminal, "c");
		Node nodeD = new Node(NodeKind.Terminal, "d");

		// Arrange - create rules
		Rule ruleOne = new Rule(nodeA);

		// A = "c", B .
		ruleOne.addNode(nodeC);
		ruleOne.addNode(nodeB);

		Rule ruleTwo = new Rule(nodeB);

		// B = "d" .
		ruleTwo.addNode(nodeD);

		// Arrange - create grammar
		Grammar grammar = new Grammar();

		grammar.addRule(ruleOne);
		grammar.addRule(ruleTwo);

		grammar.setHeadRule(ruleOne);

		// Arrange - create words
		Word wordOne = new Word();

		// Word: c
		wordOne.setNodes(Arrays.asList(new Node[] {
			nodeC
		}));

		// Arrange - create testFirstSet
		Set<Word> testFirstSet = new HashSet<Word>();

		testFirstSet.add(wordOne);

		// Act
		Set<Word> firstSet = this._grammarService.getFirstSet(grammar, ruleOne);

		// Assert
		Assert.assertEquals(testFirstSet, firstSet);
	}

	@Test
	public void getFirstSet_RuleWithComplexCycle_ReturnsEmptySet()
			throws Exception
	{
		// Arrange - create nodes
		Node nodeA = new Node(NodeKind.Nonterminal, "A");
		Node nodeB = new Node(NodeKind.Nonterminal, "B");
		Node nodeC = new Node(NodeKind.Nonterminal, "C");

		// Arrange - create rule

		// A = B .
		Rule ruleOne = new Rule(nodeA);

		ruleOne.addNode(nodeB);

		// B = C .
		Rule ruleTwo = new Rule(nodeB);

		ruleTwo.addNode(nodeC);

		// C = B .
		Rule ruleThree = new Rule(nodeC);

		ruleThree.addNode(nodeB);

		// Arrange - create grammar
		Grammar grammar = new Grammar();

		grammar.addRule(ruleOne);
		grammar.addRule(ruleTwo);
		grammar.addRule(ruleThree);

		grammar.setHeadRule(ruleOne);

		// Act
		Set<Word> firstSet = this._grammarService.getFirstSet(grammar, ruleOne);

		// Assert
		Assert.assertEquals(0, firstSet.size());
	}

	@Test
	public void getFirstSet_RuleWithEmptyFirstNonterminal_ReturnSetWithThreeWords()
			throws Exception
	{
		// Arrange - create nodes
		Node nodeA = new Node(NodeKind.Nonterminal, "A");
		Node nodeB = new Node(NodeKind.Nonterminal, "B");
		Node nodeC = new Node(NodeKind.Nonterminal, "C");
		Node nodeD = new Node(NodeKind.Terminal, "d");
		Node nodeE = new Node(NodeKind.Terminal, "e");

		// Arrange - create rules
		Rule ruleOne = new Rule(nodeA);

		// A = B, C .
		ruleOne.addNode(nodeB);
		ruleOne.addNode(nodeC);

		// B = .
		Rule ruleTwo = new Rule(nodeB);

		Rule ruleThree = new Rule(nodeB);

		// B = "d" .
		ruleThree.addNode(nodeD);

		Rule ruleFour = new Rule(nodeC);

		// C = "e" .
		ruleFour.addNode(nodeE);

		// Arrange - create grammar
		Grammar grammar = new Grammar();

		grammar.addRule(ruleOne);
		grammar.addRule(ruleTwo);
		grammar.addRule(ruleThree);
		grammar.addRule(ruleFour);

		grammar.setHeadRule(ruleOne);

		// Arrange - create words
		Word wordOne = new Word();

		// Word: d
		wordOne.setNodes(Arrays.asList(new Node[] {
			nodeD
		}));

		Word wordTwo = new Word();

		// Word: e
		wordTwo.setNodes(Arrays.asList(new Node[] {
			nodeE
		}));

		// Empty word
		Word wordThree = new Word();

		// Arrange - create testFirstSet
		Set<Word> testFirstSet = new HashSet<Word>();

		testFirstSet.add(wordOne);
		testFirstSet.add(wordTwo);
		testFirstSet.add(wordThree);

		// Act
		Set<Word> firstSet = this._grammarService.getFirstSet(grammar, ruleOne);

		// Assert
		Assert.assertEquals(testFirstSet, firstSet);
	}

	@Test
	public void getFirstSet_RuleWithNonEmptyNonterminals_ReturnsSetWithOneWord()
			throws Exception
	{
		// Arrange - create nodes
		Node nodeA = new Node(NodeKind.Nonterminal, "A");
		Node nodeB = new Node(NodeKind.Nonterminal, "B");
		Node nodeC = new Node(NodeKind.Nonterminal, "C");
		Node nodeD = new Node(NodeKind.Terminal, "d");
		Node nodeE = new Node(NodeKind.Terminal, "e");

		// Arrange - create rules
		Rule ruleOne = new Rule(nodeA);

		// A = B, C .
		ruleOne.addNode(nodeB);
		ruleOne.addNode(nodeC);

		Rule ruleTwo = new Rule(nodeB);

		// B = "d" .
		ruleTwo.addNode(nodeD);

		Rule ruleThree = new Rule(nodeC);

		// C = "e" .
		ruleThree.addNode(nodeE);

		// Arrange - create grammar
		Grammar grammar = new Grammar();

		grammar.addRule(ruleOne);
		grammar.addRule(ruleTwo);
		grammar.addRule(ruleThree);

		grammar.setHeadRule(ruleOne);

		// Arrange - create words
		Word wordOne = new Word();

		// Word: d
		wordOne.setNodes(Arrays.asList(new Node[] {
			nodeD
		}));

		// Arrange - create testFirstSet
		Set<Word> testFirstSet = new HashSet<Word>();

		testFirstSet.add(wordOne);

		// Act
		Set<Word> firstSet = this._grammarService.getFirstSet(grammar, ruleOne);

		// Assert
		Assert.assertEquals(testFirstSet, firstSet);
	}

	@Test
	public void getFirstSet_RuleWithSimpleCycle_ReturnsEmptySet()
			throws Exception
	{
		// Arrange - create nodes
		Node nodeA = new Node(NodeKind.Nonterminal, "A");

		// Arrange - create rule

		// A = A .
		Rule ruleOne = new Rule(nodeA);

		ruleOne.addNode(nodeA);

		// Arrange - create grammar
		Grammar grammar = new Grammar();

		grammar.addRule(ruleOne);

		grammar.setHeadRule(ruleOne);

		// Act
		Set<Word> firstSet = this._grammarService.getFirstSet(grammar, ruleOne);

		// Assert
		Assert.assertEquals(0, firstSet.size());
	}

	@Test
	public void getFirstSet_RuleWithTwoTerminals_ReturnsSetWithOneWord()
			throws Exception
	{
		// Arrange - create nodes
		Node nodeA = new Node(NodeKind.Nonterminal, "A");
		Node nodeB = new Node(NodeKind.Terminal, "b");
		Node nodeC = new Node(NodeKind.Terminal, "c");

		// Arrange - create rules
		Rule ruleOne = new Rule(nodeA);

		// A = "b", "c" .
		ruleOne.addNode(nodeB);
		ruleOne.addNode(nodeC);

		// Arrange - create grammar
		Grammar grammar = new Grammar();

		grammar.addRule(ruleOne);

		grammar.setHeadRule(ruleOne);

		// Arrange - create words
		Word wordOne = new Word();

		// Word: b
		wordOne.setNodes(Arrays.asList(new Node[] {
			nodeB
		}));

		// Arrange - create testFirstSet
		Set<Word> testFirstSet = new HashSet<Word>();

		testFirstSet.add(wordOne);

		// Act
		Set<Word> firstSet = this._grammarService.getFirstSet(grammar, ruleOne);

		// Assert
		Assert.assertEquals(testFirstSet, firstSet);
	}

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
		Set<Rule> testRightRecursiveRules =
			new HashSet<Rule>(Arrays.asList(new Rule[] {
					ruleTwo, ruleThree
				}));

		// Act
		Set<Rule> rightRecursiveRules =
			this._grammarService.getRightRecursiveRules(grammar);

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
		Set<Rule> testRightRecursiveRules =
			new HashSet<Rule>(Arrays.asList(new Rule[] {
					ruleOne
				}));

		// Act
		Set<Rule> rightRecursiveRules =
			this._grammarService.getRightRecursiveRules(grammar);

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
		Set<Rule> rightRecursiveRules =
			this._grammarService.getRightRecursiveRules(grammar);

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
		Set<Rule> rightRecursiveRules =
			this._grammarService.getRightRecursiveRules(grammar);

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
		Set<Rule> testRightRecursiveRules =
			new HashSet<Rule>(Arrays.asList(new Rule[] {
					ruleOne
				}));

		// Act
		Set<Rule> rightRecursiveRules =
			this._grammarService.getRightRecursiveRules(grammar);

		// Assert
		Assert.assertEquals(testRightRecursiveRules, rightRecursiveRules);
	}

	@Before
	public void setUp() throws Exception
	{
		this._grammarService = new GrammarService();
	}
}
