package tools.dynamia.themes.dynamical.viewers;

import tools.dynamia.zk.viewers.mv.MultiView;
import tools.dynamia.zk.viewers.mv.MultiViewRenderer;

public class DynamicalMultiViewRenderer extends MultiViewRenderer{
	
	
	@Override
	protected MultiView newMultiView() {
		// TODO Auto-generated method stub
		return new DynamicalMultiView();
	}

}
