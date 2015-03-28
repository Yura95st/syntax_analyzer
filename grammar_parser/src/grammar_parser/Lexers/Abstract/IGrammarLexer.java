package grammar_parser.Lexers.Abstract;

import grammar_parser.Models.Node;
import grammar_parser.Models.NodeDefinition;

public interface IGrammarLexer extends ILexer<Node>
{
	/**
	 * Gets the node definitions.
	 *
	 * @return the node definitions
	 */
	Iterable<NodeDefinition> getNodeDefinitions();

	/**
	 * Sets the node definitions.
	 *
	 * @param nodeDefinitions
	 *            the new node definitions
	 */
	void setNodeDefinitions(Iterable<NodeDefinition> nodeDefinitions);
}
