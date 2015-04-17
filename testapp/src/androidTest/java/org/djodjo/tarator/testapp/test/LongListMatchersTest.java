package org.djodjo.tarator.testapp.test;

import android.test.ActivityInstrumentationTestCase2;

import org.djodjo.tarator.testapp.LongListActivity;

import static org.djodjo.tarator.matcher.ViewMatchers.assertThat;
import static org.djodjo.tarator.testapp.test.LongListMatchers.withItemContent;
import static org.djodjo.tarator.testapp.test.LongListMatchers.withItemSize;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.equalTo;

/**
 * UnitTests for LongListMatchers matcher factory.
 */
public final class LongListMatchersTest extends ActivityInstrumentationTestCase2<LongListActivity> {

  public LongListMatchersTest() {
    super(LongListActivity.class);
  }

  @Override
  public void setUp() throws Exception {
    super.setUp();
    getActivity();
   }

  public void testWithContent() {
    assertThat(getActivity().makeItem(54), withItemContent("item: 54"));
    assertThat(getActivity().makeItem(54), withItemContent(endsWith("54")));
    assertFalse(withItemContent("hello world").matches(getActivity().makeItem(54)));
  }

  @SuppressWarnings("unchecked")
  public void testWithItemSize() {
    assertThat(getActivity().makeItem(54), withItemSize(8));
    assertThat(getActivity().makeItem(54), withItemSize(anyOf(equalTo(8), equalTo(7))));
    assertFalse(withItemSize(7).matches(getActivity().makeItem(54)));
  }
}
