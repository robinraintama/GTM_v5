package team.tracking.gtmv5;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.analytics.FirebaseAnalytics;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import team.tracking.gtmv5.dummy.DummyContent;

import java.util.ArrayList;
import java.util.List;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ItemDetailActivity} representing
 * item brand. On tablets, the activity presents the list of items and
 * item brand side-by-side using two vertical panes.
 */
public class ItemListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    /**
     * Firebase instance
     */
    private static FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        View recyclerView = findViewById(R.id.item_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        // Get firebase analytics instance

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        // Call firebase log event

        logFirebaseArray(DummyContent.firebase_items, "Product List", FirebaseAnalytics.Event.VIEW_ITEM_LIST);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, DummyContent.ITEMS, mTwoPane));
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final ItemListActivity mParentActivity;
        private final List<DummyContent.DummyItem> mValues;
        private final boolean mTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DummyContent.DummyItem item = (DummyContent.DummyItem) view.getTag();

                // Call firebase log event

                logFirebase((Bundle)DummyContent.firebase_items.get(item.position), "Product List", FirebaseAnalytics.Event.SELECT_CONTENT);

                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putString(ItemDetailFragment.ARG_ITEM_ID, item.id);
                    ItemDetailFragment fragment = new ItemDetailFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.item_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, ItemDetailActivity.class);
                    intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, item.id);

                    context.startActivity(intent);
                }
            }
        };

        SimpleItemRecyclerViewAdapter(ItemListActivity parent,
                                      List<DummyContent.DummyItem> items,
                                      boolean twoPane) {
            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mIdView.setText(mValues.get(position).name);
            holder.mContentView.setText(String.valueOf(mValues.get(position).price));

            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mIdView;
            final TextView mContentView;

            ViewHolder(View view) {
                super(view);
                mIdView = (TextView) view.findViewById(R.id.name);
                mContentView = (TextView) view.findViewById(R.id.price);
            }
        }
    }

    public static void logFirebaseArray(ArrayList arrayList, String listname, String event) {
        // Prepare ecommerce bundle

        Bundle ecommerceBundle = new Bundle();
        ecommerceBundle.putParcelableArrayList("items", arrayList);

        // Set relevant bundle-level parameters

        ecommerceBundle.putString(FirebaseAnalytics.Param.ITEM_LIST, listname);
        ecommerceBundle.putString("screen", "Item List");

        // Log event with ecommerce bundle

        mFirebaseAnalytics.logEvent(event, ecommerceBundle);
    }

    public static void logFirebase(Bundle bundle, String listname, String event) {
        // Prepare ecommerce bundle

        Bundle ecommerceBundle = new Bundle();
        ecommerceBundle.putBundle("items", bundle);
        ecommerceBundle.putString("eventCategory", "Catalog");
        ecommerceBundle.putString("eventAction", "Click on product");
        ecommerceBundle.putString("eventLabel", bundle.getString(FirebaseAnalytics.Param.ITEM_NAME));

        // Set relevant bundle-level parameters

        ecommerceBundle.putString(FirebaseAnalytics.Param.ITEM_LIST, listname);

        // Log event with ecommerce bundle

        mFirebaseAnalytics.logEvent(event, ecommerceBundle);
    }

    public static void logFirebase(String event, Bundle bundle) {
        // Log event with bundle

        mFirebaseAnalytics.logEvent(event, bundle);
    }

    public static void logFirebase(String event, ArrayList arrayList) {
        // Prepare ecommerce bundle

        Bundle ecommerceBundle = new Bundle();
        ecommerceBundle.putParcelableArrayList("items", arrayList);

        // Log event with bundle

        mFirebaseAnalytics.logEvent(event, ecommerceBundle);
    }
}
