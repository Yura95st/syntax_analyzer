package syntax_analyzer.Analyzers.Concrete;

import grammar_parser.Enums.NodeKind;
import grammar_parser.Exceptions.GrammarIsNotLLOneGrammarException;
import grammar_parser.Models.ControlTableItem;
import grammar_parser.Models.Grammar;
import grammar_parser.Models.Node;
import grammar_parser.Models.Rule;
import grammar_parser.Models.Word;
import grammar_parser.Services.Abstract.IControlTableBuildingService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import syntax_analyzer.Analyzers.Abstract.ISyntaxAnalyzer;
import syntax_analyzer.Enums.TokenKind;
import syntax_analyzer.Exceptions.GrammarIsInvalidException;
import syntax_analyzer.Exceptions.GrammarIsNotSetException;
import syntax_analyzer.Exceptions.SyntaxAnalyzerErrorException;
import syntax_analyzer.Models.Location;
import syntax_analyzer.Models.SyntaxAnalyzerResult;
import syntax_analyzer.Models.Token;

public class SyntaxAnalyzerTests
{
	private IControlTableBuildingService _controlTableBuildingServiceMock;

	@Test
	public void run_ControlTableBuildingServiceReturnsEmptyMap_ReturnsResultWithErrorOnFirstToken()
		throws Exception
	{
		// Arrange - create Grammar
		Node nodeS = new Node(NodeKind.Nonterminal, "S");

		// S = .
		Rule ruleOne = new Rule(nodeS);

		Grammar grammar = new Grammar();

		grammar.addRule(ruleOne);
		grammar.setHeadRule(ruleOne);

		// Arrange - mock controlTableBuildingService
		Map<ControlTableItem, Rule> controlTable =
			new HashMap<ControlTableItem, Rule>();

		Mockito.when(
			this._controlTableBuildingServiceMock.buildControlTable(grammar))
				.thenReturn(controlTable);

		// Arrange - create tokens
		List<Token> tokens =
			Arrays.asList(new Token[] {
				new Token(TokenKind.Identifier, "x", new Location(0, 1)),
				new Token(TokenKind.Operator, ":=", new Location(1, 2)),
				new Token(TokenKind.Number, "2", new Location(3, 1)),
				new Token(TokenKind.Punctuator, ";", new Location(4, 1))
			});

		// Arrange - create target
		ISyntaxAnalyzer target =
			new SyntaxAnalyzer(this._controlTableBuildingServiceMock);

		target.setGrammar(grammar);
		target.setTokens(tokens);

		// Act
		SyntaxAnalyzerResult result = target.run();

		// Assert
		Assert.assertEquals(false, result.isSyntaxValid());
		Assert.assertEquals(tokens.get(0), result.getWrongToken());

		Mockito.verify(this._controlTableBuildingServiceMock)
				.buildControlTable(grammar);
	}

	@Test(expected = SyntaxAnalyzerErrorException.class)
	public void run_ControlTableBuildingServiceThrewException_ThrowsSyntaxAnalyzerErrorException()
		throws Exception
	{
		// Arrange - create Grammar
		Node nodeS = new Node(NodeKind.Nonterminal, "S");

		// S = .
		Rule ruleOne = new Rule(nodeS);

		Grammar grammar = new Grammar();

		grammar.addRule(ruleOne);
		grammar.setHeadRule(ruleOne);

		// Arrange - mock controlTableBuildingService
		Mockito.when(
			this._controlTableBuildingServiceMock.buildControlTable(grammar))
				.thenThrow(new Exception());

		// Arrange - create target
		ISyntaxAnalyzer target =
			new SyntaxAnalyzer(this._controlTableBuildingServiceMock);

		target.setGrammar(grammar);

		// Act & Assert
		target.run();
	}

