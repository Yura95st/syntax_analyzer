package grammar_parser.Models;

import grammar_parser.Enums.NodeKind;

import org.junit.Assert;
import org.junit.Test;

public class RuleTests
{
	@Test
	public void equals_TwoRulesHaveDifferentHeadNodeAndNodesFields_ReturnsFalse()
		throws Exception
	{
		// Arrange
		Node nodeOne = new Node(NodeKind.Nonterminal, "A");
		Node nodeTwo = new Node(NodeKind.Terminal, "b");

		Rule ruleOne = new Rule(nodeOne);

		Rule ruleTwo = new Rule(nodeOne);

		ruleTwo.addNode(nodeTwo);

		Rule ruleThree = new Rule(nodeTwo);

		// Act
		boolean resultOne = ruleOne.equals(ruleTwo);
		boolean resultTwo = ruleOne.equals(ruleThree);
		boolean resultThree = ruleTwo.equals(ruleThree);

		// Assert
		Assert.assertEquals(false, resultOne);
		Assert.assertEquals(false, resultTwo);
		Assert.assertEquals(false, resultThree);
	}

	@Test
	public void equals_TwoRulesHaveEqualHeadNodeAndNodesFields_ReturnsTrue()
		throws Exception
	{
		// Arrange
		Node nodeOne = new Node(NodeKind.Nonterminal, "A");
		Node nodeTwo = new Node(NodeKind.Terminal, "b");

		Rule ruleOne = new Rule(nodeOne);

		ruleOne.addNode(nodeTwo);

		Rule ruleTwo = new Rule(nodeOne);

		ruleTwo.addNode(nodeTwo);

		// Act
		boolean result = ruleOne.equals(ruleTwo);

		// Assert
		Assert.assertEquals(true, result);
	}

	@Test
	public void hashCode_TwoRulesHaveDifferentHeadNodeAndNodesFields_ReturnsDifferentHashCodes()
		throws Exception
	{
		// Arrange
		Node nodeOne = new Node(NodeKind.Nonterminal, "A");
		Node nodeTwo = new Node(NodeKind.Terminal, "b");

		Rule ruleOne = new Rule(nodeOne);

		Rule ruleTwo = new Rule(nodeOne);

		ruleTwo.addNode(nodeTwo);

		Rule ruleThree = new Rule(nodeTwo);

		// Act
		int hashCodeOne = ruleOne.hashCode();
		int hashCodeTwo = ruleTwo.hashCode();
		int hashCodeThree = ruleThree.hashCode();

		// Assert
		Assert.assertNotEquals(hashCodeOne, hashCodeTwo);
		Assert.assertNotEquals(hashCodeOne, hashCodeThree);
		Assert.assertNotEquals(hashCodeTwo, hashCodeThree);
	}

	@Test
	public void hashCode_TwoRulesHaveEqualHeadNodeAndNodesFields_ReturnsEqualHashCodes()
		throws Exception
	{
		// Arrange
		Node nodeOne = new Node(NodeKind.Nonterminal, "A");
		Node nodeTwo = new Node(NodeKind.Terminal, "b");

		Rule ruleOne = new Rule(nodeOne);

		ruleOne.addNode(nodeTwo);

		Rule ruleTwo = new Rule(nodeOne);

		ruleTwo.addNode(nodeTwo);

		// Act
		int hashCodeOne = ruleOne.hashCode();
		int hashCodeTwo = ruleTwo.hashCode();

		// Assert
		Assert.assertEquals(hashCodeOne, hashCodeTwo);
	}
}
