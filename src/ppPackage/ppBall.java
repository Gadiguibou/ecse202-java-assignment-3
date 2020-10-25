/** */
package ppPackage;

import static ppPackage.ppSimParams.*;

import java.awt.Color;

import acm.graphics.GOval;

/**
 * @author gabriel
 *     <p>This class encapsulates all the relevant information and methods related to the ping-pong
 *     ball. It contains the physics logic needed for ball simulation.
 */
public class ppBall extends Thread {
  // Instance variables
  // Initial position of ball - X
  private double Xinit;
  // Initial position of ball - Y
  private double Yinit;
  // Initial velocity (Magnitude)
  private double Vo;
  // Initial direction
  private double theta;
  // Energy loss on collision
  private double loss;
  // Color of ball
  @SuppressWarnings("unused")
  private Color color;
  // Instance of ping-pong table
  private ppTable table;
  // Instance of ping-pong paddle
  private ppPaddle myPaddle;
  // Graphics object representing ball
  GOval myBall;

  /**
   * The constructor for the ppBall class copies parameters to instance variables, creates an
   * instance of a GOval to represent the ping-pong ball, and adds it to the display.
   *
   * @param Xinit Specifies the starting position of the ball X (meters)
   * @param Yinit Specifies the starting position of the ball Y (meters)
   * @param Vo Specifies the initial velocity (meters/second)
   * @param theta Specifies the initial angle to the horizontal (degrees)
   * @param color Specifies the ball color (Color)
   * @param loss Specifies the loss on collision ([0,1])
   * @param table Specifies a reference to the ppTable class used to manage the display
   * @param myPaddle Specifies a reference to the ppPaddle instance to check for collisions with.
   */
  public ppBall(
      double Xinit,
      double Yinit,
      double Vo,
      double theta,
      Color color,
      double loss,
      ppTable table,
      ppPaddle myPaddle) {
    // Copy constructor parameters to instance variables
    this.Xinit = Xinit;
    this.Yinit = Yinit;
    this.Vo = Vo;
    this.theta = theta;
    this.color = color;
    this.loss = loss;
    this.table = table;
    this.myPaddle = myPaddle;

    this.myBall =
        new GOval(
            table.toScrX(Xinit),
            table.toScrY(Yinit),
            table.toScrX(bSize * 2),
            table.toScrX(bSize * 2));
    myBall.setFilled(true);
    myBall.setColor(color);
    table.getDisplay().add(myBall);
  }

