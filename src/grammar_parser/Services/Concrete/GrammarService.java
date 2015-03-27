package grammar_parser.Services.Concrete;

import grammar_parser.Enums.NodeKind;
import grammar_parser.Exceptions.NodeIsNotTerminalException;
import grammar_parser.Exceptions.NonexistentNodeException;
import grammar_parser.Models.Grammar;
import grammar_parser.Models.Node;
import grammar_parser.Models.Rule;
import grammar_parser.Models.Word;
import grammar_parser.Services.Abstract.IGrammarService;
import grammar_parser.Utils.Guard;
import grammar_parser.Utils.SetUtils;

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
	public List<Rule> getAllRulesFromGrammar(Grammar grammar)
	{
		Guard.notNull(grammar, "grammar");

		List<Rule> rulesList = new ArrayList<Rule>();

		for (List<Rule> rules : grammar.getRulesDictionary().values())
		{
			for (Rule rule : rules)
			{
				rulesList.add(rule);
			}
		}

		return rulesList;
	}

	@Override
	public Set<Word> getFirstPlusFollowSet(Set<Word> firstSet,
		Set<Word> followSet)
	{
		Guard.notNull(firstSet, "firstSet");
		Guard.notNull(followSet, "followSet");

		Set<Word> firstPlusFollowSet = new HashSet<Word>(firstSet);

		if (firstPlusFollowSet.contains(Word.getEmptyWord()))
		{
			firstPlusFollowSet.addAll(followSet);
		}

		return firstPlusFollowSet;
	}

	@Override
	public Map<Node, Set<Word>> getFirstSetDictionary(Grammar grammar)
		throws Exception
	{
		Guard.notNull(grammar, "grammar");

		Map<Node, Set<Word>> firstSetDictionary =
			new HashMap<Node, Set<Word>>();

		// Set firstSet as empty set for each nonterminal node
		for (Node nonterminalNode : this.getNonterminalNodes(grammar))
		{
			firstSetDictionary.put(nonterminalNode, new HashSet<Word>());
		}

		Set<Rule> visitedRules = new HashSet<Rule>();

		for (Rule rule : this.getAllRulesFromGrammar(grammar))
		{
			this.processRuleFirstSet(rule, grammar, firstSetDictionary,
				visitedRules);
		}

		return firstSetDictionary;
	}

	@Override
	public Set<Word> getFirstSetForNodesList(List<Node> nodes,
		Map<Node, Set<Word>> firstSetDictionary) throws Exception
	{
		Guard.notNull(nodes, "nodes");
		Guard.notNull(firstSetDictionary, "firstSetDictionary");

		Set<Word> firstSet = new HashSet<Word>();

		for (Node node : nodes)
		{
			if (node.getKind() == NodeKind.Terminal)
			{
				Word word = new Word(node);

				firstSet.add(word);

				break;
			}
			else
			{
				Set<Word> words = firstSetDictionary.get(node);

				if (words == null)
				{
					throw new NonexistentNodeException(
						String.format(
							"Node with text '%1$s' doesn't exist in firstSetDictionary as a key.",
							node.getText()));
				}

				firstSet.addAll(words);

				if (!words.contains(Word.getEmptyWord()))
				{
					break;
				}
			}
		}

		return firstSet;
	}

	@Override
	public Map<Node, Set<Word>> getFollowSetDictionary(Grammar grammar)
		throws Exception
	{
		Guard.notNull(grammar, "grammar");

		Map<Node, Set<Word>> followSetDictionary =
			new HashMap<Node, Set<Word>>();

		// Set followSet as empty set for each nonterminal node
		for (Node nonterminalNode : this.getNonterminalNodes(grammar))
		{
			followSetDictionary.put(nonterminalNode, new HashSet<Word>());
		}

		Rule headRule = grammar.getHeadRule();

		if (headRule != null)
		{
			// Add empty word to the followSet of the headRule's headNode
			followSetDictionary.get(headRule.getHeadNode()).add(
				Word.getEmptyWord());
		}

		Map<Node, Set<Word>> firstSetDictionary =
			this.getFirstSetDictionary(grammar);

		List<Rule> rules = this.getAllRulesFromGrammar(grammar);

		boolean continueTheProcess = true;

		while (continueTheProcess)
		{
			continueTheProcess = false;

			for (Rule rule : rules)
			{
				boolean followSetIsChanged =
					this.processRuleFollowSet(rule, grammar,
						firstSetDictionary, followSetDictionary);

				if (!continueTheProcess && followSetIsChanged)
				{
					continueTheProcess = true;
				}
			}
		}

		return followSetDictionary;
	}

	@Override
	public Set<Node> getNonterminalNodes(Grammar grammar)
	{
		Guard.notNull(grammar, "grammar");

		return this
				.getNodesFromGrammarByNodeKind(grammar, NodeKind.Nonterminal);
	}

	@Override
	public Set<Rule> getRightRecursiveRules(Grammar grammar)
	{
		Guard.notNull(grammar, "grammar");

		Set<Rule> rightRecursiveRules = new HashSet<Rule>();

		// <node, value>:
		// value is "true" - exists at least one empty rule (epsilon-rule),
		// that starts from node.
		// value is "false" - all the rules, that start with the node
		// are not empty.
		Map<Node, Boolean> ruleHeadNodesMarks = new HashMap<Node, Boolean>();

		for (Rule rule : this.getAllRulesFromGrammar(grammar))
		{
			if (this.isRightRecursiveRule(rule, grammar, ruleHeadNodesMarks))
			{
				rightRecursiveRules.add(rule);
			}
		}

		return rightRecursiveRules;
	}

	@Override
	public Set<Node> getTerminalNodes(Grammar grammar)
	{
		Guard.notNull(grammar, "grammar");

		return this.getNodesFromGrammarByNodeKind(grammar, NodeKind.Terminal);
	}

	@Override
	public boolean isLLOneGrammar(Grammar grammar,
		Map<Node, Set<Word>> firstSetDictionary,
		Map<Node, Set<Word>> followSetDictionary) throws Exception
	{
		Guard.notNull(grammar, "grammar");
		Guard.notNull(firstSetDictionary, "firstSetDictionary");
		Guard.notNull(followSetDictionary, "followSetDictionary");

		Map<Node, List<Rule>> rulesDictionary = grammar.getRulesDictionary();

		for (Entry<Node, List<Rule>> entry : rulesDictionary.entrySet())
		{
			Node node = entry.getKey();
			List<Rule> rules = entry.getValue();

			int rulesListSize = rules.size();

			if (rulesListSize >= 2)
			{
				Set<Word> followSet = followSetDictionary.get(node);

				if (followSet == null)
				{
					throw new NonexistentNodeException(
						String.format(
							"Node with text '%1$s' doesn't exist in followSetDictionary as a key.",
							node.getText()));
				}

				for (int i = 0; i < rulesListSize; i++)
				{
					Rule ruleOne = rules.get(i);

					Set<Word> firstSetOne =
						this.getFirstSetForNodesList(ruleOne.getNodes(),
							firstSetDictionary);

					for (int j = 0; j < rulesListSize; j++)
					{
						if (j == i)
						{
							continue;
						}

						Rule ruleTwo = rules.get(j);

						Set<Word> firstSetTwo =
							this.getFirstSetForNodesList(ruleTwo.getNodes(),
								firstSetDictionary);

						if (!SetUtils.setsDoNotIntersect(firstSetOne,
							firstSetTwo))
						{
							return false;
						}

						if (firstSetOne.contains(Word.getEmptyWord()))
						{
							if (!SetUtils.setsDoNotIntersect(firstSetTwo,
								followSet))
							{
								return false;
							}
						}
					}
				}
			}
		}

		return true;
	}

	private Entry<Rule, List<Node>> getEntryFromRule(Rule rule)
	{
		Entry<Rule, List<Node>> entry =
			new AbstractMap.SimpleEntry<Rule, List<Node>>(rule, rule.getNodes());

		return entry;
	}

	private Set<Node> getNodesFromGrammarByNodeKind(Grammar grammar,
		NodeKind nodeKind)
	{
		Set<Node> nonterminalNodes = new HashSet<Node>();

		for (Rule rule : this.getAllRulesFromGrammar(grammar))
		{
			Node headNode = rule.getHeadNode();

			if (headNode.getKind() == nodeKind)
			{
				nonterminalNodes.add(headNode);
			}

			for (Node node : rule.getNodes())
			{
				if (node.getKind() == nodeKind)
				{
					nonterminalNodes.add(node);
				}
			}
		}

		return nonterminalNodes;
	}

	private boolean isRightRecursiveRule(Rule rule, Grammar grammar,
		Map<Node, Boolean> ruleHeadNodesMarks)
	{
		Stack<Rule> rulesStack = new Stack<Rule>();
		Set<Rule> poppedRules = new HashSet<Rule>();

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

				// Right recursion is reached.
				if (node.equals(rule.getHeadNode()))
				{
					return true;
				}

				Boolean nodeMark = ruleHeadNodesMarks.get(node);

				if (nodeMark == null)
				{
					if (node.getKind() == NodeKind.Nonterminal)
					{
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

	private void processRuleFirstSet(Rule ruleToProcess, Grammar grammar,
		Map<Node, Set<Word>> firstSetDictionary, Set<Rule> visitedRules)
		throws NodeIsNotTerminalException
	{
		Stack<Entry<Rule, List<Node>>> entriesStack =
			new Stack<Entry<Rule, List<Node>>>();

		entriesStack.push(this.getEntryFromRule(ruleToProcess));

		while (!entriesStack.isEmpty())
		{
			Entry<Rule, List<Node>> topEntry = entriesStack.peek();

			Rule topEntryRule = topEntry.getKey();
			List<Node> topEntryNodes = topEntry.getValue();

			Node topEntryHeadNode = topEntryRule.getHeadNode();

			Set<Word> firstSet = firstSetDictionary.get(topEntryHeadNode);

			if (visitedRules.contains(topEntryRule) && !topEntryNodes.isEmpty())
			{
				Node firstNode = topEntryNodes.get(0);

				if (firstNode.getKind() == NodeKind.Terminal)
				{
					// A = "b", C. => FIRST(A) = {"b"}
					topEntryNodes.clear();
				}
				else
				{
					Set<Word> words = firstSetDictionary.get(firstNode);

					firstSet.addAll(words);

					// A = B, C. => FIRST(A) = FIRST(B) U
					// (FIRST(C) if FIRST(B) contains empty word);
					if (!words.contains(Word.getEmptyWord()))
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
					Word word = new Word(firstNode);

					// Add word to the dictionary
					firstSet.add(word);
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
					firstSet.add(Word.getEmptyWord());
				}

				entriesStack.pop();
			}
		}
	}

	private boolean processRuleFollowSet(Rule ruleToProcess, Grammar grammar,
		Map<Node, Set<Word>> firstSetDictionary,
		Map<Node, Set<Word>> followSetDictionary) throws Exception
	{
		boolean followSetIsChanged = false;

		List<Node> nodes = ruleToProcess.getNodes();

		for (Node node : nodes)
		{
			if (node.getKind() == NodeKind.Nonterminal)
			{
				Set<Word> followSet = followSetDictionary.get(node);

				Set<Word> oldFollowSet = new HashSet<Word>(followSet);

				List<Node> nodesSubList =
					nodes.subList(nodes.indexOf(node) + 1, nodes.size());

				if (nodesSubList.isEmpty())
				{
					followSet.addAll(followSetDictionary.get(ruleToProcess
							.getHeadNode()));
				}
				else
				{
					Set<Word> firstSet =
						this.getFirstSetForNodesList(nodesSubList,
							firstSetDictionary);

					if (firstSet.remove(Word.getEmptyWord()))
					{
						followSet.addAll(followSetDictionary.get(ruleToProcess
								.getHeadNode()));
					}

					followSet.addAll(firstSet);
				}

				if (!followSetIsChanged && !oldFollowSet.equals(followSet))
				{
					followSetIsChanged = true;
				}
			}
		}

		return followSetIsChanged;
	}
}
