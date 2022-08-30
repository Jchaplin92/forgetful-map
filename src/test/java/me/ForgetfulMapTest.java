package me;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;


class ForgetfulMapTest {
    private ForgetfulMap forgetfulMap;

    @BeforeEach
    void setUp() {
        forgetfulMap = new ForgetfulMap();
        forgetfulMap.createForgetfulMap(2);
    }

    @AfterEach
    void tearDown() {
        forgetfulMap = null;
    }

    @Test
    void canAddSomethingToForgetfulMapButNotExceedSizeLimit() {
        forgetfulMap.add("first_key", "first_value");
        forgetfulMap.add("second_key", "second_value");
        forgetfulMap.add("third_key", "third_value");

        assertThat(forgetfulMap.getMapOfContent().size(), equalTo(2));
    }

    @Test
    void whenFirstItemIsLeastLookedUpAtZeroTimesThenItIsEjected() {
        forgetfulMap.add("first_key", "first_value");
        forgetfulMap.add("second_key", "second_value");
        forgetfulMap.find("second_key");
        forgetfulMap.add("third_key", "third_value");

        assertThat(forgetfulMap.getMapOfContent().keySet(), containsInAnyOrder("second_key", "third_key"));
    }

    @Test
    void whenSecondItemIsLeastLookedUpAtOneTimeThenItIsEjected() {
        forgetfulMap.add("first_key", "first_value");
        forgetfulMap.add("second_key", "second_value");

        forgetfulMap.find("first_key");

        forgetfulMap.add("third_key", "third_value");
        assertThat(forgetfulMap.getMapOfContent().keySet(), containsInAnyOrder("first_key", "third_key"));
    }

}