package grammar_parser.Models;

import grammar_parser.Enums.NodeKind;
import grammar_parser.Utils.Guard;

public class Node
{
	private final NodeKind _kind;

	private final String _text;

	public Node(NodeKind nodeKind, String text)
	{
		Guard.notNull(nodeKind, "nodeKind");
		Guard.notNull(text, "text");

		this._kind = nodeKind;
		this._text = text;
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

		Node other = (Node) obj;

		if (this._kind != other._kind)
		{
			return false;
		}

		if (this._text == null)
		{
			if (other._text != null)
			{
				return false;
			}
		}
		else if (!this._text.equals(other._text))
		{
			return false;
		}

		return true;
	}

	public NodeKind getKind()
	{
		return this._kind;
	}

	public String getText()
	{
		return this._text;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;

		result =
			prime * result + ((this._kind == null) ? 0 : this._kind.hashCode());

		result =
			prime * result + ((this._text == null) ? 0 : this._text.hashCode());

		return result;
	}
}
