
package tools.dynamia.themes.dynamical;

import org.springframework.beans.factory.annotation.Autowired;
import org.zkoss.lang.Library;
import org.zkoss.zul.Messagebox;
import tools.dynamia.app.template.ApplicationTemplate;
import tools.dynamia.app.template.InstallTemplate;
import tools.dynamia.app.template.Skin;
import tools.dynamia.app.template.TemplateContext;
import tools.dynamia.commons.MapBuilder;
import tools.dynamia.themes.dynamical.viewers.DynamicalCrudViewRenderer;
import tools.dynamia.viewers.ViewTypeFactory;
import tools.dynamia.zk.app.bstemplate.BootstrapConfigViewRender;
import tools.dynamia.zk.app.bstemplate.BootstrapFormViewRenderer;
import tools.dynamia.zk.app.bstemplate.BootstrapTableViewRenderer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Mario A. Serrano Leones
 */
@InstallTemplate
public class DynamicalTemplate implements ApplicationTemplate {

    /**
     *
     */
    private static final long serialVersionUID = -8646000381813253072L;

    private static final Skin DEFAULT_SKIN = newSkin("Blue", "Default blue skin", "#367fa9");

    @Autowired
    private ViewTypeFactory viewTypeFactory;


    private List<Skin> skins = new ArrayList<Skin>();

    private Map<String, Object> properties;

    public DynamicalTemplate() {
        createSkins();

        properties = MapBuilder.put(AUTHOR, "Mario Serrano", DATE, "2015", COPYRIGHT, "Dynamia Soluciones IT 2015",
                VERSION, "3.1.0", ORIGINAL_AUTHOR, "Almsaeed Studio");

    }

    @Override
    public String getName() {
        return "Dynamical";
    }

    @Override
    public Map<String, Object> getProperties() {
        return properties;
    }

    @Override
    public void init(TemplateContext context) {

        Library.setProperty("org.zkoss.theme.preferred", "dynamical");

        viewTypeFactory.setCustomViewRenderer("crud", DynamicalCrudViewRenderer.class);
        viewTypeFactory.setCustomViewRenderer("table", BootstrapTableViewRenderer.class);
        viewTypeFactory.setCustomViewRenderer("form", BootstrapFormViewRenderer.class);
        viewTypeFactory.setCustomViewRenderer("config", BootstrapConfigViewRender.class);
        Messagebox.setTemplate("~./templates/bootstrap/views/messagebox.zul");


    }

    @Override
    public List<Skin> getSkins() {
        return skins;
    }

    @Override
    public Skin getDefaultSkin() {
        return DEFAULT_SKIN;
    }

    private void createSkins() {
        skins.add(DEFAULT_SKIN);
        skins.add(newSkin("Black", null, "#808080"));
        skins.add(newSkin("Green", null, "#00a65a"));
        skins.add(newSkin("Purple", null, "#605ca8"));
        skins.add(newSkin("Red", null, "#dd4b39"));
        skins.add(newSkin("Yellow", null, "#FFC200"));
        skins.add(newSkin("Orange", null, "#f39c12"));
        skins.add(newSkin("DarkOrange", null, "#ff5722"));
        skins.add(newSkin("Olive", null, "#8FB442"));
        skins.add(newSkin("Dynamia", null, "#00709c"));
    }

    private static Skin newSkin(String name, String description, String color) {

        String n = name.toLowerCase();
        Skin skin = new Skin("skin-" + n, name, "skin-" + n + ".min.css", description);
        skin.setBaseBackgroundColor(color);
        skin.setBaseColor(color);
        return skin;
    }
}
