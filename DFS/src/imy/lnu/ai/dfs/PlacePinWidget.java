package imy.lnu.ai.dfs;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import org.netbeans.api.visual.widget.ImageWidget;
import org.netbeans.api.visual.widget.Scene;

/**
 *
 * @author mcangel
 */
public class PlacePinWidget extends ImageWidget
{
	private Graphics2D g2d;
	private int width = 10;
	private int height = 20;

	public PlacePinWidget(Scene scene)
	{
		super(scene);
		
		setImage(new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB));
		g2d = (Graphics2D) getImage().getGraphics();
		g2d.setPaint(Color.RED);
		g2d.fillArc(0, 0, 2*width, height, 90, 180);
		setOpaque(false);
	}
	
}
