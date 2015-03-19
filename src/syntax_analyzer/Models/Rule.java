package syntax_analyzer.Models;

import java.util.ArrayList;
import java.util.List;

import syntax_analyzer.Utils.Guard;

public class Rule
{
	private Node _head;
	
	private List<Node> _nodes;

	public Rule(Node headNode)
	{
		Guard.notNull(headNode, "headNode");
		
		this._head = headNode;
		
		this._nodes = new ArrayList<Node>();
	}
	
	public Node getHead()
	{
		return this._head;
	}

	public List<Node> getNodes()
	{
		return new ArrayList<Node>(this._nodes);
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
