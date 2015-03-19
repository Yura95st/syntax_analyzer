package syntax_analyzer.Lexers.Abstract;

import java.util.List;

import syntax_analyzer.Models.Node;
import syntax_analyzer.Models.NodeDefinition;

public interface IGrammarLexer
{
	/**
	 * Gets the node definitions.
	 *
	 * @return the node definitions
	 */
	List<NodeDefinition> getNodeDefinitions();

	/**
	 * Gets the source.
	 *
	 * @return the source
	 */
	String getSource();

	/**
	 * Gets the space characters.
	 *
	 * @return the space characters
	 */
	List<Character> getSpaceCharacters();

	/**
	 * Parses the source.
	 *
	 * @return the nodes, parsed from the source
	 */
	List<Node> parse();

	/**
	 * Sets the node definitions.
	 *
	 * @param nodeDefinitions
	 *            the new node definitions
	 */
	void setNodeDefinitions(List<NodeDefinition> nodeDefinitions);

	/**
	 * Sets the source.
	 *
	 * @param source
	 *            the new source
	 */
	void setSource(String source);

	/**
	 * Sets the space characters.
	 *
	 * @param spaceCharacters
	 *            the new space characters
	 */
	void setSpaceCharacters(List<Character> spaceCharacters);
}
