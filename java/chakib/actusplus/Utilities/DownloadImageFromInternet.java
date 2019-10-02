package chakib.actusplus.Utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.InputStream;

import chakib.actusplus.R;

/**
 * Created by Chakib on 15/08/2017.
 */

public class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap> {

                ImageView imageView;

        public DownloadImageFromInternet(ImageView imageView) {
                this.imageView = imageView;
                //Toast.makeText(getApplicationContext(), "Please wait, it may take a few minute...", Toast.LENGTH_SHORT).show();
                }

        protected Bitmap doInBackground(String... urls) {
                String imageURL = urls[0];
                Bitmap bimage = null;
                try {
                InputStream in = new java.net.URL(imageURL).openStream();
                bimage = BitmapFactory.decodeStream(in);

                } catch (Exception e) {
                Log.e("Error Message", e.getMessage());
                e.printStackTrace();
                }
                return bimage;
                }

        protected void onPostExecute(Bitmap result)   {
                imageView.setImageBitmap(result);
                if(result == null)
                        imageView.setImageResource(R.mipmap.ic_launcher);
        }


}
