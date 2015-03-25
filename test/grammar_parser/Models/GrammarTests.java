package grammar_parser.Models;

import grammar_parser.Enums.NodeKind;
import grammar_parser.Exceptions.NonexistentRuleException;
import grammar_parser.Exceptions.RuleAlreadyExistsException;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class GrammarTests
{
	private Grammar _grammar;

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
		List<Rule> rules = this._grammar.getRules(testRule.getHeadNode());

		Assert.assertEquals(1, rules.size());
		Assert.assertEquals(testRule, rules.get(0));
	}

	@Test
	public void addRule_RuleWithSameHeadNodeAlreadyExists_AppendsRuleToTheRulesDictionary()
		throws Exception
	{
		// Arrange
		Rule firstRule = new Rule(new Node(NodeKind.Nonterminal, "node"));

		firstRule.addNode(new Node(NodeKind.Nonterminal, "another-node"));

		this._grammar.addRule(firstRule);

		int size = this._grammar.getRules(firstRule.getHeadNode()).size();

		Rule testRule = new Rule(firstRule.getHeadNode());

		// Act
		this._grammar.addRule(testRule);

		// Assert
		List<Rule> rules = this._grammar.getRules(testRule.getHeadNode());

		Assert.assertEquals(size + 1, rules.size());
		Assert.assertTrue(rules.contains(testRule));
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
		List<Rule> rules = this._grammar.getRules(testHeadRule.getHeadNode());

		Rule headRule = this._grammar.getHeadRule();

		Assert.assertEquals(0, rules.size());
		Assert.assertEquals(null, headRule);
	}

	@Test
	public void deleteRule_RuleIsValid_DeletesRule() throws Exception
	{
		// Arrange
		Rule firstRule = new Rule(new Node(NodeKind.Nonterminal, "node"));

		firstRule.addNode(new Node(NodeKind.Nonterminal, "another-node"));

		Rule testRule = new Rule(firstRule.getHeadNode());

		this._grammar.addRule(firstRule);
		this._grammar.addRule(testRule);

		int size = this._grammar.getRules(firstRule.getHeadNode()).size();

		// Act
		this._grammar.deleteRule(testRule);

		// Assert
		List<Rule> rules = this._grammar.getRules(testRule.getHeadNode());

		Assert.assertEquals(size - 1, rules.size());
		Assert.assertFalse(rules.contains(testRule));
	}

	@Test
	public void equals_TwoGrammarsHaveDifferentHeadRuleAndRulesDictionaryFields_ReturnsFalse()
		throws Exception
	{
		// Arrange
		Rule ruleOne = new Rule(new Node(NodeKind.Nonterminal, "A"));
		Rule ruleTwo = new Rule(new Node(NodeKind.Nonterminal, "B"));

		Grammar grammarOne = new Grammar();

		grammarOne.addRule(ruleOne);
		grammarOne.addRule(ruleTwo);
		grammarOne.setHeadRule(ruleTwo);

		Grammar grammarTwo = new Grammar();

		grammarTwo.addRule(ruleOne);
		grammarTwo.addRule(ruleTwo);
		grammarTwo.setHeadRule(ruleOne);

		Grammar grammarThree = new Grammar();

		grammarThree.addRule(ruleTwo);
		grammarThree.setHeadRule(ruleTwo);

		// Act
		boolean resultOne = grammarOne.equals(grammarTwo);
		boolean resultTwo = grammarOne.equals(grammarThree);
		boolean resultThree = grammarTwo.equals(grammarThree);

		// Assert
		Assert.assertEquals(false, resultOne);
		Assert.assertEquals(false, resultTwo);
		Assert.assertEquals(false, resultThree);
	}

	@Test
	public void equals_TwoGrammarsHaveEqualHeadRuleAndRulesDictionaryFields_ReturnsTrue()
		throws Exception
	{
		// Arrange
		Rule rule = new Rule(new Node(NodeKind.Nonterminal, "A"));

		Grammar grammarOne = new Grammar();

		grammarOne.addRule(rule);
		grammarOne.setHeadRule(rule);

		Grammar grammarTwo = new Grammar();

		grammarTwo.addRule(rule);
		grammarTwo.setHeadRule(rule);

		// Act
		boolean result = grammarOne.equals(grammarTwo);

		// Assert
		Assert.assertEquals(true, result);
	}

	@Test(expected = IllegalArgumentException.class)
	public void getRules_HeadNodeIsNull_ThrowsIllegalArgumentException()
	{
		this._grammar.getRules(null);
	}

	@Test
	public void getRules_HeadNodeIsValid_ReturnsRules() throws Exception
	{
		// Arrange
		Rule testRule = new Rule(new Node(NodeKind.Nonterminal, "node"));

		this._grammar.addRule(testRule);

		// Act
		List<Rule> rules = this._grammar.getRules(testRule.getHeadNode());

		// Assert
		Assert.assertEquals(testRule, rules.get(0));
	}

	@Test
	public void getRules_NonexistentHeadNode_ReturnsEmptyList()
	{
		// Arrange
		Node headNode = new Node(NodeKind.Nonterminal, "node");

		// Act
		List<Rule> rules = this._grammar.getRules(headNode);

		// Assert
		Assert.assertEquals(0, rules.size());
	}

	@Test
	public void hashCode_TwoGrammarsHaveDifferentHeadRuleAndRulesDictionaryFields_ReturnsDifferentHashCodes()
		throws Exception
	{
		// Arrange
		Rule ruleOne = new Rule(new Node(NodeKind.Nonterminal, "A"));
		Rule ruleTwo = new Rule(new Node(NodeKind.Nonterminal, "B"));

		Grammar grammarOne = new Grammar();

		grammarOne.addRule(ruleOne);
		grammarOne.addRule(ruleTwo);
		grammarOne.setHeadRule(ruleTwo);

		Grammar grammarTwo = new Grammar();

		grammarTwo.addRule(ruleOne);
		grammarTwo.addRule(ruleTwo);
		grammarTwo.setHeadRule(ruleOne);

		Grammar grammarThree = new Grammar();

		grammarThree.addRule(ruleTwo);
		grammarThree.setHeadRule(ruleTwo);

		// Act
		int hashCodeOne = grammarOne.hashCode();
		int hashCodeTwo = grammarTwo.hashCode();
		int hashCodeThree = grammarThree.hashCode();

		// Assert
		Assert.assertNotEquals(hashCodeOne, hashCodeTwo);
		Assert.assertNotEquals(hashCodeOne, hashCodeThree);
		Assert.assertNotEquals(hashCodeTwo, hashCodeThree);
	}

	@Test
	public void hashCode_TwoGrammarsHaveEqualHeadRuleAndRulesDictionaryFields_ReturnsEqualHashCodes()
		throws Exception
	{
		// Arrange
		Rule rule = new Rule(new Node(NodeKind.Nonterminal, "A"));

		Grammar grammarOne = new Grammar();

		grammarOne.addRule(rule);
		grammarOne.setHeadRule(rule);

		Grammar grammarTwo = new Grammar();

		grammarTwo.addRule(rule);
		grammarTwo.setHeadRule(rule);

		// Act
		int hashCodeOne = grammarOne.hashCode();
		int hashCodeTwo = grammarTwo.hashCode();

		// Assert
		Assert.assertEquals(hashCodeOne, hashCodeTwo);
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
