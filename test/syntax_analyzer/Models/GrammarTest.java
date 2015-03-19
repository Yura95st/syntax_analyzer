package syntax_analyzer.Models;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import syntax_analyzer.Enums.NodeKind;
import syntax_analyzer.Exceptions.NonexistentRuleException;
import syntax_analyzer.Exceptions.RuleAlreadyExistsException;

public class GrammarTest
{
	Grammar _grammar;
	
	@Test(expected = RuleAlreadyExistsException.class)
	public void addRule_RuleAlreadyExists_ThrowsRuleAlreadyExistsException()
			throws Exception
	{
		// Arrange
		Rule testRule = new Rule(new Node(NodeKind.Nonterminal, "node"));

		this._grammar.addRule(testRule);

		// Act & Assert
		this._grammar.addRule(testRule);
	}

	@Test(expected = IllegalArgumentException.class)
	public void addRule_RuleIsNull_ThrowsIllegalArgumentException()
			throws Exception
	{
		this._grammar.addRule(null);
	}
	
	@Test
	public void addRule_RuleIsValid_AddsRuleToTheRulesDictionary()
			throws Exception
	{
		// Arrange
		Rule testRule = new Rule(new Node(NodeKind.Nonterminal, "node"));

		// Act
		this._grammar.addRule(testRule);

		// Assert
		Rule rule = this._grammar.getRule(testRule.getHeadNode());

		Assert.assertEquals(testRule, rule);
	}

	@Test(expected = NonexistentRuleException.class)
	public void deleteRule_NonexistentRule_ThrowsNonexistentRuleException()
			throws Exception
	{
		// Arrange
		Rule testRule = new Rule(new Node(NodeKind.Nonterminal, "node"));
		
		// Act & Assert
		this._grammar.deleteRule(testRule);
	}

	@Test
	public void deleteRule_RuleIsTheHeadRule_DeletesRuleAndSetsHeadRuleToNull()
			throws Exception
	{
		// Arrange
		Rule testHeadRule = new Rule(new Node(NodeKind.Nonterminal, "node"));
		
		this._grammar.addRule(testHeadRule);
		this._grammar.setHeadRule(testHeadRule);
		
		// Act
		this._grammar.deleteRule(testHeadRule);
		
		// Assert
		Rule rule = this._grammar.getRule(testHeadRule.getHeadNode());
		Rule headRule = this._grammar.getHeadRule();
		
		Assert.assertEquals(null, rule);
		Assert.assertEquals(null, headRule);
	}

	@Test
	public void deleteRule_RuleIsValid_DeletesRule() throws Exception
	{
		// Arrange
		Rule testRule = new Rule(new Node(NodeKind.Nonterminal, "node"));
		
		this._grammar.addRule(testRule);
		
		// Act
		this._grammar.deleteRule(testRule);
		
		// Assert
		Rule rule = this._grammar.getRule(testRule.getHeadNode());
		
		Assert.assertEquals(null, rule);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void getRule_HeadNodeIsNull_ThrowsIllegalArgumentException()
	{
		this._grammar.getRule(null);
	}
	
	@Test
	public void getRule_HeadNodeIsValid_ReturnsRule() throws Exception
	{
		// Arrange
		Rule testRule = new Rule(new Node(NodeKind.Nonterminal, "node"));

		this._grammar.addRule(testRule);

		// Act
		Rule rule = this._grammar.getRule(testRule.getHeadNode());

		// Assert
		Assert.assertEquals(testRule, rule);
	}
	
	@Test
	public void getRule_NonexistentHeadNode_ReturnsNull()
	{
		// Arrange
		Node headNode = new Node(NodeKind.Nonterminal, "node");

		// Act
		Rule rule = this._grammar.getRule(headNode);

		// Assert
		Assert.assertEquals(null, rule);
	}

	@Test
	public void setHeadRule_HeadRuleIsNull_SetsHeadRuleAsNull()
		throws Exception
	{
		// Arrange
		Rule testHeadRule = new Rule(new Node(NodeKind.Nonterminal, "node"));

		this._grammar.addRule(testHeadRule);

		this._grammar.setHeadRule(testHeadRule);

		// Act
		this._grammar.setHeadRule(null);

		// Assert
		Rule headRule = this._grammar.getHeadRule();

		Assert.assertEquals(null, headRule);
	}
	
	@Test
	public void setHeadRule_HeadRuleIsValid_SetsNewHeadRule() throws Exception
	{
		// Arrange
		Rule testHeadRule = new Rule(new Node(NodeKind.Nonterminal, "node"));

		this._grammar.addRule(testHeadRule);

		// Act
		this._grammar.setHeadRule(testHeadRule);

		// Assert
		Rule headRule = this._grammar.getHeadRule();

		Assert.assertEquals(testHeadRule, headRule);
	}

	@Test(expected = NonexistentRuleException.class)
	public void setHeadRule_NonexistentHeadRule_ThrowsNonexistentRuleException()
			throws Exception
	{
		// Arrange
		Rule testRule = new Rule(new Node(NodeKind.Nonterminal, "node"));
		
		// Act & Assert
		this._grammar.setHeadRule(testRule);
	}
	
	@Before
	public void setUp() throws Exception
	{
		this._grammar = new Grammar();
	}
	
}
