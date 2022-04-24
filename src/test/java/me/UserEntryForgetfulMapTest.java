package me;

import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

public class UserEntryForgetfulMapTest {

    @Test
    public void canAddKeyAndContentAndRetrieveWithFind() {
        UserEntryForgetfulMap underTest = new UserEntryForgetfulMap(1);
        underTest.add("firstkey", "firstvalue");
        assertEquals("firstvalue", underTest.find("firstkey"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void doesNotProvideAFalseValueWithTheWrongKey() {
        UserEntryForgetfulMap underTest = new UserEntryForgetfulMap(1);
        underTest.add("firstkey", "firstvalue");
        underTest.find("WRONGKEY");
    }

    @Test
    public void doesNotExceedAssociationLimit() {
        UserEntryForgetfulMap underTest = new UserEntryForgetfulMap(1);
        underTest.add("firstkey", "firstvalue");
        underTest.add("secondkey", "secondvalue");
        assertThat(underTest.numberOfAssociations(), equalTo(1));
    }

    @Test
    public void uponReachingAssociationLimitEjectsEarlierAssociation() {
        UserEntryForgetfulMap underTest = new UserEntryForgetfulMap(1);
        underTest.add("firstkey", "firstvalue");
        underTest.add("secondkey", "secondvalue");
        assertThat(underTest.numberOfAssociations(), equalTo(1));
        assertThat(underTest.find("secondkey"), equalTo("secondvalue"));
    }
    @Test(expected = IllegalArgumentException.class)
    public void uponReachingAssociationLimitEjectsLeastSearchedAssociation() {
        UserEntryForgetfulMap underTest = new UserEntryForgetfulMap(2);
        underTest.add("firstkey", "firstvalue");
        underTest.add("secondkey", "secondvalue");
        underTest.find("firstkey");
        underTest.add("thirdkey", "thirdvalue");
        assertThat(underTest.numberOfAssociations(), equalTo(2));
        assertThat(underTest.find("firstkey"), equalTo("firstvalue"));
        assertThat(underTest.find("thirdkey"), equalTo("thirdvalue"));
        underTest.find("secondkey");
    }

    @Test
    public void uponReachingAssociationLimitWithTiebreakingLeastSearchedAssociationsWillChooseAssociationWithMostRecentSearches() throws InterruptedException {
        UserEntryForgetfulMap underTest = new UserEntryForgetfulMap(2);
        underTest.add("firstkey", "firstvalue");
        underTest.add("secondkey", "secondvalue");

        underTest.find("secondkey");
        underTest.find("secondkey");

        Thread.sleep(5000L);

        underTest.find("firstkey");
        underTest.find("firstkey");

        underTest.add("thirdkey", "thirdvalue");

        assertThat(underTest.numberOfAssociations(), equalTo(2));
        assertEquals(underTest.find("firstkey"), "firstvalue");
        assertEquals(underTest.find("thirdkey"),"thirdvalue");
    }

    @Test
    public void handlesEjectingAssociationsWithNoSearchHistory() throws InterruptedException {
        UserEntryForgetfulMap underTest = new UserEntryForgetfulMap(2);
        underTest.add("firstkey", "firstvalue");
        underTest.add("secondkey", "secondvalue");
        underTest.add("thirdkey", "thirdvalue");

        assertThat(underTest.numberOfAssociations(), equalTo(2));
        assertEquals(underTest.find("thirdkey"),"thirdvalue");
    }

    @Test
    public void showsAllAssociations() {
        UserEntryForgetfulMap underTest = new UserEntryForgetfulMap(2);
        underTest.add("firstkey", "firstvalue");
        underTest.add("secondkey", "secondvalue");

        assertThat(underTest.allAssociations(), equalTo("firstkey : firstvalue\nsecondkey : secondvalue\n"));
    }
}