  /**
   * In a thread, the run method is NOT started automatically (like in Assignment 1). Instead, a
   * start message must be sent to each instance of the ppBall class, e.g.,
   *
   * <p>ppBall myBall = new ppBall (--parameters--);
   *
   * <p>myBall.start(); The body of the run method is essentially the simulator code you wrote for
   * A1.
   */
  public void run() {

    // Initialize simulation parameters
    // Initializing variables
    boolean hasEnergy = true;

    // Time (reset after each bounce)
    double time = 0;

    // Terminal velocity
    final double Vt = bMass * g / (4 * Math.PI * bSize * bSize * k);

    // Kinetic energy in X and Y directions
    double kx = ETHR;
    double ky = ETHR;

    // X position and velocity variables
    double x0;
    double x;
    double vx;

    // Y position and velocity variables
    double y0;
    double y;
    double vy;

    // Initial velocity components in X and Y
    double v0x = Vo * Math.cos(theta * Pi / 180);
    double v0y = Vo * Math.sin(theta * Pi / 180);

    // Initial X and Y position of the ball
    x0 = Xinit;
    y0 = Yinit;

    // Main simulation loop
    while (hasEnergy) {
      // Update parameters according to formulas
      x = v0x * Vt / g * (1 - Math.exp(-g * time / Vt));
      y = Vt / g * (v0y + Vt) * (1 - Math.exp(-g * time / Vt)) - Vt * time;
      vx = v0x * Math.exp(-g * time / Vt);
      vy = (v0y + Vt) * Math.exp(-g * time / Vt) - Vt;

      // Check for collision with the ground
      if ((vy < 0) && (y0 + y <= bSize)) {
        // TODO: turn all of this repetitive code into a function
        // Compute new ball energy
        kx = 0.5 * bMass * vx * vx * (1 - loss);
        ky = 0.5 * bMass * vy * vy * (1 - loss);
        final double PE = 0;

        v0x = Math.sqrt(2 * kx / bMass);
        v0y = Math.sqrt(2 * ky / bMass);

        // Making sure the ball maintains the same horizontal direction
        if (vx < 0) {
          v0x = -v0x;
        }

        // time is reset at every collision
        time = 0;
        // need to accumulate distance between collisions
        x0 += x;
        // the absolute position of the ball on the ground
        y0 = bSize;
        // (X,Y) is the instantaneous position along an arc,
        x = 0;
        // Absolute position is (Xo+X,Yo+Y).
        y = 0;

        if ((kx + ky + PE) < ETHR) {
          hasEnergy = false;
        }
      }

      // Check for collision with the paddle
      if ((vx > 0)
          && (x0 + x >= myPaddle.getX() - bSize - ppPaddleW / 2)
          && myPaddle.contact(x0 + x + bSize, y0 + y + bSize)) {
        // Compute new ball energy
        kx = 0.5 * bMass * vx * vx * (1 - loss);
        ky = 0.5 * bMass * vy * vy * (1 - loss);
        final double PE = bMass * g * y;

        // Account for loss of energy
        v0x = -Math.sqrt(2 * kx / bMass);
        v0y = Math.sqrt(2 * ky / bMass);

        // Scale X component of velocity
        v0x = v0x * ppPaddleXgain;
        // Scale Y component of velocity and use same direction as paddle
        v0y = v0y * ppPaddleYgain * myPaddle.getVy();

        // time is reset at every collision
        time = 0;
        // the absolute position of the ball on the right wall
        x0 = myPaddle.getX() - ppPaddleW / 2 - bSize;
        // need to accumulate distance between collisions
        y0 += y;
        // (X,Y) is the instantaneous position along an arc,
        x = 0;
        // Absolute position is (Xo+X,Yo+Y).
        y = 0;

        if ((kx + ky + PE) < ETHR) {
          hasEnergy = false;
        }
      }

      // Check for collision with the left wall
      if ((vx < 0) && (x0 + x <= XLWALL + bSize)) {
        // Compute new ball energy
        kx = 0.5 * bMass * vx * vx * (1 - loss);
        ky = 0.5 * bMass * vy * vy * (1 - loss);
        final double PE = bMass * g * y;

        v0x = Math.sqrt(2 * kx / bMass);
        v0y = Math.sqrt(2 * ky / bMass);

        // Making sure the ball maintains the same vertical direction
        if (vy < 0) {
          v0y = -v0y;
        }

        // time is reset at every collision
        time = 0;
        // the absolute position of the ball on the left wall
        x0 = XLWALL + bSize;
        // need to accumulate distance between collisions
        y0 += y;
        // (X,Y) is the instantaneous position along an arc,
        x = 0;
        // Absolute position is (Xo+X,Yo+Y).
        y = 0;

        if ((kx + ky + PE) < ETHR) {
          hasEnergy = false;
        }
      }

      // Increment time
      time += TICK;

      // Update and display
      // Note: bSize is subtracted from X and added to Y so
      // that ball is positioned at its center.
      final int ScrX = (int) table.toScrX(x0 + x - bSize);
      final int ScrY = (int) table.toScrY(y0 + y + bSize);

      myBall.setLocation(ScrX, ScrY);

      // Print a black dot at the ball's location
      table.getDisplay().add(new GOval(table.toScrX(x0 + x), table.toScrY(y0 + y), PD, PD));

      if (DEBUG) {
        System.out.printf(
            "t: %.2f X: %.2f Y: %.2f Vx: %.2f Vy:%.2f\n", time, x0 + x, y0 + y, vx, vy);
      }

      // Pause display
      if (REAL_TIME) {
        table.getDisplay().pause(TICK * 1000);
      }
    }
  }
}
