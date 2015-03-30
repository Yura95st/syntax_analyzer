package syntax_analyzer.Exceptions;

public class GrammarIsNotSetException extends Exception
{
	public GrammarIsNotSetException()
	{
		
	}
	
	public GrammarIsNotSetException(String message)
	{
		super(message);
	}
	
	public GrammarIsNotSetException(String message, Throwable cause)
	{
		super(message, cause);
	}
	
	public GrammarIsNotSetException(Throwable cause)
	{
		super(cause);
	}
}
