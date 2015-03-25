package grammar_parser.Models;

import grammar_parser.Enums.NodeKind;
import grammar_parser.Exceptions.NodeIsNotNonterminalException;
import grammar_parser.Utils.Guard;

public class ControlTableItem
{
	private final Node _node;

	private final Word _word;

	public ControlTableItem(Node node, Word word)
		throws NodeIsNotNonterminalException
	{
		Guard.notNull(node, "node");
		Guard.notNull(word, "word");

		if (node.getKind() != NodeKind.Nonterminal)
		{
			throw new NodeIsNotNonterminalException(String.format(
				"Node with value '%1$s' is not terminal.", node.getText()));
		}

		this._node = node;
		this._word = word;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (this.getClass() != obj.getClass())
		{
			return false;
		}
		ControlTableItem other = (ControlTableItem) obj;
		if (this._node == null)
		{
			if (other._node != null)
			{
				return false;
			}
		}
		else if (!this._node.equals(other._node))
		{
			return false;
		}
		if (this._word == null)
		{
			if (other._word != null)
			{
				return false;
			}
		}
		else if (!this._word.equals(other._word))
		{
			return false;
		}
		return true;
	}

	public Node getNode()
	{
		return this._node;
	}

	public Word getWord()
	{
		return this._word;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result =
			prime * result + ((this._node == null) ? 0 : this._node.hashCode());
		result =
			prime * result + ((this._word == null) ? 0 : this._word.hashCode());
		return result;
	}
}
