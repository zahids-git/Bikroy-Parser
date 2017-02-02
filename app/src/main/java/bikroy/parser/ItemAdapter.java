package bikroy.parser;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by SSL_ZAHID on 2/2/2017.
 */

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    ArrayList<ItemModel> itemModels;

    public ItemAdapter(ArrayList<ItemModel> itemModels){
        this.itemModels = itemModels;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_adapter, null, false));
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        try{
            Glide.with(holder.itemView.getContext()).load("http:"+itemModels.get(position).getImageUrl()).placeholder(R.drawable.placeholder).into(holder.itemImage);
            holder.title.setText(itemModels.get(position).getTitle());
            holder.otherInformation.setText("Price: "+itemModels.get(position).getPrice()+", Location: "+itemModels.get(position).getLocation());

            if(itemModels.get(position).isTop()) holder.isTopLayout.setBackgroundResource(R.drawable.top_background);
            else holder.isTopLayout.setBackgroundColor(Color.WHITE);
        }catch (Exception e){}
    }

    @Override
    public int getItemCount() {
        return itemModels.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout isTopLayout;
        private ImageView itemImage;
        private TextView title;
        private TextView otherInformation;

        public ItemViewHolder(View itemView) {
            super(itemView);
            initialize(itemView);
        }

        private void initialize(View view) {
            isTopLayout = (LinearLayout) view.findViewById(R.id.isTopLayout);
            itemImage = (ImageView) view.findViewById(R.id.itemImage);
            title = (TextView) view.findViewById(R.id.title);
            otherInformation = (TextView) view.findViewById(R.id.otherInformation);
        }

    }
}
