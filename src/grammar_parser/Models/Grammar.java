package grammar_parser.Models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import grammar_parser.Exceptions.NonexistentRuleException;
import grammar_parser.Exceptions.RuleAlreadyExistsException;
import grammar_parser.Utils.Guard;

public class Grammar
{
	private Rule _headRule;

	private final Map<Node, List<Rule>> _rulesDictionary;

	public Grammar()
	{
		this._headRule = null;
		this._rulesDictionary = new HashMap<Node, List<Rule>>();
	}

	public void addRule(Rule rule) throws RuleAlreadyExistsException
	{
		Guard.notNull(rule, "rule");

		List<Rule> rules = this._rulesDictionary.get(rule.getHeadNode());

		if (rules != null && rules.contains(rule))
		{
			throw new RuleAlreadyExistsException(String.format(
				"Rule with headNode '%1$s' already exists.", rule.getHeadNode()
						.getText()));
		}

		if (rules == null)
		{
			rules = new ArrayList<Rule>();

			this._rulesDictionary.put(rule.getHeadNode(), rules);
		}

		rules.add(rule);
	}

	public void deleteRule(Rule rule) throws NonexistentRuleException
	{
		Guard.notNull(rule, "rule");

		List<Rule> rules = this._rulesDictionary.get(rule.getHeadNode());

		if (rules == null || !rules.contains(rule))
		{
			throw new NonexistentRuleException(String.format(
				"Rule with headNode '%1$s' doesn't exist.", rule.getHeadNode()
						.getText()));
		}

		if (rule == this._headRule)
		{
			this._headRule = null;
		}

		rules.remove(rule);

		if (rules.size() == 0)
		{
			this._rulesDictionary.remove(rule.getHeadNode());
		}
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}

		if (obj == null)
		{
			return false;
		}

		if (this.getClass() != obj.getClass())
		{
			return false;
		}

		Grammar other = (Grammar) obj;

		if (this._headRule == null)
		{
			if (other._headRule != null)
			{
				return false;
			}
		}
		else if (!this._headRule.equals(other._headRule))
		{
			return false;
		}

		if (this._rulesDictionary == null)
		{
			if (other._rulesDictionary != null)
			{
				return false;
			}
		}
		else if (!this._rulesDictionary.equals(other._rulesDictionary))
		{
			return false;
		}

		return true;
	}

	public Rule getHeadRule()
	{
		return this._headRule;
	}

	public List<Rule> getRules(Node headNode)
	{
		Guard.notNull(headNode, "headNode");

		List<Rule> rules = this._rulesDictionary.get(headNode);

		if (rules != null)
		{
			return new ArrayList<Rule>(rules);
		}

		return new ArrayList<Rule>();
	}

	public Map<Node, List<Rule>> getRulesDictionary()
	{
		Map<Node, List<Rule>> hashMap = new HashMap<Node, List<Rule>>();

		for (Entry<Node, List<Rule>> entry : this._rulesDictionary.entrySet())
		{
			hashMap.put(entry.getKey(), new ArrayList<Rule>(entry.getValue()));
		}

		return hashMap;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;

		result = prime * result
				+ ((this._headRule == null) ? 0 : this._headRule.hashCode());

		result = prime
			* result
				+ ((this._rulesDictionary == null) ? 0 : this._rulesDictionary
					.hashCode());

		return result;
	}

	public void setHeadRule(Rule headRule) throws NonexistentRuleException
	{
		if (headRule != null)
		{
			List<Rule> rules = this._rulesDictionary
					.get(headRule.getHeadNode());

			if (rules == null || !rules.contains(headRule))
			{
				throw new NonexistentRuleException(String.format(
					"Rule with headNode '%1$s' doesn't exist.", headRule
							.getHeadNode().getText()));
			}
		}

		this._headRule = headRule;
	}
}
