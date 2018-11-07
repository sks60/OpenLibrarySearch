package com.example.sandysaju.openlibrarysearch.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class APIBooks {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<APIBook> ITEMS = new ArrayList<APIBook>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, APIBook> ITEM_MAP = new HashMap<String, APIBook>();

    private static final int COUNT = 0;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            //addItem(createDummyItem(i));
        }
    }

    public static void addItem(APIBook item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static APIBook createDummyItem(String isbn, String url, String bookTitle, String year) {
        return new APIBook(isbn, url, bookTitle, year);
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class APIBook {
        public final String id;//isbn
        public final String details;//url
        public final String bookTitle;
        public final String year;

        public APIBook(String id, String details, String bookTitle, String year) {
            this.id = id;//isbn
            this.details = details;//url
            this.bookTitle = bookTitle;
            this.year = year;
        }

        @Override
        public String toString() {
            return details;
        }
    }
}
