package grammar_parser.Services.Concrete;

import grammar_parser.Enums.NodeKind;
import grammar_parser.Exceptions.GrammarIsNotLLOneGrammarException;
import grammar_parser.Models.ControlTableItem;
import grammar_parser.Models.Grammar;
import grammar_parser.Models.Node;
import grammar_parser.Models.Rule;
import grammar_parser.Models.Word;
import grammar_parser.Services.Abstract.IControlTableBuildingService;
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
import org.mockito.Matchers;
import org.mockito.Mockito;

public class ControlTableBuildingServiceTests
{
	private Map<Node, Set<Word>> _firstSetDictionary;

	private HashMap<Node, Set<Word>> _followSetDictionary;

	private Grammar _grammar;

	private IGrammarService _mockedGrammarService;

	private Rule _ruleOne;

	private Rule _ruleThree;

	private Rule _ruleTwo;

	@Test
	public void buildControlTable_GrammarIsEmpty_ReturnsEmptyMap()
		throws Exception
	{
		// Arrange - mock grammarService
		List<Rule> rules = new ArrayList<Rule>();

		Mockito.when(
			this._mockedGrammarService.getAllRulesFromGrammar(Matchers
					.same(this._grammar))).thenReturn(rules);

		IControlTableBuildingService target =
			new ControlTableBuildingService(this._mockedGrammarService);

		// Act
		Map<ControlTableItem, Rule> controlTable =
			target.buildControlTable(this._grammar);

		// Assert
		Assert.assertEquals(0, controlTable.size());

		Mockito.verify(this._mockedGrammarService).getAllRulesFromGrammar(
			Matchers.same(this._grammar));

		Mockito.verify(this._mockedGrammarService, Mockito.never())
				.getFirstSetDictionary(Matchers.same(this._grammar));

		Mockito.verify(this._mockedGrammarService, Mockito.never())
				.getFollowSetDictionary(Matchers.same(this._grammar));
	}

	@Test
	public void buildControlTable_GrammarIsNotLLOneGrammar_ThrowsGrammarIsNotLLOneGrammarException()
		throws Exception
	{
		// Arrange - mock grammarService
		Mockito.when(
			this._mockedGrammarService.isLLOneGrammar(
				Matchers.same(this._grammar),
				Matchers.same(this._firstSetDictionary),
				Matchers.same(this._followSetDictionary))).thenReturn(false);

		// Arrange - create target
		IControlTableBuildingService target =
			new ControlTableBuildingService(this._mockedGrammarService);

		boolean exceptionIsThrown = false;

		// Act
		try
		{
			target.buildControlTable(this._grammar);
		}
		catch (GrammarIsNotLLOneGrammarException e)
		{
			exceptionIsThrown = true;
		}

		// Assert
		Assert.assertEquals(true, exceptionIsThrown);

		Mockito.verify(this._mockedGrammarService).getFirstSetDictionary(
			Matchers.same(this._grammar));

		Mockito.verify(this._mockedGrammarService).getFollowSetDictionary(
			Matchers.same(this._grammar));

		Mockito.verify(this._mockedGrammarService).isLLOneGrammar(
			Matchers.same(this._grammar),
			Matchers.same(this._firstSetDictionary),
			Matchers.same(this._followSetDictionary));

		Mockito.verify(this._mockedGrammarService, Mockito.never())
				.getFirstPlusFollowSet(Matchers.anySet(), Matchers.anySet());
	}

	@Test(expected = IllegalArgumentException.class)
	public void buildControlTable_GrammarIsNull_ThrowsIllegalArgumentException()
		throws Exception
	{
		// Arrange
		IControlTableBuildingService target =
			new ControlTableBuildingService(this._mockedGrammarService);

		// Act & Assert
		target.buildControlTable(null);
	}

