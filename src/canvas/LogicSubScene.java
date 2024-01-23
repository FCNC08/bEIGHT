package canvas;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/*import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;*/

import application.Main;
import canvas.components.CanvasComponent;
import canvas.components.Dot;
import canvas.components.FunctionalCanvasComponent;
import canvas.components.SingleCanvasComponent;
import canvas.components.State;
import canvas.components.StandardComponents.Wire;
import canvas.components.StandardComponents.WireDoublet;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SubScene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.transform.Translate;
import util.OcupationExeption;
import util.ComponentBox;

public class LogicSubScene extends SubScene {

	public static int wire_height = 12;
	public static int dot_radius = 7;
	public static int maxed_dot_radius = 20;
	public static int cross_distance = wire_height * 2;
	public static int memory_outline_thickness = (int) (wire_height*1.5);

	protected static Color black_grey = new Color(0.3, 0.3, 0.3, 1.0);
	protected static Color white_grey = new Color(0.85, 0.85, 0.85, 1.0);
	protected static Color black = new Color(0.0, 0.0, 0.0, 1.0);

	protected Color cross_color = black;

	public static Color focus_square_main = white_grey;
	public static Color focus_square_secondary = black_grey;

	public static boolean actual_set_state = true;

	protected int width;
	protected int height;

	protected int multiplier;

	protected int Start_Width;
	protected int Start_Height;

	protected int max_zoom;

	protected double X = 0;
	protected double Y = 0;

	private int moves_focused_x;
	private int moves_focused_y;
	private int pressed_x;
	private int pressed_y;
	private double moves_x;
	private double moves_y;

	private CanvasComponent last_focused_component = null;

	protected Camera camera;
	protected Translate camera_position;

	private FunctionalCanvasComponent adding_CanvasComponent;
	private WireDoublet adding_WireDoublet;
	protected ComponentBox[][] used;

	private Group root;

	public static void setTheme() {

	}

