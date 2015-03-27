package grammar_parser.Services.Concrete;

import grammar_parser.Enums.NodeKind;
import grammar_parser.Exceptions.NonexistentNodeException;
import grammar_parser.Models.Grammar;
import grammar_parser.Models.Node;
import grammar_parser.Models.Rule;
import grammar_parser.Models.Word;
import grammar_parser.Services.Abstract.IGrammarService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class GrammarServiceTests
{
	IGrammarService _grammarService;

	@Test
	public void getAllRulesFromGrammar_GrammarIsEmpty_ReturnsEmptyList()
		throws Exception
	{
		// Arrange
		Grammar grammar = new Grammar();

		// Act
		List<Rule> rules = this._grammarService.getAllRulesFromGrammar(grammar);

		// Assert
		Assert.assertEquals(0, rules.size());
	}

	@Test(expected = IllegalArgumentException.class)
	public void getAllRulesFromGrammar_GrammarIsNull_ThrowsIllegalArgumentException()
		throws Exception
	{
		// Act & Assert
		this._grammarService.getAllRulesFromGrammar(null);
	}

	@Test
	public void getAllRulesFromGrammar_GrammarIsValid_ReturnsRulesList()
		throws Exception
	{
		// Arrange
		Rule ruleOne = new Rule(new Node(NodeKind.Nonterminal, "A"));
		Rule ruleTwo = new Rule(new Node(NodeKind.Nonterminal, "B"));

		Set<Rule> testRules =
			new HashSet<Rule>(Arrays.asList(ruleOne, ruleTwo));

		Grammar grammar = new Grammar();

		for (Rule rule : testRules)
		{
			grammar.addRule(rule);
		}

		grammar.setHeadRule(ruleOne);

		// Act
		List<Rule> rules = this._grammarService.getAllRulesFromGrammar(grammar);

		// Assert
		Assert.assertEquals(testRules, new HashSet<Rule>(rules));
	}

	@Test
	public void getFirstPlusFollowSet_FirstSetContainsEmptySet_ReturnsTheUnionOfFirstAndFollowSets()
		throws Exception
	{
		// Arrange - create words
		Word wordOne = new Word(new Node(NodeKind.Terminal, "a"));
		Word wordTwo = new Word(new Node(NodeKind.Terminal, "b"));

		// Arrange - create first and follow sets
		Set<Word> firstSet =
			new HashSet<Word>(Arrays.asList(Word.getEmptyWord(), wordOne));
		Set<Word> followSet = new HashSet<Word>(Arrays.asList(wordTwo));

		// Arrange - create testFirstSetForNodesList
		Set<Word> testFirstPlusFollowSet =
			new HashSet<Word>(Arrays.asList(Word.getEmptyWord(), wordOne,
				wordTwo));

		// Act
		Set<Word> firstPlusFollowSet =
			this._grammarService.getFirstPlusFollowSet(firstSet, followSet);

		// Assert
		Assert.assertEquals(testFirstPlusFollowSet, firstPlusFollowSet);
	}

	@Test
	public void getFirstPlusFollowSet_FirstSetDoesNotContainEmptySet_ReturnsTheFirstSet()
		throws Exception
	{
		// Arrange - create words
		Word wordOne = new Word(new Node(NodeKind.Terminal, "a"));
		Word wordTwo = new Word(new Node(NodeKind.Terminal, "b"));

		// Arrange - create first and follow sets
		Set<Word> firstSet = new HashSet<Word>(Arrays.asList(wordOne));
		Set<Word> followSet = new HashSet<Word>(Arrays.asList(wordTwo));

		// Arrange - create testFirstSetForNodesList
		Set<Word> testFirstPlusFollowSet =
			new HashSet<Word>(Arrays.asList(wordOne));

		// Act
		Set<Word> firstPlusFollowSet =
			this._grammarService.getFirstPlusFollowSet(firstSet, followSet);

		// Assert
		Assert.assertEquals(testFirstPlusFollowSet, firstPlusFollowSet);
	}

	@Test
	public void getFirstPlusFollowSet_FirstSetIsEmpty_ReturnsEmptySet()
		throws Exception
	{
		// Arrange - create words
		Word wordOne = new Word(new Node(NodeKind.Terminal, "a"));

		// Arrange - create first and follow sets
		Set<Word> firstSet = new HashSet<Word>();
		Set<Word> followSet = new HashSet<Word>(Arrays.asList(wordOne));

		// Act
		Set<Word> firstPlusFollowSet =
			this._grammarService.getFirstPlusFollowSet(firstSet, followSet);

		// Assert
		Assert.assertEquals(0, firstPlusFollowSet.size());
	}

	@Test(expected = IllegalArgumentException.class)
	public void getFirstPlusFollowSet_FirstSetIsNull_ThrowsIllegalArgumentException()
		throws Exception
	{
		// Arrange
		Set<Word> followSet = new HashSet<Word>();

		// Act & Assert
		this._grammarService.getFirstPlusFollowSet(null, followSet);
	}

	@Test(expected = IllegalArgumentException.class)
	public void getFirstPlusFollowSet_FollowSetIsNull_ThrowsIllegalArgumentException()
		throws Exception
	{
		// Arrange
		Set<Word> firstSet = new HashSet<Word>();

		// Act & Assert
		this._grammarService.getFirstPlusFollowSet(firstSet, null);
	}

	@Test
	public void getFirstSetDictionary_GrammarIsEmpty_ReturnsEmptyDictionary()
		throws Exception
	{
		// Arrange
		Grammar grammar = new Grammar();

		// Act
		Map<Node, Set<Word>> firstSetDictionary =
			this._grammarService.getFirstSetDictionary(grammar);

		// Assert
		Assert.assertEquals(0, firstSetDictionary.size());
	}

	@Test(expected = IllegalArgumentException.class)
	public void getFirstSetDictionary_GrammarIsNull_ThrowsIllegalArgumentException()
		throws Exception
	{
		// Act & Assert
		this._grammarService.getFirstSetDictionary(null);
	}

	@Test
	public void getFirstSetDictionary_RuleIsEmpty_ReturnsValidDictionary()
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

		// Arrange - create testFirstSetDictionary
		Map<Node, Set<Word>> testFirstSetDictionary =
			new HashMap<Node, Set<Word>>();

		testFirstSetDictionary.put(nodeA,
			new HashSet<Word>(Arrays.asList(Word.getEmptyWord())));

		// Act
		Map<Node, Set<Word>> firstSetDictionary =
			this._grammarService.getFirstSetDictionary(grammar);

		// Assert
		Assert.assertEquals(testFirstSetDictionary, firstSetDictionary);
	}

	@Test
	public void getFirstSetDictionary_RuleThatStartsFromTerminal_ReturnsValidDictionary()
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

		// Word: c
		Word wordOne = new Word(nodeC);

		// Word: d
		Word wordTwo = new Word(nodeD);

		// Arrange - create testFirstSetDictionary
		Map<Node, Set<Word>> testFirstSetDictionary =
			new HashMap<Node, Set<Word>>();

		testFirstSetDictionary.put(nodeA,
			new HashSet<Word>(Arrays.asList(wordOne)));
		testFirstSetDictionary.put(nodeB,
			new HashSet<Word>(Arrays.asList(wordTwo)));

		// Act
		Map<Node, Set<Word>> firstSetDictionary =
			this._grammarService.getFirstSetDictionary(grammar);

		// Assert
		Assert.assertEquals(testFirstSetDictionary, firstSetDictionary);
	}

	@Test
	public void getFirstSetDictionary_RuleWithComplexCycle_ReturnsValidDictionary()
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

		// Arrange - create testFirstSetDictionary
		Map<Node, Set<Word>> testFirstSetDictionary =
			new HashMap<Node, Set<Word>>();

		testFirstSetDictionary.put(nodeA, new HashSet<Word>());
		testFirstSetDictionary.put(nodeB, new HashSet<Word>());
		testFirstSetDictionary.put(nodeC, new HashSet<Word>());

		// Act
		Map<Node, Set<Word>> firstSetDictionary =
			this._grammarService.getFirstSetDictionary(grammar);

		// Assert
		Assert.assertEquals(testFirstSetDictionary, firstSetDictionary);
	}

	@Test
	public void getFirstSetDictionary_RuleWithEmptyFirstNonterminal_ReturnsValidDictionary()
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

		// Word: d
		Word wordOne = new Word(nodeD);

		// Word: e
		Word wordTwo = new Word(nodeE);

		// Arrange - create testFirstSetDictionary
		Map<Node, Set<Word>> testFirstSetDictionary =
			new HashMap<Node, Set<Word>>();

		testFirstSetDictionary.put(
			nodeA,
			new HashSet<Word>(Arrays.asList(wordOne, wordTwo,
				Word.getEmptyWord())));
		testFirstSetDictionary.put(nodeB,
			new HashSet<Word>(Arrays.asList(Word.getEmptyWord(), wordOne)));
		testFirstSetDictionary.put(nodeC,
			new HashSet<Word>(Arrays.asList(wordTwo)));

		// Act
		Map<Node, Set<Word>> firstSetDictionary =
			this._grammarService.getFirstSetDictionary(grammar);

		// Assert
		Assert.assertEquals(testFirstSetDictionary, firstSetDictionary);
	}

	@Test
	public void getFirstSetDictionary_RuleWithNonEmptyNonterminals_ReturnsValidDictionary()
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

		// Word: d
		Word wordOne = new Word(nodeD);

		// Word: e
		Word wordTwo = new Word(nodeE);

		// Arrange - create testFirstSetDictionary
		Map<Node, Set<Word>> testFirstSetDictionary =
			new HashMap<Node, Set<Word>>();

		testFirstSetDictionary.put(nodeA,
			new HashSet<Word>(Arrays.asList(wordOne)));
		testFirstSetDictionary.put(nodeB,
			new HashSet<Word>(Arrays.asList(wordOne)));
		testFirstSetDictionary.put(nodeC,
			new HashSet<Word>(Arrays.asList(wordTwo)));

		// Act
		Map<Node, Set<Word>> firstSetDictionary =
			this._grammarService.getFirstSetDictionary(grammar);

		// Assert
		Assert.assertEquals(testFirstSetDictionary, firstSetDictionary);
	}

	@Test
	public void getFirstSetDictionary_RuleWithNonterminalFromNonexistentRule_ReturnsValidDictionary()
		throws Exception
	{
		// Arrange - create nodes
		Node nodeA = new Node(NodeKind.Nonterminal, "A");
		Node nodeB = new Node(NodeKind.Nonterminal, "B");

		// Arrange - create rules
		Rule ruleOne = new Rule(nodeA);

		// A = B .
		ruleOne.addNode(nodeB);

		// Arrange - create grammar
		Grammar grammar = new Grammar();

		grammar.addRule(ruleOne);

		grammar.setHeadRule(ruleOne);

		// Arrange - create testFirstSetDictionary
		Map<Node, Set<Word>> testFirstSetDictionary =
			new HashMap<Node, Set<Word>>();

		testFirstSetDictionary.put(nodeA, new HashSet<Word>());

		testFirstSetDictionary.put(nodeB, new HashSet<Word>());

		// Act
		Map<Node, Set<Word>> firstSetDictionary =
			this._grammarService.getFirstSetDictionary(grammar);

		// Assert
		Assert.assertEquals(testFirstSetDictionary, firstSetDictionary);
	}

	@Test
	public void getFirstSetDictionary_RuleWithSimpleCycle_ReturnsValidDictionary()
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

		// Arrange - create testFirstSetDictionary
		Map<Node, Set<Word>> testFirstSetDictionary =
			new HashMap<Node, Set<Word>>();

		testFirstSetDictionary.put(nodeA, new HashSet<Word>());

		// Act
		Map<Node, Set<Word>> firstSetDictionary =
			this._grammarService.getFirstSetDictionary(grammar);

		// Assert
		Assert.assertEquals(testFirstSetDictionary, firstSetDictionary);
	}

	@Test
	public void getFirstSetDictionary_RuleWithTwoTerminals_ReturnsValidDictionary()
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

		// Word: b
		Word wordOne = new Word(nodeB);

		// Arrange - create testFirstSetDictionary
		Map<Node, Set<Word>> testFirstSetDictionary =
			new HashMap<Node, Set<Word>>();

		testFirstSetDictionary.put(nodeA,
			new HashSet<Word>(Arrays.asList(wordOne)));

		// Act
		Map<Node, Set<Word>> firstSetDictionary =
			this._grammarService.getFirstSetDictionary(grammar);

		// Assert
		Assert.assertEquals(testFirstSetDictionary, firstSetDictionary);
	}

	@Test
	public void getFirstSetForNodesList_FirstNodeIsTerminalNode_ReturnsValidSet()
		throws Exception
	{
		// Arrange - create nodesList
		Node nodeA = new Node(NodeKind.Terminal, "A");
		Node nodeB = new Node(NodeKind.Nonterminal, "B");

		List<Node> nodesList = Arrays.asList(nodeA, nodeB);

		// Arrange - create words
		Word wordOne = new Word(nodeA);
		Word wordTwo = new Word(new Node(NodeKind.Terminal, "c"));

		// Arrange - create firstSetDictionary
		Map<Node, Set<Word>> firstSetDictionary =
			new HashMap<Node, Set<Word>>();

		firstSetDictionary
				.put(nodeB, new HashSet<Word>(Arrays.asList(wordTwo)));

		// Arrange - create testFirstSetForNodesList
		Set<Word> testFirstSetForNodesList =
			new HashSet<Word>(Arrays.asList(wordOne));

		// Act
		Set<Word> firstSetForNodesList =
			this._grammarService.getFirstSetForNodesList(nodesList,
				firstSetDictionary);

		// Assert
		Assert.assertEquals(testFirstSetForNodesList, firstSetForNodesList);
	}

	@Test(expected = IllegalArgumentException.class)
	public void getFirstSetForNodesList_FirstSetDictionaryIsNull_ThrowsIllegalArgumentException()
		throws Exception
	{
		// Arrange
		List<Node> nodesList = new ArrayList<Node>();

		// Act & Assert
		this._grammarService.getFirstSetForNodesList(nodesList, null);
	}

	@Test
	public void getFirstSetForNodesList_FirstSetOfTheFirstNodeContainsEmptyWord_ReturnsValidSet()
		throws Exception
	{
		// Arrange - create nodesList
		Node nodeA = new Node(NodeKind.Nonterminal, "A");
		Node nodeB = new Node(NodeKind.Nonterminal, "B");

		List<Node> nodesList = Arrays.asList(nodeA, nodeB);

		// Arrange - create words
		Word wordOne = new Word(new Node(NodeKind.Terminal, "c"));
		Word wordTwo = new Word(new Node(NodeKind.Terminal, "d"));

		// Arrange - create firstSetDictionary
		Map<Node, Set<Word>> firstSetDictionary =
			new HashMap<Node, Set<Word>>();

		firstSetDictionary.put(nodeA,
			new HashSet<Word>(Arrays.asList(Word.getEmptyWord(), wordOne)));
		firstSetDictionary
				.put(nodeB, new HashSet<Word>(Arrays.asList(wordTwo)));

		// Arrange - create testFirstSetForNodesList
		Set<Word> testFirstSetForNodesList =
			new HashSet<Word>(Arrays.asList(Word.getEmptyWord(), wordOne,
				wordTwo));

		// Act
		Set<Word> firstSetForNodesList =
			this._grammarService.getFirstSetForNodesList(nodesList,
				firstSetDictionary);

		// Assert
		Assert.assertEquals(testFirstSetForNodesList, firstSetForNodesList);
	}

	@Test
	public void getFirstSetForNodesList_FirstSetOfTheFirstNodeDoesNotContainEmptyWord_ReturnsValidSet()
		throws Exception
	{
		// Arrange - create nodesList
		Node nodeA = new Node(NodeKind.Nonterminal, "A");
		Node nodeB = new Node(NodeKind.Nonterminal, "B");

		List<Node> nodesList = Arrays.asList(nodeA, nodeB);

		// Arrange - create words
		Word wordOne = new Word(new Node(NodeKind.Terminal, "c"));
		Word wordTwo = new Word(new Node(NodeKind.Terminal, "d"));

		// Arrange - create firstSetDictionary
		Map<Node, Set<Word>> firstSetDictionary =
			new HashMap<Node, Set<Word>>();

		firstSetDictionary
				.put(nodeA, new HashSet<Word>(Arrays.asList(wordOne)));
		firstSetDictionary
				.put(nodeB, new HashSet<Word>(Arrays.asList(wordTwo)));

		// Arrange - create testFirstSetForNodesList
		Set<Word> testFirstSetForNodesList =
			new HashSet<Word>(Arrays.asList(wordOne));

		// Act
		Set<Word> firstSetForNodesList =
			this._grammarService.getFirstSetForNodesList(nodesList,
				firstSetDictionary);

		// Assert
		Assert.assertEquals(testFirstSetForNodesList, firstSetForNodesList);
	}

	@Test
	public void getFirstSetForNodesList_NodesListIsEmpty_ReturnsEmptySet()
		throws Exception
	{
		// Arrange
		List<Node> nodesList = new ArrayList<Node>();

		Map<Node, Set<Word>> firstSetDictionary =
			new HashMap<Node, Set<Word>>();

		// Act
		Set<Word> firstSetForNodesList =
			this._grammarService.getFirstSetForNodesList(nodesList,
				firstSetDictionary);

		// Assert
		Assert.assertEquals(0, firstSetForNodesList.size());
	}

	@Test(expected = IllegalArgumentException.class)
	public void getFirstSetForNodesList_NodesListIsNull_ThrowsIllegalArgumentException()
		throws Exception
	{
		// Arrange
		Map<Node, Set<Word>> firstSetDictionary =
			new HashMap<Node, Set<Word>>();

		// Act & Assert
		this._grammarService.getFirstSetForNodesList(null, firstSetDictionary);
	}

	@Test(expected = NonexistentNodeException.class)
	public void getFirstSetForNodesList_OneOfTheNodesDoesNotExistInFirstSetDictionary_ThrowsNonexistentNodeException()
		throws Exception
	{
		// Arrange
		Node nodeA = new Node(NodeKind.Nonterminal, "A");

		List<Node> nodesList = Arrays.asList(nodeA);

		Map<Node, Set<Word>> firstSetDictionary =
			new HashMap<Node, Set<Word>>();

		// Act & Assert
		this._grammarService.getFirstSetForNodesList(nodesList,
			firstSetDictionary);
	}

	@Test
	public void getFollowSetDictionary_GrammarIsEmpty_ReturnsEmptyDictionary()
		throws Exception
	{
		// Arrange
		Grammar grammar = new Grammar();

		// Act
		Map<Node, Set<Word>> followSetDictionary =
			this._grammarService.getFollowSetDictionary(grammar);

		// Assert
		Assert.assertEquals(0, followSetDictionary.size());
	}

	@Test(expected = IllegalArgumentException.class)
	public void getFollowSetDictionary_GrammarIsNull_ThrowsIllegalArgumentException()
		throws Exception
	{
		// Act & Assert
		this._grammarService.getFollowSetDictionary(null);
	}

	@Test
	public void getFollowSetDictionary_RuleIsEmpty_ReturnsValidDictionary()
		throws Exception
	{
		// Arrange - create nodes
		Node nodeA = new Node(NodeKind.Nonterminal, "A");
		Node nodeB = new Node(NodeKind.Nonterminal, "B");
		Node nodeC = new Node(NodeKind.Terminal, "c");

		// Arrange - create rules
		Rule ruleOne = new Rule(nodeA);

		// A = "c" .
		ruleOne.addNode(nodeC);

		// B = .
		Rule ruleTwo = new Rule(nodeB);

		// Arrange - create grammar
		Grammar grammar = new Grammar();

		grammar.addRule(ruleOne);
		grammar.addRule(ruleTwo);

		grammar.setHeadRule(ruleOne);

		// Arrange - create testFollowSetDictionary
		Map<Node, Set<Word>> testFollowSetDictionary =
			new HashMap<Node, Set<Word>>();

		testFollowSetDictionary.put(nodeA,
			new HashSet<Word>(Arrays.asList(Word.getEmptyWord())));

		testFollowSetDictionary.put(nodeB, new HashSet<Word>());

		// Act
		Map<Node, Set<Word>> followSetDictionary =
			this._grammarService.getFollowSetDictionary(grammar);

		// Assert
		Assert.assertEquals(testFollowSetDictionary, followSetDictionary);
	}

	@Test
	public void getFollowSetDictionary_RuleWithComplexCycle_ReturnsValidDictionary()
		throws Exception
	{
		// Arrange - create nodes
		Node nodeA = new Node(NodeKind.Nonterminal, "A");
		Node nodeB = new Node(NodeKind.Nonterminal, "B");
		Node nodeC = new Node(NodeKind.Nonterminal, "C");
		Node nodeD = new Node(NodeKind.Terminal, "d");
		Node nodeE = new Node(NodeKind.Terminal, "e");
		Node nodeF = new Node(NodeKind.Terminal, "f");

		// Arrange - create rule

		// A = B, "d" .
		Rule ruleOne = new Rule(nodeA);

		ruleOne.addNode(nodeB);
		ruleOne.addNode(nodeD);

		// B = C, "e" .
		Rule ruleTwo = new Rule(nodeB);

		ruleTwo.addNode(nodeC);
		ruleTwo.addNode(nodeE);

		// C = B, "f" .
		Rule ruleThree = new Rule(nodeC);

		ruleThree.addNode(nodeB);
		ruleThree.addNode(nodeF);

		// Arrange - create grammar
		Grammar grammar = new Grammar();

		grammar.addRule(ruleOne);
		grammar.addRule(ruleTwo);
		grammar.addRule(ruleThree);

		grammar.setHeadRule(ruleOne);

		// Arrange - create words

		// Word: d
		Word wordOne = new Word(nodeD);

		// Word: e
		Word wordTwo = new Word(nodeE);

		// Word: f
		Word wordThree = new Word(nodeF);

		// Arrange - create testFollowSetDictionary
		Map<Node, Set<Word>> testFollowSetDictionary =
			new HashMap<Node, Set<Word>>();

		testFollowSetDictionary.put(nodeA,
			new HashSet<Word>(Arrays.asList(Word.getEmptyWord())));
		testFollowSetDictionary.put(nodeB,
			new HashSet<Word>(Arrays.asList(wordOne, wordThree)));
		testFollowSetDictionary.put(nodeC,
			new HashSet<Word>(Arrays.asList(wordTwo)));

		// Act
		Map<Node, Set<Word>> followSetDictionary =
			this._grammarService.getFollowSetDictionary(grammar);

		// Assert
		Assert.assertEquals(testFollowSetDictionary, followSetDictionary);
	}

	@Test
	public void getFollowSetDictionary_RuleWithEmptyLastNonterminal_ReturnsValidDictionary()
		throws Exception
	{
		// Arrange - create nodes
		Node nodeA = new Node(NodeKind.Nonterminal, "A");
		Node nodeB = new Node(NodeKind.Nonterminal, "B");
		Node nodeC = new Node(NodeKind.Nonterminal, "C");
		Node nodeD = new Node(NodeKind.Nonterminal, "D");
		Node nodeE = new Node(NodeKind.Terminal, "e");
		Node nodeF = new Node(NodeKind.Terminal, "f");

		// Arrange - create rules
		Rule ruleOne = new Rule(nodeA);

		// A = B, "e" .
		ruleOne.addNode(nodeB);
		ruleOne.addNode(nodeE);

		Rule ruleTwo = new Rule(nodeB);

		// B = C, D .
		ruleTwo.addNode(nodeE);
		ruleTwo.addNode(nodeC);
		ruleTwo.addNode(nodeD);

		// D = .
		Rule ruleThree = new Rule(nodeD);

		Rule ruleFour = new Rule(nodeD);

		// D = "f" .
		ruleFour.addNode(nodeF);

		// Arrange - create grammar
		Grammar grammar = new Grammar();

		grammar.addRule(ruleOne);
		grammar.addRule(ruleTwo);
		grammar.addRule(ruleThree);
		grammar.addRule(ruleFour);

		grammar.setHeadRule(ruleOne);

		// Arrange - create words

		// Word: e
		Word wordOne = new Word(nodeE);

		// Word: f
		Word wordTwo = new Word(nodeF);

		// Arrange - create testFollowSetDictionary
		Map<Node, Set<Word>> testFollowSetDictionary =
			new HashMap<Node, Set<Word>>();

		testFollowSetDictionary.put(nodeA,
			new HashSet<Word>(Arrays.asList(Word.getEmptyWord())));

		testFollowSetDictionary.put(nodeB,
			new HashSet<Word>(Arrays.asList(wordOne)));

		testFollowSetDictionary.put(nodeC,
			new HashSet<Word>(Arrays.asList(wordOne, wordTwo)));

		testFollowSetDictionary.put(nodeD,
			new HashSet<Word>(Arrays.asList(wordOne)));

		// Act
		Map<Node, Set<Word>> followSetDictionary =
			this._grammarService.getFollowSetDictionary(grammar);

		// Assert
		Assert.assertEquals(testFollowSetDictionary, followSetDictionary);
	}

	@Test
	public void getFollowSetDictionary_RuleWithSimpleCycle_ReturnsValidDictionary()
		throws Exception
	{
		// Arrange - create nodes
		Node nodeA = new Node(NodeKind.Nonterminal, "A");
		Node nodeB = new Node(NodeKind.Nonterminal, "B");
		Node nodeC = new Node(NodeKind.Terminal, "c");
		Node nodeD = new Node(NodeKind.Terminal, "d");

		// Arrange - create rules
		Rule ruleOne = new Rule(nodeA);

		// A = "c" .
		ruleOne.addNode(nodeC);

		Rule ruleTwo = new Rule(nodeA);

		// B = "c", B, "d" .
		ruleTwo.addNode(nodeC);
		ruleTwo.addNode(nodeB);
		ruleTwo.addNode(nodeD);

		// Arrange - create grammar
		Grammar grammar = new Grammar();

		grammar.addRule(ruleOne);
		grammar.addRule(ruleTwo);

		grammar.setHeadRule(ruleOne);

		// Arrange - create words

		// Word: d
		Word wordOne = new Word(nodeD);

		// Arrange - create testFollowSetDictionary
		Map<Node, Set<Word>> testFollowSetDictionary =
			new HashMap<Node, Set<Word>>();

		testFollowSetDictionary.put(nodeA,
			new HashSet<Word>(Arrays.asList(Word.getEmptyWord())));

		testFollowSetDictionary.put(nodeB,
			new HashSet<Word>(Arrays.asList(wordOne)));

		// Act
		Map<Node, Set<Word>> followSetDictionary =
			this._grammarService.getFollowSetDictionary(grammar);

		// Assert
		Assert.assertEquals(testFollowSetDictionary, followSetDictionary);
	}

	@Test
	public void getFollowSetDictionary_RuleWithTwoNonterminals_ReturnsValidDictionary()
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

		Rule ruleTwo = new Rule(nodeC);

		// C = "d", "e" .
		ruleTwo.addNode(nodeD);
		ruleTwo.addNode(nodeE);

		// Arrange - create grammar
		Grammar grammar = new Grammar();

		grammar.addRule(ruleOne);
		grammar.addRule(ruleTwo);

		grammar.setHeadRule(ruleOne);

		// Arrange - create words

		// Word: d
		Word wordOne = new Word(nodeD);

		// Arrange - create testFollowSetDictionary
		Map<Node, Set<Word>> testFollowSetDictionary =
			new HashMap<Node, Set<Word>>();

		testFollowSetDictionary.put(nodeA,
			new HashSet<Word>(Arrays.asList(Word.getEmptyWord())));

		testFollowSetDictionary.put(nodeB,
			new HashSet<Word>(Arrays.asList(wordOne)));

		testFollowSetDictionary.put(nodeC,
			new HashSet<Word>(Arrays.asList(Word.getEmptyWord())));

		// Act
		Map<Node, Set<Word>> followSetDictionary =
			this._grammarService.getFollowSetDictionary(grammar);

		// Assert
		Assert.assertEquals(testFollowSetDictionary, followSetDictionary);
	}

	@Test
	public void getFollowSetDictionary_RuleWithTwoTerminals_ReturnsValidDictionary()
		throws Exception
	{
		// Arrange - create nodes
		Node nodeA = new Node(NodeKind.Nonterminal, "A");
		Node nodeB = new Node(NodeKind.Nonterminal, "B");
		Node nodeC = new Node(NodeKind.Terminal, "c");
		Node nodeD = new Node(NodeKind.Terminal, "d");

		// Arrange - create rules
		Rule ruleOne = new Rule(nodeA);

		// A = B, "c", "d" .
		ruleOne.addNode(nodeB);
		ruleOne.addNode(nodeC);
		ruleOne.addNode(nodeD);

		// Arrange - create grammar
		Grammar grammar = new Grammar();

		grammar.addRule(ruleOne);

		grammar.setHeadRule(ruleOne);

		// Arrange - create words

		// Word: c
		Word wordOne = new Word(nodeC);

		// Arrange - create testFollowSetDictionary
		Map<Node, Set<Word>> testFollowSetDictionary =
			new HashMap<Node, Set<Word>>();

		testFollowSetDictionary.put(nodeA,
			new HashSet<Word>(Arrays.asList(Word.getEmptyWord())));

		testFollowSetDictionary.put(nodeB,
			new HashSet<Word>(Arrays.asList(wordOne)));

		// Act
		Map<Node, Set<Word>> followSetDictionary =
			this._grammarService.getFollowSetDictionary(grammar);

		// Assert
		Assert.assertEquals(testFollowSetDictionary, followSetDictionary);
	}

	@Test
	public void getNonterminalNodes_GrammarIsEmpty_ReturnsEmptySet()
	{
		// Arrange
		Grammar grammar = new Grammar();

		// Act
		Set<Node> nonterminalNodes =
			this._grammarService.getNonterminalNodes(grammar);

		// Assert
		Assert.assertEquals(0, nonterminalNodes.size());
	}

	@Test(expected = IllegalArgumentException.class)
	public void getNonterminalNodes_GrammarIsNull_ThrowsIllegalArgumentException()
	{
		// Act & Assert
		this._grammarService.getNonterminalNodes(null);
	}

	@Test
	public void getNonterminalNodes_GrammarIsValid_ReturnsNonterminalNodesSet()
		throws Exception
	{
		// Arrange - create nodes
		Node nodeA = new Node(NodeKind.Nonterminal, "A");
		Node nodeB = new Node(NodeKind.Nonterminal, "B");
		Node nodeC = new Node(NodeKind.Terminal, "c");

		// Arrange - create rule
		Rule ruleOne = new Rule(nodeA);

		// A = B, "c" .
		ruleOne.addNode(nodeB);
		ruleOne.addNode(nodeC);

		// Arrange - create grammar
		Grammar grammar = new Grammar();

		grammar.addRule(ruleOne);

		grammar.setHeadRule(ruleOne);

		// Arrange - create testNonterminalNodes
		Set<Node> testNonterminalNodes =
			new HashSet<Node>(Arrays.asList(nodeA, nodeB));

		// Act
		Set<Node> nonterminalNodes =
			this._grammarService.getNonterminalNodes(grammar);

		// Assert
		Assert.assertEquals(testNonterminalNodes, nonterminalNodes);
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
			new HashSet<Rule>(Arrays.asList(ruleTwo, ruleThree));

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
			new HashSet<Rule>(Arrays.asList(ruleOne));

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

		// Arrange - create rules
		Rule ruleOne = new Rule(nodeA);

		// A = "b", A .
		ruleOne.addNode(nodeB);
		ruleOne.addNode(nodeA);

		// A = .
		Rule ruleTwo = new Rule(nodeA);

		Rule ruleThree = new Rule(nodeA);

		// A = "b" .
		ruleThree.addNode(nodeB);

		// Arrange - create grammar
		Grammar grammar = new Grammar();

		grammar.addRule(ruleOne);
		grammar.addRule(ruleTwo);
		grammar.addRule(ruleThree);

		grammar.setHeadRule(ruleOne);

		// Arrange - create rightRecursiveRules list
		Set<Rule> testRightRecursiveRules =
			new HashSet<Rule>(Arrays.asList(ruleOne));

		// Act
		Set<Rule> rightRecursiveRules =
			this._grammarService.getRightRecursiveRules(grammar);

		// Assert
		Assert.assertEquals(testRightRecursiveRules, rightRecursiveRules);
	}

	@Test
	public void getTerminalNodes_GrammarIsEmpty_ReturnsEmptySet()
	{
		// Arrange
		Grammar grammar = new Grammar();

		// Act
		Set<Node> terminalNodes =
			this._grammarService.getTerminalNodes(grammar);

		// Assert
		Assert.assertEquals(0, terminalNodes.size());
	}

	@Test(expected = IllegalArgumentException.class)
	public void getTerminalNodes_GrammarIsNull_ThrowsIllegalArgumentException()
	{
		// Act & Assert
		this._grammarService.getTerminalNodes(null);
	}

	@Test
	public void getTerminalNodes_GrammarIsValid_ReturnsTerminalNodesSet()
		throws Exception
	{
		// Arrange - create nodes
		Node nodeA = new Node(NodeKind.Nonterminal, "A");
		Node nodeB = new Node(NodeKind.Nonterminal, "B");
		Node nodeC = new Node(NodeKind.Terminal, "c");

		// Arrange - create rule
		Rule ruleOne = new Rule(nodeA);

		// A = B, "c" .
		ruleOne.addNode(nodeB);
		ruleOne.addNode(nodeC);

		// Arrange - create grammar
		Grammar grammar = new Grammar();

		grammar.addRule(ruleOne);

		grammar.setHeadRule(ruleOne);

		// Arrange - create testTerminalNodes
		Set<Node> testTerminalNodes = new HashSet<Node>(Arrays.asList(nodeC));

		// Act
		Set<Node> terminalNodes =
			this._grammarService.getTerminalNodes(grammar);

		// Assert
		Assert.assertEquals(testTerminalNodes, terminalNodes);
	}

	@Test(expected = NonexistentNodeException.class)
	public void isLLOneGrammar_FirstSetDictionaryDoesNotContainItemForSomeNode_ThrowsNonexistentNodeException()
		throws Exception
	{
		// Arrange - create grammar
		Node nodeA = new Node(NodeKind.Nonterminal, "A");
		Node nodeB = new Node(NodeKind.Nonterminal, "B");

		Rule ruleOne = new Rule(nodeA);

		ruleOne.addNode(nodeB);

		Rule ruleTwo = new Rule(nodeA);

		Grammar grammar = new Grammar();

		grammar.addRule(ruleOne);
		grammar.addRule(ruleTwo);
		grammar.setHeadRule(ruleOne);

		// Arrange - create firstSetDictionary
		Map<Node, Set<Word>> firstSetDictionary =
			new HashMap<Node, Set<Word>>();

		// Arrange - create followSetDictionary
		Map<Node, Set<Word>> followSetDictionary =
			new HashMap<Node, Set<Word>>();

		followSetDictionary.put(nodeA, new HashSet<Word>());

		// Act & Assert
		this._grammarService.isLLOneGrammar(grammar, firstSetDictionary,
			followSetDictionary);
	}

	@Test(expected = IllegalArgumentException.class)
	public void isLLOneGrammar_FirstSetDictionaryIsNull_ThrowsIllegalArgumentException()
		throws Exception
	{
		// Arrange
		Grammar grammar = new Grammar();

		Map<Node, Set<Word>> followSetDictionary =
			new HashMap<Node, Set<Word>>();

		// Act & Assert
		this._grammarService.isLLOneGrammar(grammar, null, followSetDictionary);
	}

	@Test(expected = NonexistentNodeException.class)
	public void isLLOneGrammar_FollowSetDictionaryDoesNotContainItemForSomeNode_ThrowsNonexistentNodeException()
		throws Exception
	{
		// Arrange - create grammar
		Node nodeA = new Node(NodeKind.Nonterminal, "A");
		Node nodeB = new Node(NodeKind.Nonterminal, "B");

		Rule ruleOne = new Rule(nodeA);

		ruleOne.addNode(nodeB);

		Rule ruleTwo = new Rule(nodeA);

		Grammar grammar = new Grammar();

		grammar.addRule(ruleOne);
		grammar.addRule(ruleTwo);
		grammar.setHeadRule(ruleOne);

		// Arrange - create firstSetDictionary
		Map<Node, Set<Word>> firstSetDictionary =
			new HashMap<Node, Set<Word>>();

		firstSetDictionary.put(nodeB, new HashSet<Word>());

		// Arrange - create followSetDictionary
		Map<Node, Set<Word>> followSetDictionary =
			new HashMap<Node, Set<Word>>();

		// Act & Assert
		this._grammarService.isLLOneGrammar(grammar, firstSetDictionary,
			followSetDictionary);
	}

	@Test(expected = IllegalArgumentException.class)
	public void isLLOneGrammar_FollowSetDictionaryIsNull_ThrowsIllegalArgumentException()
		throws Exception
	{
		// Arrange
		Grammar grammar = new Grammar();

		Map<Node, Set<Word>> firstSetDictionary =
			new HashMap<Node, Set<Word>>();

		// Act & Assert
		this._grammarService.isLLOneGrammar(grammar, firstSetDictionary, null);
	}

	@Test
	public void isLLOneGrammar_GrammarIsEmpty_ReturnsTrue() throws Exception
	{
		// Arrange
		Grammar grammar = new Grammar();

		Map<Node, Set<Word>> firstSetDictionary =
			new HashMap<Node, Set<Word>>();

		Map<Node, Set<Word>> followSetDictionary =
			new HashMap<Node, Set<Word>>();

		// Act
		boolean result =
			this._grammarService.isLLOneGrammar(grammar, firstSetDictionary,
				followSetDictionary);

		// Assert
		Assert.assertEquals(true, result);
	}

	@Test
	public void isLLOneGrammar_GrammarIsLLOneGrammar_ReturnsTrue()
		throws Exception
	{
		// Arrange - create grammar
		Node nodeA = new Node(NodeKind.Nonterminal, "A");
		Node nodeB = new Node(NodeKind.Nonterminal, "B");
		Node nodeC = new Node(NodeKind.Nonterminal, "C");

		// A = B .
		Rule ruleOne = new Rule(nodeA);

		ruleOne.addNode(nodeB);

		// A = C .
		Rule ruleTwo = new Rule(nodeA);

		ruleTwo.addNode(nodeC);

		Grammar grammar = new Grammar();

		grammar.addRule(ruleOne);
		grammar.addRule(ruleTwo);
		grammar.setHeadRule(ruleOne);

		// Arrange - create firstSetDictionary
		Word wordOne = new Word(new Node(NodeKind.Terminal, "d"));

		Map<Node, Set<Word>> firstSetDictionary =
			new HashMap<Node, Set<Word>>();

		firstSetDictionary.put(nodeB,
			new HashSet<Word>(Arrays.asList(Word.getEmptyWord())));
		firstSetDictionary
				.put(nodeC, new HashSet<Word>(Arrays.asList(wordOne)));

		// Arrange - create followSetDictionary
		Word wordTwo = new Word(new Node(NodeKind.Terminal, "e"));

		Map<Node, Set<Word>> followSetDictionary =
			new HashMap<Node, Set<Word>>();

		followSetDictionary.put(nodeA,
			new HashSet<Word>(Arrays.asList(wordTwo)));

		// Act
		boolean result =
			this._grammarService.isLLOneGrammar(grammar, firstSetDictionary,
				followSetDictionary);

		// Assert
		Assert.assertEquals(true, result);
	}

	@Test(expected = IllegalArgumentException.class)
	public void isLLOneGrammar_GrammarIsNull_ThrowsIllegalArgumentException()
		throws Exception
	{
		// Arrange
		Map<Node, Set<Word>> firstSetDictionary =
			new HashMap<Node, Set<Word>>();

		Map<Node, Set<Word>> followSetDictionary =
			new HashMap<Node, Set<Word>>();

		// Act & Assert
		this._grammarService.isLLOneGrammar(null, firstSetDictionary,
			followSetDictionary);
	}

	@Test
	public void isLLOneGrammar_IntersectionOfFirstAndFollowSetsIsNotEmpty_ReturnsFalse()
		throws Exception
	{
		// Arrange - create grammar
		Node nodeA = new Node(NodeKind.Nonterminal, "A");
		Node nodeB = new Node(NodeKind.Nonterminal, "B");
		Node nodeC = new Node(NodeKind.Nonterminal, "C");

		// A = B .
		Rule ruleOne = new Rule(nodeA);

		ruleOne.addNode(nodeB);

		// A = C .
		Rule ruleTwo = new Rule(nodeA);

		ruleTwo.addNode(nodeC);

		Grammar grammar = new Grammar();

		grammar.addRule(ruleOne);
		grammar.addRule(ruleTwo);
		grammar.setHeadRule(ruleOne);

		// Arrange - create firstSetDictionary
		Word commonWord = new Word(new Node(NodeKind.Terminal, "d"));

		Map<Node, Set<Word>> firstSetDictionary =
			new HashMap<Node, Set<Word>>();

		firstSetDictionary.put(nodeB,
			new HashSet<Word>(Arrays.asList(Word.getEmptyWord())));
		firstSetDictionary.put(nodeC,
			new HashSet<Word>(Arrays.asList(commonWord)));

		// Arrange - create followSetDictionary
		Map<Node, Set<Word>> followSetDictionary =
			new HashMap<Node, Set<Word>>();

		followSetDictionary.put(nodeA,
			new HashSet<Word>(Arrays.asList(commonWord)));

		// Act
		boolean result =
			this._grammarService.isLLOneGrammar(grammar, firstSetDictionary,
				followSetDictionary);

		// Assert
		Assert.assertEquals(false, result);
	}

	@Test
	public void isLLOneGrammar_IntersectionOfTwoFirstSetsIsNotEmpty_ReturnsFalse()
		throws Exception
	{
		// Arrange - create grammar
		Node nodeA = new Node(NodeKind.Nonterminal, "A");
		Node nodeB = new Node(NodeKind.Nonterminal, "B");
		Node nodeC = new Node(NodeKind.Nonterminal, "C");

		// A = B .
		Rule ruleOne = new Rule(nodeA);

		ruleOne.addNode(nodeB);

		// A = C .
		Rule ruleTwo = new Rule(nodeA);

		ruleTwo.addNode(nodeC);

		Grammar grammar = new Grammar();

		grammar.addRule(ruleOne);
		grammar.addRule(ruleTwo);
		grammar.setHeadRule(ruleOne);

		// Arrange - create firstSetDictionary
		Word commonWord = new Word(new Node(NodeKind.Terminal, "d"));

		Map<Node, Set<Word>> firstSetDictionary =
			new HashMap<Node, Set<Word>>();

		firstSetDictionary.put(nodeB,
			new HashSet<Word>(Arrays.asList(commonWord)));
		firstSetDictionary.put(nodeC,
			new HashSet<Word>(Arrays.asList(commonWord)));

		// Arrange - create followSetDictionary
		Map<Node, Set<Word>> followSetDictionary =
			new HashMap<Node, Set<Word>>();

		followSetDictionary.put(nodeA, new HashSet<Word>());

		// Act
		boolean result =
			this._grammarService.isLLOneGrammar(grammar, firstSetDictionary,
				followSetDictionary);

		// Assert
		Assert.assertEquals(false, result);
	}

	@Before
	public void setUp() throws Exception
	{
		this._grammarService = new GrammarService();
	}
}
