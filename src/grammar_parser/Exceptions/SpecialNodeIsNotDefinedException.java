package grammar_parser.Exceptions;

public class SpecialNodeIsNotDefinedException extends Exception
{
	public SpecialNodeIsNotDefinedException()
	{

	}

	public SpecialNodeIsNotDefinedException(String message)
	{
		super(message);
	}

	public SpecialNodeIsNotDefinedException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public SpecialNodeIsNotDefinedException(Throwable cause)
	{
		super(cause);
	}
}
