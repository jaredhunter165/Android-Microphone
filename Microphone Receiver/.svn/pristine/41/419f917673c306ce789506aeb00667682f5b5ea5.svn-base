//Copyright 2004, (c) Javera Software, LLC. as an unpublished work.  All rights reserved world-wide.
//This is a proprietary trade secret of Javera Software LLC.  Use restricted to licensing terms.

package edu.iup.cosc.mic.server.ui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.accessibility.Accessible;

/**
 * A Javera box layout arranges components in a vertical or horizontal format.
 * For vertical format all components are resized to have the same width of
 * the container, but have a height that is in proportion to their assigned
 * weights and such that the total height of all compoenents fills the height
 * of the container.  Likewise a horizontal format will resize components to
 * have the same width as the container and a height that is in proportion to
 * the assigned weights.  If a component has an assigned weight of 0 then that
 * components is sized according to its preferred size.
 *
 * Weights are assigned using a Double as the second argument to the
 * containers add method.  A null second argument will be interpreted as a
 * zero weight assignement.
 *
 * A Javera box layout is similar in function to a Box layout except that the
 * assigned weights, not glue or preferred sizes are used to distribute space
 * over the row or column.  Javera box layout also provides gap
 * separation and margins.
 *
 * @author David T. Smith
 */
public class JvBoxLayout implements LayoutManager2, Serializable {
	/**
	 * Horizontal axis orientation
	 */
	public final static int X_AXIS = 0;

	/**
	 * Vertical axis orientation
	 */

	public final static int Y_AXIS = 1;

	/**
	 * The axis orientation of the weighted layout.
	 */
	private int axis;

	/**
	 * The weighted layout manager allows a seperation of components with
	 * gaps.  The gap will specify the space between components.
	 *
	 * @serial
	 * @see getGap
	 * @see setGap
	 */
	private int gap;

	/**
	 * The weighted layout manager allows specification of a top margin to be
	 * reserved above laidout components.
	 *
	 * @serial
	 * @see getTopMargin
	 * @see setTopMargin
	 */
	private int topMargin;

	/**
	 * The command button layout manager allows specification of a bottom
	 * margin to be reserved below laidout components.
	 *
	 * @serial
	 * @see getBottomMargin
	 * @see setBottomMargin
	 */
	private int bottomMargin;

	/**
	 * The weighted layout manager allows specification of a left margin to be
	 * reserved to the left of the leftmost component.
	 *
	 * @serial
	 * @see getLeftMargin
	 * @see setLeftMargin
	 */
	private int leftMargin;

	/**
	 * The weighted layout manager allows specification of a right margin to
	 * be reserved to the right of the rightmost component.
	 *
	 * @serial
	 * @see getRightMargin
	 * @see setRightMargin
	 */
	private int rightMargin;

	/**
	 * A Map of each component's weight, keyed on the component
	 * objects themselves.
	 */
	private Map weightMap = new HashMap();

	/**
	 * Constructs a new Weighted Layout with specified axis, a default 5 pixel
	 * gap, and a 5 pixel margins.
	 *
	 * @param axis X_AXIS or Y_AXIS
	 */
	public JvBoxLayout(int axis) {
		this(axis, 5, 5, 5, 5, 5);
	}

	/**
	 * Constructs a new Weighted Layout with specified axis, gap, and margins.
	 *
	 * @param axis          X_AXIS or Y_AXIS
	 * @param gap           the gap between components.
	 * @param topMargin     the top margin.
	 * @param leftMargin    the left margin.
	 * @param bottomMargin  the bottom margin.
	 * @param rightMargin   the right margin.
	 */
	public JvBoxLayout(
		int axis,
		int gap,
		int topMargin,
		int leftMargin,
		int bottomMargin,
		int rightMargin) {
		this.axis = axis;
		this.gap = gap;
		this.topMargin = topMargin;
		this.leftMargin = leftMargin;
		this.bottomMargin = bottomMargin;
		this.rightMargin = rightMargin;
	}

	/**
	 * Gets the gap between components.
	 *
	 * @return the gap between components.
	 */
	public int getGap() {
		return gap;
	}

	/**
	 * Sets the gap between components.
	 *
	 * @param gap the gap between components
	 */
	public void setGap(int gap) {
		this.gap = gap;
	}

	/**
	 * Gets the top margin to be reserved above all components.
	 *
	 * @return the top margin.
	 */
	public int getTopMargin() {
		return topMargin;
	}

	/**
	 * Sets the top margin to be reserve above components.
	 *
	 * @param topMargin the top margin.
	 */
	public void setTopMargin(int topMargin) {
		this.topMargin = topMargin;
	}

