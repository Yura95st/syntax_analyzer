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

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class ControlTableBuildingServiceTests
{
	private Map<Node, Set<Word>> _firstSetDictionary;

	private Grammar _grammar;

	private IGrammarService _mockedGrammarService;

	private Rule _ruleOne;

	private Rule _ruleTwo;

	@Test
	public void buildControlTable_GrammarIsEmpty_ReturnEmptyMap()
		throws Exception
	{
		// Arrange
		Grammar grammar = new Grammar();

		IControlTableBuildingService target =
			new ControlTableBuildingService(this._mockedGrammarService);

		// Act
		Map<ControlTableItem, Rule> controlTable =
			target.buildControlTable(grammar);

		// Assert
		Assert.assertEquals(0, controlTable.size());
	}

	@Test
	public void buildControlTable_GrammarIsNotLLOneGrammarCaseOne_ThrowsGrammarIsNotLLOneGrammarException()
		throws Exception
	{
		// Arrange - mock grammarService
		Set<Word> firstSet =
			new HashSet<Word>(Arrays.asList(Word.getEmptyWord()));

		Mockito.when(
			this._mockedGrammarService.getFirstSetForNodesList(
				this._ruleOne.getNodes(), this._firstSetDictionary)).thenReturn(
			firstSet);

		Mockito.when(
			this._mockedGrammarService.getFirstSetForNodesList(
				this._ruleTwo.getNodes(), this._firstSetDictionary)).thenReturn(
			firstSet);

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
			this._grammar);

		Mockito.verify(this._mockedGrammarService).getFirstSetForNodesList(
			this._ruleOne.getNodes(), this._firstSetDictionary);

		Mockito.verify(this._mockedGrammarService).getFirstSetForNodesList(
			this._ruleTwo.getNodes(), this._firstSetDictionary);
	}

	@Test
	public void buildControlTable_GrammarIsNotLLOneGrammarCaseTwo_ThrowsGrammarIsNotLLOneGrammarException()
		throws Exception
	{
		// Arrange - mock grammarService
		Set<Word> firstSetOne =
			new HashSet<Word>(Arrays.asList(Word.getEmptyWord()));

		Set<Word> firstSetTwo =
			new HashSet<Word>(Arrays.asList(new Word(new Node(
				NodeKind.Terminal, "a"))));

		Mockito.when(
			this._mockedGrammarService.getFirstSetForNodesList(
				this._ruleOne.getNodes(), this._firstSetDictionary)).thenReturn(
			firstSetOne);

		Mockito.when(
			this._mockedGrammarService.getFirstSetForNodesList(
				this._ruleTwo.getNodes(), this._firstSetDictionary)).thenReturn(
			firstSetTwo);

		Map<Node, Set<Word>> followSetDictionary =
			new HashMap<Node, Set<Word>>();

		followSetDictionary.put(this._ruleOne.getHeadNode(), firstSetTwo);

		Mockito.when(
			this._mockedGrammarService.getFollowSetDictionary(this._grammar))
				.thenReturn(followSetDictionary);

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
			this._grammar);

		Mockito.verify(this._mockedGrammarService).getFollowSetDictionary(
			this._grammar);

		Mockito.verify(this._mockedGrammarService).getFirstSetForNodesList(
			this._ruleOne.getNodes(), this._firstSetDictionary);

		Mockito.verify(this._mockedGrammarService).getFirstSetForNodesList(
			this._ruleTwo.getNodes(), this._firstSetDictionary);
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

	@Before
	public void setUp() throws Exception
	{
		this.initRules();

		this.initGrammar();

		this.mockGrammarService();
	}

	private void initGrammar() throws Exception
	{
		this._grammar = new Grammar();

		this._grammar.addRule(this._ruleOne);
		this._grammar.addRule(this._ruleTwo);

		this._grammar.setHeadRule(this._ruleOne);
	}

	private void initRules()
	{
		Node nodeA = new Node(NodeKind.Nonterminal, "A");
		Node nodeB = new Node(NodeKind.Nonterminal, "B");
		Node nodeC = new Node(NodeKind.Nonterminal, "C");

		this._ruleOne = new Rule(nodeA);

		this._ruleOne.addNode(nodeB);

		this._ruleTwo = new Rule(nodeA);

		this._ruleTwo.addNode(nodeC);
	}

	private void mockGrammarService() throws Exception
	{
		this._mockedGrammarService = Mockito.mock(IGrammarService.class);

		this._firstSetDictionary = new HashMap<Node, Set<Word>>();

		Mockito.when(
			this._mockedGrammarService.getFirstSetDictionary(this._grammar))
				.thenReturn(this._firstSetDictionary);
	}
}
