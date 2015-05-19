package org.djodjo.tarator.base;

import android.os.Message;

import com.google.common.base.Throwables;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class UncheckedRecycler implements Recycler {
    private static final String UNCHECKED_RECYCLE_METHOD_NAME = "recycleUnchecked";
    private final Method uncheckedRecycle;

    UncheckedRecycler() {
        try {
            this.uncheckedRecycle = Message.class.getDeclaredMethod("recycleUnchecked", new Class[0]);
            this.uncheckedRecycle.setAccessible(true);
        } catch (NoSuchMethodException ex) {
            throw Throwables.propagate(ex);
        }
    }

    public void recycle(Message message) {
        try {
            this.uncheckedRecycle.invoke(message, new Object[0]);
        } catch (IllegalAccessException ex) {
            throw Throwables.propagate(ex);
        } catch (InvocationTargetException ex) {
            if (ex.getCause() != null) {
                throw Throwables.propagate(ex.getCause());
            } else {
                throw Throwables.propagate(ex);
            }
        }
    }
}