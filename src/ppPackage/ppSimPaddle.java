/** */
package ppPackage;

import static ppPackage.ppSimParams.*;

import java.awt.Color;
import java.awt.event.MouseEvent;

import acm.program.GraphicsProgram;
import acm.util.RandomGenerator;

/**
 * @author gabriel
 *     <p>This class is the base for initialization of the ping-pong simulation. It is the main
 *     graphics program for this simulation.
 */
@SuppressWarnings("serial")
public class ppSimPaddle extends GraphicsProgram {

  // Holds a reference to the ppPaddle instance
  private ppPaddle myPaddle;
  // Holds a reference to the ppTable instance
  private ppTable myTable;

  public static void main(String[] args) {
    new ppSimPaddle.start(args);
  }

  /** The init function is the entry point to the ping-pong simulation. */
  public void init() {
    this.resize(scrWIDTH + OFFSET, scrHEIGHT + OFFSET);
    addMouseListeners();

    myTable = new ppTable(this);
    RandomGenerator rgen = RandomGenerator.getInstance();

    rgen.setSeed(RSEED);

    this.myPaddle = new ppPaddle(ppPaddleXinit, ppPaddleYinit, myTable);

    Color iColor = Color.RED;
    double iYinit = rgen.nextDouble(YinitMIN, YinitMAX);
    double iLoss = rgen.nextDouble(EMIN, EMAX);
    double iVel = rgen.nextDouble(VoMIN, VoMAX);
    double iTheta = rgen.nextDouble(ThetaMIN, ThetaMAX);

    ppBall myBall = new ppBall(XINIT, iYinit, iVel, iTheta, iColor, iLoss, myTable, myPaddle);
    pause(1000);
    myBall.start();
    myPaddle.start();
  }

  /** Mouse Handler - a MouseEvent moves the paddle up and down in Y */
  public void mouseMoved(MouseEvent e) {
    myPaddle.setY(myTable.ScrtoY((double) e.getY()));
  }
}
