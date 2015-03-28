package grammar_parser.Exceptions;

public class NodeIsNotNonterminalException extends Exception
{
	public NodeIsNotNonterminalException()
	{
		
	}
	
	public NodeIsNotNonterminalException(String message)
	{
		super(message);
	}
	
	public NodeIsNotNonterminalException(String message, Throwable cause)
	{
		super(message, cause);
	}
	
	public NodeIsNotNonterminalException(Throwable cause)
	{
		super(cause);
	}
}
