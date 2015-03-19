package syntax_analyzer.Models;

import java.util.HashMap;
import java.util.Map;

import syntax_analyzer.Exceptions.NonexistentRuleException;
import syntax_analyzer.Exceptions.RuleAlreadyExistsException;
import syntax_analyzer.Utils.Guard;

public class Grammar
{
	Rule _headRule;
	
	Map<Node, Rule> _rulesDictionary;

	public Grammar()
	{
		this._headRule = null;
		this._rulesDictionary = new HashMap<Node, Rule>();
	}

	public void addRule(Rule rule) throws RuleAlreadyExistsException
	{
		Guard.notNull(rule, "rule");
		
		if (this._rulesDictionary.containsValue(rule))
		{
			throw new RuleAlreadyExistsException(String.format(
				"Rule with headNode '%1$s' already exists.", rule.getHeadNode()
				.getText()));
		}
		
		this._rulesDictionary.put(rule.getHeadNode(), rule);
	}

	public void deleteRule(Rule rule) throws NonexistentRuleException
	{
		if (!this._rulesDictionary.containsValue(rule))
		{
			throw new NonexistentRuleException(String.format(
				"Rule with headNode '%1$s' doesn't exist.", rule.getHeadNode()
				.getText()));
		}

		if (rule == this._headRule)
		{
			this._headRule = null;
		}

		this._rulesDictionary.remove(rule.getHeadNode());
	}
	
	public Rule getHeadRule()
	{
		return this._headRule;
	}

	public Rule getRule(Node headNode)
	{
		Guard.notNull(headNode, "headNode");

		return this._rulesDictionary.get(headNode);
	}
	
	public Map<Node, Rule> getRulesDictionary()
	{
		return new HashMap<Node, Rule>(this._rulesDictionary);
	}

	public void setHeadRule(Rule headRule) throws NonexistentRuleException
	{
		if (headRule != null && !this._rulesDictionary.containsValue(headRule))
		{
			throw new NonexistentRuleException(String.format(
				"Rule with headNode '%1$s' doesn't exist.", headRule
				.getHeadNode().getText()));
		}
		
		this._headRule = headRule;
	}
}