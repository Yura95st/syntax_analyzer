package syntax_analyzer.Parsers.Abstract;

import java.util.List;
import java.util.Map;

import syntax_analyzer.Enums.SpecialNodeKind;
import syntax_analyzer.Exceptions.SpecialNodeIsNotDefinedException;
import syntax_analyzer.Models.Grammar;
import syntax_analyzer.Models.Node;

public interface IGrammarParser
{
	/**
	 * Gets the nodes.
	 *
	 * @return the nodes
	 */
	List<Node> getNodes();
	
	/**
	 * Gets the special nodes dictionary.
	 *
	 * @return the special nodes dictionary
	 */
	Map<SpecialNodeKind, Node> getSpecialNodesDictionary();
	
	/**
	 * Parses the nodes.
	 *
	 * @return the grammar parsed from the nodes
	 * @throws Exception
	 *             the exception
	 */
	Grammar parse() throws Exception;
	
	/**
	 * Sets the nodes.
	 *
	 * @param nodes
	 *            the new nodes
	 */
	void setNodes(List<Node> nodes);

	/**
	 * Sets the special nodes dictionary.
	 *
	 * @param specialNodesDictionary
	 *            the special nodes dictionary
	 * @throws SpecialNodeIsNotDefinedException
	 *             if at least one of the special nodes is not defined
	 */
	void setSpecialNodesDictionary(
		Map<SpecialNodeKind, Node> specialNodesDictionary)
				throws SpecialNodeIsNotDefinedException;
}
