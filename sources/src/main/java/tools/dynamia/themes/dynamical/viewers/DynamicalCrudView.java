package tools.dynamia.themes.dynamical.viewers;

/*-
 * #%L
 * Themes - ZK Dynamical
 * %%
 * Copyright (C) 2017 - 2019 Dynamia Soluciones IT SAS
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zul.*;
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
import tools.dynamia.zk.actions.ActionToolbar;
import tools.dynamia.zk.actions.ButtonActionRenderer;
import tools.dynamia.zk.actions.MenuitemActionRenderer;
import tools.dynamia.zk.actions.ToolbarbuttonActionRenderer;
import tools.dynamia.zk.crud.CrudView;
import tools.dynamia.zk.crud.CrudViewRenderer;
import tools.dynamia.zk.crud.actions.FindAction;
import tools.dynamia.zk.util.ZKUtil;
import tools.dynamia.zk.viewers.ZKWrapperView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DynamicalCrudView<T> extends CrudView<T> {


    /**
     *
     */
    private static final long serialVersionUID = 1773528227238113127L;
    private Div formLeftActions;
    private Div formRightActions;
    private Div formActions;
    private Component leftActions;
    private Component rightActions;
    private Menupopup actionsMenu;
    private Button actionsButton;

    private Borderlayout borderlayout;
    private Map<ActionGroup, Div> actionGroupContainers;
    private ButtonActionRenderer defaultActionRenderer;

    @Override
    protected void buildGeneralView() {
        super.buildGeneralView();

        borderlayout = (Borderlayout) layout;

        Div header = new Div();
        header.setZclass("crudview-header " + ActionToolbar.CONTAINER_SCLASS);
        header.setSclass("clearfix");
        header.setParent(borderlayout.getNorth());
        toolbarContainer = header;

        formActions = new Div();
        formActions.setZclass("crudview-footer row");

        formLeftActions = new Div();
        formLeftActions.setZclass("col-xs-12 col-md-12");
        formLeftActions.setParent(formActions);

        formRightActions = new Div();
        formRightActions.setZclass("col-xs-12 col-md-12");
        formRightActions.setStyle("text-align: right");
        formRightActions.setParent(formActions);

        String menuId = "actionMenu" + StringUtils.randomString().substring(0, 4);
        actionsMenu = new Menupopup();
        actionsMenu.setId(menuId);
        actionsMenu.setParent(this);
        actionsButton = new Button();
        actionsButton.setZclass("btn btn-primary actions");
        actionsButton.setPopup(menuId + ", after_start");
        ZKUtil.configureComponentIcon("process", actionsButton, IconSize.NORMAL);

        addCrudStateChangedListener(evt -> {
            controlChangedState(evt);
        });
    }

    @Override
    protected void buildToolbars() {
        if (!HttpUtils.isSmartphone()) {
            Div toolbar = new Div();
            toolbar.setZclass("btn-toolbar");

            leftActions = toolbar;

            toolbar = new Div();
            toolbar.setZclass("btn-toolbar");
            toolbar.setSclass("actiontb-right");
            rightActions = toolbar;
        }
    }

    @Override
    protected void buildToolbarContainer() {
        if (!HttpUtils.isSmartphone()) {
            toolbarContainer.appendChild(leftActions);
            toolbarContainer.appendChild(rightActions);
        }

    }

    @SuppressWarnings("rawtypes")
    @Override
    protected ActionRenderer getDefaultActionRenderer() {
        if (defaultActionRenderer == null) {
            defaultActionRenderer = new ButtonActionRenderer();
            defaultActionRenderer.setZclass("btn btn-default");
            defaultActionRenderer.setShowLabels(false);

        }
        return defaultActionRenderer;
    }

    @Override
    protected void loadActions(CrudState state) {
        actionsMenu.getChildren().clear();

        if (HttpUtils.isSmartphone()) {

            toolbarContainer.getChildren().clear();
            if (state == CrudState.READ) {

                toolbarContainer.appendChild(actionsButton);
            } else {
                borderlayout.getNorth().setVisible(false);
            }
        }

        super.loadActions(state);
    }

    protected Component renderAction(Action action) {
        ActionRenderer actionRenderer = action.getRenderer();
        if (actionRenderer == null) {
            actionRenderer = getDefaultActionRenderer();
        }
        Component component = (Component) actionRenderer.render(action, this);

        String actionId = action.getId();
        if (action.getAttribute("internalId") != null) {
            actionId = action.getAttribute("internalId").toString();
        }
        if (component instanceof HtmlBasedComponent) {
            HtmlBasedComponent hcom = (HtmlBasedComponent) component;
            hcom.setSclass("actiontb-a " + actionId);
            if (hcom instanceof Toolbarbutton) {
                hcom.setZclass("none");
                hcom.setSclass("btn btn-default " + hcom.getSclass());
            }

            hcom.setTooltiptext(action.getName());
            if (HttpUtils.isSmartphone()) {
                if (!(component instanceof Button)) {
                    hcom.setSclass(hcom.getSclass() + " flexit");
                }
            }
        }

        return component;

    }

    @Override
    protected void showActionGroup(ActionGroup actionGroup) {
        if (HttpUtils.isSmartphone() && getState() == CrudState.READ) {
            MenuitemActionRenderer renderer = new MenuitemActionRenderer();
            for (Action action : actionGroup.getActions()) {
                if (action.getRenderer() == null || action.getRenderer() instanceof ToolbarbuttonActionRenderer) {
                    Menuitem menuitem = renderer.render(action, this);
                    actionsMenu.appendChild(menuitem);
                } else {
                    showAction(actionGroup, action);
                }
            }
        } else {

            actionGroup.getActions().forEach(a -> {
                showAction(actionGroup, a);
            });
        }
    }

    @Override
    protected void showAction(ActionGroup actionGroup, Action action) {
        if ((getState() == CrudState.CREATE || getState() == CrudState.UPDATE)
                && (action.getRenderer() == null || action.getRenderer() instanceof ToolbarbuttonActionRenderer)) {
            ButtonActionRenderer renderer = new ButtonActionRenderer();
            Button button = renderer.render(action, this);
            button.setAttribute(ACTION, action);
            applyButtonStyle(button, action);
            addButton(actionGroup, button);
        } else {
            fixFindAction(action);

            Component actionComp = renderAction(action);
            if (!HttpUtils.isSmartphone()) {
                Component group = getActionGroupContainer(actionGroup);
                group.appendChild(actionComp);
                if (group.getParent() == null) {
                    group.setParent("left".equals(actionGroup.getAlign()) ? leftActions : rightActions);
                }

            } else {
                toolbarContainer.appendChild(actionComp);
            }
        }
    }

    private Component getActionGroupContainer(ActionGroup actionGroup) {
        if (actionGroupContainers == null) {
            this.actionGroupContainers = new HashMap<>();
        }

        Div group = this.actionGroupContainers.get(actionGroup);
        if (group == null) {
            group = new Div();
            group.setZclass("btn-group");
            this.actionGroupContainers.put(actionGroup, group);
        }

        return group;
    }

    private void fixFindAction(Action action) {
        if (HttpUtils.isSmartphone()) {
            if (action instanceof FindAction) {
                action.setAttribute("zclass", "form-control");

            }
        }
    }

    private void displayMenuActions(List<Component> actionComponents) {

        toolbarContainer.appendChild(actionsButton);
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
        if (HttpUtils.isSmartphone()) {
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
        } else if (action.getPosition() <= 1) {
            button.setZclass("btn btn-primary");
        } else {
            button.setZclass("btn btn-default");
        }

        if (action != null && action.getAttribute("type") != null) {
            button.setZclass("btn btn-" + action.getAttribute("type"));
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
                if (leftActions != null && rightActions != null) {
                    if (leftActions.getChildren().isEmpty() && rightActions.getChildren().isEmpty()) {
                        borderlayout.getNorth().setVisible(false);
                    }
                }
                break;
        }
    }

    @Override
    public void clearActions() {
        super.clearActions();
        actionsMenu.getChildren().clear();
        formLeftActions.getChildren().clear();
        formRightActions.getChildren().clear();
        if (leftActions != null && rightActions != null) {
            leftActions.getChildren().clear();
            rightActions.getChildren().clear();
        }
        if (actionGroupContainers != null) {
            this.actionGroupContainers.clear();
        }

    }

    @Override
    protected void addFormViewToContainer(String formViewTitle) {
        ZKWrapperView<Object> wrapperView = new ZKWrapperView<Object>(formView);
        formActions.setParent(wrapperView);
        formViewContainer.addView(formViewTitle, wrapperView);
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected CrudViewRenderer getCrudViewRenderer() {
        return new DynamicalCrudViewRenderer();
    }


}
