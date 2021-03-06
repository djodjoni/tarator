package org.djodjo.tarator.action;

/**
 * Interface to implement size of click area.
 */
public interface PrecisionDescriber {

  /**
   * Different touch target sizes.
   *
   * @return a float[] with x and y values of size of click area.
   */
  public float[] describePrecision();
}
