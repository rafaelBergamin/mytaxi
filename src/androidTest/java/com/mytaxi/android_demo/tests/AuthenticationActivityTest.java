package com.mytaxi.android_demo.tests;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.PerformException;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.util.HumanReadables;
import android.support.test.espresso.util.TreeIterables;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import com.mytaxi.android_demo.activities.AuthenticationActivity;
import com.mytaxi.android_demo.R;
import com.mytaxi.android_demo.dao.LoginDAO;


import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by RAFAELBERGAMINDASILV on 25/11/2018.
 */

@RunWith(AndroidJUnit4.class)
public class AuthenticationActivityTest {

    private static final int BOTH_FIELDS_ID = -1;
    private LoginDAO login = new LoginDAO();
    public static final String CONNECTIVITY_SERVICE = null;

    @Rule
    public ActivityTestRule<AuthenticationActivity>
            mActivityRule = new ActivityTestRule<>(AuthenticationActivity.class, false, true);

    @Test
    public void whenActivityIsLaunched_shouldDisplayInitialState() {
        //onView(withId(R.id.login_image)).check(matches(isDisplayed()));
        onView(withId(R.id.edt_username)).check(matches(isDisplayed()));
        onView(withId(R.id.edt_password)).check(matches(isDisplayed()));
        onView(withId(R.id.btn_login)).check(matches(isDisplayed()));
    }

    @Test
    public void emptyPassword() throws InterruptedException {
        onView(withId(R.id.edt_username)).perform(typeText("test123"), closeSoftKeyboard());
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.btn_login)).perform(click());
        Thread.sleep(2000);
        onView(withText(R.string.message_login_fail)).check(matches(isDisplayed()));
    }

    @Test
    public void emptyUsername() throws InterruptedException {
        onView(withId(R.id.edt_password)).perform(typeText("test123"), closeSoftKeyboard());
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.btn_login)).perform(click());
        Thread.sleep(2000);
        onView(withText(R.string.message_login_fail)).check(matches(isDisplayed()));
    }

    @Test
    public void allFieldsEmpty() throws InterruptedException {
        //testEmptyFieldState(BOTH_FIELDS_ID);
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.btn_login)).perform(click());
        Thread.sleep(2000);
        onView(withText(R.string.message_login_fail)).check(matches(isDisplayed()));
    }

    @Test
    public void invalidLogin() throws InterruptedException {
        onView(withId(R.id.edt_username)).perform(typeText("test123"), closeSoftKeyboard());
        onView(withId(R.id.edt_password)).perform(typeText("test123"), closeSoftKeyboard());
        onView(withId(R.id.btn_login)).perform(click());
        Thread.sleep(2000);
        onView(withText(R.string.message_login_fail)).check(matches(isDisplayed()));
    }

    private void testEmptyFieldState(int notEmptyFieldId) throws InterruptedException {
        if (notEmptyFieldId != BOTH_FIELDS_ID)
            onView(withId(notEmptyFieldId)).perform(typeText("defaultText"), closeSoftKeyboard());

        onView(withId(R.id.btn_login)).perform(click());

        // wait during 15 seconds for a view
        //onView(isRoot()).perform(waitId(R.string.message_login_fail, TimeUnit.SECONDS.toMillis(15)));
        Thread.sleep(3000);
        onView(withText(R.string.message_login_fail)).check(matches(isDisplayed()));
        //onView(withText(R.string.message_login_fail)).check(matches(isDisplayed()));
    }
    // positive test, logging in app
    @Test
    public void loginValid() {

        String username = "username";
        String password = "password";

        try {
            username = login.getLogin(username);
            password = login.getLogin(password);
        } catch (IOException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.edt_username)).perform(typeText(username), closeSoftKeyboard());
        onView(withId(R.id.edt_password)).perform(typeText(password), closeSoftKeyboard());

        onView(withId(R.id.btn_login)).perform(click());
    }

    /** Perform action of waiting for a specific view id. */
    public static ViewAction waitId(final int viewId, final long millis) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isRoot();
            }

            @Override
            public String getDescription() {
                return "wait for a specific view with id <" + viewId + "> during " + millis + " millis.";
            }

            @Override
            public void perform(final UiController uiController, final View view) {
                uiController.loopMainThreadUntilIdle();
                final long startTime = System.currentTimeMillis();
                final long endTime = startTime + millis;
                final Matcher<View> viewMatcher = withId(viewId);

                do {
                    for (View child : TreeIterables.breadthFirstViewTraversal(view)) {
                        // found view with required ID
                        if (viewMatcher.matches(child)) {
                            return;
                        }
                    }
                    uiController.loopMainThreadForAtLeast(50);
                }
                while (System.currentTimeMillis() < endTime);
                // timeout happens
                throw new PerformException.Builder()
                        .withActionDescription(this.getDescription())
                        .withViewDescription(HumanReadables.describe(view))
                        .withCause(new TimeoutException())
                        .build();
            }
        };
    }
}
