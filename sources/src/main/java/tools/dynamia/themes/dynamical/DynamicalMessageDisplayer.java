package tools.dynamia.themes.dynamical;

import org.springframework.stereotype.Component;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Messagebox;

import tools.dynamia.commons.Callback;
import tools.dynamia.commons.StringUtils;
import tools.dynamia.ui.MessageType;
import tools.dynamia.zk.ui.MessageDialog;
import tools.dynamia.zk.util.ZKUtil;

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

		switch (type) {
		case ERROR:
			method = "error";
			break;
		case WARNING:
			method = "warning";
			break;

		default:
			break;
		}

		String script = "$.growl." + method + "({ title: '" + title + "', message: '" + message
				+ "', size: 'large', location: 'br'  });";
		Clients.evalJavaScript(script);

	}
	

    @Override
    public void showQuestion(String message, final Callback onYesResponse) {
        ZKUtil.showQuestion(message, "Confirmar", t -> {
		    if (t.getButton() == Messagebox.Button.YES) {
		        onYesResponse.doSomething();
		    }
		});
    }

    @Override
    public void showQuestion(String message, final Callback onYesResponse, final Callback onNoResponseCallback) {
        ZKUtil.showQuestion(message, "Confirmar", t -> {
		    if (t.getButton() == Messagebox.Button.YES) {
		        onYesResponse.doSomething();
		    } else if (t.getButton() == Messagebox.Button.NO) {
		        onNoResponseCallback.doSomething();
		    }
		});
    }
}
