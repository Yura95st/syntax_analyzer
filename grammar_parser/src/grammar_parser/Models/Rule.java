package grammar_parser.Models;

import grammar_parser.Utils.Guard;

import java.util.ArrayList;
import java.util.List;

public class Rule
{
	private final Node _headNode;

	private final List<Node> _nodes;

	public Rule(Node headNode)
	{
		Guard.notNull(headNode, "headNode");

		this._headNode = headNode;

		this._nodes = new ArrayList<Node>();
	}

	public void addNode(Node node)
	{
		Guard.notNull(node, "node");

		this._nodes.add(node);
	}

	public void deleteNode(Node node)
	{
		Guard.notNull(node, "node");

		if (this._nodes.contains(node))
		{
			this._nodes.remove(node);
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

		Rule other = (Rule) obj;

		if (this._headNode == null)
		{
			if (other._headNode != null)
			{
				return false;
			}
		}
		else if (!this._headNode.equals(other._headNode))
		{
			return false;
		}

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

	public Node getHeadNode()
	{
		return this._headNode;
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
				+ ((this._headNode == null) ? 0 : this._headNode.hashCode());

		result =
			prime * result
				+ ((this._nodes == null) ? 0 : this._nodes.hashCode());

		return result;
	}
}
