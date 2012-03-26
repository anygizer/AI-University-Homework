package imy.lnu.ai.dfs;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import javax.swing.AbstractAction;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.netbeans.api.visual.action.*;
import org.netbeans.api.visual.anchor.AnchorFactory;
import org.netbeans.api.visual.anchor.AnchorShape;
import org.netbeans.api.visual.anchor.PointShape;
import org.netbeans.api.visual.graph.GraphPinScene;
import org.netbeans.api.visual.router.RouterFactory;
import org.netbeans.api.visual.widget.*;
import org.unikernel.lnu.ai.graph.Graph;
import org.unikernel.lnu.ai.graph.Vertex;
import org.unikernel.lnu.ai.agents.Algorithm;
import org.unikernel.lnu.ai.agents.DFS;

/**
 *
 * @author mcangel
 */
public class DFSGraphScene extends GraphPinScene<Vertex, Integer, String> implements PropertyChangeListener
{
	private LayerWidget mainLayer = new LayerWidget(this);
	private LayerWidget connectionLayer = new LayerWidget(this);
	private LayerWidget interractionLayer = new LayerWidget(this);
	private int edgeCounter = 0;
	private WidgetAction connectAction = ActionFactory.createConnectAction(interractionLayer, new SceneConnectProvider());
	private WidgetAction reconnetAction = ActionFactory.createReconnectAction(new SceneReconnectProvider());
	private WidgetAction editorAction = ActionFactory.createInplaceEditorAction(new LabelTextFieldEditor());
	private final static String NAME_TEMPLATE = "Place";
	private long newNameCounter = 1;
	private Graph g;
	private Algorithm alg;

	public DFSGraphScene()
	{
		g = new Graph();
		g.addPropertyChangeListener(this);
		
		alg = new DFS(g);
		
		this.addChild(mainLayer);
		this.addChild(connectionLayer);
		this.addChild(interractionLayer);
		
		getActions().addAction(ActionFactory.createPopupMenuAction(new PopupMenuProvider() {

			@Override
			public JPopupMenu getPopupMenu(final Widget widget, Point localLocation)
			{
				final Point localPoint = localLocation;
				JPopupMenu menu = new JPopupMenu("Menu");
				JMenuItem jmi = new JMenuItem(new AbstractAction() {

					@Override
					public void actionPerformed(ActionEvent e)
					{
						g.addVertex(new Vertex(NAME_TEMPLATE + newNameCounter++, localPoint));
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
								Object[] way = alg.search().toArray();
								drawWay(alg.getWalkedTrough().toArray(), Color.orange);
								drawWay(way, Color.red);
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

	private String getNodeFirstPin(Vertex node)
	{
		return (String)(getNodePins(node).toArray()[0]);
	}
	
	private Integer findFirstEdgeBetween(String sourcePin, String targetPin)
	{
		Collection<Integer> edgesBetween = findEdgesBetween(sourcePin, targetPin);
		if(edgesBetween.isEmpty())
		{
			return null;
		}
		return (Integer)(edgesBetween.toArray()[0]);
	}
	
	private void drawWay(Object[] way, Color color)
	{
		for (int i = 0; i < way.length - 1; i++)
		{
			Integer edge = findFirstEdgeBetween(
					getNodeFirstPin((Vertex) way[i]),
					getNodeFirstPin((Vertex) way[i + 1]));
			if(edge != null)
			{
				findWidget(edge).setForeground(color);
			}
		}
	}
	
	@Override
	protected Widget attachNodeWidget(Vertex node)
	{
		PlaceWidget widget = new PlaceWidget(this);
		widget.getLabelWidget().setLabel(node.getLabel());
		
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
						alg.setStartVertex((Vertex) findObject(w));
					}
				});
				jmi.setText("Set as starting place");
				menu.add(jmi);
				jmi = new JMenuItem(new AbstractAction() {

					@Override
					public void actionPerformed(ActionEvent e)
					{
						alg.setEndVertex((Vertex) findObject(w));
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
		connection.setSourceAnchorShape(AnchorShape.NONE);
		connection.setTargetAnchorShape(AnchorShape.NONE);
		connection.setEndPointShape(PointShape.NONE);
		//connection.getLabelWidget().setLabel("Input weight please");
		
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
	protected Widget attachPinWidget(Vertex node, String pin)
	{
		PlacePinWidget widget = new PlacePinWidget(this);
		((PlaceWidget) findWidget(node)).attachPinWidget(widget);

		widget.getActions().addAction(createObjectHoverAction());
		widget.getActions().addAction(createSelectAction());
		widget.getActions().addAction(connectAction);

		return widget;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt)
	{
		if (Graph.PROP_VERT_ADDED.equals(evt.getPropertyName()))
		{
			Vertex v = (Vertex) evt.getNewValue();
			addNode(v).setPreferredLocation(v.getPreferredLocation());
			addPin(v, v.getLabel() + " pin");
			validate();
		}
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
			g.connectVertices(getPinNode(source), getPinNode(target), edgeCounter);
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