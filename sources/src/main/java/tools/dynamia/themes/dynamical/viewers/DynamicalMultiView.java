package tools.dynamia.themes.dynamical.viewers;

import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabpanel;

import tools.dynamia.zk.viewers.mv.MultiView;

public class DynamicalMultiView<T> extends MultiView<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3403191594768388998L;

	@Override
	protected Tab createTab(String label) {
		Tab tab = new Tab();
		tab.setLabel(label);
		getTabs().appendChild(tab);

		Tabpanel panel = new Tabpanel();	
		panel.setVflex("1");		
		panel.setSclass("multiview-panel");
		getTabpanels().appendChild(panel);
		return tab;
	}

}
