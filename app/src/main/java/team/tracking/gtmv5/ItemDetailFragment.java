package team.tracking.gtmv5;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.analytics.FirebaseAnalytics;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import team.tracking.gtmv5.dummy.DummyContent;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy name this fragment is presenting.
     */
    private DummyContent.DummyItem mItem;
    private static FirebaseAnalytics mFirebaseAnalytics;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy name specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load name from a name provider.
            mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.name);
            }

            // Get firebase analytics instance

            mFirebaseAnalytics = FirebaseAnalytics.getInstance(this.getActivity());

            // Call firebase log event

            logFirebase((Bundle) DummyContent.firebase_items.get(mItem.position), FirebaseAnalytics.Event.VIEW_ITEM, true);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_detail, container, false);

        // Show the dummy name as text in a TextView.
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.item_detail)).setText(mItem.brand);
        }

        ((Button) rootView.findViewById(R.id.button_atc)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = (Bundle) DummyContent.firebase_items.get(mItem.position);
                logFirebase(bundle, FirebaseAnalytics.Event.ADD_TO_CART, false);

                Snackbar.make(view, bundle.getString(FirebaseAnalytics.Param.ITEM_NAME) + " added to cart", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        return rootView;
    }

    public static void logFirebase(Bundle bundle, String event, boolean isScreen) {
        // Prepare ecommerce bundle

        Bundle ecommerceBundle = new Bundle();
        ecommerceBundle.putBundle("items", bundle);

        // Add Custom Dimension User ID (Hit)

        ecommerceBundle.putString("userId", "robin");

        // Send param key "screen" to record screen view
        if (isScreen) {
            ecommerceBundle.putString("screen", "Item Detail");
        } else {
            ecommerceBundle.putString("eventCategory", "Item Detail");
            ecommerceBundle.putString("eventAction", "Click add to cart");
            ecommerceBundle.putString("eventLabel", bundle.getString(FirebaseAnalytics.Param.ITEM_NAME));
        }

        // Log event with ecommerce bundle

        mFirebaseAnalytics.logEvent(event, ecommerceBundle);
    }
}
