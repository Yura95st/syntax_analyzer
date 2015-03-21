package grammar_parser.Services.Concrete;

import grammar_parser.Enums.NodeKind;
import grammar_parser.Models.Grammar;
import grammar_parser.Models.Node;
import grammar_parser.Models.Rule;
import grammar_parser.Models.Word;
import grammar_parser.Services.Abstract.IGrammarService;
import grammar_parser.Utils.Guard;

import java.util.AbstractMap;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Stack;

public class GrammarService implements IGrammarService
{
	@Override
	public Set<Word> getFirstSet(Grammar grammar, Rule rule) throws Exception
	{
		Map<Node, Set<Word>> firstSetsDictionary = new HashMap<Node, Set<Word>>();
		
		Stack<Entry<Rule, List<Node>>> entriesStack = new Stack<Entry<Rule, List<Node>>>();

		Set<Rule> visitedRules = new HashSet<Rule>();

		Word emptyWord = new Word();

		entriesStack.add(new AbstractMap.SimpleEntry<Rule, List<Node>>(rule,
			rule.getNodes()));

		while (!entriesStack.isEmpty())
		{
			Entry<Rule, List<Node>> topEntry = entriesStack.peek();

			Rule topEntryRule = topEntry.getKey();
			List<Node> topEntryNodes = topEntry.getValue();
			
			firstSetsDictionary.putIfAbsent(topEntryRule.getHeadNode(),
				new HashSet<Word>());

			if (visitedRules.contains(topEntryRule))
			{
				Node firstNode = topEntryNodes.get(0);

				if (firstNode.getKind() == NodeKind.Terminal)
				{
					topEntryNodes.clear();
				}
				else
				{
					Set<Word> words = firstSetsDictionary.get(firstNode);

					firstSetsDictionary.get(topEntryRule.getHeadNode()).addAll(
						words);

					if (!words.contains(emptyWord))
					{
						topEntryNodes.clear();
					}
					else
					{
						topEntryNodes.remove(firstNode);
					}
				}
			}
			else
			{
				visitedRules.add(topEntryRule);
			}

			if (!topEntryNodes.isEmpty())
			{
				// Get the first node of the rule.
				Node firstNode = topEntryNodes.get(0);

				if (firstNode.getKind() == NodeKind.Terminal)
				{
					Word word = new Word();

					word.setNodes(new ArrayList<Node>() {
						{
							this.add(firstNode);
						}
					});
					
					firstSetsDictionary.get(topEntryRule.getHeadNode()).add(
						word);
				}
				else
				{
					for (Rule ruleToStack : grammar.getRules(firstNode))
					{
						boolean ruleIsInStack = false;
						
						for (Entry<Rule, List<Node>> entry : entriesStack)
						{
							if (entry.getKey().equals(ruleToStack))
							{
								ruleIsInStack = true;
								break;
							}
						}
						
						// Prevent cycles.
						if (!ruleIsInStack)
						{
							SimpleEntry<Rule, List<Node>> entry = new AbstractMap.SimpleEntry<Rule, List<Node>>(
								ruleToStack, ruleToStack.getNodes());

							entriesStack.add(entry);
						}
					}
				}
			}
			else
			{
				// Add empty word if rule is empty.
				if (topEntryRule.getNodes().isEmpty())
				{
					firstSetsDictionary.get(topEntryRule.getHeadNode()).add(
						emptyWord);
				}

				entriesStack.pop();
			}
		}

		Set<Word> firstSet = firstSetsDictionary.get(rule.getHeadNode());
		
		return firstSet;
	}

	@Override
	public Set<Rule> getRightRecursiveRules(Grammar grammar)
	{
		Guard.notNull(grammar, "grammar");

		Set<Rule> rightRecursiveRules = new HashSet<Rule>();

		for (List<Rule> rules : grammar.getRulesDictionary().values())
		{
			for (Rule rule : rules)
			{
				if (this.isRightRecursiveRule(rule, grammar))
				{
					rightRecursiveRules.add(rule);
				}
			}
		}

		return rightRecursiveRules;
	}

	private boolean isRightRecursiveRule(Rule rule, Grammar grammar)
	{
		Stack<Rule> rulesStack = new Stack<Rule>();
		Set<Rule> poppedRules = new HashSet<Rule>();

		// <node, value>:
		// value is "true" - exists at least one empty rule (epsilon-rule), that
		// starts from node.
		// value is "false" - all the rules, that start with the node are not
		// empty.
		Map<Node, Boolean> ruleHeadNodesMarks = new HashMap<Node, Boolean>();

		rulesStack.add(rule);

		while (!rulesStack.isEmpty())
		{
			Rule topRule = rulesStack.peek();

			// Prevent cycles.
			if (poppedRules.contains(topRule))
			{
				continue;
			}

			List<Node> nodes = topRule.getNodes();

			List<Rule> rulesToPush = new ArrayList<Rule>();

			int emptyHeadNodesCount = 0;

			// Start to scan nodes from the right side.
			for (int i = nodes.size() - 1; i >= 0; i--)
			{
				Node node = nodes.get(i);

				Boolean nodeMark = ruleHeadNodesMarks.get(node);

				if (nodeMark == null)
				{
					if (node.getKind() == NodeKind.Nonterminal)
					{
						// Right recursion is reached.
						if (node.equals(rule.getHeadNode()))
						{
							return true;
						}

						for (Rule ruleToPush : grammar.getRules(node))
						{
							if (!rulesStack.contains(ruleToPush)
								&& !poppedRules.contains(ruleToPush))
							{
								rulesToPush.add(ruleToPush);
							}
						}
					}
					else
					{
						// Mark the node.
						ruleHeadNodesMarks.putIfAbsent(topRule.getHeadNode(),
							false);
					}
				}
				else if (nodeMark)
				{
					emptyHeadNodesCount++;
					continue;
				}

				break;
			}

			if (nodes.size() == emptyHeadNodesCount)
			{
				// Rule is empty (epsilon-rule).
				ruleHeadNodesMarks.put(topRule.getHeadNode(), true);
			}

			if (rulesToPush.size() == 0)
			{
				Rule poppedRule = rulesStack.pop();

				// Mark the rule as popped.
				poppedRules.add(poppedRule);
			}
			else
			{
				for (Rule ruleToPush : rulesToPush)
				{
					rulesStack.push(ruleToPush);
				}
			}
		}

		return false;
	}
}
