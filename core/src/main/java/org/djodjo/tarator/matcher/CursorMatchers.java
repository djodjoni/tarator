package org.djodjo.tarator.matcher;


import android.database.Cursor;

import com.google.common.base.Preconditions;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.SelfDescribing;
import org.hamcrest.StringDescription;

import java.util.Arrays;

public final class CursorMatchers {
    private static final int COLUMN_NOT_FOUND = -1;
    private static final int MULTIPLE_COLUMNS_FOUND = -2;
    private static final int USE_COLUMN_PICKER = -3;
    private static final CursorMatchers.MatcherApplier BLOB_MATCHER_APPLIER = new CursorMatchers.MatcherApplier() {
        public boolean apply(Cursor cursor, int chosenColumn, Matcher<?> matcher) {
            return matcher.matches(cursor.getBlob(chosenColumn));
        }

        public void describeTo(Description description) {
            description.appendText("with Blob");
        }
    };
    private static final CursorMatchers.MatcherApplier LONG_MATCHER_APPLIER = new CursorMatchers.MatcherApplier() {
        public boolean apply(Cursor cursor, int chosenColumn, Matcher<?> matcher) {
            return matcher.matches(Long.valueOf(cursor.getLong(chosenColumn)));
        }

        public void describeTo(Description description) {
            description.appendText("with Long");
        }
    };
    private static final CursorMatchers.MatcherApplier SHORT_MATCHER_APPLIER = new CursorMatchers.MatcherApplier() {
        public boolean apply(Cursor cursor, int chosenColumn, Matcher<?> matcher) {
            return matcher.matches(Short.valueOf(cursor.getShort(chosenColumn)));
        }

        public void describeTo(Description description) {
            description.appendText("with Short");
        }
    };
    private static final CursorMatchers.MatcherApplier INT_MATCHER_APPLIER = new CursorMatchers.MatcherApplier() {
        public boolean apply(Cursor cursor, int chosenColumn, Matcher<?> matcher) {
            return matcher.matches(Integer.valueOf(cursor.getInt(chosenColumn)));
        }

        public void describeTo(Description description) {
            description.appendText("with Int");
        }
    };
    private static final CursorMatchers.MatcherApplier FLOAT_MATCHER_APPLIER = new CursorMatchers.MatcherApplier() {
        public boolean apply(Cursor cursor, int chosenColumn, Matcher<?> matcher) {
            return matcher.matches(Float.valueOf(cursor.getFloat(chosenColumn)));
        }

        public void describeTo(Description description) {
            description.appendText("with Float");
        }
    };
    private static final CursorMatchers.MatcherApplier DOUBLE_MATCHER_APPLIER = new CursorMatchers.MatcherApplier() {
        public boolean apply(Cursor cursor, int chosenColumn, Matcher<?> matcher) {
            return matcher.matches(Double.valueOf(cursor.getDouble(chosenColumn)));
        }

        public void describeTo(Description description) {
            description.appendText("with Double");
        }
    };
    private static final CursorMatchers.MatcherApplier STRING_MATCHER_APPLIER = new CursorMatchers.MatcherApplier() {
        public boolean apply(Cursor cursor, int chosenColumn, Matcher<?> matcher) {
            return matcher.matches(cursor.getString(chosenColumn));
        }

        public void describeTo(Description description) {
            description.appendText("with String");
        }
    };

    private CursorMatchers() {
    }

    private static int findColumnIndex(Matcher<String> nameMatcher, Cursor cursor) {
        int result = -1;
        String[] columnNames = cursor.getColumnNames();

        for (int i = 0; i < columnNames.length; ++i) {
            String column = columnNames[i];
            if (nameMatcher.matches(column)) {
                if (result != -1) {
                    result = -2;
                    break;
                }

                result = i;
            }
        }

        return result;
    }

    public static Matcher<Object> withRowShort(int columnIndex, short value) {
        return withRowShort(columnIndex, Matchers.is(Short.valueOf(value)));
    }

    public static Matcher<Object> withRowShort(int columnIndex, Matcher<Short> valueMatcher) {
        return new CursorMatchers.CursorMatcher(columnIndex, valueMatcher, SHORT_MATCHER_APPLIER);
    }

    public static Matcher<Object> withRowShort(String columnName, short value) {
        return withRowShort(columnName, Matchers.is(Short.valueOf(value)));
    }

    public static Matcher<Object> withRowShort(String columnName, Matcher<Short> valueMatcher) {
        return withRowShort(Matchers.is(columnName), valueMatcher);
    }