	/**
	 * Gets the left margin to be reserved to the left of the
	 * leftmost component.
	 *
	 * @return the left margin.
	 */
	public int getLeftMargin() {
		return leftMargin;
	}

	/**
	 * Sets the left margin to be reserved to the left of the
	 * leftmost component.
	 *
	 * @param leftMargin the left margin.
	 */
	public void setLeftMargin(int leftMargin) {
		this.leftMargin = leftMargin;
	}

	/**
	 * Gets the bottom margin to be reserved below all components.
	 *
	 * @return the bottom margin.
	 */
	public int getBottomMargin() {
		return bottomMargin;
	}

	/**
	 * Sets the bottom margin to be reserve below components.
	 *
	 * @param bottomMargin the bottom margin.
	 */
	public void setBottomMargin(int bottomMargin) {
		this.bottomMargin = bottomMargin;
	}

	/**
	 * Gets the right margin to be reserved to the right of the
	 * rightmost component.
	 *
	 * @return the right margin.
	 */
	public int getRightMargin() {
		return rightMargin;
	}

	/**
	 * Sets the right margin to be reserved to the right of the
	 * rightmost component.
	 *
	 * @param rightMargin the right margin.
	 */
	public void setRightMargin(int rightMargin) {
		this.rightMargin = rightMargin;
	}

	/**
	 * Adds the specified component to the layout, using the specified
	 * constraint object.
	 *
	 * @param comp the component to be added
	 * @param constraints  where/how the component is added to the layout.
	 */
	public void addLayoutComponent(Component comp, Object weight) {
		if (weight instanceof Double) {
			weightMap.put(comp, weight);
		} else if (weight == null) {
			weightMap.put(comp, new Double(0.0));
		} else {
			throw new IllegalArgumentException("cannot add to layout: weight must be a Double");
		}
	}

	/**
	 * Returns the maximum size of this component.
	 *
	 * @see java.awt.Component#getMinimumSize()
	 * @see java.awt.Component#getPreferredSize()
	 * @see LayoutManager
	 */
	public Dimension maximumLayoutSize(Container target) {
		return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
	}

	/**
	 * Returns the alignment along the x axis.  This specifies how the
	 * component would like to be aligned relative to other components.  The
	 * value should be a number between 0 and 1 where 0 represents alignment
	 * along the origin, 1 is aligned the furthest away from the origin, 0.5
	 * is centered, etc.
	 */
	public float getLayoutAlignmentX(Container target) {
		return 0.5f;
	}

	/**
	 * Returns the alignment along the y axis.  This specifies how the
	 * component would like to be aligned relative to other components.  The
	 * value should be a number between 0 and 1 where 0 represents alignment
	 * along the origin, 1 is aligned the furthest away from the origin, 0.5
	 * is centered, etc.
	 */
	public float getLayoutAlignmentY(Container target) {
		return 0.5f;
	}

	/**
	 * Invalidates the layout, indicating that if the layout manager has
	 * cached information it should be discarded.
	 */
	public void invalidateLayout(Container target) {
	}

	/**
	 * Adds the specified component to the layout. Not used by this class.
	 *
	 * @param name the name of the component
	 * @param comp the component to be added
	 */
	public void addLayoutComponent(String name, Component comp) {
	}

	/**
	 * Removes the specified component from the layout.
	 *
	 * @param comp the component to remove
	 */
	public void removeLayoutComponent(Component comp) {
		weightMap.remove(comp);
	}

