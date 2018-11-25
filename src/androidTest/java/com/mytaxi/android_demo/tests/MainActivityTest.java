package com.mytaxi.android_demo.tests;


import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;


import com.mytaxi.android_demo.R;
import com.mytaxi.android_demo.activities.MainActivity;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;

import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;

import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;


/**
 * Created by RAFAELBERGAMINDASILV on 25/11/2018.
 */

public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity>
            mActivityRule = new ActivityTestRule<>(MainActivity.class, false, true);

    @Test
    public void whenActivityIsLaunched_shouldDisplayInitialState() {
        onView(withId(R.id.textSearch)).check(matches(isDisplayed()));
    }

    @Test
    public void searchSA() throws InterruptedException {

        onView(withId(R.id.textSearch)).perform(typeText("sa"), closeSoftKeyboard());
        Espresso.closeSoftKeyboard();
        Thread.sleep(5000);

        onView(withText("Sarah Scott")).perform(click());
        Thread.sleep(2000);
        onView(withId(R.id.fab)).perform(click());
    }
}