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

import tools.dynamia.zk.crud.CrudView;
import tools.dynamia.zk.crud.CrudViewRenderer;

public class DynamicalCrudViewRenderer<T> extends CrudViewRenderer<T> {

    public DynamicalCrudViewRenderer() {
        System.out.println("Starting " + getClass().getName());
    }

    @Override
    protected CrudView<T> newCrudView() {
        return new DynamicalCrudView<>();
    }

}
