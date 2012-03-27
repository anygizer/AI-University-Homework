package imy.lnu.ai.dfs;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.widget.ImageWidget;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @author mcangel
 */
public class PlaceWidget extends Widget
{
	private ImageWidget imageWidget;
	private LabelWidget labelWidget;
	private LabelWidget heuristicsWidget;
	
	private int width = 20;
	private int height = 20;

	public PlaceWidget(Scene scene)
	{
		super(scene);
		
		setLayout(LayoutFactory.createVerticalFlowLayout());

		imageWidget = new ImageWidget(scene);
		imageWidget.setLayout(LayoutFactory.createAbsoluteLayout());
		imageWidget.setImage(new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB));
		Graphics2D g2d = (Graphics2D) imageWidget.getImage().getGraphics();
		g2d.setPaint(Color.YELLOW);
		g2d.fillArc(0, 0, width, height, 270, 180);
		imageWidget.setOpaque(false);
		addChild(imageWidget);
		
		labelWidget = new LabelWidget(scene);
		addChild(labelWidget);
		
		heuristicsWidget = new LabelWidget(scene);
		addChild(heuristicsWidget);
	}

	public LabelWidget getLabelWidget()
	{
		return labelWidget;
	}

	public LabelWidget getHeuristicsWidget()
	{
		return heuristicsWidget;
	}

	void attachPinWidget(PlacePinWidget widget)
	{
		widget.setCheckClipping(true);
		imageWidget.addChild(widget);
		widget.setPreferredLocation(new Point(0, 0));
	}
}
