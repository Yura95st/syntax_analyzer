package syntax_analyzer.Models;

import org.junit.Assert;
import org.junit.Test;

import syntax_analyzer.Enums.TokenKind;

public class TokenTests
{
	@Test
	public void equals_TwoTokensHaveDifferentKindAndValueAndLocationFields_ReturnsFalse()
		throws Exception
	{
		// Arrange
		Token tokenOne = new Token(TokenKind.Number, "A", new Location(0, 1));
		Token tokenTwo = new Token(TokenKind.Identifier, "A", new Location(0, 1));
		Token tokenThree = new Token(TokenKind.Number, "B", new Location(1, 2));

		// Act
		boolean resultOne = tokenOne.equals(tokenTwo);
		boolean resultTwo = tokenOne.equals(tokenThree);
		boolean resultThree = tokenTwo.equals(tokenThree);

		// Assert
		Assert.assertEquals(false, resultOne);
		Assert.assertEquals(false, resultTwo);
		Assert.assertEquals(false, resultThree);
	}

	@Test
	public void equals_TwoTokensHaveEqualKindAndValueAndLocationFields_ReturnsTrue()
		throws Exception
	{
		// Arrange
		Token tokenOne = new Token(TokenKind.Identifier, "A", new Location(0, 1));
		Token tokenTwo = new Token(TokenKind.Identifier, "A", new Location(0, 1));

		// Act
		boolean result = tokenOne.equals(tokenTwo);

		// Assert
		Assert.assertEquals(true, result);
	}

	@Test
	public void hashCode_TwoTokensHaveDifferentKindAndValueAndLocationFields_ReturnsDifferentHashCodes()
		throws Exception
	{
		// Arrange - create tokens
		Token tokenOne = new Token(TokenKind.Number, "A", new Location(0, 1));
		Token tokenTwo = new Token(TokenKind.Identifier, "A", new Location(0, 1));
		Token tokenThree = new Token(TokenKind.Number, "B", new Location(1, 2));

		// Act
		int hashCodeOne = tokenOne.hashCode();
		int hashCodeTwo = tokenTwo.hashCode();
		int hashCodeThree = tokenThree.hashCode();

		// Assert
		Assert.assertNotEquals(hashCodeOne, hashCodeTwo);
		Assert.assertNotEquals(hashCodeOne, hashCodeThree);
		Assert.assertNotEquals(hashCodeTwo, hashCodeThree);
	}

	@Test
	public void hashCode_TwoTokensHaveEqualKindAndValueAndLocationFields_ReturnsEqualHashCodes()
		throws Exception
	{
		// Arrange
		Token tokenOne = new Token(TokenKind.Identifier, "A", new Location(0, 1));
		Token tokenTwo = new Token(TokenKind.Identifier, "A", new Location(0, 1));

		// Act
		int hashCodeOne = tokenOne.hashCode();
		int hashCodeTwo = tokenTwo.hashCode();

		// Assert
		Assert.assertEquals(hashCodeOne, hashCodeTwo);
	}
}
