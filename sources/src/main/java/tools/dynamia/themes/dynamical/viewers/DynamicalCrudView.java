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
import tools.dynamia.crud.ChangedStateEvent;
import tools.dynamia.crud.CrudState;
import tools.dynamia.crud.actions.CancelAction;
import tools.dynamia.ui.icons.Icon;
import tools.dynamia.ui.icons.IconSize;
import tools.dynamia.ui.icons.IconType;
import tools.dynamia.ui.icons.IconsTheme;
import tools.dynamia.web.util.HttpUtils;
import tools.dynamia.zk.actions.ButtonActionRenderer;
import tools.dynamia.zk.actions.MenuitemActionRenderer;
import tools.dynamia.zk.actions.ToolbarbuttonActionRenderer;
import tools.dynamia.zk.crud.CrudView;
import tools.dynamia.zk.crud.CrudViewRenderer;
import tools.dynamia.zk.crud.actions.FindAction;
import tools.dynamia.zk.util.ZKUtil;
import tools.dynamia.zk.viewers.ZKWrapperView;

public class DynamicalCrudView<T> extends CrudView<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1773528227238113127L;
	private Div formLeftActions;

	private Div formRightActions;
	private Menupopup actionsMenu;
	private Button actionsButton;
	private final boolean SMARTPHONE = HttpUtils.isSmartphone();
	private Borderlayout borderlayout;

	@Override
	protected void buildGeneralView() {
		super.buildGeneralView();

		borderlayout = (Borderlayout) layout;

		Div header = new Div();
		header.setZclass("crudview-header");
		header.setSclass("clearfix");
		header.setParent(borderlayout.getNorth());
		toolbarContainer = header;

		formLeftActions = new Div();
		formLeftActions.setZclass("crudview-footer row");

		formRightActions = new Div();
		formRightActions.setZclass("crudview-footer row");
		formRightActions.setStyle("text-align: right");

		String menuId = "actionMenu" + StringUtils.randomString().substring(0, 4);
		actionsMenu = new Menupopup();
		actionsMenu.setId(menuId);
		actionsMenu.setParent(this);
		actionsButton = new Button();
		actionsButton.setZclass("btn btn-primary");
		actionsButton.setPopup(menuId + ", after_start");
		ZKUtil.configureComponentIcon("process", actionsButton, IconSize.NORMAL);

		addCrudStateChangedListener(evt -> {
			controlChangedState(evt);
		});
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

	@SuppressWarnings("rawtypes")
	@Override
	protected ActionRenderer getDefaultActionRenderer() {
		return new ToolbarbuttonActionRenderer();
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
			formRightActions.appendChild(btnGroup);
		} else {
			formLeftActions.appendChild(btnGroup);
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

	private void controlChangedState(ChangedStateEvent evt) {
		CrudState crudState = evt.getNewState();

		switch (crudState) {

		case READ:
			borderlayout.getNorth().setVisible(true);
			break;
		default:
			borderlayout.getNorth().setVisible(false);
			break;
		}
	}

	@Override
	public void clearActions() {
		super.clearActions();
		actionsMenu.getChildren().clear();
		formLeftActions.getChildren().clear();
		formRightActions.getChildren().clear();
	}

	@Override
	protected void addFormViewToContainer(String formViewTitle) {
		ZKWrapperView<Object> wrapperView = new ZKWrapperView<Object>(formView);
		formLeftActions.setParent(wrapperView);
		formRightActions.setParent(wrapperView);
		formViewContainer.addView(formViewTitle, wrapperView);
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected CrudViewRenderer getCrudViewRenderer() {
		return new DynamicalCrudViewRenderer<>();
	}

}
