package syntax_analyzer.Utils;

public class Guard
{
	/**
	 * Checks if the specified list is not null or empty.
	 *
	 * @param <T>
	 *            the generic type
	 * @param list
	 *            the list to test
	 * @param listName
	 *            the list's name
	 */
	public static <T> void listNotNullOrEmpty(Iterable<T> list, String listName)
	{
		Guard.notNull(list, listName);

		if (!list.iterator().hasNext())
		{
			throw new IllegalArgumentException(String.format(
				"List can't be empty: %1$s", listName));
		}
	}

	/**
	 * Checks if the specified argument is more or equal to zero.
	 *
	 * @param argument
	 *            the argument to test
	 * @param argumentName
	 *            the argument's name
	 */
	public static void moreOrEqualToZero(double argument, String argumentName)
	{
		if (argument < 0)
		{
			throw new IllegalArgumentException(String.format(
				"Argument can't be less, than 0: %1$s", argumentName));
		}
	}
	
	/**
	 * Checks if the specified argument is more or equal to zero.
	 *
	 * @param argument
	 *            the argument to test
	 * @param argumentName
	 *            the argument's name
	 */
	public static void moreOrEqualToZero(int argument, String argumentName)
	{
		if (argument < 0)
		{
			throw new IllegalArgumentException(String.format(
				"Argument can't be less, than 0: %1$s", argumentName));
		}
	}

	/**
	 * Checks if the specified argument is more than zero.
	 *
	 * @param argument
	 *            the argument to test
	 * @param argumentName
	 *            the argument's name
	 */
	public static void moreThanZero(int argument, String argumentName)
	{
		if (argument <= 0)
		{
			throw new IllegalArgumentException(String.format(
				"Argument can't be less or equal to 0: %1$s", argumentName));
		}
	}

	/**
	 * Checks if the specified argument is not null.
	 *
	 * @param argument
	 *            the argument to test
	 * @param argumentName
	 *            the argument's name
	 */
	public static void notNull(Object argument, String argumentName)
	{
		if (argument == null)
		{
			throw new IllegalArgumentException(String.format(
				"Argument can't be null: %1$s", argumentName));
		}
	}
	
}
