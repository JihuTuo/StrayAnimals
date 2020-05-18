
package com.hsshy.beam.common.utils.qr;
import com.google.zxing.common.BitMatrix;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;

/**
 * Writes a {@link BitMatrix} to {@link BufferedImage},
 * file or stream. Provided here instead of core since it depends on
 * Java SE libraries.
 *
 * @author Sean Owen
 */
public final class MatrixToImageWriter {

  private static final MatrixToImageConfig DEFAULT_CONFIG = new MatrixToImageConfig();

  private MatrixToImageWriter() {}

  /**
   * Renders a {@link BitMatrix} as an image, where "false" bits are rendered
   * as white, and "true" bits are rendered as black.
   */
  public static BufferedImage toBufferedImage(BitMatrix matrix) {
    return toBufferedImage(matrix, DEFAULT_CONFIG);
  }

  /**
   * As {@link #toBufferedImage(BitMatrix)}, but allows customization of the output.
   */
  public static BufferedImage toBufferedImage(BitMatrix matrix, MatrixToImageConfig config) {
    int width = matrix.getWidth();
    int height = matrix.getHeight();
    BufferedImage image = new BufferedImage(width, height, config.getBufferedImageColorModel());
    int onColor = config.getPixelOnColor();
    int offColor = config.getPixelOffColor();
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        image.setRGB(x, y, matrix.get(x, y) ? onColor : offColor);
      }
    }
    return image;
  }

  /**
   * @deprecated use {@link #writeToPath(BitMatrix, String, Path)}
   */
  @Deprecated
  public static void writeToFile(BitMatrix matrix, String format, File file) throws IOException {
    writeToPath(matrix, format, file.toPath());
  }

  /**
   * Writes a {@link BitMatrix} to a file.
   *
   * @see #toBufferedImage(BitMatrix)
   */
  public static void writeToPath(BitMatrix matrix, String format, Path file) throws IOException {
    writeToPath(matrix, format, file, DEFAULT_CONFIG);
  }

  /**
   * @deprecated use {@link #writeToPath(BitMatrix, String, Path, MatrixToImageConfig)}
   */
  @Deprecated
  public static void writeToFile(BitMatrix matrix, String format, File file, MatrixToImageConfig config) 
      throws IOException {
    writeToPath(matrix, format, file.toPath(), config);
  }

  /**
   * As {@link #writeToFile(BitMatrix, String, File)}, but allows customization of the output.
   */
  public static void writeToPath(BitMatrix matrix, String format, Path file, MatrixToImageConfig config)
      throws IOException {
    BufferedImage image = toBufferedImage(matrix, config);
    if (!ImageIO.write(image, format, file.toFile())) {
      throw new IOException("Could not write an image of format " + format + " to " + file);
    }
  }

  /**
   * Writes a {@link BitMatrix} to a stream.
   *
   * @see #toBufferedImage(BitMatrix)
   */
  public static void writeToStream(BitMatrix matrix, String format, OutputStream stream) throws IOException {
    writeToStream(matrix, format, stream, DEFAULT_CONFIG);
  }

  /**
   * As {@link #writeToStream(BitMatrix, String, OutputStream)}, but allows customization of the output.
   */
  public static void writeToStream(BitMatrix matrix, String format, OutputStream stream, MatrixToImageConfig config) 
      throws IOException {  
    BufferedImage image = toBufferedImage(matrix, config);
    if (!ImageIO.write(image, format, stream)) {
      throw new IOException("Could not write an image of format " + format);
    }
  }

}