package grammar_parser.Utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

public class SetUtilsTests
{
	@Test(expected = IllegalArgumentException.class)
	public void setsDoNotIntersect_SetOneIsNull_ThrowsIllegalArgumentException()
	{
		// Arrange
		Set<Integer> setTwo = new HashSet<Integer>();

		// Act & Assert
		SetUtils.setsDoNotIntersect(null, setTwo);
	}

	@Test
	public void setsDoNotIntersect_SetsAreEmpty_ReturnsTrue()
	{
		// Arrange
		Set<Integer> setOne = new HashSet<Integer>();
		Set<Integer> setTwo = new HashSet<Integer>();

		// Act
		boolean result = SetUtils.setsDoNotIntersect(setOne, setTwo);

		// Assert
		Assert.assertEquals(true, result);
	}

	@Test
	public void setsDoNotIntersect_SetsDoNotHaveAnyCommonElements_ReturnsTrue()
	{
		// Arrange
		Set<Integer> setOne = new HashSet<Integer>(Arrays.asList(1, 2, 3, 4));
		Set<Integer> setTwo = new HashSet<Integer>(Arrays.asList(5, 6, 7, 8));

		// Act
		boolean result = SetUtils.setsDoNotIntersect(setOne, setTwo);

		// Assert
		Assert.assertEquals(true, result);
	}

	@Test
	public void setsDoNotIntersect_SetsHaveCommonElements_ReturnsFalse()
	{
		// Arrange
		Set<Integer> setOne = new HashSet<Integer>(Arrays.asList(1, 2, 3, 4));
		Set<Integer> setTwo = new HashSet<Integer>(Arrays.asList(2, 3, 4, 5));

		// Act
		boolean result = SetUtils.setsDoNotIntersect(setOne, setTwo);

		// Assert
		Assert.assertEquals(false, result);
	}

	@Test(expected = IllegalArgumentException.class)
	public void setsDoNotIntersect_SetTwoIsNull_ThrowsIllegalArgumentException()
	{
		// Arrange
		Set<Integer> setOne = new HashSet<Integer>();

		// Act & Assert
		SetUtils.setsDoNotIntersect(setOne, null);
	}
}
