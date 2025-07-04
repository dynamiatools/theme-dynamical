/*
 * Copyright (C) 2023 Dynamia Soluciones IT S.A.S - NIT 900302344-1
 * Colombia / South America
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tools.dynamia.themes.dynamical.viewers;

import tools.dynamia.ui.icons.IconSize;
import tools.dynamia.zk.crud.CrudViewRenderer;
import tools.dynamia.zk.util.ZKUtil;
import tools.dynamia.zk.viewers.BootstrapCrudView;

public class DynamicalCrudView<T> extends BootstrapCrudView<T> {


    /**
     *
     */
    private static final long serialVersionUID = 1773528227238113127L;


    @Override
    protected void buildGeneralView() {
        super.buildGeneralView();
        if (actionsButton != null) {
            actionsButton.setIconSclass("fa fa-bars");
        }


    }


    @SuppressWarnings("rawtypes")
    @Override
    protected CrudViewRenderer getCrudViewRenderer() {
        return new DynamicalCrudViewRenderer();
    }


}
