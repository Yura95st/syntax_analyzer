package grammar_parser.Models;

import grammar_parser.Enums.NodeKind;
import grammar_parser.Exceptions.NodeIsNotTerminalException;
import grammar_parser.Utils.Guard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Word
{
	private static Word _emptyWord;

	public static Word getEmptyWord()
	{
		if (Word._emptyWord == null)
		{
			Word._emptyWord = new Word();
		}

		return Word._emptyWord;
	}

	private final List<Node> _nodes;

	public Word()
	{
		this._nodes = new ArrayList<Node>();
	}

	public Word(Node... nodes) throws NodeIsNotTerminalException
	{
		this();

		Guard.notNull(nodes, "nodes");

		if (nodes.length > 0)
		{
			this.setNodes(Arrays.asList(nodes));
		}
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
		Word other = (Word) obj;
		if (this._nodes == null)
		{
			if (other._nodes != null)
			{
				return false;
			}
		}
		else if (!this._nodes.equals(other._nodes))
		{
			return false;
		}
		return true;
	}

	public List<Node> getNodes()
	{
		return new ArrayList<Node>(this._nodes);
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result =
			prime * result
				+ ((this._nodes == null) ? 0 : this._nodes.hashCode());
		return result;
	}

	public void setNodes(Iterable<Node> nodes)
		throws NodeIsNotTerminalException
	{
		Guard.notNull(nodes, "nodes");

		this._nodes.clear();

		for (Node node : nodes)
		{
			if (node.getKind() != NodeKind.Terminal)
			{
				throw new NodeIsNotTerminalException(
					String.format(
						"Node with value '%1$s' is not terminal and can't be part of the word.",
						node.getText()));
			}

			this._nodes.add(node);
		}
	}
}