	@Test(expected = GrammarIsNotLLOneGrammarException.class)
	public void run_ControlTableBuildingServiceThrewGrammarIsNotLLOneGrammarException_ForwardsException()
		throws Exception
	{
		// Arrange - create Grammar
		Node nodeS = new Node(NodeKind.Nonterminal, "S");

		// S = .
		Rule ruleOne = new Rule(nodeS);

		Grammar grammar = new Grammar();

		grammar.addRule(ruleOne);
		grammar.setHeadRule(ruleOne);

		// Arrange - mock controlTableBuildingService
		Mockito.when(
			this._controlTableBuildingServiceMock.buildControlTable(grammar))
				.thenThrow(new GrammarIsNotLLOneGrammarException());

		// Arrange - create target
		ISyntaxAnalyzer target =
			new SyntaxAnalyzer(this._controlTableBuildingServiceMock);

		target.setGrammar(grammar);

		// Act & Assert
		target.run();
	}

	@Test
	public void run_GrammarIsNotSet_ThrowsGrammarIsNotSetException()
		throws Exception
	{
		// Arrange - create target
		ISyntaxAnalyzer target =
			new SyntaxAnalyzer(this._controlTableBuildingServiceMock);

		boolean exceptionIsThrown = false;

		// Act
		try
		{
			target.run();
		}
		catch (GrammarIsNotSetException e)
		{
			exceptionIsThrown = true;
		}

		// Assert
		Assert.assertEquals(true, exceptionIsThrown);
	}

	@Test
	public void run_SyntaxIsInvalid_ReturnsResultIsInvalid() throws Exception
	{
		// Arrange - create Grammar
		Node nodeS = new Node(NodeKind.Nonterminal, "S");
		Node nodeA = new Node(NodeKind.Terminal, "a");
		Node nodeB = new Node(NodeKind.Terminal, "b");

		Rule ruleOne = new Rule(nodeS);

		// S = "a", "b" .
		ruleOne.addNode(nodeA);
		ruleOne.addNode(nodeB);

		Grammar grammar = new Grammar();

		grammar.addRule(ruleOne);
		grammar.setHeadRule(ruleOne);

		// Arrange - mock controlTableBuildingService
		Map<ControlTableItem, Rule> controlTable =
			new HashMap<ControlTableItem, Rule>();

		controlTable.put(new ControlTableItem(nodeS, new Word(nodeA)), ruleOne);

		Mockito.when(
			this._controlTableBuildingServiceMock.buildControlTable(grammar))
				.thenReturn(controlTable);

		// Arrange - create tokens
		List<Token> tokens =
			Arrays.asList(new Token[] {
				new Token(TokenKind.Identifier, nodeA.getText(), new Location(
					0, 1))
			});

		// Arrange - create target
		ISyntaxAnalyzer target =
			new SyntaxAnalyzer(this._controlTableBuildingServiceMock);

		target.setGrammar(grammar);
		target.setTokens(tokens);

		// Act
		SyntaxAnalyzerResult result = target.run();

		// Assert
		Assert.assertEquals(false, result.isSyntaxValid());
		Assert.assertEquals(null, result.getWrongToken());

		Mockito.verify(this._controlTableBuildingServiceMock)
				.buildControlTable(grammar);
	}

	@Test
	public void run_SyntaxIsSimple_ReturnsResultIsValid() throws Exception
	{
		// Arrange - create Grammar
		Node nodeS = new Node(NodeKind.Nonterminal, "S");
		Node nodeA = new Node(NodeKind.Nonterminal, "A");
		Node nodeB = new Node(NodeKind.Terminal, "b");
		Node nodeC = new Node(NodeKind.Terminal, "c");

		Rule ruleOne = new Rule(nodeS);

		// S = A, "b" .
		ruleOne.addNode(nodeA);
		ruleOne.addNode(nodeB);

		Rule ruleTwo = new Rule(nodeA);

		// A = "c" .
		ruleTwo.addNode(nodeC);

		Grammar grammar = new Grammar();

		grammar.addRule(ruleOne);
		grammar.addRule(ruleTwo);

		grammar.setHeadRule(ruleOne);

		// Arrange - mock controlTableBuildingService
		Map<ControlTableItem, Rule> controlTable =
			new HashMap<ControlTableItem, Rule>();

		controlTable.put(new ControlTableItem(nodeS, new Word(nodeC)), ruleOne);
		controlTable.put(new ControlTableItem(nodeA, new Word(nodeC)), ruleTwo);

		Mockito.when(
			this._controlTableBuildingServiceMock.buildControlTable(grammar))
				.thenReturn(controlTable);

		// Arrange - create tokens
		List<Token> tokens =
			Arrays.asList(new Token[] {
				new Token(TokenKind.Identifier, nodeC.getText(), new Location(
					0, 1)),
				new Token(TokenKind.Identifier, nodeB.getText(), new Location(
					1, 1))
			});

		// Arrange - create target
		ISyntaxAnalyzer target =
			new SyntaxAnalyzer(this._controlTableBuildingServiceMock);

		target.setGrammar(grammar);
		target.setTokens(tokens);

		// Act
		SyntaxAnalyzerResult result = target.run();

		// Assert
		Assert.assertEquals(true, result.isSyntaxValid());
		Assert.assertEquals(null, result.getWrongToken());

		Mockito.verify(this._controlTableBuildingServiceMock)
				.buildControlTable(grammar);
	}

