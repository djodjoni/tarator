package org.djodjo.tarator.support.v4.action;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.view.View;

import org.djodjo.tarator.IdlingResource;
import org.djodjo.tarator.PerformException;
import org.djodjo.tarator.UiController;
import org.djodjo.tarator.ViewAction;
import org.hamcrest.Matcher;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.Nullable;

import static org.djodjo.tarator.Tarator.onView;
import static org.djodjo.tarator.Tarator.registerIdlingResources;
import static org.djodjo.tarator.Tarator.unregisterIdlingResources;
import static org.djodjo.tarator.matcher.ViewMatchers.isAssignableFrom;
import static org.djodjo.tarator.matcher.ViewMatchers.withId;
import static org.djodjo.tarator.support.v4.matcher.DrawerMatchers.isClosed;
import static org.djodjo.tarator.support.v4.matcher.DrawerMatchers.isOpen;

/**
 * Tarator actions for using a {@link DrawerLayout}.
 *
 * @see <a href="http://developer.android.com/design/patterns/navigation-drawer.html">Navigation
 * drawer design guide</a>
 */
public final class DrawerActions {

    private DrawerActions() {
        // forbid instantiation
    }

    private static Field listenerField;

    /**
     * Opens the {@link DrawerLayout} with the given id. This method blocks until the drawer is fully
     * open. No operation if the drawer is already open.
     */
    public static void openDrawer(int drawerLayoutId) {
        openDrawer(drawerLayoutId, GravityCompat.START);
    }

    public static void openDrawer(int drawerLayoutId, int gravity) {
        //if the drawer is already open, return.
        if (checkDrawer(drawerLayoutId, isOpen())) {
            return;
        }
        onView(withId(drawerLayoutId))
                .perform(registerListener())
                .perform(actionOpenDrawer(gravity))
                .perform(unregisterListener());
    }

    /**
     * Closes the {@link DrawerLayout} with the given id. This method blocks until the drawer is fully
     * closed. No operation if the drawer is already closed.
     */
    public static void closeDrawer(int drawerLayoutId) {
        closeDrawer(drawerLayoutId, GravityCompat.START);
    }

    public static void closeDrawer(int drawerLayoutId, final int gravity) {
        //if the drawer is already closed, return.
        if (checkDrawer(drawerLayoutId, isClosed())) {
            return;
        }
        onView(withId(drawerLayoutId))
                .perform(registerListener())
                .perform(actionCloseDrawer(gravity))
                .perform(unregisterListener());
    }

