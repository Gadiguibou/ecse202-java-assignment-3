/** */
package ppPackage;

import acm.graphics.GRect;
import static ppPackage.ppSimParams.*;

import java.awt.Color;

/**
 * @author gabriel
 *     <p>The ppPaddle class is responsible for creating the paddle instance. It exports methods
 *     that facilitate interaction with the paddle instance.
 */
public class ppPaddle extends Thread {
  private GRect paddleRect;
  // X and Y components of the paddle's velocity
  private double Vx;
  private double Vy;
  // X and Y coordinates of the paddle's center
  private double X;
  private double Y;
  // Previous X and Y coordinates of the paddle's center at the last tick.
  private double lastX;
  private double lastY;
  // Reference to the ppTable instance.
  private ppTable myTable;
  // Whether the player can move the paddle;
  private boolean canPlay = true;

  /**
   * Initializes the ppPaddle instance and adds it to the display.
   *
   * @param X Specifies the X position of the center of the paddle.
   * @param Y Specifies the Y position of the center of the paddle.
   * @param myTable Specifies a reference to the ppTable object holding a reference to the display.
   */
  ppPaddle(double X, double Y, ppTable myTable) {
    this.X = X;
    this.Y = Y;
    this.myTable = myTable;

    final double Xb = X - ppPaddleW / 2;
    final double Yb = Y + ppPaddleH / 2;
    final GRect paddleRect =
        new GRect(
            myTable.toScrX(Xb),
            myTable.toScrY(Yb),
            myTable.toScrX(ppPaddleW),
            myTable.toScrX(ppPaddleH));
    paddleRect.setFilled(true);
    paddleRect.setFillColor(Color.BLACK);

    this.paddleRect = paddleRect;

    myTable.getDisplay().add(paddleRect);
  }

  /** Overrides the run method of java.lang.Thread. Holds the main loop for the ppPaddle. */
  public void run() {
    while (canPlay) {
      Vx = (X - lastX) / TICK;
      Vy = (Y - lastY) / TICK;
      lastX = X;
      lastY = Y;
      // Time has to be translated to ms
      myTable.getDisplay().pause(TICK * TIMESCALE);
    }
  }

  /**
   * getVx returns the velocity of the paddle in the X direction.
   *
   * @return Returns the velocity of the paddle in the X direction.
   */
  public double getVx() {
    return Vx;
  }

  /**
   * getVy returns the velocity of the paddle in the Y direction.
   *
   * @return Returns the velocity of the paddle in the Y direction.
   */
  public double getVy() {
    return Vy;
  }

  /**
   * setX sets the X position of the paddle.
   *
   * @param X Specifies the new X position for the paddle.
   */
  public void setX(double X) {
    if (canPlay) {
      this.X = X;

      paddleRect.setLocation(myTable.toScrX(X), myTable.toScrY(Y));
    }
  }

  /**
   * setY sets the Y position of the paddle.
   *
   * @param Y Specifies the new Y position for the paddle.
   */
  public void setY(double Y) {
    if (canPlay) {
      this.Y = Y;

      paddleRect.setLocation(myTable.toScrX(X), myTable.toScrY(Y));
    }
  }

  /**
   * getX returns the X position of the paddle.
   *
   * @return Returns the X position of the paddle.
   */
  public double getX() {
    return X;
  }

  /**
   * getY returns the Y position of the paddle.
   *
   * @return Returns the Y position of the paddle.
   */
  public double getY() {
    return Y;
  }

  /**
   * getSgnVy returns the direction the paddle is moving in (either positive when moving up or
   * negative when moving down).
   *
   * @return Returns the sign of the Y velocity of the paddle.
   */
  public double getSgnVy() {
    if (Vy >= 0) {
      return 1;
    } else {
      return -1;
    }
  }

  /**
   * contact checks for the collision of a surface at (Sx, Sy) with the paddle.
   *
   * @param Sx Specifies the X position of the surface.
   * @param Sy Specifies the Y position of the surface.
   * @return Returns true if a surface at position (Sx,Sy) is deemed to be in contact with the
   *     paddle.
   */
  public boolean contact(double Sx, double Sy) {
    // Note: This contains the collision detection for the X coordinates even though it is not
    // explicitly needed. This ensures scalability and clarity of the code as it would be weird if
    // contact() returned true when the surface is far to the left or to the right of the paddle.

    // Also, this is now the only collision detection in both X and Y that is being done as having
    // them separate made for an infuriating experience when the paddle and ball's collision
    // detection were not in sync with each other.
    return Sy >= Y - ppPaddleH / 2
        && Sy <= Y + ppPaddleH / 2
        && Sx >= X - ppPaddleW / 2
        && Sx - 2 * bSize <= X + ppPaddleW / 2;
  }

  /** stopPlay stops the paddle from moving once it is called. */
  public void stopPlay() {
    canPlay = false;
  }
}