	@Test
	public void run_SyntaxWithCycle_ReturnsResultIsValid() throws Exception
	{
		// Arrange - create Grammar
		Node nodeS = new Node(NodeKind.Nonterminal, "S");
		Node nodeA = new Node(NodeKind.Nonterminal, "A");
		Node nodeB = new Node(NodeKind.Terminal, "b");
		Node nodeC = new Node(NodeKind.Terminal, "c");

		Rule ruleOne = new Rule(nodeS);

		// S = A .
		ruleOne.addNode(nodeA);

		Rule ruleTwo = new Rule(nodeA);

		// A = "b", A .
		ruleTwo.addNode(nodeB);
		ruleTwo.addNode(nodeA);

		Rule ruleThree = new Rule(nodeA);

		// A = "c" .
		ruleThree.addNode(nodeC);

		Grammar grammar = new Grammar();

		grammar.addRule(ruleOne);
		grammar.addRule(ruleTwo);
		grammar.addRule(ruleThree);

		grammar.setHeadRule(ruleOne);

		// Arrange - mock controlTableBuildingService
		Map<ControlTableItem, Rule> controlTable =
			new HashMap<ControlTableItem, Rule>();

		controlTable.put(new ControlTableItem(nodeS, new Word(nodeB)), ruleOne);
		controlTable.put(new ControlTableItem(nodeS, new Word(nodeC)), ruleOne);
		controlTable.put(new ControlTableItem(nodeA, new Word(nodeB)), ruleTwo);
		controlTable.put(new ControlTableItem(nodeA, new Word(nodeC)),
			ruleThree);

		Mockito.when(
			this._controlTableBuildingServiceMock.buildControlTable(grammar))
				.thenReturn(controlTable);

		// Arrange - create target
		ISyntaxAnalyzer target =
			new SyntaxAnalyzer(this._controlTableBuildingServiceMock);

		target.setGrammar(grammar);

		// Arrange - create tokens
		List<List<Token>> tokensList = new ArrayList<List<Token>>();

		// Tokens: c
		tokensList
				.add(Arrays.asList(new Token[] {
					new Token(TokenKind.Identifier, nodeC.getText(),
						new Location(0, 1))
				}));

		// Tokens: b, c
		tokensList
				.add(Arrays.asList(new Token[] {
					new Token(TokenKind.Identifier, nodeB.getText(),
						new Location(0, 1)),
					new Token(TokenKind.Identifier, nodeC.getText(),
						new Location(1, 1))
				}));

		// Tokens: b, b, c
		tokensList
				.add(Arrays.asList(new Token[] {
					new Token(TokenKind.Identifier, nodeB.getText(),
						new Location(0, 1)),
					new Token(TokenKind.Identifier, nodeB.getText(),
						new Location(1, 1)),
					new Token(TokenKind.Identifier, nodeC.getText(),
						new Location(2, 1))
				}));

		int numberOfInvocations = 0;

		for (List<Token> tokens : tokensList)
		{
			numberOfInvocations++;

			target.setTokens(tokens);

			// Act
			SyntaxAnalyzerResult result = target.run();

			// Assert
			Assert.assertEquals(true, result.isSyntaxValid());
			Assert.assertEquals(null, result.getWrongToken());

			Mockito.verify(this._controlTableBuildingServiceMock,
				Mockito.times(numberOfInvocations)).buildControlTable(grammar);
		}
	}

