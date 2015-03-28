package syntax_analyzer.Lexers.Concrete;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import syntax_analyzer.Enums.TokenKind;
import syntax_analyzer.Lexers.Abstract.IPrimitiveLanguageLexer;
import syntax_analyzer.Models.Location;
import syntax_analyzer.Models.Token;

public class PrimitiveLanguageLexerTests
{
	private IPrimitiveLanguageLexer _primitiveLanguageLexer;

	@Test
	public void parse_MethodIsIdempotent()
	{
		// Arrange
		String source = "x1 = 2;";

		this._primitiveLanguageLexer.setSource(source);

		// Act
		List<Token> tokensOne = this._primitiveLanguageLexer.parse();
		List<Token> tokensTwo = this._primitiveLanguageLexer.parse();

		// Assert
		Assert.assertEquals(tokensOne, tokensTwo);
	}

	@Test
	public void parse_ReturnsValidTokens()
	{
		// Arrange - create tokens
		Token[] testTokens =
			{
				new Token(TokenKind.Keyword, "begin", new Location(0, 5)),
				new Token(TokenKind.Identifier, "_x1", new Location(6, 3)),
				new Token(TokenKind.Operator, ":=", new Location(10, 2)),
				new Token(TokenKind.Number, "0123", new Location(13, 4)),
				new Token(TokenKind.Punctuator, ";", new Location(18, 1)),
				new Token(TokenKind.Keyword, "end", new Location(20, 3)),
				new Token(TokenKind.Punctuator, ".", new Location(24, 1)),
			};

		// Arrange - create source string
		StringBuilder source = new StringBuilder();

		for (int i = 0, count = testTokens.length; i < count; i++)
		{
			source.append(testTokens[i].getValue());
			source.append(" ");
		}

		this._primitiveLanguageLexer.setSource(source.toString().trim());

		// Act
		List<Token> tokens = this._primitiveLanguageLexer.parse();

		// Assert
		Assert.assertEquals(testTokens.length, tokens.size());

		Assert.assertEquals(Arrays.asList(testTokens), tokens);
	}

	@Test
	public void parse_SourceContainsOnlyIdentifiers_ReturnsIdentifierTokens()
	{
		// Arrange
		String[] identifiers =
			new String[] {
				"identifier1", "_identifier2", "identifier_3", "IDENTIFIER1",
				"_IDENTIFIER2", "IDENTIFIER_3"
			};

		for (String identifier : identifiers)
		{
			Token token =
				new Token(TokenKind.Identifier, identifier, new Location(0,
					identifier.length()));

			this._primitiveLanguageLexer.setSource(identifier);

			// Act
			List<Token> tokens = this._primitiveLanguageLexer.parse();

			// Assert
			Assert.assertEquals(1, tokens.size());

			Assert.assertEquals(token, tokens.get(0));
		}
	}

	@Test
	public void parse_SourceContainsOnlyKeywords_ReturnsKeywordTokens()
	{
		// Arrange
		String[] keywords =
			new String[] {
				"begin", "call", "const", "do", "end", "if", "odd",
				"procedure", "then", "var", "while", "BEGIN", "CALL", "CONST",
				"DO", "END", "IF", "ODD", "PROCEDURE", "THEN", "VAR", "WHILE"
			};

		for (String keyword : keywords)
		{
			Token token =
				new Token(TokenKind.Keyword, keyword, new Location(0,
					keyword.length()));

			this._primitiveLanguageLexer.setSource(keyword);

			// Act
			List<Token> tokens = this._primitiveLanguageLexer.parse();

			// Assert
			Assert.assertEquals(1, tokens.size());

			Assert.assertEquals(token, tokens.get(0));
		}
	}