    public static Matcher<Object> withRowShort(Matcher<String> columnNameMatcher, Matcher<Short> valueMatcher) {
        return new CursorMatchers.CursorMatcher(columnNameMatcher, valueMatcher, SHORT_MATCHER_APPLIER);
    }

    public static Matcher<Object> withRowInt(int columnIndex, int value) {
        return withRowInt(columnIndex, Matchers.is(Integer.valueOf(value)));
    }

    public static Matcher<Object> withRowInt(int columnIndex, Matcher<Integer> valueMatcher) {
        return new CursorMatchers.CursorMatcher(columnIndex, valueMatcher, INT_MATCHER_APPLIER);
    }

    public static Matcher<Object> withRowInt(String columnName, int value) {
        return withRowInt(columnName, Matchers.is(Integer.valueOf(value)));
    }

    public static Matcher<Object> withRowInt(String columnName, Matcher<Integer> valueMatcher) {
        return withRowInt(Matchers.is(columnName), valueMatcher);
    }

    public static Matcher<Object> withRowInt(Matcher<String> columnNameMatcher, Matcher<Integer> valueMatcher) {
        return new CursorMatchers.CursorMatcher(columnNameMatcher, valueMatcher, INT_MATCHER_APPLIER);
    }

    public static Matcher<Object> withRowLong(int columnIndex, long value) {
        return withRowLong(columnIndex, Matchers.is(Long.valueOf(value)));
    }

    public static Matcher<Object> withRowLong(int columnIndex, Matcher<Long> valueMatcher) {
        return new CursorMatchers.CursorMatcher(columnIndex, valueMatcher, LONG_MATCHER_APPLIER);
    }

    public static Matcher<Object> withRowLong(String columnName, long value) {
        return withRowLong(columnName, Matchers.is(Long.valueOf(value)));
    }

    public static Matcher<Object> withRowLong(String columnName, Matcher<Long> valueMatcher) {
        return withRowLong(Matchers.is(columnName), valueMatcher);
    }

    public static Matcher<Object> withRowLong(Matcher<String> columnNameMatcher, Matcher<Long> valueMatcher) {
        return new CursorMatchers.CursorMatcher(columnNameMatcher, valueMatcher, LONG_MATCHER_APPLIER);
    }

    public static Matcher<Object> withRowFloat(int columnIndex, float value) {
        return withRowFloat(columnIndex, Matchers.is(Float.valueOf(value)));
    }

    public static Matcher<Object> withRowFloat(int columnIndex, Matcher<Float> valueMatcher) {
        return new CursorMatchers.CursorMatcher(columnIndex, valueMatcher, FLOAT_MATCHER_APPLIER);
    }

    public static Matcher<Object> withRowFloat(String columnName, float value) {
        return withRowFloat(columnName, Matchers.is(Float.valueOf(value)));
    }

    public static Matcher<Object> withRowFloat(String columnName, Matcher<Float> valueMatcher) {
        return withRowFloat(Matchers.is(columnName), valueMatcher);
    }

    public static Matcher<Object> withRowFloat(Matcher<String> columnNameMatcher, Matcher<Float> valueMatcher) {
        return new CursorMatchers.CursorMatcher(columnNameMatcher, valueMatcher, FLOAT_MATCHER_APPLIER);
    }

    public static Matcher<Object> withRowDouble(int columnIndex, double value) {
        return withRowDouble(columnIndex, Matchers.is(Double.valueOf(value)));
    }

    public static Matcher<Object> withRowDouble(int columnIndex, Matcher<Double> valueMatcher) {
        return new CursorMatchers.CursorMatcher(columnIndex, valueMatcher, DOUBLE_MATCHER_APPLIER);
    }

    public static Matcher<Object> withRowDouble(String columnName, double value) {
        return withRowDouble(columnName, Matchers.is(Double.valueOf(value)));
    }

    public static Matcher<Object> withRowDouble(String columnName, Matcher<Double> valueMatcher) {
        return withRowDouble(Matchers.is(columnName), valueMatcher);
    }

    public static Matcher<Object> withRowDouble(Matcher<String> columnNameMatcher, Matcher<Double> valueMatcher) {
        return new CursorMatchers.CursorMatcher(columnNameMatcher, valueMatcher, DOUBLE_MATCHER_APPLIER);
    }

    public static Matcher<Object> withRowString(int columnIndex, String value) {
        return withRowString(columnIndex, Matchers.is(value));
    }

