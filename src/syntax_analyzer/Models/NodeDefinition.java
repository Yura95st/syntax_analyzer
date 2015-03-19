package syntax_analyzer.Models;

import java.util.regex.Pattern;

import syntax_analyzer.Enums.NodeKind;

public class NodeDefinition
{
	private final NodeKind _kind;
	
	private final Pattern _representation;
	
	public NodeDefinition(String representation, NodeKind kind)
	{
		this._representation = Pattern.compile(representation);
		this._kind = kind;
	}
	
	public NodeKind getKind()
	{
		return this._kind;
	}

	public Pattern getRepresentation()
	{
		return this._representation;
	}
	
}
