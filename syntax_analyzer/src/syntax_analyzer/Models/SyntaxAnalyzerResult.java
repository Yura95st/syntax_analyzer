package syntax_analyzer.Models;

public class SyntaxAnalyzerResult
{
	private final boolean _isSyntaxValid;

	private final Token _wrongToken;

	public SyntaxAnalyzerResult(boolean isSyntaxValid, Token wrongToken)
	{
		this._isSyntaxValid = isSyntaxValid;
		this._wrongToken = wrongToken;
	}

	public Token getWrongToken()
	{
		return this._wrongToken;
	}

	public boolean isSyntaxValid()
	{
		return this._isSyntaxValid;
	}
}
