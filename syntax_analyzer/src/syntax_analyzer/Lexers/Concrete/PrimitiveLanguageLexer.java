package syntax_analyzer.Lexers.Concrete;

import grammar_parser.Lexers.Abstract.Lexer;
import grammar_parser.Utils.Guard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;

import syntax_analyzer.Enums.TokenKind;
import syntax_analyzer.Lexers.Abstract.IPrimitiveLanguageLexer;
import syntax_analyzer.Models.Location;
import syntax_analyzer.Models.Token;
import syntax_analyzer.Models.TokenDefinition;

public class PrimitiveLanguageLexer extends Lexer<Token> implements
		IPrimitiveLanguageLexer
{
	private Set<String> _keywords;

	private List<TokenDefinition> _tokenDefinitions;

	public PrimitiveLanguageLexer()
	{
		super();

		this._keywords =
			new HashSet<String>(Arrays.asList("begin", "call", "const", "do",
				"end", "if", "odd", "procedure", "then", "var", "while"));

		this._tokenDefinitions = new ArrayList<TokenDefinition>() {
			{
				this.add(new TokenDefinition("[a-zA-Z_][a-zA-Z0-9_]*",
					TokenKind.Identifier));
				this.add(new TokenDefinition("[0-9]+", TokenKind.Number));
				this.add(new TokenDefinition(
					">=|<=|:=|>|<|=|#|\\?|!|\\+|-|\\*|/", TokenKind.Operator));
				this.add(new TokenDefinition("[\\Q().,;\\E]",
					TokenKind.Punctuator));
			}
		};
	}

	@Override
	public Set<String> getKeywords()
	{
		return this._keywords;
	}

	@Override
	public List<TokenDefinition> getTokenDefinitions()
	{
		return this._tokenDefinitions;
	}

	@Override
	public List<Token> parse()
	{
		List<Token> tokens = new ArrayList<Token>();

		this._offset = 0;

		while (this.isInBounds())
		{
			this.skipSpaces();

			if (!this.isInBounds())
			{
				break;
			}

			Token token = this.processToken();

			if (token == null)
			{
				String tokenValue =
					this._source.substring(this._offset, this._offset + 1);

				token =
					new Token(TokenKind.Unknown, tokenValue, new Location(
						this._offset, tokenValue.length()));

				this._offset += tokenValue.length();
			}

			tokens.add(token);
		}

		return tokens;
	}

	@Override
	public void setKeywords(Iterable<String> keywords)
	{
		Guard.notNull(keywords, "keywords");

		this._keywords = new HashSet<String>();

		for (String keyword : keywords)
		{
			this._keywords.add(keyword);
		}
	}

	@Override
	public void setTokenDefinitions(Iterable<TokenDefinition> tokenDefinitions)
	{
		Guard.notNull(tokenDefinitions, "tokenDefinitions");

		this._tokenDefinitions = new ArrayList<TokenDefinition>();

		for (TokenDefinition tokenDefinition : tokenDefinitions)
		{
			this._tokenDefinitions.add(tokenDefinition);
		}
	}

	private Token processToken()
	{
		List<Token> foundTokens = new ArrayList<Token>();

		for (TokenDefinition definition : this._tokenDefinitions)
		{
			String matchString = this._source.substring(this._offset);

			Matcher matcher =
				definition.getRepresentation().matcher(matchString);

			if (!matcher.lookingAt())
			{
				continue;
			}

			Location location = new Location(this._offset, matcher.end());

			TokenKind tokenKind = definition.getKind();

			String tokenValue = matchString.substring(0, matcher.end());

			if (this._keywords.contains(tokenValue.toLowerCase()))
			{
				tokenKind = TokenKind.Keyword;
			}

			Token token = new Token(tokenKind, tokenValue, location);

			foundTokens.add(token);
		}

		if (foundTokens.size() == 0)
		{
			return null;
		}

		Token longestToken =
			Collections.max(foundTokens, (n1, n2) -> Integer.compare(n1
					.getValue().length(), n2.getValue().length()));

		this._offset += longestToken.getValue().length();

		return longestToken;
	}
}