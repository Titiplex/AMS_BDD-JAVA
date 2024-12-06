package tabs;

import javafx.scene.Node;

interface TabTemplate {

	/**Used to generate tabs for a javafx app
	 *
	 * @return a tab
	 */
	Node createTab();
}
