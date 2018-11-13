package com.example.android.hubert;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.android.hubert.Activities.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule
            = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void click_FloatBtn_launchs_AddMemberActivity(){
        // Find the float btn and perform a click on it
        onView(withId(R.id.fab)).perform(click());

        // Check to see if the addMember activity class is opened by checking if the
        // add btn is available
        onView(withId(R.id.bt_add)).check(matches(withText("ADD")));


    }


}
