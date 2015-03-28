package grammar_parser.Models;

import grammar_parser.Enums.NodeKind;
import grammar_parser.Exceptions.NodeIsNotNonterminalException;

import org.junit.Assert;
import org.junit.Test;

public class ControlTableItemTests
{
	@Test(expected = NodeIsNotNonterminalException.class)
	public void constructor_NodeIsNotNonterminal_ThrowsNodeIsNotNonterminalException()
		throws Exception
	{
		// Arrange
		Node node = new Node(NodeKind.Terminal, "a");

		// Act & Assert
		new ControlTableItem(node, Word.getEmptyWord());
	}

	@Test
	public void equals_TwoItemsHaveDifferentNodeAndWordFields_ReturnsFalse()
		throws Exception
	{
		// Arrange - create nodes
		Node nodeOne = new Node(NodeKind.Nonterminal, "A");
		Node nodeTwo = new Node(NodeKind.Nonterminal, "B");

		// Arrange - create words
		Word wordOne = Word.getEmptyWord();
		Word wordTwo = new Word(new Node(NodeKind.Terminal, "a"));

		// Arrange - create controlTableItems
		ControlTableItem controlTableItemOne =
			new ControlTableItem(nodeOne, wordOne);
		ControlTableItem controlTableItemTwo =
			new ControlTableItem(nodeOne, wordTwo);
		ControlTableItem controlTableItemThree =
			new ControlTableItem(nodeTwo, wordOne);

		// Act
		boolean resultOne = controlTableItemOne.equals(controlTableItemTwo);
		boolean resultTwo = controlTableItemOne.equals(controlTableItemThree);
		boolean resultThree = controlTableItemTwo.equals(controlTableItemThree);

		// Assert
		Assert.assertEquals(false, resultOne);
		Assert.assertEquals(false, resultTwo);
		Assert.assertEquals(false, resultThree);
	}

	@Test
	public void equals_TwoItemsHaveEqualNodeAndWordFields_ReturnsTrue()
		throws Exception
	{
		// Arrange
		Node node = new Node(NodeKind.Nonterminal, "A");
		Word word = Word.getEmptyWord();

		ControlTableItem controlTableItemOne = new ControlTableItem(node, word);
		ControlTableItem controlTableItemTwo = new ControlTableItem(node, word);

		// Act
		boolean result = controlTableItemOne.equals(controlTableItemTwo);

		// Assert
		Assert.assertEquals(true, result);
	}

	@Test
	public void hashCode_TwoItemsHaveDifferentNodeAndWordFields_ReturnsDifferentHashCodes()
		throws Exception
	{
		// Arrange - create nodes
		Node nodeOne = new Node(NodeKind.Nonterminal, "A");
		Node nodeTwo = new Node(NodeKind.Nonterminal, "B");

		// Arrange - create words
		Word wordOne = Word.getEmptyWord();
		Word wordTwo = new Word(new Node(NodeKind.Terminal, "a"));

		// Arrange - create controlTableItems
		ControlTableItem controlTableItemOne =
			new ControlTableItem(nodeOne, wordOne);
		ControlTableItem controlTableItemTwo =
			new ControlTableItem(nodeOne, wordTwo);
		ControlTableItem controlTableItemThree =
			new ControlTableItem(nodeTwo, wordOne);

		// Act
		int hashCodeOne = controlTableItemOne.hashCode();
		int hashCodeTwo = controlTableItemTwo.hashCode();
		int hashCodeThree = controlTableItemThree.hashCode();

		// Assert
		Assert.assertNotEquals(hashCodeOne, hashCodeTwo);
		Assert.assertNotEquals(hashCodeOne, hashCodeThree);
		Assert.assertNotEquals(hashCodeTwo, hashCodeThree);
	}

	@Test
	public void hashCode_TwoItemsHaveEqualNodeAndWordFields_ReturnsEqualHashCodes()
		throws Exception
	{
		// Arrange
		Node node = new Node(NodeKind.Nonterminal, "A");
		Word word = Word.getEmptyWord();

		ControlTableItem controlTableItemOne = new ControlTableItem(node, word);
		ControlTableItem controlTableItemTwo = new ControlTableItem(node, word);

		// Act
		int hashCodeOne = controlTableItemOne.hashCode();
		int hashCodeTwo = controlTableItemTwo.hashCode();

		// Assert
		Assert.assertEquals(hashCodeOne, hashCodeTwo);
	}
}
