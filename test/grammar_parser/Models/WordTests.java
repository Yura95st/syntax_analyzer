package grammar_parser.Models;

import grammar_parser.Enums.NodeKind;
import grammar_parser.Exceptions.NodeIsNotTerminalException;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class WordTests
{
	private Word _word;

	@Test
	public void equals_TwoWordsHaveDifferentNodes_ReturnsFalse()
		throws Exception
	{
		// Arrange
		Word wordOne = Word.getEmptyWord();
		Word wordTwo = new Word(new Node(NodeKind.Terminal, "a"));

		// Act
		boolean result = wordOne.equals(wordTwo);

		// Assert
		Assert.assertEquals(false, result);
	}

	@Test
	public void equals_TwoWordsHaveEqualNodes_ReturnsTrue() throws Exception
	{
		// Arrange
		Node nodeOne = new Node(NodeKind.Terminal, "a");
		Node nodeTwo = new Node(NodeKind.Terminal, "b");

		Word wordOne = new Word(nodeOne, nodeTwo);
		Word wordTwo = new Word(nodeOne, nodeTwo);

		// Act
		boolean result = wordOne.equals(wordTwo);

		// Assert
		Assert.assertEquals(true, result);
	}

	@Test
	public void getEmptyWord_EachTimeReturnsTheSameWord()
	{
		// Act
		Word emptyWordOne = Word.getEmptyWord();
		Word emptyWordTwo = Word.getEmptyWord();

		// Assert
		Assert.assertSame(emptyWordOne, emptyWordTwo);
	}

	@Test
	public void getEmptyWord_ReturnsEmptyWord()
	{
		// Arrange
		Word testEmptyWord = new Word();

		// Act
		Word emptyWord = Word.getEmptyWord();

		// Assert
		Assert.assertEquals(testEmptyWord, emptyWord);
	}

	@Test
	public void hashCode_TwoWordsHaveDifferentNodes_ReturnsDifferentHashCodes()
		throws Exception
	{
		// Arrange
		Word wordOne = Word.getEmptyWord();
		Word wordTwo = new Word(new Node(NodeKind.Terminal, "a"));

		// Act
		int hashCodeOne = wordOne.hashCode();
		int hashCodeTwo = wordTwo.hashCode();

		// Assert
		Assert.assertNotEquals(hashCodeOne, hashCodeTwo);
	}

	@Test
	public void hashCode_TwoWordsHaveEqualNodes_ReturnsEqualHashCodes()
		throws Exception
	{
		// Arrange
		Node nodeOne = new Node(NodeKind.Terminal, "a");
		Node nodeTwo = new Node(NodeKind.Terminal, "b");

		Word wordOne = new Word(nodeOne, nodeTwo);
		Word wordTwo = new Word(nodeOne, nodeTwo);

		// Act
		int hashCodeOne = wordOne.hashCode();
		int hashCodeTwo = wordTwo.hashCode();

		// Assert
		Assert.assertEquals(hashCodeOne, hashCodeTwo);
	}

	@Test(expected = IllegalArgumentException.class)
	public void setNodes_NodesListIsNull_ThrowsIllegalArgumentException()
		throws Exception
	{
		// Act & Assert
		this._word.setNodes(null);
	}

	@Test
	public void setNodes_NodesListIsValid_SetsNewNodes() throws Exception
	{
		// Arrange
		List<Node> testNodes =
			Arrays.asList(new Node(NodeKind.Terminal, "terminalOne"), new Node(
				NodeKind.Terminal, "terminalTwo"), new Node(NodeKind.Terminal,
				"terminalThree"));

		// Act
		this._word.setNodes(testNodes);

		// Assert

		List<Node> nodes = this._word.getNodes();

		Assert.assertEquals(testNodes, nodes);
	}

	@Test(expected = NodeIsNotTerminalException.class)
	public void setNodes_OneOfTheNodesIsNotTerminal_ThrowsNodeIsNotTerminalException()
		throws Exception
	{
		// Arrange
		List<Node> testNodes =
			Arrays.asList(new Node(NodeKind.Terminal, "terminal"), new Node(
				NodeKind.Nonterminal, "nonterminal"));

		// Act & Assert
		this._word.setNodes(testNodes);
	}

	@Before
	public void setUp() throws Exception
	{
		this._word = new Word();
	}

	@Test(expected = IllegalArgumentException.class)
	public void wordConstructor_NodesArrayIsNull_ThrowsIllegalArgumentException()
		throws Exception
	{
		// Act & Assert
		this._word = new Word((Node[]) null);
	}

	@Test
	public void wordConstructor_NodesArrayIsValid_SetsNewNodes()
		throws Exception
	{
		// Arrange
		Node[] nodes =
			{
				new Node(NodeKind.Terminal, "terminalOne"),
				new Node(NodeKind.Terminal, "terminalTwo"),
				new Node(NodeKind.Terminal, "terminalThree")
			};

		// Act
		this._word = new Word(nodes);

		// Assert
		List<Node> nodesList = this._word.getNodes();

		Assert.assertEquals(Arrays.asList(nodes), nodesList);
	}

	@Test(expected = NodeIsNotTerminalException.class)
	public void wordConstructor_OneOfTheNodesIsNotTerminal_ThrowsNodeIsNotTerminalException()
		throws Exception
	{
		// Arrange
		Node[] nodes =
			{
				new Node(NodeKind.Terminal, "terminal"),
				new Node(NodeKind.Nonterminal, "nonterminal")
			};

		// Act & Assert
		this._word = new Word(nodes);
	}
}
