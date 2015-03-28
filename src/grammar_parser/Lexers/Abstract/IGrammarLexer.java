package grammar_parser.Lexers.Abstract;

import java.util.List;

import grammar_parser.Models.Node;
import grammar_parser.Models.NodeDefinition;

public interface IGrammarLexer extends ILexer<Node>
{
	/**
	 * Gets the node definitions.
	 *
	 * @return the node definitions
	 */
	List<NodeDefinition> getNodeDefinitions();

	/**
	 * Sets the node definitions.
	 *
	 * @param nodeDefinitions
	 *            the new node definitions
	 */
	void setNodeDefinitions(List<NodeDefinition> nodeDefinitions);
}
