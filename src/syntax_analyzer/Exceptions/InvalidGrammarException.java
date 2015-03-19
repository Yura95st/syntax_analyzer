package syntax_analyzer.Exceptions;

public class InvalidGrammarException extends Exception
{
	public InvalidGrammarException()
	{
		
	}
	
	public InvalidGrammarException(String message)
	{
		super(message);
	}
	
	public InvalidGrammarException(String message, Throwable cause)
	{
		super(message, cause);
	}
	
	public InvalidGrammarException(Throwable cause)
	{
		super(cause);
	}
}
