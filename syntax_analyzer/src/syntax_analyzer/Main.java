package syntax_analyzer;

import grammar_parser.Enums.NodeKind;
import grammar_parser.Enums.SpecialNodeKind;
import grammar_parser.Lexers.Abstract.IGrammarLexer;
import grammar_parser.Lexers.Concrete.GrammarLexer;
import grammar_parser.Models.ControlTableItem;
import grammar_parser.Models.Grammar;
import grammar_parser.Models.Node;
import grammar_parser.Models.Rule;
import grammar_parser.Models.Word;
import grammar_parser.Parsers.Abstract.IGrammarParser;
import grammar_parser.Parsers.Concrete.GrammarParser;
import grammar_parser.Services.Abstract.IControlTableBuildingService;
import grammar_parser.Services.Abstract.IGrammarService;
import grammar_parser.Services.Concrete.ControlTableBuildingService;
import grammar_parser.Services.Concrete.GrammarService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import syntax_analyzer.Analyzers.Abstract.ISyntaxAnalyzer;
import syntax_analyzer.Analyzers.Concrete.SyntaxAnalyzer;
import syntax_analyzer.Lexers.Abstract.IPrimitiveLanguageLexer;
import syntax_analyzer.Lexers.Concrete.PrimitiveLanguageLexer;
import syntax_analyzer.Models.SyntaxAnalyzerResult;
import syntax_analyzer.Models.Token;

public class Main
{
	private static IControlTableBuildingService _controlTableBuildingService;

	private static IGrammarLexer _grammarLexer;

	private static IGrammarParser _grammarParser;

	private static IGrammarService _grammarService;

	private static IPrimitiveLanguageLexer _primitiveLanguageLexer;

	public static void main(String[] args)
	{
		try
		{
			Main.init();

			// Read all lines from grammar file.
			String source = Main.readAllLinesFromFile(args[0]);

			// Parse the list of nodes from the source.
			Main._grammarLexer.setSource(source);

			List<Node> nodes = Main._grammarLexer.parse();

			Main.printNodes(nodes);

			// Parse the grammar from the list of nodes.
			Main._grammarParser.setNodes(nodes);

			Grammar grammar = Main._grammarParser.parse();

			Main.printGrammar(grammar);

			// Get the rightRecursiveRules.
			Set<Rule> rightRecursiveRules =
				Main._grammarService.getRightRecursiveRules(grammar);

			Main.printRightRecursiveRules(rightRecursiveRules);

			// Get the firstSetDictionary.
			Map<Node, Set<Word>> firstSetDictionary =
				Main._grammarService.getFirstSetDictionary(grammar);

			Main.printFirstSetDictionary(firstSetDictionary);

			// Get the followSetDictionary.
			Map<Node, Set<Word>> followSetDictionary =
				Main._grammarService.getFollowSetDictionary(grammar);

			Main.printFollowSetDictionary(followSetDictionary);

			// Get the controlTable.
			Map<ControlTableItem, Rule> controlTable =
				Main._controlTableBuildingService.buildControlTable(grammar);

			Main.printControlTable(controlTable);

			// Read all lines from sourceCode file.
			source = Main.readAllLinesFromFile(args[1]);

			// Parse the list of tokens from the source.
			Main._primitiveLanguageLexer.setSource(source);

			List<Token> tokens = Main._primitiveLanguageLexer.parse();

			Main.printTokens(tokens);

			// Create SyntaxAnalyzer
			ISyntaxAnalyzer syntaxAnalyzer =
				new SyntaxAnalyzer(Main._controlTableBuildingService);

			syntaxAnalyzer.setGrammar(grammar);
			syntaxAnalyzer.setTokens(tokens);

			// Run the SyntaxAnalyzer
			SyntaxAnalyzerResult syntaxAnalyzerResult = syntaxAnalyzer.run();

			Main.printSyntaxAnalyzerResult(syntaxAnalyzerResult);
		}
		catch (Exception exception)
		{
			System.err.println("Error occured:");
			System.err.println(exception);
		}
	}

	private static void init() throws Exception
	{
		Main._grammarLexer = new GrammarLexer();

		Main._grammarParser = new GrammarParser();

		Main._grammarService = new GrammarService();

		Main._controlTableBuildingService =
			new ControlTableBuildingService(Main._grammarService);

		Main._primitiveLanguageLexer = new PrimitiveLanguageLexer();
	}

	private static String nodeToString(Node node)
	{
		StringBuilder stringBuilder = new StringBuilder();

		stringBuilder.append(String.format("%1$s (%2$s)", node.getText(), node
				.getKind().name()));

		String resultString = stringBuilder.toString();

		return resultString;
	}

