package tools.dynamia.themes.dynamical;

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

import org.zkoss.zk.ui.util.Clients;

import tools.dynamia.integration.sterotypes.Listener;
import tools.dynamia.navigation.NavigationListener;
import tools.dynamia.navigation.PageEvent;
import tools.dynamia.zk.util.ZKUtil;

@Listener
public class DynamicalPageListener implements NavigationListener {

    @Override
    public void onPageLoad(PageEvent evt) {
        if (ZKUtil.isInEventListener()) {
            System.out.println("New page loaded " + evt.getPage());
            Clients.evalJavaScript("onPageLoaded();");
        }
    }

    @Override
    public void onPageUnload(PageEvent evt) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageClose(PageEvent evt) {
        // TODO Auto-generated method stub

    }

}
