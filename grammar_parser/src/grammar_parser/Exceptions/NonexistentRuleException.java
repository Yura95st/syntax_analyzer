package grammar_parser.Exceptions;

public class NonexistentRuleException extends Exception
{
	public NonexistentRuleException()
	{
		
	}
	
	public NonexistentRuleException(String message)
	{
		super(message);
	}
	
	public NonexistentRuleException(String message, Throwable cause)
	{
		super(message, cause);
	}
	
	public NonexistentRuleException(Throwable cause)
	{
		super(cause);
	}
}