	@Test
	public void run_SyntaxWithOptionalPart_ReturnsResultIsValid()
		throws Exception
	{
		// Arrange - create Grammar
		Node nodeS = new Node(NodeKind.Nonterminal, "S");
		Node nodeA = new Node(NodeKind.Nonterminal, "A");
		Node nodeB = new Node(NodeKind.Terminal, "b");
		Node nodeC = new Node(NodeKind.Terminal, "c");

		Rule ruleOne = new Rule(nodeS);

		// S = A, "b" .
		ruleOne.addNode(nodeA);
		ruleOne.addNode(nodeB);

		Rule ruleTwo = new Rule(nodeA);

		// A = "c" .
		ruleTwo.addNode(nodeC);

		// A = .
		Rule ruleThree = new Rule(nodeA);

		Grammar grammar = new Grammar();

		grammar.addRule(ruleOne);
		grammar.addRule(ruleTwo);
		grammar.addRule(ruleThree);

		grammar.setHeadRule(ruleOne);

		// Arrange - mock controlTableBuildingService
		Map<ControlTableItem, Rule> controlTable =
			new HashMap<ControlTableItem, Rule>();

		controlTable.put(new ControlTableItem(nodeS, new Word(nodeC)), ruleOne);
		controlTable.put(new ControlTableItem(nodeS, Word.getEmptyWord()),
			ruleOne);
		controlTable.put(new ControlTableItem(nodeA, new Word(nodeC)), ruleTwo);
		controlTable.put(new ControlTableItem(nodeA, Word.getEmptyWord()),
			ruleThree);

		Mockito.when(
			this._controlTableBuildingServiceMock.buildControlTable(grammar))
				.thenReturn(controlTable);

		// Arrange - create target
		ISyntaxAnalyzer target =
			new SyntaxAnalyzer(this._controlTableBuildingServiceMock);

		target.setGrammar(grammar);

		// Arrange - create tokens
		List<List<Token>> tokensList = new ArrayList<List<Token>>();

		// Tokens: b
		tokensList
				.add(Arrays.asList(new Token[] {
					new Token(TokenKind.Identifier, nodeB.getText(),
						new Location(0, 1)),
				}));

		// Tokens: c, b
		tokensList
				.add(Arrays.asList(new Token[] {
					new Token(TokenKind.Identifier, nodeC.getText(),
						new Location(0, 1)),
					new Token(TokenKind.Identifier, nodeB.getText(),
						new Location(1, 1))
				}));

		int numberOfInvocations = 0;

		for (List<Token> tokens : tokensList)
		{
			numberOfInvocations++;

			target.setTokens(tokens);

			// Act
			SyntaxAnalyzerResult result = target.run();

			// Assert
			Assert.assertEquals(true, result.isSyntaxValid());
			Assert.assertEquals(null, result.getWrongToken());

			Mockito.verify(this._controlTableBuildingServiceMock,
				Mockito.times(numberOfInvocations)).buildControlTable(grammar);
		}
	}

	@Test
	public void run_TokensListIsEmptyAndGrammarAllowsEmptySource_ReturnsResultIsValid()
		throws Exception
	{
		// Arrange - create Grammar
		Node nodeS = new Node(NodeKind.Nonterminal, "S");
		Node nodeA = new Node(NodeKind.Nonterminal, "A");

		Rule ruleOne = new Rule(nodeS);

		// S = "A" .
		ruleOne.addNode(nodeA);

		// A = .
		Rule ruleTwo = new Rule(nodeA);

		Grammar grammar = new Grammar();

		grammar.addRule(ruleOne);
		grammar.addRule(ruleTwo);

		grammar.setHeadRule(ruleOne);

		// Arrange - mock controlTableBuildingService
		Map<ControlTableItem, Rule> controlTable =
			new HashMap<ControlTableItem, Rule>();

		controlTable.put(new ControlTableItem(nodeS, Word.getEmptyWord()),
			ruleOne);
		controlTable.put(new ControlTableItem(nodeA, Word.getEmptyWord()),
			ruleTwo);

		Mockito.when(
			this._controlTableBuildingServiceMock.buildControlTable(grammar))
				.thenReturn(controlTable);

		// Arrange - create target
		ISyntaxAnalyzer target =
			new SyntaxAnalyzer(this._controlTableBuildingServiceMock);

		target.setGrammar(grammar);

		// Act
		SyntaxAnalyzerResult result = target.run();

		// Assert
		Assert.assertEquals(true, result.isSyntaxValid());
		Assert.assertEquals(null, result.getWrongToken());

		Mockito.verify(this._controlTableBuildingServiceMock)
				.buildControlTable(grammar);
	}

