package grammar_parser.Models;

import grammar_parser.Enums.NodeKind;

import org.junit.Assert;
import org.junit.Test;

public class NodeTests
{
	@Test
	public void equals_TwoNodesHaveDifferentKindAndTextFields_ReturnsFalse()
		throws Exception
	{
		// Arrange
		Node nodeOne = new Node(NodeKind.Nonterminal, "A");
		Node nodeTwo = new Node(NodeKind.Terminal, "A");
		Node nodeThree = new Node(NodeKind.Nonterminal, "B");

		// Act
		boolean resultOne = nodeOne.equals(nodeTwo);
		boolean resultTwo = nodeOne.equals(nodeThree);
		boolean resultThree = nodeTwo.equals(nodeThree);

		// Assert
		Assert.assertEquals(false, resultOne);
		Assert.assertEquals(false, resultTwo);
		Assert.assertEquals(false, resultThree);
	}

	@Test
	public void equals_TwoNodesHaveEqualKindAndTextFields_ReturnsTrue()
		throws Exception
	{
		// Arrange
		Node nodeOne = new Node(NodeKind.Terminal, "A");
		Node nodeTwo = new Node(NodeKind.Terminal, "A");

		// Act
		boolean result = nodeOne.equals(nodeTwo);

		// Assert
		Assert.assertEquals(true, result);
	}

	@Test
	public void hashCode_TwoNodesHaveDifferentKindAndTextFields_ReturnsDifferentHashCodes()
		throws Exception
	{
		// Arrange - create nodes
		Node nodeOne = new Node(NodeKind.Nonterminal, "A");
		Node nodeTwo = new Node(NodeKind.Terminal, "A");
		Node nodeThree = new Node(NodeKind.Nonterminal, "B");

		// Act
		int hashCodeOne = nodeOne.hashCode();
		int hashCodeTwo = nodeTwo.hashCode();
		int hashCodeThree = nodeThree.hashCode();

		// Assert
		Assert.assertNotEquals(hashCodeOne, hashCodeTwo);
		Assert.assertNotEquals(hashCodeOne, hashCodeThree);
		Assert.assertNotEquals(hashCodeTwo, hashCodeThree);
	}

	@Test
	public void hashCode_TwoNodesHaveEqualKindAndTextFields_ReturnsEqualHashCodes()
		throws Exception
	{
		// Arrange
		Node nodeOne = new Node(NodeKind.Terminal, "A");
		Node nodeTwo = new Node(NodeKind.Terminal, "A");

		// Act
		int hashCodeOne = nodeOne.hashCode();
		int hashCodeTwo = nodeTwo.hashCode();

		// Assert
		Assert.assertEquals(hashCodeOne, hashCodeTwo);
	}
}
