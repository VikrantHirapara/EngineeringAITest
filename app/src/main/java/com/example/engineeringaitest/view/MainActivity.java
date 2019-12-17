package com.example.engineeringaitest.view;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.engineeringaitest.R;
import com.example.engineeringaitest.adapter.ItemAdapter;
import com.example.engineeringaitest.api.ApiService;
import com.example.engineeringaitest.api.RetrofitInstance;
import com.example.engineeringaitest.model.Item;
import com.example.engineeringaitest.model.ItemHits;
import com.example.engineeringaitest.util.PaginationListener;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.engineeringaitest.util.PaginationListener.PAGE_START;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "MainActivity";

    @BindView(R.id.lstItems)
    RecyclerView lstItems;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;
    private ItemAdapter adapter;
    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private int totalPage = 10;
    private boolean isLoading = false;
    int itemCount = 0;
    static int selectCount = 0;
    TextView notifCount;
    private Context ctx = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        swipeRefresh.setOnRefreshListener(this);
        lstItems.setHasFixedSize(true);
        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        lstItems.setLayoutManager(layoutManager);

        adapter = new ItemAdapter(ctx, new ArrayList<>(), () -> {
            int count = 0;
            for (int i = 0; i < adapter.getItems().size(); i++) {

                if (adapter.getItems().get(i).isSelected()) {
                    count = count + 1;

                }
            }
            setNotifCount(count);
        });
        lstItems.setAdapter(adapter);
        doApiCall();

        /**
         * add scroll listener while user reach in bottom load more will call
         */
        lstItems.addOnScrollListener(new PaginationListener(layoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage++;
                doApiCall();
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
    }

    @Override
    public void onRefresh() {
        itemCount = 0;
        currentPage = PAGE_START;
        isLastPage = false;
        adapter.clear();
        doApiCall();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.mainmenu, menu);

        View count = menu.findItem(R.id.count).getActionView();
        notifCount = (TextView) count.findViewById(R.id.notif_count);
        notifCount.setText(String.valueOf(selectCount));
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * do api call here to fetch data from server
     * In example i'm adding data manually
     */
    private void doApiCall() {

        if (currentPage != PAGE_START) adapter.removeLoading();

        // check weather is last page or not
        if (totalPage != 0) {
            adapter.addLoading();
            apiCall(currentPage);
        } else {
            isLastPage = true;
        }
        isLoading = false;
    }


    private void apiCall(int page) {
        ApiService service = RetrofitInstance.getRetrofitInstance().create(ApiService.class);
        Call<ItemHits> call = service.itemHits(page, "story");

        call.enqueue(new Callback<ItemHits>() {
            @Override
            public void onResponse(Call<ItemHits> call, Response<ItemHits> response) {
                adapter.removeLoading();
                swipeRefresh.setRefreshing(false);
                if (response.body() != null) {

                    //currentPage = response.body().getPage() + 1;
                    totalPage = response.body().getNbPages();
                    adapter.addItems(response.body().getHits());
                }
            }

            @Override
            public void onFailure(Call<ItemHits> call, Throwable t) {
                adapter.removeLoading();
                Toast.makeText(MainActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setNotifCount(int count) {
        selectCount = count;
        invalidateOptionsMenu();
    }


}
