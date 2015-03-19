package syntax_analyzer.Utils;

public class Guard
{
	/**
	 * Checks if the specified list is not null and doesn't contain any null
	 * element.
	 *
	 * @param <T>
	 *            the generic type
	 * @param list
	 *            the list to test
	 * @param listName
	 *            the list's name
	 */
	public static <T> void listAndElementsNotNull(Iterable<T> list,
		String listName)
	{
		Guard.notNull(list, listName);
		
		for (T element : list)
		{
			if (element == null)
			{
				throw new IllegalArgumentException(
					String.format(
						"Argument '%1$s' can't contain elements, that are equal to null.",
						listName));
			}
		}
	}

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
				"List '%1$s' can't be empty.", listName));
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
				"Argument '%1$s' can't be less, than 0.", argumentName));
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
				"Argument '%1$s' can't be less, than 0.", argumentName));
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
				"Argument '%1$s' can't be less or equal to 0.", argumentName));
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
				"Argument '%1$s' can't be null.", argumentName));
		}
	}

}
