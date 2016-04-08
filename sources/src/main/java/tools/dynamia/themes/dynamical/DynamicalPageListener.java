package tools.dynamia.themes.dynamical;

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
