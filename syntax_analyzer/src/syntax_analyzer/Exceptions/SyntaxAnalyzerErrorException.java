package syntax_analyzer.Exceptions;

public class SyntaxAnalyzerErrorException extends Exception
{
	public SyntaxAnalyzerErrorException()
	{
		
	}
	
	public SyntaxAnalyzerErrorException(String message)
	{
		super(message);
	}
	
	public SyntaxAnalyzerErrorException(String message, Throwable cause)
	{
		super(message, cause);
	}
	
	public SyntaxAnalyzerErrorException(Throwable cause)
	{
		super(cause);
	}
}