	/**
	 * Returns the preferred dimensions for this layout given the components
	 * in the specified target container.
	 *
	 * @param target the component which needs to be laid out
	 * @return    the preferred dimensions to lay out the subcomponents of the
	 * specified container.
	 */
	public Dimension minimumLayoutSize(Container target) {
		synchronized (target.getTreeLock()) {
			int n = 0;
			Insets insets = target.getInsets();
			int fixed = 0;
			int total = 0;
			double minRatio = 0.0;
			int minValue = 0;
			double minWeight = 0.0;
			double totalWeight = 0.0;

			for (Iterator iter = weightMap.keySet().iterator();
				iter.hasNext();
				) {
				Component child = (Component) iter.next();
				Double weight = (Double) weightMap.get(child);

				if (child.isVisible()) {
					n++;

					if (weight.doubleValue() > 0.0) {
						totalWeight += weight.doubleValue();

						Dimension dim = child.getMinimumSize();

						if (axis == X_AXIS) {
							if (dim.height > fixed) {
								fixed = dim.height;
							}

							double ratio = dim.width / weight.doubleValue();

							if (ratio > minRatio) {
								minRatio = ratio;
								minValue = dim.width;
								minWeight = weight.doubleValue();
							}
						} else {
							if (dim.width > fixed) {
								fixed = dim.width;
							}

							double ratio = dim.height / weight.doubleValue();

							if (ratio > minRatio) {
								minRatio = ratio;
								minValue = dim.height;
								minWeight = weight.doubleValue();
							}
						}
					} else {
						Dimension dim = child.getPreferredSize();

						if (axis == X_AXIS) {
							if (dim.height > fixed) {
								fixed = dim.height;
							}

							total += dim.width;
						} else {
							if (dim.width > fixed) {
								fixed = dim.width;
							}

							total += dim.height;
						}
					}
				}
			}

			if (n > 1) {
				total += gap * (n - 1);
			}

			if (totalWeight > 0) {
				total += (minValue * totalWeight / minWeight);
			}

			if (axis == X_AXIS) {
				return new Dimension(
					total
						+ insets.left
						+ insets.right
						+ leftMargin
						+ rightMargin,
					fixed
						+ insets.top
						+ insets.bottom
						+ topMargin
						+ bottomMargin);
			} else {
				return new Dimension(
					fixed
						+ insets.left
						+ insets.right
						+ leftMargin
						+ rightMargin,
					total
						+ insets.top
						+ insets.bottom
						+ topMargin
						+ bottomMargin);
			}
		}
	}

	/**
	 * Returns the minimum dimensions needed to layout the components
	 * contained in the specified target container.
	 *
	 * @param target the component which needs to be laid out
	 * @return    the minimum dimensions to lay out the subcomponents of the
	 * specified container.
	 */
	public Dimension preferredLayoutSize(Container target) {
		synchronized (target.getTreeLock()) {
			int n = 0;
			Insets insets = target.getInsets();
			int fixed = 0;
			int total = 0;
			double minRatio = 0.0;
			int minValue = 0;
			double minWeight = 0.0;
			double totalWeight = 0.0;

			for (int i = 0; i < target.getComponents().length; i++) {
				Component child = target.getComponent(i);
				Double weight = (Double) weightMap.get(child);

				if (child.isVisible()) {

					n++;

					if (weight.doubleValue() > 0.0) {
						totalWeight += weight.doubleValue();

						Dimension dim = child.getPreferredSize();
						
						if (axis == X_AXIS) {
							if (dim.height > fixed) {
								fixed = dim.height;
							}

							double ratio = dim.width / weight.doubleValue();

							if (ratio > minRatio) {
								minRatio = ratio;
								minValue = dim.width;
								minWeight = weight.doubleValue();
							}
						} else {
							if (dim.width > fixed) {
								fixed = dim.width;
							}

							double ratio = dim.height / weight.doubleValue();

							if (ratio > minRatio) {
								minRatio = ratio;
								minValue = dim.height;
								minWeight = weight.doubleValue();
							}
						}
					} else {
						Dimension dim = child.getPreferredSize();

						if (axis == X_AXIS) {
							if (dim.height > fixed) {
								fixed = dim.height;
							}

							total += dim.width;
						} else {
							if (dim.width > fixed) {
								fixed = dim.width;
							}

							total += dim.height;
						}
					}
				}
			}

			if (n > 1) {
				total += gap * (n - 1);
			}

			if (totalWeight > 0 && minValue > 0 && minWeight > 0) {
				total += (minValue * totalWeight / minWeight);
			}

			if (axis == X_AXIS) {

				return new Dimension(
					total
						+ insets.left
						+ insets.right
						+ leftMargin
						+ rightMargin,
					fixed
						+ insets.top
						+ insets.bottom
						+ topMargin
						+ bottomMargin);
			} else {
				return new Dimension(
					fixed
						+ insets.left
						+ insets.right
						+ leftMargin
						+ rightMargin,
					total
						+ insets.top
						+ insets.bottom
						+ topMargin
						+ bottomMargin);
			}
		}
	}

