package app.insti.fragment;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import app.insti.Constants;
import app.insti.R;
import app.insti.Utils;
import app.insti.activity.MainActivity;
import app.insti.fragment.NewSearchFragment;
import app.insti.adapter.SearchAdapter;
import app.insti.adapter.TrainingBlogAdapter;
import app.insti.api.RetrofitInterface;
import app.insti.api.model.SearchDataPost;
import app.insti.api.model.TrainingBlogPost;
import retrofit2.Call;
import app.insti.fragment.SearchRecyclerViewFragment;


public class SearchFragment extends SearchRecyclerViewFragment<SearchDataPost, SearchAdapter> {

    private FloatingActionButton fab;

    List<String> filter_tags = new ArrayList<>();

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();




        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Search FAQs");
        Utils.setSelectedMenuItem(getActivity(), R.id.nav_search);

        setHasOptionsMenu(true);

        postType = SearchDataPost.class;
        adapterType = SearchAdapter.class;
        recyclerView = getActivity().findViewById(R.id.search_feed_recycler_view);
        swipeRefreshLayout = getActivity().findViewById(R.id.search_feed_swipe_refresh_layout);
        fab = getView().findViewById(R.id.fab_search);
        initFab();
        updateData();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initFab();
                updateData();
            }
        });
    }

    @Override
    protected Call<List<SearchDataPost>> getCall(RetrofitInterface retrofitInterface, String sessionIDHeader, int postCount) {
        return retrofitInterface.getSearchFeed(sessionIDHeader, postCount, 20, searchQuery, filter_tags);
    }

    private void initFab() {
            fab.show();
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NewSearchFragment newSearchFragment = new NewSearchFragment();
                    ((MainActivity) getActivity()).updateFragment(newSearchFragment);
                }
            });
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    if (dy > 0) fab.hide();
                    else if (dy < 0) fab.show();
                }
            });
//        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
     if (item.getItemId() != R.id.action_search && item.getItemId() != R.id.action_filter) {
        if (item.isChecked()) {
            item.setChecked(false);
            int idx = 0;
            while (idx < filter_tags.size()) {
                if (filter_tags.get(idx).equals(item.getTitle())) {
                    // Remove item
                    filter_tags.remove(idx);

                } else {
                    ++idx;
                }
            }
        } else {
            item.setChecked(true);
            filter_tags.add((String) item.getTitle());


        }

    }
    return true;


}


}