	@Test
	public void buildControlTable_GrammarIsValid_ReturnsControlTable()
		throws Exception
	{
		// Arrange - mock getFirstSetForNodesList method of the grammarService
		Map<Rule, Set<Word>> firstSetForNodesDictionary =
			new HashMap<Rule, Set<Word>>();

		firstSetForNodesDictionary.put(this._ruleOne, new HashSet<Word>());
		firstSetForNodesDictionary.put(this._ruleTwo, new HashSet<Word>());
		firstSetForNodesDictionary.put(this._ruleThree, new HashSet<Word>(
			Arrays.asList(Word.getEmptyWord())));

		Mockito.when(
			this._mockedGrammarService.getFirstSetForNodesList(
				Matchers.eq(this._ruleOne.getNodes()),
				Matchers.same(this._firstSetDictionary))).thenReturn(
			firstSetForNodesDictionary.get(this._ruleOne));

		Mockito.when(
			this._mockedGrammarService.getFirstSetForNodesList(
				Matchers.eq(this._ruleTwo.getNodes()),
				Matchers.same(this._firstSetDictionary))).thenReturn(
			firstSetForNodesDictionary.get(this._ruleTwo));

		// Arrange - mock getFirstPlusFollowSet method of the grammarService
		Word wordOne = new Word(new Node(NodeKind.Terminal, "a"));
		Word wordTwo = new Word(new Node(NodeKind.Terminal, "b"));
		Word wordThree = new Word(new Node(NodeKind.Terminal, "c"));

		Map<Rule, Set<Word>> firstPlusFollowSetDictionary =
			new HashMap<Rule, Set<Word>>();

		firstPlusFollowSetDictionary.put(this._ruleOne, new HashSet<Word>(
			Arrays.asList(Word.getEmptyWord(), wordOne)));

		firstPlusFollowSetDictionary.put(this._ruleTwo, new HashSet<Word>(
			Arrays.asList(wordTwo)));

		firstPlusFollowSetDictionary.put(this._ruleThree, new HashSet<Word>(
			Arrays.asList(Word.getEmptyWord(), wordThree)));

		Mockito.when(
			this._mockedGrammarService.getFirstPlusFollowSet(Matchers
					.same(firstSetForNodesDictionary.get(this._ruleOne)),
				Matchers.same(this._followSetDictionary.get(this._ruleOne
						.getHeadNode())))).thenReturn(
			firstPlusFollowSetDictionary.get(this._ruleOne));

		Mockito.when(
			this._mockedGrammarService.getFirstPlusFollowSet(Matchers
					.same(firstSetForNodesDictionary.get(this._ruleTwo)),
				Matchers.same(this._followSetDictionary.get(this._ruleTwo
						.getHeadNode())))).thenReturn(
			firstPlusFollowSetDictionary.get(this._ruleTwo));

		Mockito.when(
			this._mockedGrammarService.getFirstPlusFollowSet(Matchers
					.eq(firstSetForNodesDictionary.get(this._ruleThree)),
				Matchers.same(this._followSetDictionary.get(this._ruleThree
						.getHeadNode())))).thenReturn(
			firstPlusFollowSetDictionary.get(this._ruleThree));

		// Arrange - create testControlTable
		Map<ControlTableItem, Rule> testControlTable =
			new HashMap<ControlTableItem, Rule>();

		testControlTable.put(new ControlTableItem(this._ruleOne.getHeadNode(),
			Word.getEmptyWord()), this._ruleOne);

		testControlTable.put(new ControlTableItem(this._ruleOne.getHeadNode(),
			wordOne), this._ruleOne);

		testControlTable.put(new ControlTableItem(this._ruleTwo.getHeadNode(),
			wordTwo), this._ruleTwo);

		testControlTable.put(new ControlTableItem(
			this._ruleThree.getHeadNode(), Word.getEmptyWord()),
			this._ruleThree);

		testControlTable.put(new ControlTableItem(
			this._ruleThree.getHeadNode(), wordThree), this._ruleThree);

		// Arrange - create target
		IControlTableBuildingService target =
			new ControlTableBuildingService(this._mockedGrammarService);

		// Act
		Map<ControlTableItem, Rule> controlTable =
			target.buildControlTable(this._grammar);

		// Assert
		Assert.assertEquals(testControlTable, controlTable);

		Mockito.verify(this._mockedGrammarService).getFirstSetForNodesList(
			Matchers.eq(this._ruleOne.getNodes()),
			Matchers.same(this._firstSetDictionary));

		Mockito.verify(this._mockedGrammarService).getFirstSetForNodesList(
			Matchers.eq(this._ruleTwo.getNodes()),
			Matchers.same(this._firstSetDictionary));

		Mockito.verify(this._mockedGrammarService, Mockito.never())
				.getFirstSetForNodesList(
					Matchers.eq(this._ruleThree.getNodes()),
					Matchers.same(this._firstSetDictionary));

		Mockito.verify(this._mockedGrammarService).getFirstPlusFollowSet(
			Matchers.same(firstSetForNodesDictionary.get(this._ruleOne)),
			Matchers.same(this._followSetDictionary.get(this._ruleOne
					.getHeadNode())));

		Mockito.verify(this._mockedGrammarService).getFirstPlusFollowSet(
			Matchers.same(firstSetForNodesDictionary.get(this._ruleTwo)),
			Matchers.same(this._followSetDictionary.get(this._ruleTwo
					.getHeadNode())));

		Mockito.verify(this._mockedGrammarService).getFirstPlusFollowSet(
			Matchers.eq(firstSetForNodesDictionary.get(this._ruleThree)),
			Matchers.same(this._followSetDictionary.get(this._ruleThree
					.getHeadNode())));
	}

