package imy.lnu.ai.dfs;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.netbeans.api.visual.action.*;
import org.netbeans.api.visual.anchor.AnchorFactory;
import org.netbeans.api.visual.anchor.AnchorShape;
import org.netbeans.api.visual.anchor.PointShape;
import org.netbeans.api.visual.graph.GraphPinScene;
import org.netbeans.api.visual.router.RouterFactory;
import org.netbeans.api.visual.vmd.VMDConnectionWidget;
import org.netbeans.api.visual.widget.*;
import org.openide.util.Exceptions;

/**
 *
 * @author mcangel
 */
public class DFSGraphScene extends GraphPinScene<String, Integer, String>
{
	private LayerWidget mainLayer = new LayerWidget(this);
	private LayerWidget connectionLayer = new LayerWidget(this);
	private LayerWidget interractionLayer = new LayerWidget(this);
	private int edgeCounter = 0;
	private WidgetAction connectAction = ActionFactory.createConnectAction(interractionLayer, new SceneConnectProvider());
	private WidgetAction reconnetAction = ActionFactory.createReconnectAction(new SceneReconnectProvider());
	private WidgetAction editorAction = ActionFactory.createInplaceEditorAction(new LabelTextFieldEditor());
	private final static String NAME_TEMPLATE = "Place";
	private long newNameCounter;
	
	private String startPlace;
	private String endPlace;
	private ArrayList<String> walkedTrough = new ArrayList<String>();

	public DFSGraphScene()
	{
		this.addChild(mainLayer);
		this.addChild(connectionLayer);
		this.addChild(interractionLayer);
		
		getActions().addAction(ActionFactory.createPopupMenuAction(new PopupMenuProvider() {

			@Override
			public JPopupMenu getPopupMenu(Widget widget, Point localLocation)
			{
				final Point localPoint = localLocation;
				JPopupMenu menu = new JPopupMenu("Menu");
				JMenuItem jmi = new JMenuItem(new AbstractAction() {

					@Override
					public void actionPerformed(ActionEvent e)
					{
						String newName = NAME_TEMPLATE;
						newNameCounter = 1;
						while(isObject(newName))
						{
							newName = NAME_TEMPLATE + newNameCounter++;
						}
						addNode(newName).setPreferredLocation(localPoint);
						addPin(newName, newName + " pin");
						validate();
					}
				});
				jmi.setText("Create place");
				menu.add(jmi);
				jmi = new JMenuItem(new AbstractAction() {

					@Override
					public void actionPerformed(ActionEvent e)
					{
						new Thread(new Runnable() {

							@Override
							public void run()
							{
								walkedTrough.clear();
								search(startPlace);
							}
						}).start();
					}
				});
				jmi.setText("Start");
				menu.add(jmi);
				return menu;
			}
		}));
	}
	
	private boolean search(String place)
	{
		try
		{
			Thread.sleep(1000);
		} catch (InterruptedException ex)
		{
			Exceptions.printStackTrace(ex);
		}
		
		walkedTrough.add(place);
		if(place.equals(endPlace))
		{
			//GOAL REACHED!!!
			return true;
		}
		else
		{
			for(Integer i: findPinEdges(getNodePins(place).toArray()[0].toString(), true, false))
			{
				String nextNode = getPinNode(getEdgeTarget(i));
				if(!walkedTrough.contains(nextNode))
				{
					((LabeledConnectionWidget) findWidget(i)).setLineColor(Color.RED);
					((LabeledConnectionWidget) findWidget(i)).setForeground(Color.RED);
					if(search(nextNode))
					{
						return true;
					}
					else
					{
						((LabeledConnectionWidget) findWidget(i)).setLineColor(Color.ORANGE);
						((LabeledConnectionWidget) findWidget(i)).setForeground(Color.ORANGE);
					}
				}
			}
			return false;
		}
	}

	@Override
	protected Widget attachNodeWidget(String node)
	{
		PlaceWidget widget = new PlaceWidget(this);
		widget.getLabelWidget().setLabel(node);
		
		widget.getLabelWidget().getActions().addAction(editorAction);
		widget.getActions().addAction(createSelectAction());
		widget.getActions().addAction(ActionFactory.createMoveAction());
		widget.getActions().addAction(ActionFactory.createPopupMenuAction(new PopupMenuProvider() {

			@Override
			public JPopupMenu getPopupMenu(Widget widget, Point localLocation)
			{
				final Widget w = widget;
				JPopupMenu menu = new JPopupMenu("Menu");
				JMenuItem jmi = new JMenuItem(new AbstractAction() {

					@Override
					public void actionPerformed(ActionEvent e)
					{
						startPlace = (String) findObject(w);
					}
				});
				jmi.setText("Set as starting place");
				menu.add(jmi);
				jmi = new JMenuItem(new AbstractAction() {

					@Override
					public void actionPerformed(ActionEvent e)
					{
						endPlace = (String) findObject(w);
					}
				});
				jmi.setText("Set as ending place");
				menu.add(jmi);
				return menu;
			}
		}));
		
		mainLayer.addChild(widget);
		return widget;
	}

