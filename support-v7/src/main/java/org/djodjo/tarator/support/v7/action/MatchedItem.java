package org.djodjo.tarator.support.v7.action;

/**
 * Created by sic on 13/05/15.
 */
public class MatchedItem {
    public final int position;
    public final String description;

    MatchedItem(int position, String description) {
        this.position = position;
        this.description = description;
    }

    public String toString() {
        return this.description;
    }
}