	@Test
	public void run_TokensListIsEmptyAndGrammarDoesNotAllowEmptySource_ReturnsResultIsInvalid()
		throws Exception
	{
		// Arrange - create Grammar
		Node nodeS = new Node(NodeKind.Nonterminal, "S");
		Node nodeA = new Node(NodeKind.Nonterminal, "A");
		Node nodeB = new Node(NodeKind.Terminal, "b");

		Rule ruleOne = new Rule(nodeS);

		// S = "A" .
		ruleOne.addNode(nodeA);

		Rule ruleTwo = new Rule(nodeA);

		// A = "b" .
		ruleTwo.addNode(nodeB);

		Grammar grammar = new Grammar();

		grammar.addRule(ruleOne);
		grammar.addRule(ruleTwo);

		grammar.setHeadRule(ruleOne);

		// Arrange - mock controlTableBuildingService
		Map<ControlTableItem, Rule> controlTable =
			new HashMap<ControlTableItem, Rule>();

		controlTable.put(new ControlTableItem(nodeS, new Word(nodeB)), ruleOne);
		controlTable.put(new ControlTableItem(nodeA, new Word(nodeB)), ruleTwo);

		Mockito.when(
			this._controlTableBuildingServiceMock.buildControlTable(grammar))
				.thenReturn(controlTable);

		// Arrange - create target
		ISyntaxAnalyzer target =
			new SyntaxAnalyzer(this._controlTableBuildingServiceMock);

		target.setGrammar(grammar);

		// Act
		SyntaxAnalyzerResult result = target.run();

		// Assert
		Assert.assertEquals(false, result.isSyntaxValid());
		Assert.assertEquals(null, result.getWrongToken());

		Mockito.verify(this._controlTableBuildingServiceMock)
				.buildControlTable(grammar);
	}

	@Test(expected = GrammarIsInvalidException.class)
	public void setGrammar_GrammarIsEmpty_ThrowsGrammarIsInvalidException()
		throws Exception
	{
		// Arrange
		Grammar grammar = new Grammar();

		// Arrange - create target
		ISyntaxAnalyzer target =
			new SyntaxAnalyzer(this._controlTableBuildingServiceMock);

		// Act & Assert
		target.setGrammar(grammar);
	}

	@Test(expected = IllegalArgumentException.class)
	public void setGrammar_GrammarIsNull_ThrowsIllegalArgumentException()
		throws Exception
	{
		// Arrange - create target
		ISyntaxAnalyzer target =
			new SyntaxAnalyzer(this._controlTableBuildingServiceMock);

		// Act & Assert
		target.setGrammar(null);
	}

	@Test(expected = GrammarIsInvalidException.class)
	public void setGrammar_GrammarRuleHeadIsNull_ThrowsGrammarIsInvalidException()
		throws Exception
	{
		// Arrange
		Rule rule = new Rule(new Node(NodeKind.Nonterminal, "S"));

		Grammar grammar = new Grammar();

		grammar.addRule(rule);

		// Arrange - create target
		ISyntaxAnalyzer target =
			new SyntaxAnalyzer(this._controlTableBuildingServiceMock);

		// Act & Assert
		target.setGrammar(grammar);
	}

	@Before
	public void setUp() throws Exception
	{
		this._controlTableBuildingServiceMock =
			Mockito.mock(IControlTableBuildingService.class);
	}
}
