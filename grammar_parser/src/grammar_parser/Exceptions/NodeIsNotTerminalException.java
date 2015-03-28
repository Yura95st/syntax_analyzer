package grammar_parser.Exceptions;

public class NodeIsNotTerminalException extends Exception
{
	public NodeIsNotTerminalException()
	{
		
	}
	
	public NodeIsNotTerminalException(String message)
	{
		super(message);
	}
	
	public NodeIsNotTerminalException(String message, Throwable cause)
	{
		super(message, cause);
	}
	
	public NodeIsNotTerminalException(Throwable cause)
	{
		super(cause);
	}
}
