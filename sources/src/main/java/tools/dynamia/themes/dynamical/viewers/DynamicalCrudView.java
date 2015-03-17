package tools.dynamia.themes.dynamical.viewers;

import java.util.List;
import java.util.stream.Collectors;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Menupopup;

import tools.dynamia.actions.Action;
import tools.dynamia.actions.ActionGroup;
import tools.dynamia.actions.ActionRenderer;
import tools.dynamia.commons.StringUtils;
import tools.dynamia.crud.CrudState;
import tools.dynamia.crud.actions.CancelAction;
import tools.dynamia.integration.Containers;
import tools.dynamia.ui.icons.Icon;
import tools.dynamia.ui.icons.IconSize;
import tools.dynamia.ui.icons.IconType;
import tools.dynamia.ui.icons.IconsTheme;
import tools.dynamia.viewers.Field;
import tools.dynamia.viewers.ViewFactory;
import tools.dynamia.web.util.HttpUtils;
import tools.dynamia.zk.actions.ButtonActionRenderer;
import tools.dynamia.zk.actions.MenuitemActionRenderer;
import tools.dynamia.zk.actions.ToolbarbuttonActionRenderer;
import tools.dynamia.zk.crud.CrudView;
import tools.dynamia.zk.crud.CrudViewRenderer;
import tools.dynamia.zk.crud.actions.FindAction;
import tools.dynamia.zk.util.ZKUtil;
import tools.dynamia.zk.viewers.form.FormView;
import tools.dynamia.zk.viewers.mv.MultiView;
import tools.dynamia.zk.viewers.ui.Viewer;