	@Override
	protected Widget attachEdgeWidget(Integer edge)
	{
		LabeledConnectionWidget connection = new LabeledConnectionWidget(this,
				//RouterFactory.createDirectRouter()
				RouterFactory.createOrthogonalSearchRouter(mainLayer, connectionLayer)
						);
		connection.setTargetAnchorShape(AnchorShape.TRIANGLE_FILLED);
		connection.setEndPointShape(PointShape.NONE);
		connection.getLabelWidget().setLabel("Input weight please");
		
		connection.getActions().addAction(createObjectHoverAction());
		//double-click, the event is consumed while double-clicking only:
		connection.getLabelWidget().getActions().addAction(editorAction);
		//single-click, the event is not consumed:
		connection.getActions().addAction(createSelectAction());
		//mouse-dragged, the event is consumed while mouse is dragged:
		connection.getActions().addAction(reconnetAction);
		connection.getActions().addAction(ActionFactory.createOrthogonalMoveControlPointAction());

		connectionLayer.addChild(connection);
		return connection;
	}

	@Override
	protected void attachEdgeSourceAnchor(Integer edge, String oldSourceNode, String sourceNode)
	{
		Widget w = sourceNode != null ? findWidget(sourceNode) : null;
		((ConnectionWidget) findWidget(edge)).setSourceAnchor(
				AnchorFactory.createRectangularAnchor(w));
	}

	@Override
	protected void attachEdgeTargetAnchor(Integer edge, String oldTargetNode, String targetNode)
	{
		Widget w = targetNode != null ? findWidget(targetNode) : null;
		((ConnectionWidget) findWidget(edge)).setTargetAnchor(
				AnchorFactory.createRectangularAnchor(w));
	}

	@Override
	protected Widget attachPinWidget(String node, String pin)
	{
		PlacePinWidget widget = new PlacePinWidget(this);
		((PlaceWidget) findWidget(node)).attachPinWidget(widget);

		widget.getActions().addAction(createObjectHoverAction());
		widget.getActions().addAction(createSelectAction());
		widget.getActions().addAction(connectAction);

		return widget;
	}

//TODO: add connection weight prompter
	private class SceneConnectProvider implements ConnectProvider
	{
		private String source = null;
		private String target = null;

		@Override
		public boolean isSourceWidget(Widget sourceWidget)
		{
			Object object = findObject(sourceWidget);
			source = isPin(object) ? (String) object : null;
			return source != null;
		}

		@Override
		public ConnectorState isTargetWidget(Widget sourceWidget, Widget targetWidget)
		{
			Object object = findObject(targetWidget);
			target = isPin(object) ? (String) object : null;
			if (target != null)
			{
				return !source.equals(target) ? ConnectorState.ACCEPT : ConnectorState.REJECT_AND_STOP;
			}
			return object != null ? ConnectorState.REJECT_AND_STOP : ConnectorState.REJECT;
		}

		@Override
		public boolean hasCustomTargetWidgetResolver(Scene scene)
		{
			return false;
		}

		@Override
		public Widget resolveTargetWidget(Scene scene, Point sceneLocation)
		{
			return null;
		}

		@Override
		public void createConnection(Widget sourceWidget, Widget targetWidget)
		{
			Integer edge = new Integer(edgeCounter++);
			addEdge(edge);
			setEdgeSource(edge, source);
			setEdgeTarget(edge, target);
		}
	}

	private class SceneReconnectProvider implements ReconnectProvider
	{
		Integer edge;
		String originalNode;
		String replacementNode;

		@Override
		public void reconnectingStarted(ConnectionWidget connectionWidget, boolean reconnectingSource)
		{
		}

		@Override
		public void reconnectingFinished(ConnectionWidget connectionWidget, boolean reconnectingSource)
		{
		}

		@Override
		public boolean isSourceReconnectable(ConnectionWidget connectionWidget)
		{
			Object object = findObject(connectionWidget);
			edge = isEdge(object) ? (Integer) object : null;
			originalNode = edge != null ? getEdgeSource(edge) : null;
			return originalNode != null;
		}

		@Override
		public boolean isTargetReconnectable(ConnectionWidget connectionWidget)
		{
			Object object = findObject(connectionWidget);
			edge = isEdge(object) ? (Integer) object : null;
			originalNode = edge != null ? getEdgeTarget(edge) : null;
			return originalNode != null;
		}

		@Override
		public ConnectorState isReplacementWidget(ConnectionWidget connectionWidget, Widget replacementWidget, boolean reconnectingSource)
		{
			Object object = findObject(replacementWidget);
			replacementNode = isPin(object) ? (String) object : null;
			if (replacementNode != null)
			{
				return ConnectorState.ACCEPT;
			}
			return object != null ? ConnectorState.REJECT_AND_STOP : ConnectorState.REJECT;
		}

		@Override
		public boolean hasCustomReplacementWidgetResolver(Scene scene)
		{
			return false;
		}

		@Override
		public Widget resolveReplacementWidget(Scene scene, Point sceneLocation)
		{
			return null;
		}

		@Override
		public void reconnect(ConnectionWidget connectionWidget, Widget replacementWidget, boolean reconnectingSource)
		{
			if (replacementWidget == null)
			{
				removeEdge(edge);
			} else if (reconnectingSource)
			{
				setEdgeSource(edge, replacementNode);
			} else
			{
				setEdgeTarget(edge, replacementNode);
			}
		}
	}
	
	private class LabelTextFieldEditor implements TextFieldInplaceEditor
	{
		@Override
		public boolean isEnabled(Widget widget)
		{
			return true;
		}

		@Override
		public String getText(Widget widget)
		{
			return ((LabelWidget) widget).getLabel();
		}

		@Override
		public void setText(Widget widget, String text)
		{
			((LabelWidget) widget).setLabel(text);
		}
	}
}