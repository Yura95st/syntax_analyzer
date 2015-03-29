package syntax_analyzer.Models;

import org.junit.Assert;
import org.junit.Test;

public class LocationTests
{
	@Test(expected = IllegalArgumentException.class)
	public void constructor_lengthIsNegative_ThrowsIllegalArgumentException()
			throws Exception
	{
		// Act & Assert
		new Location(0, -1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void constructor_offsetIsNegative_ThrowsIllegalArgumentException()
			throws Exception
	{
		// Act & Assert
		new Location(-1, 0);
	}

	@Test
	public void equals_TwoLocationsHaveDifferentKindAndValueAndLocationFields_ReturnsFalse()
			throws Exception
	{
		// Arrange
		Location locationOne = new Location(0, 1);
		Location locationTwo = new Location(0, 2);
		Location locationThree = new Location(1, 2);

		// Act
		boolean resultOne = locationOne.equals(locationTwo);
		boolean resultTwo = locationOne.equals(locationThree);
		boolean resultThree = locationTwo.equals(locationThree);

		// Assert
		Assert.assertEquals(false, resultOne);
		Assert.assertEquals(false, resultTwo);
		Assert.assertEquals(false, resultThree);
	}

	@Test
	public void equals_TwoLocationsHaveEqualKindAndValueAndLocationFields_ReturnsTrue()
			throws Exception
	{
		// Arrange
		Location locationOne = new Location(0, 1);
		Location locationTwo = new Location(0, 1);

		// Act
		boolean result = locationOne.equals(locationTwo);

		// Assert
		Assert.assertEquals(true, result);
	}

	@Test
	public void hashCode_TwoLocationsHaveDifferentKindAndValueAndLocationFields_ReturnsDifferentHashCodes()
			throws Exception
	{
		// Arrange - create locations
		Location locationOne = new Location(0, 1);
		Location locationTwo = new Location(0, 2);
		Location locationThree = new Location(1, 2);

		// Act
		int hashCodeOne = locationOne.hashCode();
		int hashCodeTwo = locationTwo.hashCode();
		int hashCodeThree = locationThree.hashCode();

		// Assert
		Assert.assertNotEquals(hashCodeOne, hashCodeTwo);
		Assert.assertNotEquals(hashCodeOne, hashCodeThree);
		Assert.assertNotEquals(hashCodeTwo, hashCodeThree);
	}

	@Test
	public void hashCode_TwoLocationsHaveEqualKindAndValueAndLocationFields_ReturnsEqualHashCodes()
			throws Exception
	{
		// Arrange
		Location locationOne = new Location(0, 1);
		Location locationTwo = new Location(0, 1);

		// Act
		int hashCodeOne = locationOne.hashCode();
		int hashCodeTwo = locationTwo.hashCode();

		// Assert
		Assert.assertEquals(hashCodeOne, hashCodeTwo);
	}
}