	public LogicSubScene(Group Mainroot, int StartWidth, int StartHeight, int multiplier) {
		// initializing LogicSubScene with Height
		super(Mainroot, StartWidth, StartHeight);

		max_zoom = (int) ((StartWidth * -0.5 * multiplier) + (cross_distance));

		this.Start_Width = StartWidth;
		this.Start_Height = StartHeight;
		this.multiplier = multiplier;

		int Newwidth = StartWidth * multiplier;
		int Newheight = StartHeight * multiplier;

		if (Newwidth % cross_distance != 0 || Newheight % cross_distance != 0) {
			throw new IllegalArgumentException("The size have to fit with the distance between the crosses");
		}

		width = Newwidth;
		height = Newheight;

		// Array of ComponentBoxes
		// Declares the Componentsids in each point on the area
		used = new ComponentBox[Newwidth / cross_distance][Newheight / cross_distance];

		for (int x = 0; x < used.length; x++) {
			for (int y = 0; y < used[0].length; y++) {
				used[x][y] = new ComponentBox();
			}
		}

		WritableImage Test_Background = generateBackgroundImage();

		Mainroot.getChildren().add(new ImageView(Test_Background));

		// Adding camera and setting it to the middle of the area
		camera_position = new Translate(0, 0, 0);
		camera = new PerspectiveCamera();
		camera.getTransforms().add(camera_position);

		camera_position.setX(width / 4 - cross_distance / 2);
		camera_position.setY(height / 4 - cross_distance / 2);

		setCamera(camera);

		addZTranslate(multiplier);

		root = Mainroot;
		EventHandler<MouseEvent> dragging_Event_Handler = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent me) {
				// Checking which mousebutton is down
				if (me.isSecondaryButtonDown()) {
					addXTranslate(moves_x - (me.getSceneX() - X));
					addYTranslate(moves_y - (me.getSceneY() - Y));
					moves_x = me.getSceneX() - X;
					moves_y = me.getSceneY() - Y;

				} else if (me.isPrimaryButtonDown()) {
					boolean moved = false;
					if (me.getTarget() instanceof ImageView) {
						ImageView view = (ImageView) me.getTarget();
						if (view.getImage() instanceof CanvasComponent) {
							CanvasComponent component = (CanvasComponent) view.getImage();
							if (component == last_focused_component) {
								try {
									move(component, (int) ((me.getSceneX() - X + getXTranslate())), (int) ((me.getSceneY() - Y - 25 + getYTranslate())));
									moves_focused_x = (int) (me.getSceneX() - X + getXTranslate());
									moves_focused_y = (int) (me.getSceneY() - Y - 25 + getYTranslate());
									moved = true;
								} catch (OcupationExeption e) {
									e.printStackTrace();
									moved = false;
								}
							}

						}
					}
					if (adding_WireDoublet != null && !moved) {
						// Checking if a WireDoublet already exists
						try {
							// Trys to remove old WireDoublet(doesn't work) and adding new Wiredoublet with
							// new coordinates
							removeTry(adding_WireDoublet);
							adding_WireDoublet = getWires(pressed_x, pressed_y, (int) (me.getSceneX() - X + getXTranslate()), (int) (me.getSceneY() - Y - 25 + getYTranslate()));
							addTry(adding_WireDoublet);
						} catch (Exception e) {
							System.out.println("Exeption");
							adding_WireDoublet = null;
						}
					} else if (!moved) {
						try {
							// No wiredoublet exists so it creates a new and adds it to the SubScene
							adding_WireDoublet = getWires(pressed_x, pressed_y, (int) (me.getSceneX() - X + getXTranslate()), (int) (me.getSceneY() - Y - 25 + getYTranslate()));
							addTry(adding_WireDoublet);
							System.out.println("Added Wire doublet");
						} catch (Exception e) {
							System.out.println("Exeption");
							adding_WireDoublet = null;
						}
					}
				}
			}
		};
		EventHandler<MouseEvent> press_Event_Handler = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent me) {
				// Checks which Mousebutton is pressed to figure out which action to perform(
				// try to build new Wire/moves Object or moves scene)
				System.out.println("pressed");
				// Setting the coord to createWire/moveScene
				if (me.isPrimaryButtonDown()) {
					moves_focused_x = pressed_x = (int) (me.getSceneX() - X + getXTranslate());
					moves_focused_y = pressed_y = (int) (me.getSceneY() - Y - 25 + getYTranslate());
				} else if (me.isSecondaryButtonDown()) {
					moves_x = me.getSceneX() - X;
					moves_y = me.getSceneY() - Y;
				}

			}
		};

		EventHandler<MouseEvent> released_Mouse_Handler = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent me) {
				// Adding Wiredoublet finally
				if (adding_WireDoublet != null) {
					removeTry(adding_WireDoublet);
					try {
						add(adding_WireDoublet);
					} catch (Exception e) {
						e.printStackTrace();
					}
					adding_WireDoublet = null; // Reset WireDoublet on release
				}

				if (adding_CanvasComponent != null) {
					adding_CanvasComponent = null;
				}
			}
		};
		EventHandler<MouseEvent> click_Event_Handler = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent me) {
				// Used to highlight a component but not working
				System.out.println("click");
				if (me.getButton() == MouseButton.PRIMARY) {
					if (last_focused_component != null) {
						last_focused_component.setFocus(false);
						System.out.println("Entfokused");
						last_focused_component = null;
					}
					if (me.isStillSincePress()) {
						if (me.getTarget() instanceof ImageView) {
							Image img = ((ImageView) me.getTarget()).getImage();
							if (img instanceof CanvasComponent) {
								CanvasComponent comp = (CanvasComponent) img;
								if (!(comp instanceof Dot)) {
									comp.setFocus(true);
									last_focused_component = comp;
								}
							}
						}
					}
				} else if (me.getButton() == MouseButton.SECONDARY) {
					if (me.isStillSincePress()) {
						System.out.println("Still");

						if (me.getTarget() instanceof ImageView) {
							Image img = ((ImageView) me.getTarget()).getImage();
							if (img instanceof CanvasComponent) {
								CanvasComponent component = (CanvasComponent) img;
								component.setRotation(!component.getRotation());
								if (component instanceof Wire) {
									Wire wire = (Wire) component;
									wire.setState(State.getState(wire.getState().mode, !wire.getState().state));
								}
								System.out.println("ROTATE");
							}
						}
					}
				}

			}
		};

		addEventFilter(MouseEvent.MOUSE_PRESSED, press_Event_Handler);
		addEventFilter(MouseEvent.MOUSE_RELEASED, released_Mouse_Handler);
		addEventFilter(MouseEvent.MOUSE_DRAGGED, dragging_Event_Handler);
		addEventFilter(MouseEvent.MOUSE_CLICKED, click_Event_Handler);

		EventHandler<ScrollEvent> zoom_Event_Handler = new EventHandler<ScrollEvent>() {
			@Override
			public void handle(ScrollEvent se) {
				// Adds Z-Translate to Camera(Zooms out/in) using the mousewheel
				addZTranslate(se.getDeltaY());
			}
		};

		addEventFilter(ScrollEvent.SCROLL, zoom_Event_Handler);

	}

	public void add(FunctionalCanvasComponent component) throws OcupationExeption {
		System.out.println("ELEMENT: " + component.getX() + " " + component.getY());
		root.getChildren().add(component.getImageView());
		component.setStandardDotLocations();

		// doesnt contain the outline to make it possible to connect to the dots
		System.out.println(component.getXPoint() + "  " + component.getHeightPoint() + " " + component.getYPoint());
		for (int x = component.getXPoint() + 1; x < (component.getXPoint() + component.getHeightPoint()); x++) {
			for (int y = component.getYPoint() + 1; y < (component.getYPoint() + component.getHeightPoint()); y++) {
				if (used[x][y].HorizontalComponent != null) {
					System.out.println(used[x][y].HorizontalComponent);
					throw new OcupationExeption();
				} else {
					used[x][y].HorizontalComponent = ComponentBox.occupied;
				}
				if (used[x][y].VerticalComponent != null) {
					throw new OcupationExeption();
				} else {
					used[x][y].VerticalComponent = ComponentBox.occupied;
				}
			}
		}
		// Adding each Dot(Communication between wires and Components)
		for (Dot d : component.inputs) {
			add(d);
			d.setState(State.getState(true, Main.random.nextBoolean()));

		}
		for (Dot d : component.outputs) {
			add(d);
		}
		// Adding component to the ID-System
		component.setLogicSubScene(this);
		System.out.println("Add Finally");
	}

	public void addTry(WireDoublet doublet) throws OcupationExeption {
		// Adding each Wire of a WireDoublet
		if (doublet.getHorizontalWire() != null) {
			root.getChildren().add(doublet.getHorizontalWire().getImageView());
		}
		if (doublet.getVerticalWire() != null) {
			root.getChildren().add(doublet.getVerticalWire().getImageView());
		}

	}

	public void add(WireDoublet doublet) throws OcupationExeption {
		// Adding each Wire of a WireDoublet
		add(doublet.getHorizontalWire());
		add(doublet.getVerticalWire());

	}

	public void add(Dot component) throws OcupationExeption {
		if (component != null) {
			if (used[component.point_X][component.point_Y].Dot == null) {
				used[component.point_X][component.point_Y].Dot = component;
			} else {
				throw new OcupationExeption();
			}

			if (used[component.point_X][component.point_Y].VerticalComponent != null && !(used[component.point_X][component.point_Y].VerticalComponent == ComponentBox.occupied)) {
				component.addComponent(used[component.point_X][component.point_Y].VerticalComponent);
				used[component.point_X][component.point_Y].VerticalComponent.addComponent(component);
			}
			if (used[component.point_X][component.point_Y].HorizontalComponent != null && !(used[component.point_X][component.point_Y].HorizontalComponent == ComponentBox.occupied)) {
				component.addComponent(used[component.point_X][component.point_Y].HorizontalComponent);
				used[component.point_X][component.point_Y].HorizontalComponent.addComponent(component);
			}
			component.setLogicSubScene(this);
			root.getChildren().add(component.getImageView());
			// component.printComponents();
			// System.out.println("Line: 519");
		}
	}

	public void add(Wire component) throws OcupationExeption {
		if (component != null) {
			ComponentBox loc_ID;
			System.out.println("Added: " + component);

			// Checking other elements blocking the Wire/connecting wires together
			if (component.rotation == CanvasComponent.HORIZONTAL) {
				// Checking for horizontal and adding the ID to the Horizonzalcomponent in
				// ComponentBox
				int x;
				for (x = component.getXPoint() + 1; x < component.getXPoint() + component.getWidthPoint(); x++) {
					loc_ID = used[x][component.getYPoint()];
					if (loc_ID.HorizontalComponent == null) {
						used[x][component.getYPoint()].HorizontalComponent = component;
						if (loc_ID.VerticalComponent != null) {
							if (loc_ID.VerticalComponent.checkEnd(x, component.getYPoint())) {
								component.addComponent(loc_ID.VerticalComponent);
								loc_ID.VerticalComponent.addComponent(component);
							}
						}
						if (loc_ID.Dot != null) {
							component.addComponent(loc_ID.Dot);
							loc_ID.Dot.addComponent(component);
							
						}
						System.out.println(component);
					} else if (loc_ID.HorizontalComponent == ComponentBox.occupied) {
						System.out.println("Error1");
						throw new OcupationExeption();
					} else {
						System.out.println("Another");
						if (loc_ID.HorizontalComponent.getXPoint() < component.getXPoint()) {
							if (!(loc_ID.HorizontalComponent.getXPoint() + loc_ID.HorizontalComponent.getWidthPoint() > component.getXPoint() + component.getWidthPoint())) {
								Wire wire = new Wire((component.getXPoint() + component.getWidthPoint() - loc_ID.HorizontalComponent.getXPoint()) * cross_distance + wire_height * 3 / 4);
								wire.setYPoint(component.getYPoint());
								wire.setXPoint(loc_ID.HorizontalComponent.getXPoint());
								wire.setRotation(CanvasComponent.HORIZONTAL);
								for (SingleCanvasComponent s : component.getConnectedComponents()) {
									wire.addComponent(s);
									s.addComponent(wire);
								}
								for (SingleCanvasComponent s : loc_ID.HorizontalComponent.getConnectedComponents()) {
									wire.addComponent(s);
									s.addComponent(wire);
								}
								remove(loc_ID.HorizontalComponent);
								remove(component);
								System.out.println(wire + " ");
								add(wire);
								return;
							} else {
								System.out.println(loc_ID.HorizontalComponent + " ");
								remove(component);
								return;
							}
						} else {
							if (!(component.getXPoint() + component.getWidthPoint() > loc_ID.HorizontalComponent.getXPoint() + loc_ID.HorizontalComponent.getWidthPoint())) {
								System.out.println(loc_ID.HorizontalComponent.getXPoint() + loc_ID.HorizontalComponent.getWidthPoint() - component.getXPoint());
								Wire wire = new Wire((loc_ID.HorizontalComponent.getXPoint() + loc_ID.HorizontalComponent.getWidthPoint() - component.getXPoint()) * cross_distance + wire_height * 3 / 4);
								wire.setYPoint(component.getYPoint());
								wire.setXPoint(component.getXPoint());
								wire.setRotation(CanvasComponent.HORIZONTAL);
								for (SingleCanvasComponent s : component.getConnectedComponents()) {
									wire.addComponent(s);
									s.addComponent(wire);
								}
								for (SingleCanvasComponent s : loc_ID.HorizontalComponent.getConnectedComponents()) {
									wire.addComponent(s);
									s.addComponent(wire);
								}
								remove(loc_ID.HorizontalComponent);
								remove(component);
								add(wire);
								return;
							} else {
								used[x][component.getYPoint()].HorizontalComponent = component;
							}
						}
					}
				}
				x = component.getXPoint();
				loc_ID = used[x][component.getYPoint()];
				if (loc_ID.HorizontalComponent == null) {
					used[x][component.getYPoint()].HorizontalComponent = component;
					if (loc_ID.VerticalComponent != null && loc_ID.VerticalComponent != ComponentBox.occupied) {
						component.addComponent(loc_ID.VerticalComponent);
						loc_ID.VerticalComponent.addComponent(component);
						System.out.println("connected");
					}
					if (loc_ID.Dot != null && loc_ID.Dot != ComponentBox.occupied) {
						component.addComponent(loc_ID.Dot);
						loc_ID.Dot.addComponent(component);
					}

					System.out.println(component);
				} else if (loc_ID.HorizontalComponent == ComponentBox.occupied) {
					System.out.println("Error1");
					throw new OcupationExeption();
				} else {
					System.out.println("Another");
					if (loc_ID.HorizontalComponent.getXPoint() < component.getXPoint()) {
						if (!(loc_ID.HorizontalComponent.getXPoint() + loc_ID.HorizontalComponent.getWidthPoint() > component.getXPoint() + component.getWidthPoint())) {
							Wire wire = new Wire((component.getXPoint() + component.getWidthPoint() - loc_ID.HorizontalComponent.getXPoint()) * cross_distance + wire_height * 3 / 4);
							wire.setYPoint(component.getYPoint());
							wire.setXPoint(loc_ID.HorizontalComponent.getXPoint());
							wire.setRotation(CanvasComponent.HORIZONTAL);
							for (SingleCanvasComponent s : component.getConnectedComponents()) {
								wire.addComponent(s);
								s.addComponent(wire);
							}
							for (SingleCanvasComponent s : loc_ID.HorizontalComponent.getConnectedComponents()) {
								wire.addComponent(s);
								s.addComponent(wire);
							}
							remove(loc_ID.HorizontalComponent);
							remove(component);
							System.out.println(wire + " ");
							add(wire);
							return;
						} else {
							System.out.println(loc_ID.HorizontalComponent + " ");
							remove(component);
							return;
						}
					} else {
						if (!(component.getXPoint() + component.getWidthPoint() > loc_ID.HorizontalComponent.getXPoint() + loc_ID.HorizontalComponent.getWidthPoint())) {
							System.out.println(loc_ID.HorizontalComponent.getXPoint() + loc_ID.HorizontalComponent.getWidthPoint() - component.getXPoint());
							Wire wire = new Wire((loc_ID.HorizontalComponent.getXPoint() + loc_ID.HorizontalComponent.getWidthPoint() - component.getXPoint()) * cross_distance + wire_height * 3 / 4);
							wire.setYPoint(component.getYPoint());
							wire.setXPoint(component.getXPoint());
							wire.setRotation(CanvasComponent.HORIZONTAL);
							for (SingleCanvasComponent s : component.getConnectedComponents()) {
								wire.addComponent(s);
								s.addComponent(wire);
							}
							for (SingleCanvasComponent s : loc_ID.HorizontalComponent.getConnectedComponents()) {
								wire.addComponent(s);
								s.addComponent(wire);
							}
							remove(loc_ID.HorizontalComponent);
							remove(component);
							add(wire);
							return;
						} else {
							used[x][component.getYPoint()].HorizontalComponent = component;
						}
					}
				}
				x = component.getXPoint() + component.getWidthPoint();
				loc_ID = used[x][component.getYPoint()];
				if (loc_ID.HorizontalComponent == null) {
					used[x][component.getYPoint()].HorizontalComponent = component;
					if (loc_ID.VerticalComponent != null && loc_ID.VerticalComponent != ComponentBox.occupied) {
						component.addComponent(loc_ID.VerticalComponent);
						loc_ID.VerticalComponent.addComponent(component);
					}
					if (loc_ID.Dot != null && loc_ID.Dot != ComponentBox.occupied) {
						component.addComponent(loc_ID.Dot);
						loc_ID.Dot.addComponent(component);
					}

					System.out.println(component);
				} else if (loc_ID.HorizontalComponent == ComponentBox.occupied) {
					System.out.println("Error1");
					throw new OcupationExeption();
				} else {
					System.out.println("Another");
					if (loc_ID.HorizontalComponent.getXPoint() < component.getXPoint()) {
						if (!(loc_ID.HorizontalComponent.getXPoint() + loc_ID.HorizontalComponent.getWidthPoint() > component.getXPoint() + component.getWidthPoint())) {
							Wire wire = new Wire((component.getXPoint() + component.getWidthPoint() - loc_ID.HorizontalComponent.getXPoint()) * cross_distance + wire_height * 3 / 4);
							wire.setYPoint(component.getYPoint());
							wire.setXPoint(loc_ID.HorizontalComponent.getXPoint());
							wire.setRotation(CanvasComponent.HORIZONTAL);
							for (SingleCanvasComponent s : component.getConnectedComponents()) {
								wire.addComponent(s);
								s.addComponent(wire);
							}
							for (SingleCanvasComponent s : loc_ID.HorizontalComponent.getConnectedComponents()) {
								wire.addComponent(s);
								s.addComponent(wire);
							}
							remove(loc_ID.HorizontalComponent);
							remove(component);
							System.out.println(wire + " ");
							add(wire);
							return;
						} else {
							System.out.println(loc_ID.HorizontalComponent + " ");
							remove(component);
							return;
						}
					} else {
						if (!(component.getXPoint() + component.getWidthPoint() > loc_ID.HorizontalComponent.getXPoint() + loc_ID.HorizontalComponent.getWidthPoint())) {
							System.out.println(loc_ID.HorizontalComponent.getXPoint() + loc_ID.HorizontalComponent.getWidthPoint() - component.getXPoint());
							Wire wire = new Wire((loc_ID.HorizontalComponent.getXPoint() + loc_ID.HorizontalComponent.getWidthPoint() - component.getXPoint()) * cross_distance + wire_height * 3 / 4);
							wire.setYPoint(component.getYPoint());
							wire.setXPoint(component.getXPoint());
							wire.setRotation(CanvasComponent.HORIZONTAL);
							for (SingleCanvasComponent s : component.getConnectedComponents()) {
								wire.addComponent(s);
								s.addComponent(wire);
							}
							for (SingleCanvasComponent s : loc_ID.HorizontalComponent.getConnectedComponents()) {
								wire.addComponent(s);
								s.addComponent(wire);
							}
							remove(loc_ID.HorizontalComponent);
							remove(component);
							add(wire);
							return;
						} else {
							used[x][component.getYPoint()].HorizontalComponent = component;
						}
					}
				}

			} else {
				// Same as for Horizontal with Vertical
				int y;
				for (y = component.getYPoint() + 1; y < component.getYPoint() + component.getWidthPoint(); y++) {
					loc_ID = used[component.getXPoint()][y];
					if (loc_ID.VerticalComponent == null) {
						used[component.getXPoint()][y].VerticalComponent = component;
						if (loc_ID.HorizontalComponent != null) {
							if (loc_ID.HorizontalComponent.checkEnd(component.getXPoint(), y)) {
								component.addComponent(loc_ID.HorizontalComponent);
								loc_ID.HorizontalComponent.addComponent(component);
							}
						}
						if (loc_ID.Dot != null) {
							if (loc_ID.Dot.checkEnd(component.getXPoint(), y)) {
								component.addComponent(loc_ID.Dot);
								loc_ID.Dot.addComponent(component);
							}
						}

					} else if (loc_ID.VerticalComponent == ComponentBox.occupied) {
						throw new OcupationExeption();
					} else {
						if (loc_ID.VerticalComponent.getYPoint() < component.getYPoint()) {
							if (!(loc_ID.VerticalComponent.getYPoint() + loc_ID.VerticalComponent.getWidthPoint() > component.getYPoint() + component.getWidthPoint())) {
								System.out.println(component.getYPoint() + component.getWidthPoint() - loc_ID.VerticalComponent.getYPoint());
								Wire wire = new Wire((component.getYPoint() + component.getWidthPoint() - loc_ID.VerticalComponent.getYPoint()) * cross_distance + wire_height * 3 / 4);
								wire.setXPoint(component.getXPoint());
								wire.setYPoint(loc_ID.VerticalComponent.getYPoint());
								wire.setRotation(CanvasComponent.VERTICAL);
								for (SingleCanvasComponent s : component.getConnectedComponents()) {
									wire.addComponent(s);
									s.addComponent(wire);
								}
								for (SingleCanvasComponent s : loc_ID.VerticalComponent.getConnectedComponents()) {
									wire.addComponent(s);
									s.addComponent(wire);
								}
								remove(loc_ID.VerticalComponent);
								remove(component);
								System.out.println(wire + " ");
								add(wire);
								return;
							} else {
								remove(component);
								return;
							}
						} else {
							if (!(component.getYPoint() + component.getWidthPoint() > loc_ID.VerticalComponent.getYPoint() + loc_ID.VerticalComponent.getWidthPoint())) {
								System.out.println(loc_ID.VerticalComponent.getYPoint() + loc_ID.VerticalComponent.getWidthPoint() - component.getYPoint());
								Wire wire = new Wire((loc_ID.VerticalComponent.getYPoint() + loc_ID.VerticalComponent.getWidthPoint() - component.getYPoint()) * cross_distance + wire_height * 3 / 4);
								wire.setXPoint(component.getXPoint());
								wire.setYPoint(component.getYPoint());
								wire.setRotation(CanvasComponent.VERTICAL);
								for (SingleCanvasComponent s : component.getConnectedComponents()) {
									wire.addComponent(s);
									s.addComponent(wire);
								}
								for (SingleCanvasComponent s : loc_ID.VerticalComponent.getConnectedComponents()) {
									wire.addComponent(s);
									s.addComponent(wire);
								}
								remove(loc_ID.VerticalComponent);
								remove(component);
								add(wire);
								return;
							} else {
								used[component.getXPoint()][y].VerticalComponent = component;
							}
						}
					}

					y = component.getYPoint();
					loc_ID = used[component.getXPoint()][y];
					if (loc_ID.VerticalComponent == null) {
						used[component.getXPoint()][y].VerticalComponent = component;
						if (loc_ID.HorizontalComponent != null && loc_ID.HorizontalComponent != ComponentBox.occupied) {
							component.addComponent(loc_ID.HorizontalComponent);
							loc_ID.HorizontalComponent.addComponent(component);
						}
						if (loc_ID.Dot != null && loc_ID.Dot != ComponentBox.occupied) {
							component.addComponent(loc_ID.Dot);
							loc_ID.Dot.addComponent(component);
						}
					} else if (loc_ID.VerticalComponent == ComponentBox.occupied) {
						throw new OcupationExeption();
					} else {
						if (loc_ID.VerticalComponent.getYPoint() < component.getYPoint()) {
							if (!(loc_ID.VerticalComponent.getYPoint() + loc_ID.VerticalComponent.getWidthPoint() > component.getYPoint() + component.getWidthPoint())) {
								System.out.println(component.getYPoint() + component.getWidthPoint() - loc_ID.VerticalComponent.getYPoint());
								Wire wire = new Wire((component.getYPoint() + component.getWidthPoint() - loc_ID.VerticalComponent.getYPoint()) * cross_distance + wire_height * 3 / 4);
								wire.setXPoint(component.getXPoint());
								wire.setYPoint(loc_ID.VerticalComponent.getYPoint());
								wire.setRotation(CanvasComponent.VERTICAL);
								for (SingleCanvasComponent s : component.getConnectedComponents()) {
									wire.addComponent(s);
									s.addComponent(wire);
								}
								for (SingleCanvasComponent s : loc_ID.VerticalComponent.getConnectedComponents()) {
									wire.addComponent(s);
									s.addComponent(wire);
								}
								remove(loc_ID.VerticalComponent);
								remove(component);
								System.out.println(wire + " ");
								add(wire);
								return;
							} else {
								remove(component);
								return;
							}
						} else {
							if (!(component.getYPoint() + component.getWidthPoint() > loc_ID.VerticalComponent.getYPoint() + loc_ID.VerticalComponent.getWidthPoint())) {
								System.out.println(loc_ID.VerticalComponent.getYPoint() + loc_ID.VerticalComponent.getWidthPoint() - component.getYPoint());
								Wire wire = new Wire((loc_ID.VerticalComponent.getYPoint() + loc_ID.VerticalComponent.getWidthPoint() - component.getYPoint()) * cross_distance + wire_height * 3 / 4);
								wire.setXPoint(component.getXPoint());
								wire.setYPoint(component.getYPoint());
								wire.setRotation(CanvasComponent.VERTICAL);
								for (SingleCanvasComponent s : component.getConnectedComponents()) {
									wire.addComponent(s);
									s.addComponent(wire);
								}
								for (SingleCanvasComponent s : loc_ID.VerticalComponent.getConnectedComponents()) {
									wire.addComponent(s);
									s.addComponent(wire);
								}
								remove(loc_ID.VerticalComponent);
								remove(component);
								add(wire);
								return;
							} else {
								used[component.getXPoint()][y].VerticalComponent = component;
							}
						}
					}
					y = component.getYPoint() + component.getWidthPoint();
					loc_ID = used[component.getXPoint()][y];
					if (loc_ID.VerticalComponent == null) {
						used[component.getXPoint()][y].VerticalComponent = component;
						if (loc_ID.HorizontalComponent != null && loc_ID.HorizontalComponent != ComponentBox.occupied) {
							component.addComponent(loc_ID.HorizontalComponent);
							loc_ID.HorizontalComponent.addComponent(component);
						}
						if (loc_ID.Dot != null && loc_ID.Dot != ComponentBox.occupied) {
							component.addComponent(loc_ID.Dot);
							loc_ID.Dot.addComponent(component);
						}
					} else if (loc_ID.VerticalComponent == ComponentBox.occupied) {
						throw new OcupationExeption();
					} else {
						if (loc_ID.VerticalComponent.getYPoint() < component.getYPoint()) {
							if (!(loc_ID.VerticalComponent.getYPoint() + loc_ID.VerticalComponent.getWidthPoint() > component.getYPoint() + component.getWidthPoint())) {
								System.out.println(component.getYPoint() + component.getWidthPoint() - loc_ID.VerticalComponent.getYPoint());
								Wire wire = new Wire((component.getYPoint() + component.getWidthPoint() - loc_ID.VerticalComponent.getYPoint()) * cross_distance + wire_height * 3 / 4);
								wire.setXPoint(component.getXPoint());
								wire.setYPoint(loc_ID.VerticalComponent.getYPoint());
								wire.setRotation(CanvasComponent.VERTICAL);
								for (SingleCanvasComponent s : component.getConnectedComponents()) {
									wire.addComponent(s);
									s.addComponent(wire);
								}
								for (SingleCanvasComponent s : loc_ID.VerticalComponent.getConnectedComponents()) {
									wire.addComponent(s);
									s.addComponent(wire);
								}
								remove(loc_ID.VerticalComponent);
								remove(component);
								System.out.println(wire + " ");
								add(wire);
								return;
							} else {
								remove(component);
								return;
							}
						} else {
							if (!(component.getYPoint() + component.getWidthPoint() > loc_ID.VerticalComponent.getYPoint() + loc_ID.VerticalComponent.getWidthPoint())) {
								System.out.println(loc_ID.VerticalComponent.getYPoint() + loc_ID.VerticalComponent.getWidthPoint() - component.getYPoint());
								Wire wire = new Wire((loc_ID.VerticalComponent.getYPoint() + loc_ID.VerticalComponent.getWidthPoint() - component.getYPoint()) * cross_distance + wire_height * 3 / 4);
								wire.setXPoint(component.getXPoint());
								wire.setYPoint(component.getYPoint());
								wire.setRotation(CanvasComponent.VERTICAL);
								for (SingleCanvasComponent s : component.getConnectedComponents()) {
									wire.addComponent(s);
									s.addComponent(wire);
								}
								for (SingleCanvasComponent s : loc_ID.VerticalComponent.getConnectedComponents()) {
									wire.addComponent(s);
									s.addComponent(wire);
								}
								remove(loc_ID.VerticalComponent);
								remove(component);
								add(wire);
								return;
							} else {
								used[component.getXPoint()][y].VerticalComponent = component;
							}
						}
					}
				}

			}
			// Adding component to SubSCene
			component.setLogicSubScene(this);
			root.getChildren().add(component.getImageView());
			System.out.println(component.getX() + " " + component.getY());
			System.out.println(root.getChildren().contains(component.getImageView()) + " ");
			System.out.println(component.getImageView());
			component.printComponents();
		}
	}

	public void remove(CanvasComponent component) {
		if (component instanceof FunctionalCanvasComponent) {
			remove((FunctionalCanvasComponent) component);
		} else if (component instanceof Dot) {
			remove((Dot) component);
		} else if (component instanceof SingleCanvasComponent) {
			remove((SingleCanvasComponent) component);
		} else {
			root.getChildren().remove(component.getImageView());
		}
	}

	public void remove(FunctionalCanvasComponent component) {
		// Removing similar to adding in opposition
		for (Dot d : component.inputs) {
			remove(d);
		}
		for (Dot d : component.outputs) {
			remove(d);
		}
		if (component.rotation == CanvasComponent.HORIZONTAL) {
			for (int x = component.getXPoint()+1; x < (component.getXPoint() + component.getWidthPoint()); x++) {
				for (int y = component.getYPoint()+1; y < (component.getYPoint() + component.getHeightPoint()); y++) {
					if(used[x][y].HorizontalComponent == ComponentBox.occupied) {
						used[x][y].HorizontalComponent = null;
					}
					if(used[x][y].VerticalComponent == ComponentBox.occupied) {
						used[x][y].VerticalComponent = null;
					}
				}
			}
		} else {
			for (int x = component.getXPoint()+1; x < (component.getXPoint() + component.getHeightPoint()); x++) {
				for (int y = component.getYPoint()+1; y < (component.getYPoint() + component.getWidthPoint()); y++) {
					if(used[x][y].HorizontalComponent == ComponentBox.occupied) {
						used[x][y].HorizontalComponent = null;
					}
					if(used[x][y].VerticalComponent == ComponentBox.occupied) {
						used[x][y].VerticalComponent = null;
					}
				}
			}
			
		}
		root.getChildren().remove(component.getImageView());
	}

	public void removeTry(WireDoublet doublet) {
		if (doublet.getHorizontalWire() != null) {
			root.getChildren().remove(doublet.getHorizontalWire().getImageView());
		}
		if (doublet.getVerticalWire() != null) {
			root.getChildren().remove(doublet.getVerticalWire().getImageView());
		}
	}

	public void remove(WireDoublet doublet) {
		if (doublet.getHorizontalWire() != null) {
			remove(doublet.getHorizontalWire());
		}
		if (doublet.getVerticalWire() != null) {
			remove(doublet.getVerticalWire());
		}
	}

	public void remove(Dot component) {
		if (component != null) {
			if (used[component.point_X][component.point_Y].Dot == component) {
				used[component.point_X][component.point_Y].Dot = null;
			}
			for (SingleCanvasComponent i : component.getConnectedComponents()) {
				i.removeComponent(component);
				component.removeComponent(i);
			}
			root.getChildren().remove(component.getImageView());
		}
	}

	public void remove(SingleCanvasComponent component) {
		if (component != null) {
			if (component.rotation == CanvasComponent.HORIZONTAL) {
				// Removing Horizontal/Vertical ID from used
				for (int x = component.getXPoint(); x <= component.getXPoint() + component.getWidthPoint(); x++) {
					if (used[x][component.getYPoint()].HorizontalComponent == component) {
						used[x][component.getYPoint()].HorizontalComponent = null;
					}
				}
			} else {
				for (int y = component.getYPoint(); y <= component.getYPoint() + component.getWidthPoint(); y++) {
					if (used[component.getXPoint()][y].VerticalComponent == component) {
						used[component.getXPoint()][y].VerticalComponent = null;
					}
				}
			}
			for (SingleCanvasComponent i : component.getConnectedComponents()) {
				i.removeComponent(component);
				component.removeComponent(i);
			}
			root.getChildren().remove(component.getImageView());
		}
	}

	public void move(CanvasComponent component, int new_X, int new_Y) throws OcupationExeption {
		if (component instanceof FunctionalCanvasComponent) {
			move((FunctionalCanvasComponent) component, new_X, new_Y);
		} else if (component instanceof Dot) {
			move((Dot) component, new_X, new_Y);
		} else if (component instanceof Wire) {
			move((Wire) component, new_X, new_Y);
		} else {
			System.out.println("Unknown Component");
		}
	}

	public void move(FunctionalCanvasComponent component, int new_X, int new_Y) throws OcupationExeption {
		if (component.point_X_rest + new_X > cross_distance / 2 || component.point_Y_rest + new_Y > cross_distance / 2) {
			// Removing blockings from used
			if (component.rotation == CanvasComponent.HORIZONTAL) {
				for (int x = component.getXPoint()+1; x < (component.getXPoint() + component.getWidthPoint()); x++) {
					for (int y = component.getYPoint()+1; y < (component.getYPoint() + component.getHeightPoint()); y++) {
						if(used[x][y].HorizontalComponent == ComponentBox.occupied) {
							used[x][y].HorizontalComponent = null;
						}
						if(used[x][y].VerticalComponent == ComponentBox.occupied) {
							used[x][y].VerticalComponent = null;
						}
					}
				}
			} else {
				for (int x = component.getXPoint()+1; x < (component.getXPoint() + component.getHeightPoint()); x++) {
					for (int y = component.getYPoint()+1; y < (component.getYPoint() + component.getWidthPoint()); y++) {
						if(used[x][y].HorizontalComponent == ComponentBox.occupied) {
							used[x][y].HorizontalComponent = null;
						}
						if(used[x][y].VerticalComponent == ComponentBox.occupied) {
							used[x][y].VerticalComponent = null;
						}
					}
				}
				
			}
			for(Dot d: component.inputs) {
				remove(d);
			}
			for(Dot d: component.outputs) {
				remove(d);
			}
			component.setX(new_X);
			component.setY(new_Y);
			
			for(Dot d: component.inputs) {
				add(d);
			}
			for(Dot d: component.outputs) {
				add(d);
			}
			
			if(component.rotation == CanvasComponent.HORIZONTAL) {
				for(int x = component.getXPoint()+1; x < (component.getXPoint()+component.getWidthPoint()); x++) {
					for(int y = component.getYPoint()+1; y < (component.getYPoint()+component.getHeightPoint()); y++) {
						if(used[x][y].HorizontalComponent == null) {
							used[x][y].HorizontalComponent = ComponentBox.occupied;
						}else {
							throw new OcupationExeption();
						}
						if(used[x][y].VerticalComponent == null) {
							used[x][y].VerticalComponent = ComponentBox.occupied;
						}else {
							throw new OcupationExeption();
						}
					}
				}
			}else {
				for(int x = component.getXPoint()+1; x < (component.getXPoint()+component.getHeightPoint()); x++) {
					for(int y = component.getYPoint()+1; y<(component.getYPoint()+component.getWidthPoint()); y++) {
						if(used[x][y].HorizontalComponent == null) {
							used[x][y].HorizontalComponent = ComponentBox.occupied;
						}else {
							throw new OcupationExeption();
						}
						if(used[x][y].VerticalComponent == null) {
							used[x][y].VerticalComponent = ComponentBox.occupied;
						}else {
							throw new OcupationExeption();
						}
					}
				}
			}

		}

	}

	public void move(Dot component, int new_X, int new_Y) throws OcupationExeption {
		// Removing Dot from used
		remove(component);
		component.addX(new_X);
		component.addY(new_Y);
		// Adding Dot to used
		add(component);
	}

	public void move(Wire component, int new_X, int new_Y) throws OcupationExeption {
		// Removing component from used
		if (component.rotation == CanvasComponent.HORIZONTAL) {
			// Removing Horizontal/Vertical ID from used
			for (int x = component.getXPoint(); x <= component.getXPoint() + component.getWidthPoint(); x++) {
				used[x][component.getYPoint()].HorizontalComponent = null;
			}
		} else {
			for (int y = component.getYPoint(); y <= component.getYPoint() + component.getWidthPoint(); y++) {
				used[component.getXPoint()][y].VerticalComponent = null;
			}
		}
		for (SingleCanvasComponent i : component.getConnectedComponents()) {
			i.removeComponent(component);
			component.removeComponent(i);
		}

		component.setX(new_X);
		component.setY(new_Y);
		add(component);
	}

	public static LogicSubScene init(int start_width, int start_height, int multiplier) {
		// Initializing LogicSubScene with a new Group
		return new LogicSubScene(new Group(), start_width, start_height, multiplier);
	}

	protected WritableImage generateBackgroundImage() {
		// Generates BackgroundImage with crosses every cross_distance
		WritableImage image = new WritableImage(width, height);
		PixelWriter writer = image.getPixelWriter();
		for (int x = 0; x <= width - 1; x += cross_distance) {
			for (int y = 0; y <= height - 1; y += cross_distance) {
				writer.setColor(x + 1, y, black);
				writer.setColor(x, y + 1, black);
				writer.setColor(x, y, black);

				if (x >= 1) {
					writer.setColor(x - 1, y, black);
				}
				if (y >= 1) {
					writer.setColor(x, y - 1, black);
				}
			}
		}
		return image;
	}

	public static int roundToNextDot(int coordinat) throws IllegalArgumentException {
		// Check nearest dot but throwing error if it is too far away
		int overflow = coordinat % cross_distance;
		if (overflow <= cross_distance / 3) {
			return coordinat - overflow;
		} else if (cross_distance - overflow <= cross_distance / 3) {
			return coordinat + cross_distance - overflow;
		} else {
			throw new IllegalArgumentException();
		}
	}

	public static int getNearesDot(int coordinat) {
		// returning nearest dot
		int overflow = coordinat % cross_distance;
		if (overflow <= cross_distance / 2) {
			return coordinat - overflow;
		} else {
			return coordinat + cross_distance - overflow;
		}
	}

	public WireDoublet getWires(int start_X, int start_Y, int end_X, int end_Y) throws IllegalArgumentException {
		// Generating WireDoublet with two Wires
		boolean error = false;
		int round_start_x;
		int round_start_y;
		try {
			round_start_x = roundToNextDot(start_X);
		} catch (Exception e) {
			round_start_x = getNearesDot(start_X);
			error = true;
		}
		try {
			round_start_y = roundToNextDot(start_Y);
		} catch (Exception e) {
			if (error) {
				throw new IllegalArgumentException();
			} else {
				round_start_y = getNearesDot(start_X);
			}
		}
		int round_end_x = getNearesDot(end_X);
		int round_end_y = getNearesDot(end_Y);

		WireDoublet doublet = new WireDoublet();

		// Checking if there is a Horizontal Wire and generating it if yes
		if (round_start_x != round_end_x) {
			Wire wire_horizontal = new Wire(Math.abs(round_start_x - round_end_x) + wire_height * 3 / 4);

			// Location relative to point and minus half of
			if (round_start_x >= round_end_x) {
				wire_horizontal.setX(round_end_x);
			} else {
				wire_horizontal.setX(round_start_x);
			}
			wire_horizontal.setY(round_start_y);

			wire_horizontal.setState(CanvasComponent.UNSET);

			doublet.setHorizontalWire(wire_horizontal);
			wire_horizontal.setRotation(CanvasComponent.HORIZONTAL);
		}
		// Checking if there is a vertical Wire and generating it if yes
		if (round_start_y != round_end_y) {
			Wire wire_vertical = new Wire(Math.abs(round_start_y - round_end_y) + wire_height * 3 / 4);
			if (round_start_y >= round_end_y) {
				wire_vertical.setY(round_end_y);
			} else {
				wire_vertical.setY(round_start_y);
			}
			wire_vertical.setX(round_end_x);

			wire_vertical.setState(CanvasComponent.UNSET);
			wire_vertical.setRotation(CanvasComponent.VERTICAL);

			doublet.setVerticalWire(wire_vertical);
		}
		return doublet;
	}

	// Moving whole SubScene in X/Y direction
	public void addX(int ADD_X) {
		X = getLayoutX() + ADD_X;
		setLayoutX(X);
	}

	public void addY(int ADD_Y) {
		Y = getLayoutY() + ADD_Y;
		setLayoutY(Y);
	}

	// Getting current movement
	public double getX() {
		return X;
	}

	public double getY() {
		return Y;
	}

	// Setting Zoom to standard
	public void setStandardZoom() {
		camera_position.setZ(multiplier);
	}

	// Adding Camera Translate
	public void addZTranslate(double z) {
		double Z_Postion = camera_position.getZ() + z;
		if (Z_Postion <= max_zoom) {
		} else {
			camera_position.setZ(Z_Postion);
		}
		checkXYTanslate();
	}

	public void addXTranslate(double x) {
		double X_Postion = camera_position.getX() + x;
		if (X_Postion < 0 || X_Postion > (width - Start_Width)) {
		} else {
			camera_position.setX(X_Postion);
		}
	}

	public void addYTranslate(double y) {
		double Y_Position = camera_position.getY() + y;
		if (Y_Position < 0 || Y_Position > (height - Start_Height)) {
		} else {
			camera_position.setY(Y_Position);
		}
	}

	public void checkXYTanslate() {
		// TODO prevent CameraPosition to move outside depending on zoom
		if (getZTranslate() / -2 > getXTranslate()) {
			camera_position.setX(getZTranslate() / -2);
			System.out.println(getXTranslate() + "X-Coord");
		}
	}

	// Getting camera Translate
	public double getZTranslate() {
		return camera_position.getZ();
	}

	public double getXTranslate() {
		return camera_position.getX();
	}

	public double getYTranslate() {
		return camera_position.getY();
	}

	public void triggerKeyEvent(KeyEvent ke) {
		if ((ke.getCode() == KeyCode.DELETE || ke.getCode() == KeyCode.BACK_SPACE) && last_focused_component != null) {
			remove(last_focused_component);
		}
	}

	public void SaveAsPDF(String filepath) {
		// Creating PDF out of SubScene
		/*File temp_image_file = new File("temp.png");
		try {
			double start_Z = getZTranslate();

			// camera_position.setZ(max_zoom);

			WritableImage image = this.snapshot(null, null);
			BufferedImage buff_image = SwingFXUtils.fromFXImage(image, null);

			// Create a new PDF document
			PDDocument document = new PDDocument();
			PDPage page = new PDPage();
			document.addPage(page);

			// Create a content stream for writing to the page
			PDPageContentStream contentStream = new PDPageContentStream(document, page);

			// Draw the image on the page
			contentStream.drawImage(LosslessFactory.createFromImage(document, buff_image), 0, 0, page.getMediaBox().getWidth(), page.getMediaBox().getHeight());

			// Close the content stream
			contentStream.close();

			// Save the document to the output file
			document.save(filepath);

			// Close the document
			document.close();

			camera_position.setZ(start_Z);

		} catch (IOException e) {
			e.printStackTrace();
		}
		temp_image_file.delete();*/
	}
}
