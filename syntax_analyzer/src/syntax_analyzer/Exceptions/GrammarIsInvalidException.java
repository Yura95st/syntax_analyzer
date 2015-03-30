package syntax_analyzer.Exceptions;

public class GrammarIsInvalidException extends Exception
{
	public GrammarIsInvalidException()
	{
		
	}
	
	public GrammarIsInvalidException(String message)
	{
		super(message);
	}
	
	public GrammarIsInvalidException(String message, Throwable cause)
	{
		super(message, cause);
	}
	
	public GrammarIsInvalidException(Throwable cause)
	{
		super(cause);
	}
}
