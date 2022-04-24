package me;

import java.util.ArrayList;
import java.util.HashMap;

import static me.TimestampCreator.getCurrentTimestamp;

public class UserEntryForgetfulMap implements ForgetfulMap<String, String> {
    private final int associationLimit;
    private HashMap<String, String> keyContentPairs;
    private HashMap<String, FindFrequency> keySearchFrequencyPairs;

    public UserEntryForgetfulMap(int associationLimit) {
        this.associationLimit = associationLimit;
        this.keyContentPairs = new HashMap<>();
        this.keySearchFrequencyPairs = new HashMap<>();
    }

    public void add(String key, String content) {
        if (keyContentPairs.size() < associationLimit) {
            addAndIncrementSearchCount(key, content);
        } else {
            String leastSearchedKey = null;
            Integer lowestSearchCount = null;
            for (String findFrequencyKey : keySearchFrequencyPairs.keySet()) {
                if (leastSearchedKey == null && lowestSearchCount == null) {
                    leastSearchedKey = findFrequencyKey;
                    lowestSearchCount = keySearchFrequencyPairs.get(findFrequencyKey).searchCount;
                } else {
                    //check if current findFrequencyCount is lower
                    if (keySearchFrequencyPairs.get(findFrequencyKey).searchCount < lowestSearchCount) {
                        leastSearchedKey = findFrequencyKey;
                        lowestSearchCount = keySearchFrequencyPairs.get(findFrequencyKey).searchCount;
                    } else if (keySearchFrequencyPairs.get(findFrequencyKey).searchCount == lowestSearchCount) {
                        //code to deal with equally low search counts
                        double currentKeyAverageSearchTime = keySearchFrequencyPairs.get(findFrequencyKey).searchHistory.stream().mapToDouble(x -> x.doubleValue()).average().orElse(0);
                        double previousLeastSearchedKeySearchTime = keySearchFrequencyPairs.get(leastSearchedKey).searchHistory.stream().mapToDouble(x -> x.doubleValue()).average().orElse(0);
                        //this key is on average less recently searched
                        if (currentKeyAverageSearchTime < previousLeastSearchedKeySearchTime)
                            leastSearchedKey = findFrequencyKey;
                    }
                }
            }

            keyContentPairs.remove(leastSearchedKey);
            keySearchFrequencyPairs.remove(leastSearchedKey);
            addAndIncrementSearchCount(key, content);
        }
    }

    private void addAndIncrementSearchCount(String key, String content) {
        keyContentPairs.put(key, content);
        keySearchFrequencyPairs.put(key, new FindFrequency());
    }

    public String find(String key) {
        if (keyContentPairs.containsKey(key)) {
            keySearchFrequencyPairs.get(key).incrementSearchHistory();
            return keyContentPairs.get(key);
        } else {
            throw new IllegalArgumentException("cannot find content for key '" + key + "'\n");
        }
    }

    public int numberOfAssociations() {
        return keyContentPairs.size();
    }

    public String allAssociations() {
        String result = "";
        for (String key : keyContentPairs.keySet()) {
            result += key + " : " + keyContentPairs.get(key) + "\n";
        }

        return result;
    }

    private class FindFrequency {
        private ArrayList<Long> searchHistory;
        private int searchCount;
        public FindFrequency() {
            this.searchHistory = new ArrayList<>();
            this.searchCount = 0;
        }

        private void incrementSearchHistory() {
            searchHistory.add(getCurrentTimestamp());
            searchCount++;
        }
    }


}
