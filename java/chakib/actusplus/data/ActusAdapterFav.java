package chakib.actusplus.data;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import chakib.actusplus.R;
import chakib.actusplus.Utilities.DownloadImageFromInternet;

/**
 * Created by Chakib on 02/08/2017.
 */

public class ActusAdapterFav extends RecyclerView.Adapter<ActusAdapterFav.ActusViewHolder> {

    ArrayList<Actus> actusArrayList = new ArrayList();
    ArrayList<ImageView> ImageArrayList = new ArrayList();
    private final ActusAdapterOnClickHandler mClickHandler;

    public interface ActusAdapterOnClickHandler {
        void onClick(Actus selectedActus);
    }

    public ActusAdapterFav(ActusAdapterOnClickHandler mClickHandler) {
        this.mClickHandler = mClickHandler;
    }

    public ActusAdapterFav(ArrayList<Actus> actusArrayList, ActusAdapterOnClickHandler mClickHandler) {
        this.actusArrayList = actusArrayList;
        this.mClickHandler = mClickHandler;
    }


    public class ActusViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView titre;
        TextView upperTitle;
        TextView date;
        ImageView image;

        public ActusViewHolder(View itemView) {
            super(itemView);
            titre = (TextView) itemView.findViewById(R.id.tv_actus_title);
            upperTitle = (TextView) itemView.findViewById(R.id.tv_actus_upper_title);
            date = (TextView) itemView.findViewById(R.id.tv_actus_date);
            image = (ImageView) itemView.findViewById(R.id.iv_actus_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
                Actus selectedActus = actusArrayList.get(adapterPosition);
                mClickHandler.onClick(selectedActus);

        }


    }


    @Override
    public ActusViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.actus_item,parent,false);
        return new ActusViewHolder(layout);

    }

    @Override
    public void onBindViewHolder(ActusViewHolder holder, int position) {

            holder.titre.setText(actusArrayList.get(position).getTitre());
            holder.upperTitle.setText(actusArrayList.get(position).getUpperTitle());
            new DownloadImageFromInternet(holder.image)
                    .execute(actusArrayList.get(position).getImageDrawable());
            holder.date.setText(actusArrayList.get(position).getDate());


    }

    @Override
    public int getItemCount() {
        return actusArrayList.size() ;

    }

    public Object getItem(int position) {
        return actusArrayList.get(position);
    }

    public void setActusData(ArrayList<Actus>  ActusData) {
        actusArrayList.clear();
        actusArrayList.addAll(ActusData);
        notifyDataSetChanged();
    }

    public void addToActusData(ArrayList<Actus>  ActusData) {
        actusArrayList.addAll(ActusData);
        notifyDataSetChanged();
    }




}
