package syntax_analyzer;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import syntax_analyzer.Enums.SpecialNodeKind;
import syntax_analyzer.Helpers.ExceptionHelper;
import syntax_analyzer.Lexers.Abstract.IGrammarLexer;
import syntax_analyzer.Lexers.Concrete.GrammarLexer;
import syntax_analyzer.Models.Grammar;
import syntax_analyzer.Models.Node;
import syntax_analyzer.Models.Rule;
import syntax_analyzer.Parsers.Abstract.IGrammarParser;
import syntax_analyzer.Parsers.Concrete.GrammarParser;
import syntax_analyzer.Utils.Guard;

public class Main
{
	public static String getStringFromGrammar(Grammar grammar,
		Map<SpecialNodeKind, Node> specialNodesDictionary)
	{
		Guard.notNull(grammar, "grammar");
		Guard.notNull(specialNodesDictionary, "specialNodesDictionary");

		StringBuilder stringBuilder = new StringBuilder();

		for (List<Rule> rules : grammar.getRulesDictionary().values())
		{
			for (Rule rule : rules)
			{
				stringBuilder.append(rule.getHeadNode().getText());
				stringBuilder.append(" ");
				stringBuilder.append(specialNodesDictionary.get(
					SpecialNodeKind.Definition).getText());

				boolean isFirstNode = true;

				for (Node node : rule.getNodes())
				{
					if (!isFirstNode)
					{
						stringBuilder.append(specialNodesDictionary.get(
							SpecialNodeKind.Concatenation).getText());
					}
					else
					{
						isFirstNode = false;
					}

					stringBuilder.append(" ");
					stringBuilder.append(node.getText());
				}

				stringBuilder.append(" ");
				stringBuilder.append(specialNodesDictionary.get(
					SpecialNodeKind.Termination).getText());

				stringBuilder.append(System.lineSeparator());
				stringBuilder.append(System.lineSeparator());
			}
		}

		String string = stringBuilder.toString();

		return string;
	}

	public static String getStringFromNodes(List<Node> nodes)
	{
		Guard.notNull(nodes, "nodes");

		StringBuilder stringBuilder = new StringBuilder();

		for (Node node : nodes)
		{
			stringBuilder.append(String.format("%1$s (%2$s)", node.getText(),
				node.getKind().name()));
			stringBuilder.append(System.lineSeparator());
		}

		String result = stringBuilder.toString();

		return result;
	}

	public static void main(String[] args)
	{
		IGrammarLexer grammarLexer = new GrammarLexer();

		try
		{
			Path path = Paths.get(args[0]);

			List<String> lines = Files.readAllLines(path,
				StandardCharsets.UTF_8);

			StringBuilder stringBuilder = new StringBuilder();

			for (String line : lines)
			{
				stringBuilder.append(line);
				stringBuilder.append(System.getProperty("line.separator"));
			}

			String source = stringBuilder.toString().trim();

			grammarLexer.setSource(source);

			List<Node> nodes = grammarLexer.parse();

			System.out.println("----- Nodes: -----");
			System.out.println();
			System.out.println(Main.getStringFromNodes(nodes));

			IGrammarParser grammarParser = new GrammarParser();

			grammarParser.setNodes(nodes);

			Grammar grammar = grammarParser.parse();

			System.out.println("----- Grammar: -----");
			System.out.println();
			System.out.println(Main.getStringFromGrammar(grammar,
				grammarParser.getSpecialNodesDictionary()));
		}
		catch (Exception exception)
		{
			System.out.println("Error occured:");
			System.out.println(ExceptionHelper
				.getFullExceptionMessage(exception));
		}
	}
}
