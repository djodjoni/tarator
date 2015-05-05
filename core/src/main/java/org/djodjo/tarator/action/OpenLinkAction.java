package org.djodjo.tarator.action;


import android.net.Uri;
import android.text.Spanned;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.TextView;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import org.djodjo.tarator.PerformException.Builder;
import org.djodjo.tarator.UiController;
import org.djodjo.tarator.ViewAction;
import org.djodjo.tarator.matcher.ViewMatchers;
import org.djodjo.tarator.util.HumanReadables;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

import java.util.ArrayList;
import java.util.Arrays;

public final class OpenLinkAction implements ViewAction {
    private final Matcher<String> linkTextMatcher;
    private final Matcher<Uri> uriMatcher;

    public OpenLinkAction(Matcher<String> linkTextMatcher, Matcher<Uri> uriMatcher) {
        this.linkTextMatcher = (Matcher) Preconditions.checkNotNull(linkTextMatcher);
        this.uriMatcher = (Matcher) Preconditions.checkNotNull(uriMatcher);
    }

    public Matcher<View> getConstraints() {
        return Matchers.allOf(new Matcher[]{ViewMatchers.isDisplayed(),
                ViewMatchers.isAssignableFrom(TextView.class), ViewMatchers.hasLinks()});
    }

    public String getDescription() {
        return String.format("open link with text %s and uri %s",
                new Object[]{this.linkTextMatcher, this.uriMatcher});
    }

    public void perform(UiController uiController, View view) {
        TextView textView = (TextView) view;
        String allText = textView.getText().toString();
        URLSpan[] urls = textView.getUrls();
        Spanned spanned = (Spanned) textView.getText();
        ArrayList allLinks = Lists.newArrayList();
        URLSpan[] arr$ = urls;
        int len$ = urls.length;

        for (int i$ = 0; i$ < len$; ++i$) {
            URLSpan url = arr$[i$];
            int start = spanned.getSpanStart(url);
            Preconditions.checkState(start != -1, "Unable to get start of text associated with url: " + url);
            int end = spanned.getSpanEnd(url);
            Preconditions.checkState(end != -1, "Unable to get end of text associated with url: " + url);
            String linkText = allText.substring(start, end);
            allLinks.add(linkText);
            if (this.linkTextMatcher.matches(linkText) && this.uriMatcher.matches(Uri.parse(url.getURL()))) {
                url.onClick(view);
                return;
            }
        }

        throw (new Builder()).withActionDescription(this.getDescription())
                .withViewDescription(HumanReadables.describe(view))
                .withCause(new RuntimeException(String.format("Link with text \'%s\' and uri \'%s\' " +
                                "not found. List of links found in this view: %s\nList of uris: %s",
                        new Object[]{this.linkTextMatcher, this.uriMatcher, allLinks,
                                Arrays.asList(urls)}))).build();
    }
}
