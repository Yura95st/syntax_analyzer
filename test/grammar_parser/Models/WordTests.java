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
		List<Node> testNodes = Arrays.asList(new Node[] {
			new Node(NodeKind.Terminal, "terminalOne"),
			new Node(NodeKind.Terminal, "terminalTwo"),
			new Node(NodeKind.Terminal, "terminalThree")
		});

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
		List<Node> testNodes = Arrays.asList(new Node[] {
			new Node(NodeKind.Terminal, "terminal"),
			new Node(NodeKind.Nonterminal, "nonterminal")
		});

		// Act & Assert
		this._word.setNodes(testNodes);
	}
	
	@Before
	public void setUp() throws Exception
	{
		this._word = new Word();
	}

}
