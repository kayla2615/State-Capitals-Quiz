package edu.uga.cs.statecapitalsquiz;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.HashMap;
import java.util.Map;

/**
 * QuizPagerAdapter is a FragmentStateAdapter that manages QuizFragment instances
 * for the ViewPager2 in QuizActivity. It maintains a map of fragments by position
 * to allow the activity to access and interact with individual quiz question fragments.
 */
public class QuizPagerAdapter extends FragmentStateAdapter {
    
    /**
     * Map that stores QuizFragment instances by their position index
     */
    private Map<Integer, QuizFragment> fragmentMap = new HashMap<>();
    
    /**
     * Constructs a new QuizPagerAdapter.
     *
     * @param fragmentManager The FragmentManager that will interact with this adapter
     * @param lifecycle The Lifecycle of the adapter
     */
    public QuizPagerAdapter(
            FragmentManager fragmentManager,
            Lifecycle lifecycle ) {
        super( fragmentManager, lifecycle );
    }

    /**
     * Creates a new QuizFragment for the specified position.
     *
     * @param position The position (0-based index) of the question
     * @return A new QuizFragment instance for the specified position
     */
    @Override
    public Fragment createFragment(int position){
        QuizFragment fragment = QuizFragment.newInstance( position );
        fragmentMap.put(position, fragment);
        return fragment;
    }

    /**
     * Returns the total number of items in the adapter.
     *
     * @return The number of quiz questions (6)
     */
    @Override
    public int getItemCount() {
        return 6;
    }
    
    /**
     * Gets the QuizFragment at the specified position.
     *
     * @param position The position (0-based index) of the fragment
     * @return The QuizFragment at that position, or null if not found
     */
    public QuizFragment getFragment(int position) {
        return fragmentMap.get(position);
    }

    /**
     * Clears all fragments from the adapter and notifies that the data set has changed.
     * Used when transitioning from quiz view to results view.
     */
    public void clear() {
        fragmentMap.clear();
        notifyDataSetChanged();
    }
}
