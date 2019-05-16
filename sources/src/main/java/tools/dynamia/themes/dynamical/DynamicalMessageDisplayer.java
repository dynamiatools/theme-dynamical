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

import org.springframework.stereotype.Component;
import org.zkoss.zk.ui.util.Clients;
import tools.dynamia.commons.StringUtils;
import tools.dynamia.ui.MessageType;
import tools.dynamia.zk.ui.MessageDialog;

@Component
public class DynamicalMessageDisplayer extends MessageDialog {

    @Override
    public void showMessage(String message, String title, MessageType type) {

        String method = "notice";
        String size = "large";
        String location = "br";
        if (title == null) {
            title = "Message";
            if (type != MessageType.NORMAL) {
                title = StringUtils.capitalize(type.name());
            }
        }

        switch (type) {
            case ERROR:
                method = "error";
                break;
            case WARNING:
                method = "warning";
                break;
            case CRITICAL:
                method = "error";
                location = "tc";
                break;
            default:
                break;
        }

        String script = "$.growl." + method + "({ title: '" + title + "', message: '" + message
                + "', size: '" + size + "', location: '" + location + "'  });";
        Clients.evalJavaScript(script);

    }

}
