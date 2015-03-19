package syntax_analyzer.Models;

import syntax_analyzer.Enums.NodeKind;
import syntax_analyzer.Utils.Guard;

public class Node
{
	private NodeKind _kind;

	private String _text;
	
	public Node(NodeKind nodeKind, String text)
	{
		Guard.notNull(nodeKind, "nodeKind");
		Guard.notNull(text, "text");

		this._kind = nodeKind;
		this._text = text;
	}
	
	public NodeKind getKind()
	{
		return this._kind;
	}
	
	public String getText()
	{
		return this._text;
	}
}
