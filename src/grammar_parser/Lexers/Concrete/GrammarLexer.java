package grammar_parser.Lexers.Concrete;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;

import grammar_parser.Enums.NodeKind;
import grammar_parser.Lexers.Abstract.IGrammarLexer;
import grammar_parser.Models.Node;
import grammar_parser.Models.NodeDefinition;
import grammar_parser.Utils.Guard;

public class GrammarLexer implements IGrammarLexer
{
	private List<NodeDefinition> _nodeDefinitions;

	private int _offset;

	private String _source;

	private List<Character> _spaceCharacters;
	
	public GrammarLexer()
	{
		this._source = "";

		this._nodeDefinitions = new ArrayList<NodeDefinition>() {
			{
				this.add(new NodeDefinition("[a-zA-Z][a-zA-Z0-9_-]*",
					NodeKind.Nonterminal));
				this.add(new NodeDefinition(
					"\"(?:\\\\[\'\"\\\\0abfnrtuUxv]|[^\\\\\"\n])*\"",
					NodeKind.Terminal));
				this.add(new NodeDefinition("[=,|.;]", NodeKind.Delimiter));
			}
		};

		this._spaceCharacters = new ArrayList<Character>() {
			{
				this.add(' ');
				this.add('\n');
				this.add('\r');
				this.add('\t');
			}
		};
	}
	
	@Override
	public List<NodeDefinition> getNodeDefinitions()
	{
		return this._nodeDefinitions;
	}
	
	@Override
	public String getSource()
	{
		return this._source;
	}

	@Override
	public List<Character> getSpaceCharacters()
	{
		return this._spaceCharacters;
	}
	
	@Override
	public List<Node> parse()
	{
		List<Node> nodes = new ArrayList<Node>();
		
		this._offset = 0;

		while (this.isInBounds())
		{
			this.skipSpaces();

			if (!this.isInBounds())
			{
				break;
			}

			Node node = this.processNode();

			if (node == null)
			{
				String nodeValue = this._source.substring(this._offset,
					this._offset + 1);

				node = new Node(NodeKind.Unknown, nodeValue);

				this._offset += nodeValue.length();
			}

			nodes.add(node);
		}

		return nodes;
	}

	@Override
	public void setNodeDefinitions(List<NodeDefinition> nodeDefinitions)
	{
		Guard.notNull(nodeDefinitions, "nodeDefinitions");

		this._nodeDefinitions = new ArrayList<NodeDefinition>(nodeDefinitions);
	}

	@Override
	public void setSource(String source)
	{
		Guard.notNull(source, "source");
		
		this._source = source;
		
		this._offset = 0;
	}

	@Override
	public void setSpaceCharacters(List<Character> spaceCharacters)
	{
		Guard.notNull(spaceCharacters, "spaceCharacters");

		this._spaceCharacters = new ArrayList<Character>(spaceCharacters);
	}
	
	private boolean isInBounds()
	{
		return this._offset < this._source.length();
	}
	
	private Node processNode()
	{
		List<Node> foundNodes = new ArrayList<Node>();

		for (NodeDefinition definition : this._nodeDefinitions)
		{
			String matchString = this._source.substring(this._offset);

			Matcher matcher = definition.getRepresentation().matcher(
				matchString);

			if (!matcher.lookingAt())
			{
				continue;
			}

			NodeKind nodeKind = definition.getKind();
			
			String nodeValue = matchString.substring(0, matcher.end());

			Node node = new Node(nodeKind, nodeValue);

			foundNodes.add(node);
		}

		if (foundNodes.size() == 0)
		{
			return null;
		}
		
		Node longestNode = Collections.max(foundNodes, (n1, n2) -> Integer
				.compare(n1.getText().length(), n2.getText().length()));
		
		this._offset += longestNode.getText().length();
		
		return longestNode;
	}
	
	private void skipSpaces()
	{
		while (this.isInBounds()
			&& this._spaceCharacters
					.contains(this._source.charAt(this._offset)))
		{
			this._offset++;
		}
	}
}