public class DynamicalCrudView<T> extends CrudView<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1773528227238113127L;
	private Div footer;

	private Div footer2;
	private Menupopup actionsMenu;
	private Button actionsButton;
	private final boolean SMARTPHONE = HttpUtils.isSmartphone();
	private Borderlayout borderlayout;
	private Div body;

	@Override
	protected void buildGeneralView() {
		super.buildGeneralView();
		setZclass("crudview");

		borderlayout = (Borderlayout) layout;


		Div header = new Div();
		header.setZclass("crudview-header");
		header.setSclass("clearfix");
		header.setParent(borderlayout.getNorth());
		toolbarContainer = header;

		this.body = new Div();
		body.setHeight("100%");
		body.setZclass("crudview-body");
		body.setParent(borderlayout.getCenter());
		

		footer = new Div();
		footer.setZclass("crudview-footer row");

		footer2 = new Div();
		footer2.setZclass("crudview-footer row");
		footer2.setStyle("text-align: right");

		String menuId = "actionMenu" + StringUtils.randomString().substring(0, 4);
		actionsMenu = new Menupopup();
		actionsMenu.setId(menuId);
		actionsMenu.setParent(this);
		actionsButton = new Button();
		actionsButton.setZclass("btn btn-primary");
		actionsButton.setPopup(menuId + ", after_start");
		ZKUtil.configureComponentIcon("process", actionsButton, IconSize.NORMAL);

	}

	@Override
	protected void buildToolbarContainer() {
		Div leftDiv = new Div();
		leftDiv.setZclass("pull-left");
		leftDiv.appendChild(toolbarLeft);
		toolbarContainer.appendChild(leftDiv);

		Div rightDiv = new Div();
		rightDiv.setZclass("pull-right");

		rightDiv.appendChild(toolbarRight);
		toolbarContainer.appendChild(rightDiv);
	}

	@Override
	protected ActionRenderer getDefaultActionRenderer() {
		return new ToolbarbuttonActionRenderer();
	}

	protected void buildFormView() {
		System.out.println("Bulding FormView");		

		ViewFactory viewFactory = Containers.get().findObject(ViewFactory.class);

		formView = (FormView) viewFactory.getView("form", getValue(), getViewDescriptor().getBeanClass());
		formViewContainer = new DynamicalMultiView<>();
		formViewContainer.addView("Datos Generales", formView);
		formViewContainer.setParentView(this);
		formViewContainer.setVflex("1");
		// Find collection and viewers fields
		for (Field field : formView.getViewDescriptor().getFields()) {
			if (field.isCollection() && field.getComponentClass() == CrudView.class) {
				addSubCrudView(field);
			} else if (field.getComponentClass() == Viewer.class) {
				addSubGenericView(field);
			}
		}
	}

	@Override
	protected Component renderAction(Action action) {
		if ((getState() == CrudState.CREATE || getState() == CrudState.UPDATE) &&
				(action.getRenderer() == null || action.getRenderer() instanceof ToolbarbuttonActionRenderer)) {
			ButtonActionRenderer renderer = new ButtonActionRenderer();
			Button button = renderer.render(action, this);
			button.setAttribute(ACTION, action);

			applyButtonStyle(button, action);
			return button;

		} else {
			Component component = super.renderAction(action);
			fixFindAction(action, component);
			return component;
		}
	}

	private void fixFindAction(Action action, Component component) {
		if (SMARTPHONE) {
			if (action instanceof FindAction && component instanceof Bandbox) {
				Bandbox bandbox = (Bandbox) component;
				bandbox.setSclass("form-zcontrol");
				bandbox.setStyle("text-align: right");
			}
		}
	}

	@Override
	protected void displayAction(ActionGroup group, List<Component> actionComponents) {

		if (getState() == CrudState.CREATE || getState() == CrudState.UPDATE) {
			actionComponents.stream().filter(c -> c instanceof Button).forEach(btn -> addButton(group, btn));
			super.displayAction(group, actionComponents.stream().filter(c -> !(c instanceof Button)).collect(Collectors.toList()));
		} else {
			if (SMARTPHONE) {
				displayMenuActions(actionComponents.stream().filter(c -> c instanceof Button).collect(Collectors.toList()));
				super.displayAction(group, actionComponents.stream().filter(c -> !(c instanceof Button)).collect(Collectors.toList()));
			} else {
				super.displayAction(group, actionComponents);
			}
		}
	}

	private void displayMenuActions(List<Component> actionComponents) {

		toolbarLeft.appendChild(actionsButton);
		MenuitemActionRenderer menuRenderer = new MenuitemActionRenderer();
		for (Component component : actionComponents) {
			System.out.println("MENU ACTION: " + component);
			Action action = (Action) component.getAttribute(ACTION);
			if (action != null) {
				System.out.println("  " + action);
				Menuitem menuitem = menuRenderer.render(action, this);
				actionsMenu.appendChild(menuitem);
			}
		}

	}

	private void addButton(ActionGroup group, Component btn) {

		Div btnGroup = new Div();
		if (SMARTPHONE) {
			btnGroup.setZclass("btn-group col-md-3 col-sm-3 col-xs-12");
		} else {
			btnGroup.setZclass("btn-group");
		}
		btnGroup.appendChild(btn);

		if ("right".equals(group.getAlign())) {
			btnGroup.setSclass("pull-right");
			footer2.appendChild(btnGroup);
		} else {
			footer.appendChild(btnGroup);
		}

	}

	private void applyButtonStyle(Button button, Action action) {
		if (action instanceof CancelAction) {
			button.setZclass("btn btn-danger");
		} else {
			if (action.getPosition() <= 1) {
				button.setZclass("btn btn-primary");
			} else {
				button.setZclass("btn btn-default");
			}
		}

		Icon icon = IconsTheme.get().getIcon(action.getImage());
		if (icon.getType() == IconType.IMAGE) {
			button.setImage(null);
		}
	}

	@Override
	public void setState(CrudState crudState) {
		body.getChildren().clear();
		boolean showFooter = false;
		switch (crudState) {
		case READ:
			borderlayout.getNorth().setVisible(true);
			actionsMenu.getChildren().clear();
			footer.getChildren().clear();
			footer2.getChildren().clear();
			break;

		default:
			borderlayout.getNorth().setVisible(false);
			showFooter = true;
			break;
		}

		super.setState(crudState);

		if (showFooter) {
			borderlayout.getCenter().getChildren().clear();
			formViewContainer.setParent(body);			
			
			footer.setParent(formViewContainer.getSelectedPanel());
			footer2.setParent(formViewContainer.getSelectedPanel());
			body.setParent(borderlayout.getCenter());
			
		}

	}

	@Override
	protected CrudViewRenderer getCrudViewRenderer() {
		return new DynamicalCrudViewRenderer<>();
	}

}
