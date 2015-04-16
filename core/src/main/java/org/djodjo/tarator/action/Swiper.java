package org.djodjo.tarator.action;

import org.djodjo.tarator.UiController;

/**
 * Interface to implement different swipe types.
 */
public interface Swiper {

  /**
   * The result of the swipe.
   */
  public enum Status {
    /**
     * The swipe action completed successfully.
     */
    SUCCESS,
    /**
     * Injecting the event was a complete failure.
     */
    FAILURE
  }

  /**
   * Swipes from {@code startCoordinates} to {@code endCoordinates} using the given
   * {@code uiController} to send {@link android.view.MotionEvent}s.
   *
   * @param uiController a UiController to use to send MotionEvents to the screen.
   * @param startCoordinates a float[] with x and y co-ordinates of the start of the swipe.
   * @param endCoordinates a float[] with x and y co-ordinates of the end of the swipe.
   * @param precision a float[] with x and y values of precision of the tap.
   * @return The status of the swipe.
   */
  public Status sendSwipe(UiController uiController, float[] startCoordinates,
                          float[] endCoordinates, float[] precision);

}
