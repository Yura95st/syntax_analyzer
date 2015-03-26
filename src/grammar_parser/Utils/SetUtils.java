package grammar_parser.Utils;

import java.util.HashSet;
import java.util.Set;

public class SetUtils
{
	/**
	 * Determines whether intersection of two sets is empty or not.
	 *
	 * @param <T>
	 *            the generic type
	 * @param setOne
	 *            the set one
	 * @param setTwo
	 *            the set two
	 * @return true, if intersection of two sets is empty, false - otherwise.
	 */
	public static <T> boolean setsDoNotIntersect(Set<T> setOne, Set<T> setTwo)
	{
		Guard.notNull(setOne, "setOne");
		Guard.notNull(setTwo, "setTwo");

		Set<T> intersection = new HashSet<T>(setOne);

		intersection.retainAll(setTwo);

		boolean result = intersection.size() == 0;

		return result;
	}
}
