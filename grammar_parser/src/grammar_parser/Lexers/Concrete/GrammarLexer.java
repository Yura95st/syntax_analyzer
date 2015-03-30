package grammar_parser.Lexers.Concrete;

import grammar_parser.Enums.NodeKind;
import grammar_parser.Lexers.Abstract.IGrammarLexer;
import grammar_parser.Lexers.Abstract.Lexer;
import grammar_parser.Models.Node;
import grammar_parser.Models.NodeDefinition;
import grammar_parser.Utils.Guard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;

public class GrammarLexer extends Lexer<Node> implements IGrammarLexer
{
	private List<NodeDefinition> _nodeDefinitions;

	public GrammarLexer()
	{
		super();

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
	}

	@Override
	public Iterable<NodeDefinition> getNodeDefinitions()
	{
		return this._nodeDefinitions;
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
				String nodeValue =
						this._source.substring(this._offset, this._offset + 1);

				node = new Node(NodeKind.Unknown, nodeValue);

				this._offset += nodeValue.length();
			}
			else if (node.getKind() == NodeKind.Terminal)
			{
				String nodeText = node.getText();

				// Remove quotes around the nodeText.
				nodeText = nodeText.substring(1, nodeText.length() - 1);

				node = new Node(node.getKind(), nodeText);
			}

			nodes.add(node);
		}

		return nodes;
	}

	@Override
	public void setNodeDefinitions(Iterable<NodeDefinition> nodeDefinitions)
	{
		Guard.notNull(nodeDefinitions, "nodeDefinitions");

		this._nodeDefinitions = new ArrayList<NodeDefinition>();

		for (NodeDefinition nodeDefinition : nodeDefinitions)
		{
			this._nodeDefinitions.add(nodeDefinition);
		}
	}

	private Node processNode()
	{
		List<Node> foundNodes = new ArrayList<Node>();

		for (NodeDefinition definition : this._nodeDefinitions)
		{
			String matchString = this._source.substring(this._offset);

			Matcher matcher =
					definition.getRepresentation().matcher(matchString);

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

		Node longestNode =
				Collections.max(foundNodes, (n1, n2) -> Integer.compare(n1
					.getText().length(), n2.getText().length()));

		this._offset += longestNode.getText().length();

		return longestNode;
	}
}