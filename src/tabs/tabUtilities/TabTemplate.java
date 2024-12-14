package tabs.tabUtilities;

import javafx.scene.Node;

public interface TabTemplate {

    /**
     * Used to generate tabs for a javafx app
     *
     * @return a tab
     */
    Node createTab();
}
