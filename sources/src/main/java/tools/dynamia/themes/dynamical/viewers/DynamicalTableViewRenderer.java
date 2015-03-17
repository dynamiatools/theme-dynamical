package tools.dynamia.themes.dynamical.viewers;

import java.util.List;

import tools.dynamia.viewers.View;
import tools.dynamia.viewers.ViewDescriptor;
import tools.dynamia.web.util.HttpUtils;
import tools.dynamia.zk.viewers.table.TableView;
import tools.dynamia.zk.viewers.table.TableViewRenderer;

public class DynamicalTableViewRenderer<T> extends TableViewRenderer<T> {

	@Override
	public View<List<T>> render(ViewDescriptor descriptor, List<T> value) {

		descriptor.addParam("showRowNumber", false);

		TableView<T> view = (TableView<T>) super.render(descriptor, value);

		if (HttpUtils.isSmartphone()) {
			view.setSclass("tableview-mobile");
			view.getPagingChild().setMold(null);
		}
		return view;
	}
}