	private static void printControlTable(
		Map<ControlTableItem, Rule> controlTable)
	{
		System.out.println(String.format("%1$s----- Table: -----%1$s",
			System.getProperty("line.separator")));

		for (Entry<ControlTableItem, Rule> entry : controlTable.entrySet())
		{
			ControlTableItem controlTableItem = entry.getKey();
			Rule rule = entry.getValue();

			Word word = controlTableItem.getWord();

			System.out.println(String.format("[ %1$s, %2$s ] -> %3$s",
				controlTableItem.getNode().getText(), Main.wordToString(word),
				Main.ruleToString(rule)));
		}
	}

	private static void printFirstOrFollowSetDictionary(
		Map<Node, Set<Word>> firstOrFollowSetDictionary, boolean isFirstSet)
	{
		System.out.println(String.format("%1$s----- %2$s: -----%1$s", System
				.getProperty("line.separator"), (isFirstSet ? "FIRST"
			: "FOLLOW")));

		for (Entry<Node, Set<Word>> entry : firstOrFollowSetDictionary
				.entrySet())
		{
			System.out.print(String.format("%1$s(%2$s) = { ",
				(isFirstSet ? "First" : "Follow"), entry.getKey().getText()));

			boolean isFirstWord = true;

			for (Word word : entry.getValue())
			{
				if (!isFirstWord)
				{
					System.out.print(", ");
				}
				else
				{
					isFirstWord = false;
				}

				System.out.print(Main.wordToString(word));
			}

			System.out.println(" }");
		}
	}

	private static void printFirstSetDictionary(
		Map<Node, Set<Word>> firstSetDictionary)
	{
		Main.printFirstOrFollowSetDictionary(firstSetDictionary, true);
	}

	private static void printFollowSetDictionary(
		Map<Node, Set<Word>> followSetDictionary)
	{
		Main.printFirstOrFollowSetDictionary(followSetDictionary, false);
	}

	private static void printGrammar(Grammar grammar)
	{
		System.out.println(String.format("%1$s----- Grammar: -----%1$s",
			System.getProperty("line.separator")));

		for (Rule rule : Main._grammarService.getAllRulesFromGrammar(grammar))
		{
			System.out.println(Main.ruleToString(rule));
		}
	}

	private static void printNodes(List<Node> nodes)
	{
		System.out.println(String.format("----- Nodes: -----%1$s",
			System.getProperty("line.separator")));

		for (Node node : nodes)
		{
			System.out.println(Main.nodeToString(node));
		}
	}

	private static void printRightRecursiveRules(Set<Rule> rightRecursiveRules)
	{
		System.out.println(String.format(
			"%1$s----- RightRecursive rules: -----%1$s",
			System.getProperty("line.separator")));

		if (rightRecursiveRules.size() == 0)
		{
			System.out
					.println("Grammar doesn't contain any rightRecursive rule.");
			return;
		}

		for (Rule rule : rightRecursiveRules)
		{
			System.out.println(Main.ruleToString(rule));
		}
	}

	private static void printSyntaxAnalyzerResult(
		SyntaxAnalyzerResult syntaxAnalyzerResult)
	{
		System.out.println(String.format(
			"----- SyntaxAnalyzerResult: -----%1$s",
			System.getProperty("line.separator")));

		if (syntaxAnalyzerResult.isSyntaxValid())
		{
			System.out.println("Syntax is valid.");
		}
		else
		{
			System.out.println("Syntax is invalid.");

			Token wrongToken = syntaxAnalyzerResult.getWrongToken();

			if (wrongToken != null)
			{
				System.out
						.println(String
								.format(
									"Error near the token with value \"%1$s\" and offset \"%2$s\".",
									wrongToken.getValue(), wrongToken
											.getLocation().getOffset()));
			}
		}

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

	private static String ruleToString(Rule rule)
	{
		Map<SpecialNodeKind, Node> specialNodesDictionary =
			Main._grammarParser.getSpecialNodesDictionary();

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

			if (node.getKind() == NodeKind.Terminal)
			{
				stringBuilder.append(String.format("%1$s%2$s%1$s", "\"", node.getText()));
			}
			else
			{
				stringBuilder.append(node.getText());
			}
		}

		stringBuilder.append(" ");
		stringBuilder.append(specialNodesDictionary.get(
			SpecialNodeKind.Termination).getText());

		String resultString = stringBuilder.toString();

		return resultString;
	}

	private static String wordToString(Word word)
	{
		StringBuilder stringBuilder = new StringBuilder();

		boolean isFirstNode = true;

		if (word.equals(Word.getEmptyWord()))
		{
			stringBuilder.append("<empty-word>");
		}
		else
		{
			for (Node node : word.getNodes())
			{
				if (!isFirstNode)
				{
					stringBuilder.append(", ");
				}
				else
				{
					isFirstNode = false;
				}

				stringBuilder.append(node.getText());
			}
		}

		String resultString = stringBuilder.toString();

		return resultString;
	}
}
