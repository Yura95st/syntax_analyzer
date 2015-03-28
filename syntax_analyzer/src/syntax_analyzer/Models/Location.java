package syntax_analyzer.Models;

import grammar_parser.Utils.Guard;

public class Location
{
	private final int length;

	private final int offset;

	public Location(int offset, int length)
	{
		Guard.moreOrEqualToZero(offset, "offset");
		Guard.moreOrEqualToZero(length, "length");

		this.offset = offset;
		this.length = length;
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
		Location other = (Location) obj;
		if (this.length != other.length)
		{
			return false;
		}
		if (this.offset != other.offset)
		{
			return false;
		}
		return true;
	}

	/**
	 * Gets length
	 *
	 * @return the length
	 */
	public int getLength()
	{
		return this.length;
	}

	/**
	 * Gets offset
	 *
	 * @return the offset
	 */
	public int getOffset()
	{
		return this.offset;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;

		int result = 1;

		result = prime * result + this.length;

		result = prime * result + this.offset;

		return result;
	}
}
