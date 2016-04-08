package tools.dynamia.themes.dynamical.viewers;

import tools.dynamia.zk.crud.CrudView;
import tools.dynamia.zk.crud.CrudViewRenderer;

public class DynamicalCrudViewRenderer<T> extends CrudViewRenderer<T> {

    public DynamicalCrudViewRenderer() {
        System.out.println("Starting " + getClass().getName());
    }

    @Override
    protected CrudView<T> newCrudView() {
        CrudView<T> crudView = new DynamicalCrudView<>();
        return crudView;
    }

}