    public static Matcher<Object> withRowString(int columnIndex, Matcher<String> valueMatcher) {
        return new CursorMatchers.CursorMatcher(columnIndex, valueMatcher, STRING_MATCHER_APPLIER);
    }

    public static Matcher<Object> withRowString(String columnName, String value) {
        return withRowString(Matchers.is(columnName), Matchers.is(value));
    }

    public static Matcher<Object> withRowString(String columnName, Matcher<String> valueMatcher) {
        return withRowString(Matchers.is(columnName), valueMatcher);
    }

    public static Matcher<Object> withRowString(Matcher<String> columnPicker, Matcher<String> valueMatcher) {
        return new CursorMatchers.CursorMatcher(columnPicker, valueMatcher, STRING_MATCHER_APPLIER);
    }

    public static Matcher<Object> withRowBlob(int columnIndex, byte[] value) {
        return withRowBlob(columnIndex, Matchers.is(value));
    }

    public static Matcher<Object> withRowBlob(int columnIndex, Matcher<byte[]> valueMatcher) {
        return new CursorMatchers.CursorMatcher(columnIndex, valueMatcher, BLOB_MATCHER_APPLIER);
    }

    public static Matcher<Object> withRowBlob(String columnName, byte[] value) {
        return withRowBlob(Matchers.is(columnName), Matchers.is(value));
    }

    public static Matcher<Object> withRowBlob(String columnName, Matcher<byte[]> valueMatcher) {
        return withRowBlob(Matchers.is(columnName), valueMatcher);
    }

    public static Matcher<Object> withRowBlob(Matcher<String> columnPicker, Matcher<byte[]> valueMatcher) {
        return new CursorMatchers.CursorMatcher(columnPicker, valueMatcher, BLOB_MATCHER_APPLIER);
    }

    private interface MatcherApplier extends SelfDescribing {
        boolean apply(Cursor var1, int var2, Matcher<?> var3);
    }

    private static class CursorMatcher extends BoundedMatcher<Object, Cursor> {
        private final int columnIndex;
        private final Matcher<String> columnNameMatcher;
        private final Matcher<?> valueMatcher;
        private final CursorMatchers.MatcherApplier applier;

        private CursorMatcher(int columnIndex, Matcher<?> valueMatcher, CursorMatchers.MatcherApplier applier) {
            super(Cursor.class);
            Preconditions.checkArgument(columnIndex >= 0);
            this.columnIndex = columnIndex;
            this.valueMatcher = (Matcher) Preconditions.checkNotNull(valueMatcher);
            this.applier = (CursorMatchers.MatcherApplier) Preconditions.checkNotNull(applier);
            this.columnNameMatcher = null;
        }

        private CursorMatcher(Matcher<String> columnPicker, Matcher<?> valueMatcher, CursorMatchers.MatcherApplier applier) {
            super(Cursor.class);
            this.columnNameMatcher = (Matcher) Preconditions.checkNotNull(columnPicker);
            this.valueMatcher = (Matcher) Preconditions.checkNotNull(valueMatcher);
            this.applier = (CursorMatchers.MatcherApplier) Preconditions.checkNotNull(applier);
            this.columnIndex = -3;
        }

        public boolean matchesSafely(Cursor cursor) {
            int chosenColumn = this.columnIndex;
            if (chosenColumn < 0) {
                chosenColumn = CursorMatchers.findColumnIndex(this.columnNameMatcher, cursor);
                if (chosenColumn < 0) {
                    StringDescription description = new StringDescription();
                    this.columnNameMatcher.describeTo(description);
                    if (chosenColumn == -1) {
                        throw new IllegalArgumentException("Couldn\'t find column in " + Arrays.asList(cursor.getColumnNames()) + " matching " + description.toString());
                    }

                    if (chosenColumn == -2) {
                        throw new IllegalArgumentException("Multiple columns in " + Arrays.asList(cursor.getColumnNames()) + " match " + description.toString());
                    }

                    throw new IllegalArgumentException("Couldn\'t find column in " + Arrays.asList(cursor.getColumnNames()));
                }
            }

            return this.applier.apply(cursor, chosenColumn, this.valueMatcher);
        }

        public void describeTo(Description description) {
            description.appendText("Rows with column: ");
            if (this.columnIndex < 0) {
                this.columnNameMatcher.describeTo(description);
            } else {
                description.appendText(" index = " + this.columnIndex + " ");
            }

            this.applier.describeTo(description);
            description.appendText(" ");
            this.valueMatcher.describeTo(description);
        }
    }
}
