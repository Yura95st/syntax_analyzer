package grammar_parser.Helpers.TableBuilder;

import grammar_parser.Helpers.TableBuilder.Models.TableBuilderSettings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TableBuilderTest
{
	private TableBuilderSettings _tableBuilderSettings;

	@Test
	public void setContent_ContentContainsNullCellValues_ReplacesNullValuesWithDefaultCellValue()
	{
		// Arrange
		String[] tableRow = new String[] {
			"A", null, "C"
		};

		int columnsCount = tableRow.length;

		List<Iterable<String>> newContent = new ArrayList<Iterable<String>>();

		newContent.add(Arrays.asList(tableRow));

		// Arrange - create target
		ITableBuilder target =
			new TableBuilder(columnsCount, this._tableBuilderSettings);

		// Act
		target.setContent(newContent);

		// Assert
		List<List<String>> content = target.getContent();

		Assert.assertEquals(newContent.size(), content.size());

		for (int i = 0, count = content.size(); i < count; i++)
		{
			List<String> contentRow = content.get(i);

			Assert.assertEquals(columnsCount, contentRow.size());

			int j = 0;

			for (String cellContent : newContent.get(i))
			{
				cellContent =
					cellContent == null ? this._tableBuilderSettings
							.getDefaultCellContent() : cellContent;

				Assert.assertEquals(cellContent, contentRow.get(j));

				j++;
			}
		}
	}

	@Test
	public void setContent_ContentListContainsNullRows_SkipsNullRows()
	{
		// Arrange
		String[] tableRow = new String[] {
			"A", "B", "C"
		};

		List<Iterable<String>> newContent = new ArrayList<Iterable<String>>();

		newContent.add(Arrays.asList(tableRow));
		newContent.add(null);
		newContent.add(Arrays.asList(tableRow));

		int columnsCount = tableRow.length;

		// Arrange - create target
		ITableBuilder target =
			new TableBuilder(columnsCount, this._tableBuilderSettings);

		// Act
		target.setContent(newContent);

		// Assert
		List<List<String>> content = target.getContent();

		Assert.assertEquals(newContent.size() - 1, content.size());

		int i = 0;
		for (Iterable<String> row : newContent)
		{
			if (row != null)
			{
				int j = 0;
				Iterator<String> iterator = row.iterator();

				while (j < columnsCount && iterator.hasNext())
				{
					Assert.assertEquals(iterator.next(), content.get(i).get(j));
					j++;
				}

				i++;
			}
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void setContent_ContentListIsNull_ThrowsIllegalArgumentException()
	{
		// Arrange - create target
		ITableBuilder target = new TableBuilder(1, this._tableBuilderSettings);

		// Act
		target.setContent(null);
	}

	@Test
	public void setContent_ContentRowsSizeIsGreaterThanColumnsCount_IgnoresExtraCells()
	{
		// Arrange
		String[] tableRow = new String[] {
			"A", "B", "C"
		};

		int columnsCount = tableRow.length - 1;

		List<Iterable<String>> newContent = new ArrayList<Iterable<String>>();

		newContent.add(Arrays.asList(tableRow));
		newContent.add(Arrays.asList(Arrays.copyOfRange(tableRow, 0,
			columnsCount)));

		// Arrange - create target
		ITableBuilder target =
			new TableBuilder(columnsCount, this._tableBuilderSettings);

		// Act
		target.setContent(newContent);

		// Assert
		List<List<String>> content = target.getContent();

		Assert.assertEquals(newContent.size(), content.size());

		for (int i = 0, count = content.size(); i < count; i++)
		{
			List<String> contentRow = content.get(i);

			Assert.assertEquals(columnsCount, contentRow.size());

			int j = 0;
			Iterator<String> iterator = newContent.get(i).iterator();

			while (j < columnsCount && iterator.hasNext())
			{
				Assert.assertEquals(iterator.next(), contentRow.get(j));
				j++;
			}
		}
	}

	@Test
	public void setContent_ContentRowsSizeIsLessThanColumnsCount_SetsMissingCellsAsNull()
	{
		// Arrange
		String[] tableRow = new String[] {
			"A", "B", "C"
		};

		int columnsCount = tableRow.length + 1;

		List<Iterable<String>> newContent = new ArrayList<Iterable<String>>();

		newContent.add(Arrays.asList(tableRow));
		newContent.add(Arrays.asList(Arrays.copyOfRange(tableRow, 0,
			tableRow.length - 1)));

		// Arrange - create target
		ITableBuilder target =
			new TableBuilder(columnsCount, this._tableBuilderSettings);

		// Act
		target.setContent(newContent);

		// Assert
		List<List<String>> content = target.getContent();

		Assert.assertEquals(newContent.size(), content.size());

		for (int i = 0, count = content.size(); i < count; i++)
		{
			List<String> contentRow = content.get(i);

			Assert.assertEquals(columnsCount, contentRow.size());

			int j = 0;

			for (String cellContent : newContent.get(i))
			{
				Assert.assertEquals(cellContent, contentRow.get(j));

				j++;
			}

			for (; j < columnsCount; j++)
			{
				Assert.assertEquals(
					this._tableBuilderSettings.getDefaultCellContent(),
					contentRow.get(j));
			}
		}
	}

	@Test
	public void setHeaderCaptions_HeaderCaptionsContainsNullValues_ReplacesNullValuesWithDefaultCellValue()
	{
		// Arrange
		String[] newHeaderCaptions = {
			"A", null, "C"
		};

		int columnCount = newHeaderCaptions.length;

		// Arrange - create target
		ITableBuilder target =
			new TableBuilder(columnCount, this._tableBuilderSettings);

		// Act
		target.setHeaderCaptions(Arrays.asList(newHeaderCaptions));

		// Assert
		List<String> headerCaptions = target.getHeaderCaptions();

		Assert.assertEquals(columnCount, headerCaptions.size());

		for (int i = 0, count = columnCount; i < count; i++)
		{
			String expectedCaption =
				newHeaderCaptions[i] == null ? this._tableBuilderSettings
						.getDefaultCellContent() : newHeaderCaptions[i];

			Assert.assertEquals(expectedCaption, headerCaptions.get(i));
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void setHeaderCaptions_HeaderCaptionsListIsNull_ThrowsIllegalArgumentException()
	{
		// Arrange - create target
		ITableBuilder target = new TableBuilder(1, this._tableBuilderSettings);

		// Act
		target.setHeaderCaptions(null);
	}

	@Test
	public void setHeaderCaptions_HeaderCaptionsListSizeIsGreaterThanColumnsCount_IgnoresExtraCaptions()
	{
		// Arrange
		String[] newHeaderCaptions = {
			"A", "B", "C"
		};

		int columnCount = newHeaderCaptions.length - 1;

		// Arrange - create target
		ITableBuilder target =
			new TableBuilder(columnCount, this._tableBuilderSettings);

		// Act
		target.setHeaderCaptions(Arrays.asList(newHeaderCaptions));

		// Assert
		List<String> headerCaptions = target.getHeaderCaptions();

		Assert.assertEquals(columnCount, headerCaptions.size());

		for (int i = 0; i < columnCount; i++)
		{
			Assert.assertEquals(newHeaderCaptions[i], headerCaptions.get(i));
		}
	}

	@Test
	public void setHeaderCaptions_HeaderCaptionsListSizeIsLessThanColumnsCount_InitsMissingCaptionsWithDefaultCellValue()
	{
		// Arrange
		String[] newHeaderCaptions = {
			"A", "B", "C"
		};

		int columnCount = newHeaderCaptions.length + 3;

		// Arrange - create target
		ITableBuilder target =
			new TableBuilder(columnCount, this._tableBuilderSettings);

		// Act
		target.setHeaderCaptions(Arrays.asList(newHeaderCaptions));

		// Assert
		List<String> headerCaptions = target.getHeaderCaptions();

		Assert.assertEquals(columnCount, headerCaptions.size());

		for (int i = 0, count = newHeaderCaptions.length; i < count; i++)
		{
			Assert.assertEquals(newHeaderCaptions[i], headerCaptions.get(i));
		}

		for (int i = newHeaderCaptions.length; i < columnCount; i++)
		{
			Assert.assertEquals(
				this._tableBuilderSettings.getDefaultCellContent(),
				headerCaptions.get(i));
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void setSettings_SettingsAreNull_ThrowsIllegalArgumentException()
	{
		// Arrange - create target
		ITableBuilder target = new TableBuilder(1, this._tableBuilderSettings);

		// Act
		target.setSettings(null);
	}

	@Before
	public void setUp() throws Exception
	{
		this._tableBuilderSettings = new TableBuilderSettings();
	}
}