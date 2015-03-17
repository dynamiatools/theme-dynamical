package tools.dynamia.themes.dynamical;

import org.zkoss.zul.Menuitem;

import tools.dynamia.ui.icons.Icon;
import tools.dynamia.ui.icons.IconSize;
import tools.dynamia.ui.icons.IconType;


public class FAIcon extends Icon {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FAIcon(String name, String internalName) {
		super(name, internalName, IconType.FONT);
	}

	@Override
	public String getRealPath(Object component, IconSize size) {
		// TODO Auto-generated method stub
		String path = super.getRealPath(component, size);
		String fontSize = "";
		String alt = "";

		switch (size) {
		case LARGE:
			fontSize = " fa-2x";
			break;
		case NORMAL:
			fontSize = " fa-lg";
			break;
		}

		if (component instanceof Menuitem) {
			alt = " fa-fw";
		}

		return path + fontSize + alt;
	}

}
