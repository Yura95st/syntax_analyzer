package grammar_parser;

import grammar_parser.Enums.SpecialNodeKind;
import grammar_parser.Helpers.ExceptionHelper;
import grammar_parser.Lexers.Abstract.IGrammarLexer;
import grammar_parser.Lexers.Concrete.GrammarLexer;
import grammar_parser.Models.Grammar;
import grammar_parser.Models.Node;
import grammar_parser.Models.Rule;
import grammar_parser.Parsers.Abstract.IGrammarParser;
import grammar_parser.Parsers.Concrete.GrammarParser;
import grammar_parser.Services.Abstract.IGrammarService;
import grammar_parser.Services.Concrete.GrammarService;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Main
{
	public static void main(String[] args)
	{
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

			IGrammarLexer grammarLexer = new GrammarLexer();

			grammarLexer.setSource(source);

			List<Node> nodes = grammarLexer.parse();

			System.out.println(String.format("----- Nodes: -----%1$s",
				System.getProperty("line.separator")));

			for (Node node : nodes)
			{
				System.out.println(Main.nodeToString(node));
			}

			IGrammarParser grammarParser = new GrammarParser();

			grammarParser.setNodes(nodes);

			Grammar grammar = grammarParser.parse();

			System.out.println(String.format("%1$s----- Grammar: -----%1$s",
				System.getProperty("line.separator")));

			Map<SpecialNodeKind, Node> specialNodesDictionary = grammarParser
					.getSpecialNodesDictionary();

			for (List<Rule> rules : grammar.getRulesDictionary().values())
			{
				for (Rule rule : rules)
				{
					System.out.println(Main.ruleToString(rule,
						specialNodesDictionary));
				}
			}

			IGrammarService grammarService = new GrammarService();

			System.out.println(String.format(
				"%1$s----- RightRecursive rules: -----%1$s",
				System.getProperty("line.separator")));

			Set<Rule> rightRecursiveRules = grammarService
					.getRightRecursiveRules(grammar);

			if (rightRecursiveRules.size() == 0)
			{
				System.out
				.println("Grammar doesn't contain any rightRecursive rule.");
			}
			else
			{
				for (Rule rule : rightRecursiveRules)
				{
					System.out.println(Main.ruleToString(rule,
						specialNodesDictionary));
				}
			}
		}
		catch (Exception exception)
		{
			System.out.println("Error occured:");
			System.out.println(ExceptionHelper
					.getFullExceptionMessage(exception));
		}
	}

	static String nodeToString(Node node)
	{
		StringBuilder stringBuilder = new StringBuilder();

		stringBuilder.append(String.format("%1$s (%2$s)", node.getText(), node
				.getKind().name()));

		String resultString = stringBuilder.toString();

		return resultString;
	}

	static String ruleToString(Rule rule,
		Map<SpecialNodeKind, Node> specialNodesDictionary)
	{
		StringBuilder stringBuilder = new StringBuilder();

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

		String resultString = stringBuilder.toString();

		return resultString;
	}
}
