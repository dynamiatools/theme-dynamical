package tools.dynamia.themes.dynamical;

import java.util.HashMap;
import java.util.Map;

import org.zkoss.zhtml.A;
import org.zkoss.zhtml.I;
import org.zkoss.zhtml.Li;
import org.zkoss.zhtml.Text;
import org.zkoss.zhtml.Ul;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;

import tools.dynamia.navigation.Module;
import tools.dynamia.navigation.NavigationViewBuilder;
import tools.dynamia.navigation.Page;
import tools.dynamia.navigation.PageGroup;
import tools.dynamia.ui.icons.IconSize;
import tools.dynamia.web.util.HttpUtils;
import tools.dynamia.zk.navigation.ZKNavigationManager;
import tools.dynamia.zk.util.ZKUtil;

public class DynamicalMenuBuilder implements NavigationViewBuilder {

	private transient Ul sidebar;
	private transient Map<Module, Component> modulesContent = new HashMap<Module, Component>();
	private transient Map<PageGroup, Component> pgContent = new HashMap<PageGroup, Component>();
	private transient Map<Page, Component> pageContent = new HashMap<Page, Component>();

	public DynamicalMenuBuilder() {
		System.out.println("Starting " + getClass().getName());
		sidebar = new Ul();
		sidebar.setSclass("sidebar-menu");
	}

	@Override
	public Component getNavigationView() {
		Clients.evalJavaScript("applyJqueryStuff();");
		return sidebar;
	}

	@Override
	public void createModuleView(Module module) {

		if (module.getProperty("submenus") != Boolean.FALSE) {

			Li menu = new Li();
			menu.setSclass("treeview");
			menu.setParent(sidebar);

			A a = new A();
			a.setDynamicProperty("href", "#");
			a.setParent(menu);

			I icon = new I();
			icon.setParent(a);

			if (module.getIcon() != null && !module.getIcon().isEmpty()) {
				ZKUtil.configureComponentIcon(module.getIcon(), icon, IconSize.SMALL);
			}

			Text label = new Text(" " + module.getName());
			label.setParent(a);

			I angle = new I();
			angle.setSclass("fa fa-angle-left pull-right");
			angle.setParent(a);

			Ul submenu = new Ul();
			submenu.setSclass("treeview-menu");
			submenu.setParent(menu);

			modulesContent.put(module, submenu);
		}
	}

	@Override
	public void createPageGroupView(PageGroup pageGroup) {
		boolean submenus = true;
		if (pageGroup.getParentModule() != null && pageGroup.getParentModule().getProperty("submenus") == Boolean.FALSE) {
			submenus = false;
		}
		if (submenus) {
			Ul menuPg = new Ul();
			menuPg.setSclass("treeview-menu");

			A pgItem = new A();
			pgItem.setDynamicProperty("href", "#");

			I pgIcon = new I();
			pgIcon.setSclass("fa fa-circle-o");
			pgIcon.setParent(pgItem);

			Text label = new Text(" " + pageGroup.getName());
			label.setParent(pgItem);

			I pgAngle = new I();
			pgAngle.setSclass("fa fa-angle-left pull-right");
			pgAngle.setParent(pgItem);

			Li pgLi = new Li();
			pgItem.setParent(pgLi);

			Component menu = null;
			if (pageGroup.getParentModule() != null) {
				menu = modulesContent.get(pageGroup.getParentModule());
			} else {
				menu = pgContent.get(pageGroup.getParentGroup());
			}

			pgLi.setParent(menu);
			menuPg.setParent(pgLi);
			pgContent.put(pageGroup, menuPg);
		}
	}

	@Override
	public void createPageView(Page page) {
		Component menu = null;
		if (page.getPageGroup().getParentModule() != null) {
			menu = modulesContent.get(page.getPageGroup().getParentModule());
		} else {
			menu = pgContent.get(page.getPageGroup().getParentGroup());
		}

		Component menuPg = pgContent.get(page.getPageGroup());

		Li pageli = new Li();
		pageContent.put(page, pageli);
		org.zkoss.zul.A pageitem = new org.zkoss.zul.A();
		pageitem.getAttributes().put("page", page);
		pageitem.addEventListener(Events.ON_CLICK, evt -> {
			Li currentPageLi = (Li) pageContent.get(ZKNavigationManager.getInstance().getCurrentPage());
			if (currentPageLi != null) {
				currentPageLi.setSclass(null);
			}

			ZKNavigationManager.getInstance().setCurrentPage(page);
			pageli.setSclass("active");

			Clients.evalJavaScript("changeHash('" + page.getPrettyVirtualPath() + "');");

		});

		I pageicon = new I();
		pageicon.setSclass("fa fa-circle-o");
		pageicon.setParent(pageitem);

		Text label = new Text(page.getName());
		label.setParent(pageitem);

		String context = HttpUtils.getCurrentRequest().getContextPath();

		pageitem.setParent(pageli);

		Component pageParent = sidebar;
		if (menuPg != null) {
			pageParent = menuPg;
		} else if (menu != null) {
			pageParent = menu;
		}

		pageli.setParent(pageParent);

	}
}
