package grammar_parser.Utils;

import java.util.ArrayList;

import org.junit.Test;

public class GuardTests
{
	@Test(expected = IllegalArgumentException.class)
	public void listAndElementsNotNull_ListContainsNullElements_ThrowsIllegalArgumentException()
	{
		Iterable<Object> list = new ArrayList<Object>() {
			{
				this.add(new Object());
				this.add(null);
			}
		};
		String listName = "listName";

		Guard.listAndElementsNotNull(list, listName);
	}
	
	@Test
	public void listAndElementsNotNull_ListContainsOnlyNotNullElements_DoesNotThrowAnyException()
	{
		Iterable<Object> list = new ArrayList<Object>() {
			{
				this.add(new Object());
			}
		};
		String listName = "listName";

		Guard.listAndElementsNotNull(list, listName);
	}
	
	@Test
	public void listAndElementsNotNull_ListIsEmpty_DoesNotThrowAnyException()
	{
		Iterable<Object> list = new ArrayList<Object>();
		String listName = "listName";

		Guard.listAndElementsNotNull(list, listName);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void listAndElementsNotNull_ListIsNull_ThrowsIllegalArgumentException()
	{
		Guard.listAndElementsNotNull(null, "listName");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void listNotNullOrEmpty_ListIsEmpty_ThrowsIllegalArgumentException()
	{
		Iterable<Object> list = new ArrayList<Object>();
		String listName = "listName";

		Guard.listNotNullOrEmpty(list, listName);
	}

	@Test
	public void listNotNullOrEmpty_ListIsNotEmpty_DoesNotThrowAnyException()
	{
		Iterable<Object> list = new ArrayList<Object>() {
			{
				this.add(new Object());
			}
		};
		String listName = "listName";

		Guard.listNotNullOrEmpty(list, listName);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void listNotNullOrEmpty_ListIsNull_ThrowsIllegalArgumentException()
	{
		Guard.listNotNullOrEmpty(null, "listName");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void moreOrEqualToZero_DoubleNumberIsLessThanZero_ThrowsIllegalArgumentException()
	{
		double number = -1.0;
		Guard.moreOrEqualToZero(number, "number");
	}
	
	@Test
	public void moreOrEqualToZero_DoubleNumberisMoreOrEqualToZero_DoesNotThrowAnyException()
	{
		double number = 0.0;

		Guard.moreOrEqualToZero(number, "number");

		number = 1.0;

		Guard.moreOrEqualToZero(number, "number");
	}

	@Test(expected = IllegalArgumentException.class)
	public void moreOrEqualToZero_IntegerNumberIsLessThanZero_ThrowsIllegalArgumentException()
	{
		int number = -1;
		Guard.moreOrEqualToZero(number, "number");
	}

	@Test
	public void moreOrEqualToZero_IntegerNumberisMoreOrEqualToZero_DoesNotThrowAnyException()
	{
		int number = 0;

		Guard.moreOrEqualToZero(number, "number");

		number = 1;

		Guard.moreOrEqualToZero(number, "number");
	}

	@Test
	public void moreThanZero_IntegerNumberisMoreThanZero_DoesNotThrowAnyException()
	{
		int number = 1;

		Guard.moreThanZero(number, "number");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void moreThanZero_NumberIsEqualToZero_ThrowsIllegalArgumentException()
	{
		int number = 0;
		Guard.moreThanZero(number, "number");
	}

	@Test(expected = IllegalArgumentException.class)
	public void moreThanZero_NumberIsLessThanZero_ThrowsIllegalArgumentException()
	{
		int number = -1;
		Guard.moreThanZero(number, "number");
	}
	
	@Test
	public void notNull_ObjectIsNotNull_DoesNotThrowAnyException()
	{
		Object tempObject = new Object();
		
		Guard.notNull(tempObject, "tempObject");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void notNull_ObjectIsNull_ThrowsIllegalArgumentException()
	{
		Object tempObject = null;
		
		Guard.notNull(tempObject, "tempObject");
	}
}
