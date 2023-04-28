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
