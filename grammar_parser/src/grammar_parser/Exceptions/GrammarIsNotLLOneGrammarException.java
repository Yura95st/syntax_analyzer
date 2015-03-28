package grammar_parser.Exceptions;

public class GrammarIsNotLLOneGrammarException extends Exception
{
	public GrammarIsNotLLOneGrammarException()
	{
		
	}
	
	public GrammarIsNotLLOneGrammarException(String message)
	{
		super(message);
	}
	
	public GrammarIsNotLLOneGrammarException(String message, Throwable cause)
	{
		super(message, cause);
	}
	
	public GrammarIsNotLLOneGrammarException(Throwable cause)
	{
		super(cause);
	}
}