    /**
     * Returns true if the given matcher matches the drawer.
     */
    private static boolean checkDrawer(int drawerLayoutId, final Matcher<View> matcher) {
        final AtomicBoolean matches = new AtomicBoolean(false);
        onView(withId(drawerLayoutId)).perform(new ViewAction() {

            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(DrawerLayout.class);
            }

            @Override
            public String getDescription() {
                return "check drawer";
            }

            @Override
            public void perform(UiController uiController, View view) {
                matches.set(matcher.matches(view));
            }
        });
        return matches.get();
    }

    private static ViewAction actionOpenDrawer(final int gravity) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(DrawerLayout.class);
            }

            @Override
            public String getDescription() {
                return "open drawer";
            }

            @Override
            public void perform(UiController uiController, View view) {
                ((DrawerLayout) view).openDrawer(gravity);
            }
        };
    }

    private static ViewAction actionCloseDrawer(final int gravity) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(DrawerLayout.class);
            }

            @Override
            public String getDescription() {
                return "close drawer";
            }

            @Override
            public void perform(UiController uiController, View view) {
                ((DrawerLayout) view).closeDrawer(gravity);
            }
        };
    }

    /**
     * Returns a {@link ViewAction} that adds an {@link IdlingDrawerListener} as a drawer listener to
     * the {@link DrawerLayout}. The idling drawer listener wraps any listener that already exists.
     */
    private static ViewAction registerListener() {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(DrawerLayout.class);
            }

            @Override
            public String getDescription() {
                return "register idling drawer listener";
            }

            @Override
            public void perform(UiController uiController, View view) {
                DrawerLayout drawer = (DrawerLayout) view;
                DrawerListener existingListener = getDrawerListener(drawer);
                if (existingListener instanceof IdlingDrawerListener) {
                    // listener is already registered. No need to assign.
                    return;
                }
                DrawerActions.IdlingDrawerListener instance = DrawerActions.IdlingDrawerListener.getInstance(existingListener);
                drawer.setDrawerListener(instance);
                registerIdlingResources(new IdlingResource[]{instance});
            }
        };
    }

    private static ViewAction unregisterListener() {
        return new ViewAction() {
            public Matcher<View> getConstraints() {
                return isAssignableFrom(DrawerLayout.class);
            }

            public String getDescription() {
                return "unregister idling drawer listener";
            }

            public void perform(UiController uiController, View view) {
                DrawerLayout drawer = (DrawerLayout) view;
                DrawerListener existingListener = DrawerActions.getDrawerListener(drawer);
                if (existingListener instanceof DrawerActions.IdlingDrawerListener) {
                    unregisterIdlingResources(new IdlingResource[]{(IdlingResource) existingListener});
                    drawer.setDrawerListener(((DrawerActions.IdlingDrawerListener) existingListener).parentListener);
                }

            }
        };
    }

    /**
     * Pries the current {@link DrawerListener} loose from the cold dead hands of the given
     * {@link DrawerLayout}. Uses reflection.
     */
    @Nullable
    private static DrawerListener getDrawerListener(DrawerLayout drawer) {
        try {
            if (listenerField == null) {
                // lazy initialization of reflected field.
                listenerField = DrawerLayout.class.getDeclaredField("mListener");
                listenerField.setAccessible(true);
            }
            return (DrawerListener) listenerField.get(drawer);
        } catch (IllegalArgumentException ex) {
            // Pity we can't use Java 7 multi-catch for all of these.
            throw new PerformException.Builder().withCause(ex).build();
        } catch (IllegalAccessException ex) {
            throw new PerformException.Builder().withCause(ex).build();
        } catch (NoSuchFieldException ex) {
            throw new PerformException.Builder().withCause(ex).build();
        } catch (SecurityException ex) {
            throw new PerformException.Builder().withCause(ex).build();
        }
    }

    /**
     * Drawer listener that wraps an existing {@link DrawerListener}, and functions as an
     * {@link IdlingResource} for Tarator.
     */
    private static class IdlingDrawerListener implements DrawerListener, IdlingResource {

        private static IdlingDrawerListener instance;
        @Nullable
        private DrawerListener parentListener;
        private ResourceCallback callback;
        // Idle state is only accessible from main thread.
        private volatile boolean idle = true;

        private static IdlingDrawerListener getInstance(DrawerListener parentListener) {
            if (instance == null) {
                instance = new IdlingDrawerListener();
            }
            instance.setParentListener(parentListener);
            return instance;
        }

        public void setParentListener(@Nullable DrawerListener parentListener) {
            this.parentListener = parentListener;
        }

        @Override
        public void onDrawerClosed(View drawer) {
            if (parentListener != null) {
                parentListener.onDrawerClosed(drawer);
            }
        }

        @Override
        public void onDrawerOpened(View drawer) {
            if (parentListener != null) {
                parentListener.onDrawerOpened(drawer);
            }
        }

        @Override
        public void onDrawerSlide(View drawer, float slideOffset) {
            if (parentListener != null) {
                parentListener.onDrawerSlide(drawer, slideOffset);
            }
        }

        @Override
        public void onDrawerStateChanged(int newState) {
            if (newState == DrawerLayout.STATE_IDLE) {
                idle = true;
                if (callback != null) {
                    callback.onTransitionToIdle();
                }
            } else {
                idle = false;
            }
            if (parentListener != null) {
                parentListener.onDrawerStateChanged(newState);
            }
        }

        @Override
        public String getName() {
            return "IdlingDrawerListener";
        }

        @Override
        public boolean isIdleNow() {
            return idle;
        }

        @Override
        public void registerIdleTransitionCallback(ResourceCallback callback) {
            this.callback = callback;
        }
    }
}
