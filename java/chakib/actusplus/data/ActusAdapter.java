package chakib.actusplus.data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.util.ArrayList;

import chakib.actusplus.MainActivity;
import chakib.actusplus.R;
import chakib.actusplus.Utilities.DownloadImageFromInternet;

import static java.lang.String.*;

/**
 * Created by Chakib on 02/08/2017.
 */

public class ActusAdapter extends RecyclerView.Adapter<ActusAdapter.ActusViewHolder> {

    ArrayList<Actus> actusArrayList = new ArrayList();
    ArrayList<ImageView> ImageArrayList = new ArrayList();
    private final ActusAdapterOnClickHandler mClickHandler;

    public interface ActusAdapterOnClickHandler {
        void onClick(Actus selectedActus);
    }

    public ActusAdapter(ActusAdapterOnClickHandler mClickHandler) {
        this.mClickHandler = mClickHandler;
    }

    public ActusAdapter(ArrayList<Actus> actusArrayList, ActusAdapterOnClickHandler mClickHandler) {
        this.actusArrayList = actusArrayList;
        this.mClickHandler = mClickHandler;
    }


    public class ActusViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView titre;
        TextView upperTitle;
        TextView date;
        ImageView image;
        Button button;

        public ActusViewHolder(View itemView) {
            super(itemView);
            titre = (TextView) itemView.findViewById(R.id.tv_actus_title);
            upperTitle = (TextView) itemView.findViewById(R.id.tv_actus_upper_title);
            date = (TextView) itemView.findViewById(R.id.tv_actus_date);
            image = (ImageView) itemView.findViewById(R.id.iv_actus_image);
            button = (Button) itemView.findViewById(R.id.button1);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            if(adapterPosition != actusArrayList.size())
            {
                Actus selectedActus = actusArrayList.get(adapterPosition);
                mClickHandler.onClick(selectedActus);
            }
        }


    }


    @Override
    public ActusViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        /*View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.actus_item,parent,false);
        return new ActusViewHolder(layout);*/

        View itemView;

        if(viewType == R.layout.actus_item){
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.actus_item, parent, false);
        }

        else {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.button, parent, false);
        }

        return new ActusViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ActusViewHolder holder, int position) {

        if(position == actusArrayList.size()) {
            /*holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(MainActivity.class.getInterfaces(), "Button Clicked", Toast.LENGTH_LONG).show();
                }
            });*/
        }
        else {
            holder.titre.setText(actusArrayList.get(position).getTitre());
            holder.upperTitle.setText(actusArrayList.get(position).getUpperTitle());
            new DownloadImageFromInternet(holder.image)
                    .execute(actusArrayList.get(position).getImageDrawable());
            holder.date.setText(actusArrayList.get(position).getDate());
            }

    }

    @Override
    public int getItemCount() {
        //return actusArrayList.size() ;
        return actusArrayList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return (position ==actusArrayList.size()) ? R.layout.button : R.layout.actus_item;
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
