package tools.dynamia.themes.dynamical.viewers;

import static tools.dynamia.viewers.util.ViewersExpressionUtil.$s;
import static tools.dynamia.viewers.util.ViewersExpressionUtil.isExpression;

import java.util.Collection;
import java.util.Collections;

import org.zkforge.ckez.CKeditor;
import org.zkoss.bind.Binder;
import org.zkoss.util.Locales;
import org.zkoss.zhtml.Form;
import org.zkoss.zhtml.H3;
import org.zkoss.zhtml.I;
import org.zkoss.zhtml.Label;
import org.zkoss.zhtml.Text;
import org.zkoss.zhtml.impl.AbstractTag;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Button;
import org.zkoss.zul.Cell;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Image;
import org.zkoss.zul.impl.InputElement;

import tools.dynamia.commons.BeanUtils;
import tools.dynamia.ui.icons.IconSize;
import tools.dynamia.ui.icons.IconsTheme;
import tools.dynamia.viewers.Field;
import tools.dynamia.viewers.FieldGroup;
import tools.dynamia.viewers.IndexableComparator;
import tools.dynamia.viewers.ViewDescriptor;
import tools.dynamia.viewers.ViewLayout;
import tools.dynamia.viewers.util.Viewers;
import tools.dynamia.zk.util.ZKBindingUtil;
import tools.dynamia.zk.util.ZKUtil;
import tools.dynamia.zk.viewers.form.FormFieldComponent;
import tools.dynamia.zk.viewers.form.FormView;
import tools.dynamia.zk.viewers.form.FormViewRenderer;

public class DynamicalFormViewRenderer<T> extends FormViewRenderer<T> {

	@Override
	protected void renderRows(FormView<T> view, ViewDescriptor viewDesc, int realCols, T value) {

		Div row = null;
		Binder binder = ZKBindingUtil.createBinder();
		ZKBindingUtil.initBinder(binder, view, view);

		Collections.sort(viewDesc.getFields(), new IndexableComparator());

		for (Field field : viewDesc.getFields()) {
			if (field.isVisible() && field.getGroup() == null) {

				if (!hasSpace(row, realCols, field)) {
					row = newRow();
					row.setParent(view);
				}
				renderField(row, field, binder, view, value, realCols);
			}
		}

		Collections.sort(viewDesc.getFieldGroups(), new IndexableComparator());
		for (FieldGroup fieldGroup : viewDesc.getFieldGroups()) {
			Component group = createGroup(fieldGroup, realCols, view);
			row = newRow();
			row.setParent(group);
			for (Field field : fieldGroup.getFields()) {
				if (field.isVisible()) {
					if (!hasSpace(row, realCols, field)) {
						row = newRow();
						row.setParent(group);
					}
					renderField(row, field, binder, view, value, realCols);
				}
			}
		}
		view.setBinder(binder);
	}

	private Div newRow() {
		Div div = new Div();
		div.setZclass("row");
		return div;
	}

	protected void renderField(Component row, Field field, Binder binder, FormView<T> view, T value, int realCols) {
		boolean showLabel = true;
		Object sl = field.getParams().get(Viewers.PARAM_SHOW_LABEL);
		if (sl != null && (sl == Boolean.FALSE || sl.toString().equalsIgnoreCase("false"))) {
			showLabel = false;
		}

		Div column = new Div();
		column.setParent(row);

		String labelText = field.getLocalizedLabel(Locales.getCurrent());
		if (isExpression(labelText)) {
			labelText = $s(labelText);
		}

		String decriptionText = field.getLocalizedDescription(Locales.getCurrent());
		if (isExpression(decriptionText)) {
			decriptionText = $s(decriptionText);
		}

		Label label = new Label();
		label.appendChild(new Text(labelText));

		label.setSclass("form-view-lbl");

		int colspan = 1;
		try {
			colspan = Integer.parseInt(field.getParams().get("span").toString());
			column.setAttribute(Viewers.ATTRIBUTE_COLSPAN, colspan);
		} catch (Exception e) {
		}
		colspan = getRealColspan(colspan, realCols);
		column.setZclass("form-group col-xs-12 col-sm-" + colspan + " col-md-" + colspan);

		Component component = createComponent(field, view, value);

		if (component instanceof Checkbox) {
			showLabel = false;
		}

		if (showLabel) {
			label.setParent(column);
		}

		if (component instanceof HtmlBasedComponent) {
			HtmlBasedComponent hcom = (HtmlBasedComponent) component;
			hcom.setTooltiptext($s(field.getDescription()));
		}
		if (component instanceof CKeditor) {
			Form form = new Form();
			component.setParent(form);
			form.setParent(column);
		} else {
			applyComponentCSS(component, labelText, field);
			component.setParent(column);
		}
		createBinding(component, field, binder, value);
		view.getComponentsFieldsMap().put(field.getName(),
				new FormFieldComponent(field.getName(), new org.zkoss.zul.Label(labelText), component));
	}

