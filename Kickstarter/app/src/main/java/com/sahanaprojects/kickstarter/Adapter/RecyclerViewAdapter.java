package com.sahanaprojects.kickstarter.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sahanaprojects.kickstarter.DetailsActivity;
import com.sahanaprojects.kickstarter.MainActivity;
import com.sahanaprojects.kickstarter.Model.KickstarterDetails;
import com.sahanaprojects.kickstarter.R;
import com.sahanaprojects.kickstarter.utils.DateTimeConverter;

import java.util.ArrayList;
import java.util.List;

//Recyclerview adapter class
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    public List<KickstarterDetails> kickstarterDetailsList;
    Context context;
    private List<KickstarterDetails> kickstarterDetailsFiltered;

    //constructor
    public RecyclerViewAdapter(List<KickstarterDetails> kickstarterDetailsList, Context context,List<KickstarterDetails> kickstarterDetailsFiltered){
        this.context = context;
        this.kickstarterDetailsList = kickstarterDetailsList;
        this.kickstarterDetailsFiltered = kickstarterDetailsFiltered;
    }
    //on createView holder
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items, parent, false);
            return new ItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_layout, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    //bindViewHolder
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        if (viewHolder instanceof ItemViewHolder) {

            populateItemRows((ItemViewHolder) viewHolder, position);
        } else if (viewHolder instanceof LoadingViewHolder) {
            showLoadingView((LoadingViewHolder) viewHolder, position);
        }

    }

    @Override
    public int getItemCount() {
        return kickstarterDetailsList == null ? 0 : kickstarterDetailsList.size();
    }

    /**
     * The following method decides the type of ViewHolder to display in the RecyclerView
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        return kickstarterDetailsList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    //to search the list according to the title
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    kickstarterDetailsList = kickstarterDetailsFiltered;
                } else {
                    List<KickstarterDetails> filteredList = new ArrayList<>();
                    for (KickstarterDetails row : kickstarterDetailsList) {

                        if (row.getTitle().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    kickstarterDetailsList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = kickstarterDetailsList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                kickstarterDetailsList = (ArrayList<KickstarterDetails>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    //ItemViewHolder class to load the layout
    private class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView tvHeading, tvPledge, tvNumber, tvBacker;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            tvHeading = itemView.findViewById(R.id.heading);
            tvPledge = itemView.findViewById(R.id.pedge);
            tvBacker = itemView.findViewById(R.id.banker);
            tvNumber = itemView.findViewById(R.id.number_of_days);

            //on click of the itemView, go to next page
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    KickstarterDetails kickstarterDetails = kickstarterDetailsList.get(pos);
                    //Add the data to the bundle and send via intent
                    Bundle bundle = new Bundle();
                    bundle.putString(context.getString(R.string.Image),"https://www.kickstarter.com/"+kickstarterDetails.getUrl());
                    bundle.putString(context.getString(R.string.Title),kickstarterDetails.getTitle());
                    bundle.putString(context.getString(R.string.Description),kickstarterDetails.getBlurb());
                    bundle.putString(context.getString(R.string.by),kickstarterDetails.getBy());
                    bundle.putString(context.getString(R.string.Pledges),kickstarterDetails.getAmtPledged()+"");
                    bundle.putString(context.getString(R.string.Bancker),kickstarterDetails.getNumBackers());
                    bundle.putString(context.getString(R.string.Percentage),kickstarterDetails.getPercentageFunded()+"");
                    bundle.putString(context.getString(R.string.Location),kickstarterDetails.getLocation());
                    bundle.putString(context.getString(R.string.state),kickstarterDetails.getState()+" ,"+kickstarterDetails.getCountry());
                    bundle.putString(context.getString(R.string.Currency),kickstarterDetails.getCurrency());
                    bundle.putString(context.getString(R.string.type),kickstarterDetails.getType());
                    bundle.putString(context.getString(R.string.endTime),kickstarterDetails.getEndTime());
                    Intent intent = new Intent(context, DetailsActivity.class);
                    intent.putExtra("BUNDLE",bundle);
                    context.startActivity(intent);
                }
            });
        }
    }

    //Display progress bar here
    private class LoadingViewHolder extends RecyclerView.ViewHolder {

        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressbar);
        }
    }

    private void showLoadingView(LoadingViewHolder viewHolder, int position) {
        //ProgressBar would be displayed

    }



    //Populate the recycler view
    private void populateItemRows(ItemViewHolder viewHolder, int position) {

           KickstarterDetails item = kickstarterDetailsList.get(position);
           viewHolder.tvNumber.setText("End Time : "+ DateTimeConverter.convertDateTimetoDate(item.getEndTime()));
           viewHolder.tvBacker.setText("No. Of Backers : "+item.getNumBackers());
           viewHolder.tvPledge.setText("Amount Pledged : $"+item.getAmtPledged());
           viewHolder.tvHeading.setText(item.getTitle()+"");



    }


}