	/**
	 * Lays out the container. This method lets each component take its
	 * preferred size by reshaping the components in the target container in
	 * order to satisfy the constraints of this <code>FlowLayout</code> object.
	 *
	 * @param target the specified component being laid out.
	 * @see Container
	 * @see       java.awt.Container#doLayout
	 */
	public void layoutContainer(Container target) {
		synchronized (target.getTreeLock()) {
			int n = 0;
			Insets insets = target.getInsets();
			int fixed = 0;
			int used = 0;
			int avail = 0;
			int x = insets.left + leftMargin;
			int y = insets.top + topMargin;
			double totalWeight = 0.0;

			if (axis == X_AXIS) {
				avail =
					target.getSize().width
						- (insets.left + insets.right + leftMargin + rightMargin);
				fixed =
					target.getSize().height
						- (insets.top + insets.bottom + topMargin + bottomMargin);
			} else {
				fixed =
					target.getSize().width
						- (insets.left + insets.right + leftMargin + rightMargin);
				avail =
					target.getSize().height
						- (insets.top + insets.bottom + topMargin + bottomMargin);
			}

			for (int i = 0; i < target.getComponents().length; i++) {
				Component child = target.getComponent(i);
				Double weight = (Double) weightMap.get(child);

				if (child.isVisible()) {
					n++;

					if (weight.doubleValue() == 0.0) {
						Dimension dim = child.getPreferredSize();

						if (axis == X_AXIS) {
							used += dim.width;
						} else {
							used += dim.height;
						}
					} else {
						totalWeight += weight.doubleValue();
					}
				}
			}

			if (n > 1) {
				used += gap * (n - 1);
			}

			avail -= used;

			for (int i = 0; i < target.getComponents().length; i++) {
				Component child = target.getComponent(i);
				Double weight = (Double) weightMap.get(child);

				if (child.isVisible()) {
					child.setLocation(x, y);

					if (weight.doubleValue() == 0) {
						Dimension dim = child.getPreferredSize();

						if (axis == X_AXIS) {
							child.setSize(dim.width, fixed);
							x += gap + dim.width;
						} else {
							child.setSize(fixed, dim.height);
							y += gap + dim.height;
						}
					} else {
						int prop;

						prop =
							(int) ((avail * weight.doubleValue() + 0.5)
								/ totalWeight);

						if (axis == X_AXIS) {
							child.setSize(prop, fixed);
							x += gap + prop;
						} else {
							child.setSize(fixed, prop);
							y += gap + prop;
						}
					}
				}
			}
		}
	}

	/**
	 * Create a filler component that can be added to a container with a
	 * weighted layout layout.  When added to a container without a weight, n
	 * size space will be reserved.  When added to a container with weight,
	 * the size is ignored.
	 *
	 * @param width the width of the filler
	 * @param height the height of the filler
	 */
	public static Component createFiller(int width, int height) {
		return new Filler(width, height);
	}

	/**
	 * An implementation of a lightweight component that participates in
	 * layout but has no view.
	 */
	public static class Filler extends Component implements Accessible {
		private Dimension dim;

		/**
		 * Constructor to create shape with the given size ranges.
		 *
		 * @param width the width of the filler
		 * @param height the height of the filler
		 */
		public Filler(int width, int height) {
			dim = new Dimension(width, height);
		}

		/**
		 * Returns the minimum size of the component.
		 *
		 * @return the size
		 */
		public Dimension getMinimumSize() {
			return dim;
		}

		/**
		 * Returns the preferred size of the component.
		 *
		 * @return the size
		 */
		public Dimension getPreferredSize() {
			return dim;
		}

		/**
		 * Returns the maximum size of the component.
		 *
		 * @return the size
		 */
		public Dimension getMaximumSize() {
			return dim;
		}
	}

	public int layoutPrint(Container target, int pageHeight) {
		Insets insets = target.getInsets();
		int height = getTopMargin() + insets.top;
		int nPageHeight = pageHeight;
		boolean useGap = false;

		for (int i = 0; i < target.getComponents().length; i++) {
			Component child = target.getComponent(i);
			
			if (child.isVisible()) {
				int childHeight;
				
				childHeight = child.getPreferredSize().height;
				
				if (axis == X_AXIS) {
					if (childHeight + getTopMargin() + insets.top > height) {
						height = childHeight + getTopMargin() + insets.top;
					}
				} else {
					if (useGap) {
						height += gap;
					}
					
					if (height + childHeight > nPageHeight) {
						height = nPageHeight;
						nPageHeight += pageHeight;
					}
					
					child.setLocation(child.getX(), height);
					child.setSize(child.getWidth(), childHeight);
					
					height += childHeight;
					
					useGap = true;
				}
			}
		}	
		
		if (axis == X_AXIS) {
			int childHeight = height - (getTopMargin() + insets.top);
			
			for (int i = 0; i < target.getComponents().length; i++) {
				Component child = target.getComponent(i);
			
				if (child.isVisible()) {				
					child.setSize(child.getWidth(), childHeight);
					
//					if (child instanceof JPanel) {
//						child.doLayout();
//					}
				}
			}
		}
		
		return height + bottomMargin + insets.bottom;
	}


}
