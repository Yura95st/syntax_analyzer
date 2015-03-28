package syntax_analyzer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import syntax_analyzer.Lexers.Abstract.IPrimitiveLanguageLexer;
import syntax_analyzer.Lexers.Concrete.PrimitiveLanguageLexer;
import syntax_analyzer.Models.Token;

public class Main
{
	private static IPrimitiveLanguageLexer _primitiveLanguageLexer;

	public static void main(String[] args)
	{
		try
		{
			Main.init();

			// Read all lines from sourceCode file.
			String source = Main.readAllLinesFromFile(args[0]);

			// Parse the list of tokens from the source.
			Main._primitiveLanguageLexer.setSource(source);

			List<Token> tokens = Main._primitiveLanguageLexer.parse();

			Main.printTokens(tokens);
		}
		catch (Exception exception)
		{
			System.err.println("Error occured:");
			System.err.println(exception);
		}
	}

	private static void init() throws Exception
	{
		Main._primitiveLanguageLexer = new PrimitiveLanguageLexer();
	}

	private static void printTokens(List<Token> tokens)
	{
		System.out.println(String.format("%1$s----- Tokens: -----%1$s",
			System.getProperty("line.separator")));

		for (Token token : tokens)
		{
			System.out.println(String.format("%1$s (%2$s)", token.getValue(),
				token.getKind().name()));
		}
	}

	private static String readAllLinesFromFile(String filePath)
		throws IOException
	{
		Path path = Paths.get(filePath);

		List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);

		StringBuilder stringBuilder = new StringBuilder();

		for (String line : lines)
		{
			stringBuilder.append(line);
			stringBuilder.append(System.getProperty("line.separator"));
		}

		String source = stringBuilder.toString().trim();

		return source;
	}
}
