package syntax_analyzer.Models;

import java.util.ArrayList;
import java.util.List;

import syntax_analyzer.Utils.Guard;

public class Rule
{
	private Node _headNode;
	
	private List<Node> _nodes;

	public Rule(Node headNode)
	{
		Guard.notNull(headNode, "headNode");
		
		this._headNode = headNode;
		
		this._nodes = new ArrayList<Node>();
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

		result = prime * result
				+ ((this._headNode == null) ? 0 : this._headNode.hashCode());

		result = prime * result
				+ ((this._nodes == null) ? 0 : this._nodes.hashCode());

		return result;
	}

	public void setNodes(List<Node> nodes)
	{
		Guard.notNull(nodes, "nodes");

		for (Node node : nodes)
		{
			if (node != null)
			{
				this._nodes.add(node);
			}
		}
	}
}