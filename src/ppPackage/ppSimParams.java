/** */
package ppPackage;

/**
 * @author gabriel
 *     <p>This class provides all the constants needed by the simulation.
 */
public class ppSimParams {
  // 1. Parameters defined in screen coordinates
  static final int scrWIDTH = 1080;
  static final int scrHEIGHT = 600;
  static final int OFFSET = 200;

  // 2. Parameters defined in simulation coordinates
  // MKS
  static final double g = 9.8;
  // Vt constant
  static final double k = 0.1316;
  static final double Pi = 3.1416;

  // Maximum value of X (pp table)
  static final double XMAX = 2.74;
  // Maximum value of Y (height above table)
  static final double YMAX = 1.52;
  // position of left wall
  static final double XLWALL = 0.1;
  // position of right wall
  static final double XRWALL = XMAX;

  // pp ball radius
  static final double bSize = 0.02;
  // pp ball mass
  static final double bMass = 0.0027;
  // Initial ball location (X)
  static final double XINIT = XLWALL + bSize;
  // Initial ball location (Y)
  static final double YINIT = YMAX / 2;

  // Clock tick duration (sec)
  static final double TICK = 0.01;
  // Minimum ball energy needed to move
  static final double ETHR = 0.001;
  // Trace point diameter
  static final double PD = 1;
  // Pixels/meter
  static final double SCALE = scrHEIGHT / YMAX;

  // Paddle height
  static final double ppPaddleH = 8 * 2.54 / 100;
  // Paddle width
  static final double ppPaddleW = 0.5 * 2.54 / 100;
  // Initial Paddle X Position
  static final double ppPaddleXinit = XMAX - ppPaddleW / 2;
  // Initial Paddle Y Position
  static final double ppPaddleYinit = YINIT;
  // Vx gain on paddle hit
  static final double ppPaddleXgain = 2.0;
  // Vy gain on paddle hit
  static final double ppPaddleYgain = 1.0;
  // TICK to ms scale factor (1000 -> real time)
  static final double TIMESCALE = 5000;

  // 3. Parameters used by the ppSim (main) class
  // # pp balls to simulate
  static final int NUMBALLS = 1;
  // Max inital height at 75% of range
  static final double YinitMAX = 0.75 * YMAX;
  // Min inital height at 25% of range
  static final double YinitMIN = 0.25 * YMAX;
  // Minimum loss coefficient
  static final double EMIN = 0.2;
  // Maximum loss coefficient
  static final double EMAX = 0.2;
  // Minimum velocity
  static final double VoMIN = 5.0;
  // Maximum velocity
  static final double VoMAX = 5.0;
  // Minimum launch angle
  static final double ThetaMIN = 0.0;
  // Maximum launch angle
  static final double ThetaMAX = 20.0;

  // 4. Miscellaneous
  // Enable print to console
  static final boolean DEBUG = false;
  // Slow down simulation to real time
  static final boolean TEST = false;
  // Random number generator seed value
  static final long RSEED = 8976232;
}
