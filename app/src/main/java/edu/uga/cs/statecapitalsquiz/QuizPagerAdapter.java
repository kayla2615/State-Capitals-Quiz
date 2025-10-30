package edu.uga.cs.statecapitalsquiz;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.HashMap;
import java.util.Map;

public class QuizPagerAdapter extends FragmentStateAdapter {
    private Map<Integer, QuizFragment> fragmentMap = new HashMap<>();
    
    public QuizPagerAdapter(
            FragmentManager fragmentManager,
            Lifecycle lifecycle ) {
        super( fragmentManager, lifecycle );
    }

    @Override
    public Fragment createFragment(int position){
        QuizFragment fragment = QuizFragment.newInstance( position );
        fragmentMap.put(position, fragment);
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 6;
    }
    
    /**
     * Get the fragment at the specified position
     * @param position the position
     * @return the QuizFragment at that position, or null if not found
     */
    public QuizFragment getFragment(int position) {
        return fragmentMap.get(position);
    }

    public void clear() {
        fragmentMap.clear();
        notifyDataSetChanged();
    }
}
