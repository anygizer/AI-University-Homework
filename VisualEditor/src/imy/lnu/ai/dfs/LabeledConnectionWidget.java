package imy.lnu.ai.dfs;

import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.router.Router;
import org.netbeans.api.visual.vmd.VMDConnectionWidget;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Scene;

/**
 *
 * @author mcangel
 */
public class LabeledConnectionWidget extends VMDConnectionWidget
{
	private LabelWidget labelWidget;

	public LabeledConnectionWidget(Scene scene, Router router)
	{
		super(scene, router);
		
		labelWidget = new LabelWidget(scene);
		addChild(labelWidget);
		setConstraint(labelWidget, LayoutFactory.ConnectionWidgetLayoutAlignment.NONE, (float)0.5);
	}
	
	public LabelWidget getLabelWidget()
	{
		return labelWidget;
	}
}
