package com.plexxoo;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class AsyncTaskDownloadImageActivity 
	extends Activity {
	// Static so that the thread access the latest attribute
	private static ImageView imageView;
	private static Bitmap downloadBitmap;

	private static ProgressBar cargando;
	private static String url="https://i1.wp.com/cursosdedesarrollo.com/wp-content/uploads/2017/05/final-architecture.png";
	private static Button descarga;
	
/** Called when the activity is first created. */


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		// get the latest imageView after restart of the application
		imageView = (ImageView) findViewById(R.id.imageView1);
		cargando = (ProgressBar) findViewById(R.id.cargando);
		descarga=(Button)findViewById(R.id.download);
		// Did we already download the image?
		if (downloadBitmap != null) {
			imageView.setImageBitmap(downloadBitmap);
		}
	}

	public void resetPicture(View view) {
		if (downloadBitmap != null) {
			downloadBitmap = null;
		}
		imageView.setImageResource(R.drawable.ic_launcher);
	}

	public void downloadPicture(View view) {
		MiTarea tarea=new MiTarea();
		tarea.execute(new String[] { url });
	}

    public void descarga(View v){
        MiTarea tarea=new MiTarea();
        tarea.execute(new String[] { url });
    }



	

	// Utiliy method to download image from the internet
	static private Bitmap downloadBitmap(String url) throws IOException {
		HttpUriRequest request = new HttpGet(url.toString());
		HttpClient httpClient = new DefaultHttpClient();
		HttpResponse response = httpClient.execute(request);

		StatusLine statusLine = response.getStatusLine();
		int statusCode = statusLine.getStatusCode();
		if (statusCode == 200) {
			HttpEntity entity = response.getEntity();
			byte[] bytes = EntityUtils.toByteArray(entity);

			Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0,
					bytes.length);
			return bitmap;
		} else {
			throw new IOException("Download failed, HTTP response code "
					+ statusCode + " - " + statusLine.getReasonPhrase());
		}
	}
	static public class MiTarea extends AsyncTask
	<String,Void,Bitmap>{
		protected void onPreExecute(){
			cargando.setVisibility(View.VISIBLE);
			descarga.setEnabled(false);
		}

		@Override
		protected Bitmap doInBackground(String... params) {
			try {
				downloadBitmap = 
						downloadBitmap(params[0]);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return downloadBitmap;
		}
		protected void onPostExecute(Bitmap bm){
			imageView.setImageBitmap(bm);
			//dialog.dismiss();
			cargando.setVisibility(View.GONE);
			descarga.setEnabled(true);
		}
		
	}
}
