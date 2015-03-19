package grammar_parser.Services.Concrete;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import grammar_parser.Enums.NodeKind;
import grammar_parser.Models.Grammar;
import grammar_parser.Models.Node;
import grammar_parser.Models.Rule;
import grammar_parser.Services.Abstract.IGrammarService;
import grammar_parser.Utils.Guard;

public class GrammarService implements IGrammarService
{
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
		// value is "true" - exists at least one empty rule (epsilon-rule), that starts from node.
		// value is "false" - all the rules, that start with the node are not empty.
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
						ruleHeadNodesMarks.putIfAbsent(topRule.getHeadNode(), false);
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
