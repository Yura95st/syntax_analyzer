package syntax_analyzer.Models;

import java.util.regex.Pattern;

import syntax_analyzer.Enums.TokenKind;

public class TokenDefinition
{
	private final TokenKind _kind;

	private final Pattern _representation;

	public TokenDefinition(String representation, TokenKind kind)
	{
		this._representation = Pattern.compile(representation);
		this._kind = kind;
	}

	public TokenKind getKind()
	{
		return this._kind;
	}

	public Pattern getRepresentation()
	{
		return this._representation;
	}

}
