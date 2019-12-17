package com.example.engineeringaitest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.example.engineeringaitest.R;
import com.example.engineeringaitest.model.Item;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ItemAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private boolean isLoaderVisible = false;
    private final OnItemClickListener listener;
    private Context ctx;
    private List<Item> items;

    public ItemAdapter(Context ctx, List<Item> items, OnItemClickListener listener) {
        this.ctx = ctx;
        this.items = items;
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick();
    }


    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new ViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main, parent, false));
            case VIEW_TYPE_LOADING:
                return new ProgressHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false));
            default:
                return null;
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (isLoaderVisible) {
            return position == items.size() - 1 ? VIEW_TYPE_LOADING : VIEW_TYPE_NORMAL;
        } else {
            return VIEW_TYPE_NORMAL;
        }
    }

    @Override
    public void onViewRecycled(@NonNull BaseViewHolder holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    public void addItems(List<Item> postItems) {
        items.addAll(postItems);
        notifyDataSetChanged();
    }

    public void addLoading() {
        isLoaderVisible = true;
        items.add(new Item());
        notifyItemInserted(items.size() - 1);
    }

    public void removeLoading() {
        isLoaderVisible = false;
        int position = items.size() - 1;
        Item item = getItem(position);
        if (item != null) {
            items.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    Item getItem(int position) {
        return items.get(position);
    }


    public class ViewHolder extends BaseViewHolder {
        @BindView(R.id.txtTitle)
        TextView txtTitle;
        @BindView(R.id.txtCreatedAt)
        TextView txtCreatedAt;
        @BindView(R.id.swtSelection)
        Switch swtSelection;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        protected void clear() {

        }

        public void onBind(int position) {
            super.onBind(position);
            Item item = items.get(position);
            txtTitle.setText(item.getTitle());


            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss'Z'");
            SimpleDateFormat output = new SimpleDateFormat("dd/MM/yyyy");

            try {
                txtCreatedAt.setText(output.format(dateFormat.parse(item.getCreated_at())));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            // Make sure we update the model if the user taps the switch
            swtSelection.setOnCheckedChangeListener((compoundButton, isChecked) -> {

                item.setSelected(isChecked);

                listener.onItemClick();
            });

            swtSelection.setChecked(item.isSelected());
        }
    }

    public List<Item> getItems() {
        return items;
    }

    public class ProgressHolder extends BaseViewHolder {
        ProgressHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        protected void clear() {
        }
    }


}