	@Test
	public void parse_SourceContainsOnlyNumbers_ReturnsNumberTokens()
	{
		// Arrange
		String[] numbers = new String[] {
			"000", "01", "001234", "1234"
		};

		for (String number : numbers)
		{
			Token token =
				new Token(TokenKind.Number, number, new Location(0,
					number.length()));

			this._primitiveLanguageLexer.setSource(number);

			// Act
			List<Token> tokens = this._primitiveLanguageLexer.parse();

			// Assert
			Assert.assertEquals(1, tokens.size());

			Assert.assertEquals(token, tokens.get(0));
		}
	}

	@Test
	public void parse_SourceContainsOnlyOperators_ReturnsOperatorTokens()
	{
		// Arrange
		String[] operators = new String[] {
			">=", "<=", ":=", ">", "<", "=", "#", "?", "!", "+", "-", "*", "/"
		};

		for (String operator : operators)
		{
			Token token =
				new Token(TokenKind.Operator, operator, new Location(0,
					operator.length()));

			this._primitiveLanguageLexer.setSource(operator);

			// Act
			List<Token> tokens = this._primitiveLanguageLexer.parse();

			// Assert
			Assert.assertEquals(1, tokens.size());

			Assert.assertEquals(token, tokens.get(0));
		}
	}

	@Test
	public void parse_SourceContainsOnlyPunctuators_ReturnsPunctuatorTokens()
	{
		// Arrange
		String[] punctuators = new String[] {
			"(", ")", ".", ",", ";"
		};

		for (String punctuator : punctuators)
		{
			Token token =
				new Token(TokenKind.Punctuator, punctuator, new Location(0,
					punctuator.length()));

			this._primitiveLanguageLexer.setSource(punctuator);

			// Act
			List<Token> tokens = this._primitiveLanguageLexer.parse();

			// Assert
			Assert.assertEquals(1, tokens.size());

			Assert.assertEquals(token, tokens.get(0));
		}
	}

	@Test
	public void parse_SourceContainsOnlySpaceCharacters_ReturnsEmptyTokensList()
	{
		// Arrange
		StringBuilder source = new StringBuilder();

		List<Character> spaceCharacters = new ArrayList<Character>();

		for (Character spaceCharacter : this._primitiveLanguageLexer
				.getSpaceCharacters())
		{
			spaceCharacters.add(spaceCharacter);
		}

		for (int i = 0, count = spaceCharacters.size(); i < count; i++)
		{
			source.append(spaceCharacters.get(i));
		}

		this._primitiveLanguageLexer.setSource(source.toString());

		// Act
		List<Token> tokens = this._primitiveLanguageLexer.parse();

		// Assert
		Assert.assertEquals(0, tokens.size());
	}

	@Test
	public void parse_SourceContainsUnknownTokens_ReturnsValidTokens()
	{
		// Arrange - create tokens
		Token[] testTokens =
			{
				new Token(TokenKind.Identifier, "x1", new Location(0, 2)),
				new Token(TokenKind.Operator, "=", new Location(2, 1)),
				new Token(TokenKind.Number, "1", new Location(3, 1)),
				new Token(TokenKind.Unknown, "\\", new Location(4, 1)),
				new Token(TokenKind.Unknown, "\"", new Location(5, 1)),
				new Token(TokenKind.Unknown, ":", new Location(6, 1))
			};

		// Arrange - create source string
		StringBuilder source = new StringBuilder();

		for (int i = 0, count = testTokens.length; i < count; i++)
		{
			source.append(testTokens[i].getValue());
		}

		this._primitiveLanguageLexer.setSource(source.toString());

		// Act
		List<Token> tokens = this._primitiveLanguageLexer.parse();

		// Assert
		Assert.assertEquals(Arrays.asList(testTokens), tokens);
	}

	@Test
	public void parse_SourceIsEmpty_ReturnsEmptyTokensList()
	{
		// Arrange
		String source = "";
		this._primitiveLanguageLexer.setSource(source);

		// Act
		List<Token> tokens = this._primitiveLanguageLexer.parse();

		// Assert
		Assert.assertEquals(0, tokens.size());
	}

	@Before
	public void setUp() throws Exception
	{
		this._primitiveLanguageLexer = new PrimitiveLanguageLexer();
	}
}
