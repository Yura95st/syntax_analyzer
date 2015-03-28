package syntax_analyzer.Models;

import grammar_parser.Utils.Guard;
import syntax_analyzer.Enums.TokenKind;

public class Token
{
	private final TokenKind kind;

	private final Location location;

	private final String value;

	public Token(TokenKind kind, String value, Location location)
	{
		Guard.notNull(value, "value");
		Guard.notNull(kind, "kind");
		Guard.notNull(location, "location");

		this.value = value;
		this.kind = kind;
		this.location = location;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (this.getClass() != obj.getClass())
		{
			return false;
		}
		Token other = (Token) obj;
		if (this.kind != other.kind)
		{
			return false;
		}
		if (this.location == null)
		{
			if (other.location != null)
			{
				return false;
			}
		}
		else if (!this.location.equals(other.location))
		{
			return false;
		}
		if (this.value == null)
		{
			if (other.value != null)
			{
				return false;
			}
		}
		else if (!this.value.equals(other.value))
		{
			return false;
		}

		return true;
	}

	/**
	 * Gets kind
	 *
	 * @return the kind
	 */
	public TokenKind getKind()
	{
		return this.kind;
	}

	/**
	 * Gets location
	 *
	 * @return the location
	 */
	public Location getLocation()
	{
		return this.location;
	}

	/**
	 * Gets value
	 *
	 * @return the value
	 */
	public String getValue()
	{
		return this.value;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;

		int result = 1;

		result =
			prime * result + ((this.kind == null) ? 0 : this.kind.hashCode());

		result =
			prime * result
				+ ((this.location == null) ? 0 : this.location.hashCode());

		result =
			prime * result + ((this.value == null) ? 0 : this.value.hashCode());

		return result;
	}
}
