package grammar_parser.Helpers.TableBuilder;

import grammar_parser.Helpers.TableBuilder.Models.TableBuilderSettings;
import grammar_parser.Utils.Guard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class TableBuilder implements ITableBuilder
{
	private final int _columnsCount;

	private final int[] _columnWidths;

	private final List<String[]> _content;

	private final int[] _contentColumnWidths;

	private final String[] _headerCaptions;

	private TableBuilderSettings _settings;

	public TableBuilder(int columnsCount, TableBuilderSettings settings)
	{
		Guard.moreThanZero(columnsCount, "columnsCount");
		Guard.notNull(settings, "settings");

		this._columnsCount = columnsCount;
		this._settings = settings;

		this._headerCaptions = new String[columnsCount];
		this._content = new ArrayList<String[]>();

		this._columnWidths = new int[columnsCount];
		this._contentColumnWidths = new int[columnsCount];

		this.resetTableRow(this._headerCaptions);
	}

	@Override
	public String buildTable()
	{
		this.updateColumnWidths();

		StringBuilder stringBuilder = new StringBuilder();

		stringBuilder.append(this.getTableHeader());

		if (this._content.size() > 0)
		{
			for (String[] tableRow : this._content)
			{
				stringBuilder.append(System.lineSeparator());
				stringBuilder.append(this.getTableRow(tableRow));
			}

			stringBuilder.append(System.lineSeparator());
			stringBuilder.append(this.getTableLine());
		}

		return stringBuilder.toString();
	}

	@Override
	public List<List<String>> getContent()
	{
		List<List<String>> result = new ArrayList<List<String>>();

		for (String[] tableRow : this._content)
		{
			result.add(Arrays.asList(tableRow));
		}

		return result;
	}

	@Override
	public List<String> getHeaderCaptions()
	{
		return Arrays.asList(this._headerCaptions);
	}

	@Override
	public void setContent(Iterable<Iterable<String>> content)
	{
		Guard.notNull(content, "content");

		this.resetContent();

		for (Iterable<String> row : content)
		{
			String[] tableRow = new String[this._columnsCount];

			this.resetTableRow(tableRow);

			if (row != null)
			{
				int i = 0;
				Iterator<String> iterator = row.iterator();

				while (i < this._columnsCount)
				{
					if (iterator.hasNext())
					{
						String cellContent = iterator.next();

						if (cellContent != null)
						{
							tableRow[i] = cellContent;
						}
					}

					this._contentColumnWidths[i] =
							Math.max(this._contentColumnWidths[i],
								tableRow[i].length());

					i++;
				}

				this._content.add(tableRow);
			}
		}
	}

	@Override
	public void setHeaderCaptions(Iterable<String> headerCaptions)
	{
		Guard.notNull(headerCaptions, "headerCaptions");

		// Reset header captions
		this.resetTableRow(this._headerCaptions);

		int i = 0;
		Iterator<String> iterator = headerCaptions.iterator();

		while (i < this._columnsCount && iterator.hasNext())
		{
			String caption = iterator.next();

			if (caption != null)
			{
				this._headerCaptions[i] = caption;
			}

			i++;
		}
	}

	@Override
	public void setSettings(TableBuilderSettings settings)
	{
		Guard.notNull(settings, "settings");

		this._settings = settings;
	}

	private String getTableCell(int columnIndex, String cellContent)
	{
		StringBuilder stringBuilder = new StringBuilder();

		if (columnIndex == 0)
		{
			stringBuilder.append(this._settings.getCellDelimiterCharacter());
		}

		stringBuilder.append(this._settings.getSpaceCharacter());
		stringBuilder.append(cellContent);

		int numberOfSpacesToAppend =
				this._columnWidths[columnIndex] - cellContent.length();

		for (int i = 0; i < numberOfSpacesToAppend; i++)
		{
			stringBuilder.append(this._settings.getSpaceCharacter());
		}

		stringBuilder.append(this._settings.getSpaceCharacter());
		stringBuilder.append(this._settings.getCellDelimiterCharacter());

		return stringBuilder.toString();
	}

	private String getTableHeader()
	{
		StringBuilder stringBuilder = new StringBuilder();

		stringBuilder.append(this.getTableLine());

		stringBuilder.append(System.lineSeparator());

		for (int i = 0; i < this._columnsCount; i++)
		{
			stringBuilder.append(this.getTableCell(i, this._headerCaptions[i]));
		}

		stringBuilder.append(System.lineSeparator());

		stringBuilder.append(this.getTableLine());

		return stringBuilder.toString();
	}

	private String getTableLine()
	{
		StringBuilder stringBuilder = new StringBuilder();

		int tableWidth = 4 + 3 * (this._columnsCount - 1);

		for (int maxColumnWidth : this._columnWidths)
		{
			tableWidth += maxColumnWidth;
		}

		for (int i = 0; i < tableWidth; i++)
		{
			stringBuilder.append(this._settings.getLineCharacter());
		}

		return stringBuilder.toString();
	}

	private String getTableRow(String[] tableRow)
	{
		StringBuilder stringBuilder = new StringBuilder();

		for (int i = 0; i < this._columnsCount; i++)
		{
			stringBuilder.append(this.getTableCell(i, tableRow[i]));
		}

		return stringBuilder.toString();
	}

	private void resetContent()
	{
		this._content.clear();

		// Clear the content column widths
		Arrays.fill(this._contentColumnWidths, 0);
	}

	private void resetTableRow(String[] tableRow)
	{
		Arrays.fill(tableRow, this._settings.getDefaultCellContent());
	}

	private void updateColumnWidths()
	{
		for (int i = 0; i < this._columnsCount; i++)
		{
			this._columnWidths[i] =
					Math.max(this._contentColumnWidths[i],
						this._headerCaptions[i].length());
		}
	}
}