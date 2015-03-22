package grammar_parser.Services.Concrete;

import grammar_parser.Enums.NodeKind;
import grammar_parser.Models.Grammar;
import grammar_parser.Models.Node;
import grammar_parser.Models.Rule;
import grammar_parser.Models.Word;
import grammar_parser.Services.Abstract.IGrammarService;
import grammar_parser.Utils.Guard;

import java.util.AbstractMap;
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
		Guard.notNull(grammar, "grammar");
		Guard.notNull(rule, "rule");

		Map<Node, Set<Word>> firstSetsDictionary =
				new HashMap<Node, Set<Word>>();

		Stack<Entry<Rule, List<Node>>> entriesStack =
				new Stack<Entry<Rule, List<Node>>>();

		Set<Rule> visitedRules = new HashSet<Rule>();

		Word emptyWord = new Word();

		entriesStack.push(this.getEntryFromRule(rule));

		while (!entriesStack.isEmpty())
		{
			Entry<Rule, List<Node>> topEntry = entriesStack.peek();

			Rule topEntryRule = topEntry.getKey();
			List<Node> topEntryNodes = topEntry.getValue();
			Node topEntryHeadNode = topEntryRule.getHeadNode();

			firstSetsDictionary.putIfAbsent(topEntryHeadNode,
				new HashSet<Word>());

			if (visitedRules.contains(topEntryRule))
			{
				Node firstNode = topEntryNodes.get(0);

				if (firstNode.getKind() == NodeKind.Terminal)
				{
					// A = "b", C. => FIRST(A) = {"b"}
					topEntryNodes.clear();
				}
				else
				{
					Set<Word> words = firstSetsDictionary.get(firstNode);

					firstSetsDictionary.get(topEntryHeadNode).addAll(words);

					// A = B, C. => FIRST(A) = FIRST(B) U
					// (FIRST(C) if FIRST(B) contains empty word);
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
				// Mark rule as visited
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

					// Add word to the dictionary
					firstSetsDictionary.get(topEntryHeadNode).add(word);
				}
				else
				{
					for (Rule ruleToPush : grammar.getRules(firstNode))
					{
						// Prevent cycles.
						if (!this
								.isRuleInEntriesStack(ruleToPush, entriesStack))
						{
							entriesStack
							.push(this.getEntryFromRule(ruleToPush));
						}
					}
				}
			}
			else
			{
				// Add empty word to the dictionary if the rule is empty.
				if (topEntryRule.getNodes().isEmpty())
				{
					firstSetsDictionary.get(topEntryHeadNode).add(emptyWord);
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

	private Entry<Rule, List<Node>> getEntryFromRule(Rule rule)
	{
		Entry<Rule, List<Node>> entry =
				new AbstractMap.SimpleEntry<Rule, List<Node>>(rule, rule.getNodes());

		return entry;
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

	private boolean isRuleInEntriesStack(Rule rule,
		Stack<Entry<Rule, List<Node>>> entriesStack)
	{
		for (Entry<Rule, List<Node>> entry : entriesStack)
		{
			if (entry.getKey().equals(rule))
			{
				return true;
			}
		}

		return false;
	}
}
