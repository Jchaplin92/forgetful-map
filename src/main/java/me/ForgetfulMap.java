package me;

import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class ForgetfulMap implements ForgetfulMapMinimum<String, String> {

    private Integer sizeLimit;
    private HashMap<String, ContentAndLookups> delegate;

    public ForgetfulMap() {
        this.delegate = new HashMap<>();
    }

    public void createForgetfulMap(int sizeLimit) {
        this.sizeLimit = sizeLimit;
        this.delegate = new HashMap<>();
    }

    public void resetForgetfulMap() {
        this.sizeLimit = null;
        this.delegate = null;
    }

    @Override
    public void add(String key, String content) {
        if (delegate.size() < sizeLimit) {
            delegate.put(key, new ContentAndLookups(content));
        } else {
            delegate.remove(delegate.entrySet().stream().min(Map.Entry.comparingByValue()).get().getKey());
            delegate.put(key, new ContentAndLookups(content));
        }
    }

    @Override
    public String find(String key) {
            delegate.get(key).incrementLookups();
            return delegate.get(key).getContent();
    }

    public Map<String, String> getMapOfContent() {
        return delegate.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().getContent()));
    }

    public StringBuilder fractionFilled() {
        StringBuilder sb = new StringBuilder();
        sb.append(delegate.size() + "/" + sizeLimit);
        return sb;
    }

    private class ContentAndLookups implements Comparable<ContentAndLookups> {
        private String content;
        private int lookups;

        public ContentAndLookups(String content) {
            this.content = content;
            this.lookups = 0;
        }

        private void incrementLookups() {
            lookups++;
        }

        private String getContent() {
            return content;
        }

        private int getLookups() {
            return lookups;
        }

        @Override
        public int compareTo(ContentAndLookups o) {
            return Comparator.comparing(ContentAndLookups::getLookups).compare(this, o);
        }
    }

}