	@Before
	public void setUp() throws Exception
	{
		this.initRules();

		this.initGrammar();

		this.initFirstAndFollowSetDictionaries();

		this.mockGrammarService();
	}

	private void initFirstAndFollowSetDictionaries()
	{
		this._firstSetDictionary = new HashMap<Node, Set<Word>>();

		this._followSetDictionary = new HashMap<Node, Set<Word>>();

		this._followSetDictionary.put(this._ruleOne.getHeadNode(),
			new HashSet<Word>());

		this._followSetDictionary.put(this._ruleTwo.getHeadNode(),
			new HashSet<Word>());

		this._followSetDictionary.put(this._ruleThree.getHeadNode(),
			new HashSet<Word>());
	}

	private void initGrammar() throws Exception
	{
		this._grammar = new Grammar();

		this._grammar.addRule(this._ruleOne);
		this._grammar.addRule(this._ruleTwo);
		this._grammar.addRule(this._ruleThree);

		this._grammar.setHeadRule(this._ruleOne);
	}

	private void initRules()
	{
		Node nodeA = new Node(NodeKind.Nonterminal, "A");
		Node nodeB = new Node(NodeKind.Nonterminal, "B");
		Node nodeC = new Node(NodeKind.Terminal, "c");
		Node nodeD = new Node(NodeKind.Terminal, "d");

		this._ruleOne = new Rule(nodeA);

		this._ruleOne.addNode(nodeC);

		this._ruleTwo = new Rule(nodeA);

		this._ruleTwo.addNode(nodeD);

		this._ruleThree = new Rule(nodeB);
	}

	private void mockGrammarService() throws Exception
	{
		this._mockedGrammarService = Mockito.mock(IGrammarService.class);

		List<Rule> rules =
			Arrays.asList(this._ruleOne, this._ruleTwo, this._ruleThree);

		Mockito.when(
			this._mockedGrammarService.getAllRulesFromGrammar(Matchers
					.same(this._grammar))).thenReturn(rules);

		Mockito.when(
			this._mockedGrammarService.getFirstSetDictionary(Matchers
					.same(this._grammar))).thenReturn(this._firstSetDictionary);

		Mockito.when(
			this._mockedGrammarService.getFollowSetDictionary(Matchers
					.same(this._grammar)))
				.thenReturn(this._followSetDictionary);

		Mockito.when(
			this._mockedGrammarService.isLLOneGrammar(
				Matchers.same(this._grammar),
				Matchers.same(this._firstSetDictionary),
				Matchers.same(this._followSetDictionary))).thenReturn(true);
	}
}