	protected int getRealColspan(int colspan, int realCols) {
		return (12 / realCols) * colspan;

	}

	protected boolean hasSpace(Component row, int realCols, Field field) {
		if (row == null) {
			return false;
		}

		if (field.getParams().containsKey(Viewers.PARAM_NEW_ROW)) {
			return false;
		}

		if (BeanUtils.isAssignable(field.getFieldClass(), Collection.class)) {
			return false;
		}

		int space = 0;
		if (row.getChildren() != null) {
			for (Component comp : row.getChildren()) {
				if (comp instanceof Cell) {
					Cell cell = (Cell) comp;
					space += cell.getColspan();
				} else if (comp.getAttribute(Viewers.ATTRIBUTE_COLSPAN) != null) {
					space += (int) comp.getAttribute(Viewers.ATTRIBUTE_COLSPAN);
				} else {
					space++;
				}
			}
		}

		int colspan = 1;
		try {
			colspan = Integer.parseInt(field.getParams().get(Viewers.PARAM_SPAN).toString());
		} catch (Exception e) {
		}

		int empty = realCols - space;

		return (empty >= colspan);
	}

	@Override
	protected void applyFieldParams(Component comp, Field field) {
		comp.setAttribute(Viewers.ATTRIBUTE_FIELD_NAME, field.getName());
		comp.setAttribute(Viewers.ATTRIBUTE_FIELD_CLASS, field.getFieldClass());
	}

	protected void applyComponentCSS(Component component, String labelText, Field field) {
		String styleClass = (String) field.getParams().get(Viewers.PARAM_STYLE_CLASS);
		if (styleClass == null) {
			styleClass = "";
		}

		if (component instanceof InputElement) {
			InputElement element = (InputElement) component;
			element.setPlaceholder(labelText);
			element.setHflex(null);

			if (element instanceof Datebox ||
					element instanceof Combobox ||
					element instanceof Bandbox) {

				element.setSclass("form-zcontrol");
			} else {
				element.setZclass("form-control");
			}
		}

		if (component instanceof Checkbox) {
			Checkbox checkbox = (Checkbox) component;
			checkbox.setLabel(" " + labelText);
		}

		if (component instanceof Button) {
			Button button = (Button) component;
			button.setZclass("none");
			button.setStyle("margin-left: 5px");
			button.setSclass("form-zcontrol btn btn-success" + " " + styleClass);
		}

		if (component instanceof org.zkoss.zul.Label || component instanceof Image) {
			HtmlBasedComponent html = (HtmlBasedComponent) component;
			html.setSclass("form-zcontrol");
		}
	}

	protected Component createGroup(FieldGroup fieldGroup, int realCols, Component rows) {
		String styleClass = (String) fieldGroup.getParams().get(Viewers.PARAM_STYLE_CLASS);
		if (styleClass == null) {
			styleClass = "";
		}

		Div box = new Div();
		box.setZclass("box box-solid box-warning field-group");
		rows.appendChild(box);

		Div header = new Div();
		header.setZclass("box-header");
		box.appendChild(header);

		H3 title = new H3();
		title.setSclass("box-title");
		header.appendChild(title);

		String label = fieldGroup.getLabel();

		if (fieldGroup.getIcon() != null) {
			I icon = new I();
			icon.setParent(title);
			label = " " + label;
			ZKUtil.configureComponentIcon(IconsTheme.get().getIcon(fieldGroup.getIcon()), icon, IconSize.NORMAL);
		}
		title.appendChild(new Text(label));

		return rows;

	}

	private <TAG extends AbstractTag> TAG tag(Class<TAG> tagClass, String content, String styleClass) {
		AbstractTag tag = BeanUtils.newInstance(tagClass);
		tag.appendChild(new Text(content));
		if (styleClass != null) {
			tag.setSclass(styleClass);
		}
		return (TAG) tag;
	}

	@Override
	protected int renderHeaders(FormView<T> view, ViewDescriptor d) {
		int colNum = 2;

		ViewLayout layout = d.getLayout();
		if (layout != null) {
			try {
				colNum = Integer.parseInt(layout.getParams().get(Viewers.LAYOUT_PARAM_COLUMNS).toString());
			} catch (Exception e) {

			}
		}

		if (colNum > 12) {
			colNum = 12;
		}
		if (colNum < 1) {
			colNum = 1;
		}

		return colNum;
	}

	@Override
	protected FormView<T> newFormView() {
		FormView<T> view = new FormView<>();
		view.setZclass("content");
		return view;
	}

}
