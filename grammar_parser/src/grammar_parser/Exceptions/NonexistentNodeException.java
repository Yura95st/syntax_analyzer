package grammar_parser.Exceptions;

public class NonexistentNodeException extends Exception
{
	public NonexistentNodeException()
	{

	}

	public NonexistentNodeException(String message)
	{
		super(message);
	}

	public NonexistentNodeException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public NonexistentNodeException(Throwable cause)
	{
		super(cause);
	}
}
