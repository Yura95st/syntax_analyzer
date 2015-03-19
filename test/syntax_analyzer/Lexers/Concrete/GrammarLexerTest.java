package syntax_analyzer.Lexers.Concrete;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import syntax_analyzer.Enums.NodeKind;
import syntax_analyzer.Lexers.Abstract.IGrammarLexer;
import syntax_analyzer.Models.Node;

public class GrammarLexerTest
{
	IGrammarLexer _grammarLexer;
	
	@Test
	public void parse_ReturnsValidNodes()
	{
		// Arrange - create nodes
		Node[] testNodes = {
			new Node(NodeKind.Nonterminal, "A"),
			new Node(NodeKind.Delimiter, "="),
			new Node(NodeKind.Nonterminal, "B"),
			new Node(NodeKind.Terminal, "\"c\""),
			new Node(NodeKind.Delimiter, "|"),
			new Node(NodeKind.Terminal, "\"d\""),
			new Node(NodeKind.Delimiter, ".")
		};

		List<Character> spaceCharacters = this._grammarLexer
				.getSpaceCharacters();
		
		// Arrange - create source string
		StringBuilder source = new StringBuilder();
		
		for (int i = 0, count = testNodes.length; i < count; i++)
		{
			source.append(testNodes[i].getText());

			// Append all space characters
			for (int j = 0, countTwo = spaceCharacters.size(); j < countTwo; j++)
			{
				source.append(spaceCharacters.get(j));
			}
		}

		this._grammarLexer.setSource(source.toString());

		// Act
		List<Node> nodes = this._grammarLexer.parse();

		// Assert
		Assert.assertEquals(testNodes.length, nodes.size());

		Assert.assertEquals(Arrays.asList(testNodes), nodes);
	}
	
	@Test
	public void parse_SourceContainsOnlyDelimiters_ReturnsDelimiterNodes()
	{
		// Arrange
		String[] delimiters = new String[] {
			"=", ",", ".", "|", ";"
		};

		for (String delimiter : delimiters)
		{
			Node node = new Node(NodeKind.Delimiter, delimiter);

			this._grammarLexer.setSource(delimiter);

			// Act
			List<Node> nodes = this._grammarLexer.parse();

			// Assert
			Assert.assertEquals(1, nodes.size());

			Assert.assertEquals(node, nodes.get(0));
		}
	}

	@Test
	public void parse_SourceContainsOnlyNonterminals_ReturnsNonterminalNodes()
	{
		// Arrange
		String[] nonterminals = new String[] {
			"nonterminal", "nonterminal01234", "nonterminal-", "nonterminal_",
			"prefix-nonterminal", "nonterminal_suffix",
			"prefix-nonterminal-01234_suffix"
		};

		for (String nonterminal : nonterminals)
		{
			Node node = new Node(NodeKind.Nonterminal, nonterminal);

			this._grammarLexer.setSource(nonterminal);

			// Act
			List<Node> nodes = this._grammarLexer.parse();

			// Assert
			Assert.assertEquals(1, nodes.size());

			Assert.assertEquals(node, nodes.get(0));
		}
	}

	@Test
	public void parse_SourceContainsOnlySpaceCharacters_ReturnsEmptyNodesList()
	{
		// Arrange
		StringBuilder source = new StringBuilder();

		List<Character> spaceCharacters = this._grammarLexer
				.getSpaceCharacters();

		for (int i = 0, count = spaceCharacters.size(); i < count; i++)
		{
			source.append(spaceCharacters.get(i));
		}
		
		this._grammarLexer.setSource(source.toString());
		
		// Act
		List<Node> nodes = this._grammarLexer.parse();
		
		// Assert
		Assert.assertEquals(0, nodes.size());
	}
	
	@Test
	public void parse_SourceContainsOnlyTerminals_ReturnsTerminalNodes()
	{
		// Arrange
		String[] terminals = new String[] {
			"\"terminal\"",
			"\" symbols ~`!@#$%^&*()_+-={}[];':,.<>/?|\"",
			"\" escaped \\\\ \\\" \\\' \\n \\0 \\a \\b \\f \\n \\r \\t \\u \\U \\x \\v symbols \""
		};

		for (String terminal : terminals)
		{
			Node node = new Node(NodeKind.Terminal, terminal);

			this._grammarLexer.setSource(terminal);

			// Act
			List<Node> nodes = this._grammarLexer.parse();

			// Assert
			Assert.assertEquals(1, nodes.size());

			Assert.assertEquals(node, nodes.get(0));
		}
	}

	@Test
	public void parse_SourceContainsUnknownNodes_ReturnsValidNodes()
	{
		// Arrange - create nodes
		Node[] testNodes = {
			new Node(NodeKind.Nonterminal, "A"),
			new Node(NodeKind.Delimiter, "="), new Node(NodeKind.Unknown, "_"),
			new Node(NodeKind.Unknown, "\""), new Node(NodeKind.Unknown, "0"),
			new Node(NodeKind.Unknown, "1"), new Node(NodeKind.Unknown, "2"),
			new Node(NodeKind.Unknown, "3")
		};
		
		// Arrange - create source string
		StringBuilder source = new StringBuilder();
		
		for (int i = 0, count = testNodes.length; i < count; i++)
		{
			source.append(testNodes[i].getText());
		}

		this._grammarLexer.setSource(source.toString());

		// Act
		List<Node> nodes = this._grammarLexer.parse();

		// Assert
		Assert.assertEquals(testNodes.length, nodes.size());

		Assert.assertEquals(Arrays.asList(testNodes), nodes);
	}

	@Test
	public void parse_SourceIsEmpty_ReturnsEmptyNodesList()
	{
		// Arrange
		String source = "";
		this._grammarLexer.setSource(source);
		
		// Act
		List<Node> nodes = this._grammarLexer.parse();
		
		// Assert
		Assert.assertEquals(0, nodes.size());
	}
	
	@Before
	public void setUp()
	{
		this._grammarLexer = new GrammarLexer();
	}
	
}
