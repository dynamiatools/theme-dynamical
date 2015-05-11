/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.dynamia.themes.dynamical;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.zkoss.lang.Library;

import tools.dynamia.app.template.ApplicationTemplate;
import tools.dynamia.app.template.InstallTemplate;
import tools.dynamia.app.template.Skin;
import tools.dynamia.app.template.TemplateContext;
import tools.dynamia.commons.MapBuilder;
import tools.dynamia.themes.dynamical.viewers.DynamicalConfigViewRender;
import tools.dynamia.themes.dynamical.viewers.DynamicalCrudViewRenderer;
import tools.dynamia.themes.dynamical.viewers.DynamicalFormViewRenderer;
import tools.dynamia.themes.dynamical.viewers.DynamicalTableViewRenderer;
import tools.dynamia.viewers.ViewTypeFactory;

/**
 *
 * @author Mario A. Serrano Leones
 */
@InstallTemplate
public class DynamicalTemplate implements ApplicationTemplate {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8646000381813253072L;

	private static final Skin DEFAULT_SKIN = newSkin("Blue", "Default blue skin");

	@Autowired
	private ViewTypeFactory viewTypeFactory;

	private List<Skin> skins = new ArrayList<Skin>();

	private Map<String, Object> properties;

	public DynamicalTemplate() {
		createSkins();

		properties = MapBuilder.put(
				AUTHOR, "Mario Serrano",
				DATE, "2015",
				COPYRIGHT, "Dynamia Soluciones IT 2015",
				VERSION, "3.0.0",
				ORIGINAL_AUTHOR, "Almsaeed Studio");

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
		viewTypeFactory.setCustomViewRenderer("table", DynamicalTableViewRenderer.class);
		viewTypeFactory.setCustomViewRenderer("form", DynamicalFormViewRenderer.class);
		viewTypeFactory.setCustomViewRenderer("config", DynamicalConfigViewRender.class);
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
		skins.add(newSkin("Black", null));
		skins.add(newSkin("Green", null));
		skins.add(newSkin("Purple", null));
		skins.add(newSkin("Red", null));
		skins.add(newSkin("Yellow", null));
		skins.add(newSkin("Orange", null));
		skins.add(newSkin("Olive", null));
	}

	private static Skin newSkin(String name, String description) {

		String n = name.toLowerCase();
		return new Skin("skin-" + n, name, "skin-" + n + ".min.css", description);
	}
}
