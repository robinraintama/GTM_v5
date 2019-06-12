package team.tracking.gtmv5.dummy;

import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample name for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<DummyItem> ITEMS = new ArrayList<DummyItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();

    /**
     * An array of sample (dummy) firebase items.
     */
    public static final ArrayList firebase_items = new ArrayList();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(i));
        }
    }

    private static void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, item.id);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, item.name);
        bundle.putString(FirebaseAnalytics.Param.ITEM_BRAND, item.brand);
        bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, item.category);
        bundle.putString(FirebaseAnalytics.Param.ITEM_VARIANT, item.variant);
        bundle.putString(FirebaseAnalytics.Param.CURRENCY, item.currency);
        bundle.putDouble(FirebaseAnalytics.Param.PRICE, item.price);
        bundle.putLong(FirebaseAnalytics.Param.INDEX, item.position);
        firebase_items.add(bundle);
    }

    private static DummyItem createDummyItem(int position) {
        return new DummyItem(position, String.valueOf(position), "Item " + position, "brand " + position, "category " + position, "variant " + position, "IDR", position * 10000);
    }

    /**
     * A dummy item representing a piece of name.
     */
    public static class DummyItem {
        public final int position;
        public final String id;
        public final String name;
        public final String brand;
        public final String category;
        public final String variant;
        public final String currency;
        public final double price;

        public DummyItem(int position, String id, String name, String brand, String category, String variant, String currency, double price) {
            this.position = position;
            this.id = id;
            this.name = name;
            this.brand = brand;
            this.category = category;
            this.variant = variant;
            this.currency = currency;
            this.price = price;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
