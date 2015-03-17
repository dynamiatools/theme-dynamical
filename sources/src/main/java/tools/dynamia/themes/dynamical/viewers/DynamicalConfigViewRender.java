/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.dynamia.themes.dynamical.viewers;

import java.util.List;

import tools.dynamia.domain.query.Parameter;
import tools.dynamia.viewers.ViewDescriptor;
import tools.dynamia.zk.crud.cfg.ConfigView;
import tools.dynamia.zk.crud.cfg.ConfigViewRender;
import tools.dynamia.zk.viewers.form.FormView;

/**
 *
 * @author Mario A. Serrano Leones
 */
public class DynamicalConfigViewRender extends ConfigViewRender {

	public DynamicalConfigViewRender() {
	}

	@Override
	protected FormView<List<Parameter>> newFormView() {
		ConfigView formView = new ConfigView();
		formView.setZclass("content");
		return formView;
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected ConfigView delegateRender(ViewDescriptor descriptor, List<Parameter> value) {
		DynamicalFormViewRenderer delegate = new DynamicalFormViewRenderer<>();
		return (ConfigView) delegate.render(newFormView(), descriptor, value);
	}

}
