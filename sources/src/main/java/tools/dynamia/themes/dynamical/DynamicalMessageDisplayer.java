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
