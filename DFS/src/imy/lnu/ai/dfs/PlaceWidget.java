package imy.lnu.ai.dfs;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.TwoStateHoverProvider;
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
	
	private int width = 40;
	private int height = 40;

	public PlaceWidget(Scene scene)
	{
		super(scene);
		
		setLayout(LayoutFactory.createVerticalFlowLayout());

		imageWidget = new ImageWidget(scene);
		imageWidget.setLayout(LayoutFactory.createAbsoluteLayout());
		imageWidget.setImage(new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB));
		Graphics2D g2d = (Graphics2D) imageWidget.getImage().getGraphics();
		g2d.setPaint(Color.BLACK);
		g2d.fillRect(0, height/4*3, width, height/4);
		g2d.setPaint(Color.WHITE);
		g2d.fillOval(width/2-17, height/4*3-3, 33, 5);
		g2d.fillOval(width/2-17, height-3, 33, 5);
		imageWidget.setOpaque(false);
		imageWidget.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		addChild(imageWidget);
		
		labelWidget = new LabelWidget(scene);
		addChild(labelWidget);
	}

	public LabelWidget getLabelWidget()
	{
		return labelWidget;
	}

	void attachPinWidget(PlacePinWidget widget)
	{
		widget.setCheckClipping(true);
		imageWidget.addChild(widget);
		widget.setPreferredLocation(new Point(0, 0));
	}
}
