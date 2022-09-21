/*
 * Copyright (C) 2021 Dynamia Soluciones IT S.A.S - NIT 900302344-1
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

package tools.dynamia.themes.dynamical;

import org.springframework.stereotype.Component;
import org.zkoss.zk.ui.util.Clients;
import tools.dynamia.commons.StringUtils;
import tools.dynamia.ui.MessageType;
import tools.dynamia.ui.UIMessages;
import tools.dynamia.zk.ui.MessageDialog;

@Component
public class DynamicalMessageDisplayer extends MessageDialog {

    @Override
    public void showMessage(String message, String title, MessageType type) {

        String method = "notice";

        if (title == null) {
            title = "Message";
            if (type != MessageType.NORMAL) {
                title = StringUtils.capitalize(type.name());
            }
        }

        title = UIMessages.getLocalizedMessage(title);
        String icon = "";

        switch (type) {
            case NORMAL:
                method = "bg-success";
                icon = "check";
                break;
            case INFO:
                method = "bg-info";
                icon = "info-circle";
                break;
            case ERROR:
                method = "bg-danger";
                icon = "exclamation-circle";
                break;
            case WARNING:
                method = "bg-warning";
                icon = "exclamation-triangle";
                break;
            case CRITICAL:
                method = "bg-danger";
                icon = "bomb";
                break;
            default:
                break;
        }

        String script = String.format("toast('%s','%s','%s','%s');", method + " m-3", title, message, "fas fa-" + icon);
        Clients.evalJavaScript(script);

    }

}
