package imy.lnu.ai.dfs;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import org.netbeans.api.visual.widget.ImageWidget;
import org.netbeans.api.visual.widget.Scene;

/**
 *
 * @author mcangel
 */
public class PlacePinWidget extends ImageWidget
{
	private Graphics2D g2d;
	private int width = 40;
	private int height = 30;

	public PlacePinWidget(Scene scene)
	{
		super(scene);
		
		setImage(new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB));
		g2d = (Graphics2D) getImage().getGraphics();
		g2d.setPaint(Color.GRAY);
		g2d.fillRect(0, height/3, width, height*2/3);
		g2d.setPaint(Color.getHSBColor(.05f, 1, 0.7f));
		int[] x = {width, width/2, 0};
		int[] y = {height/3, 0, height/3};
		g2d.fillPolygon(x, y, x.length);
		g2d.setPaint(Color.DARK_GRAY);
		g2d.fillRect((width-8)/2, height-12, 8, 12);
		g2d.setPaint(Color.CYAN);
		g2d.fillRect(width/4-4-2, height-16, 7, 7);
		g2d.fillRect(width*3/4-4+3, height-16, 7, 7);
		g2d.setPaint(Color.getHSBColor(.15f, 0.9f, 0.9f));
		g2d.drawRect(width/4-4-3, height-17, 8, 8);
		g2d.drawLine(width/4-3, height-17, width/4-3, height-9);
		g2d.drawLine(width/4-7, height-13, width/4+1, height-13);
		g2d.drawRect(width*3/4-4+2, height-17, 8, 8);
		g2d.drawLine(width*3/4+2, height-17, width*3/4+2, height-9);
		g2d.drawLine(width*3/4-2, height-13, width*3/4+6, height-13);
		setOpaque(false);
		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}
	
}
