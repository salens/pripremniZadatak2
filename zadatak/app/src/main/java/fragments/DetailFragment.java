package fragments;

import android.app.Fragment;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.android.zadatak.MainActivity;
import java.sql.SQLException;


import db.model.Glumac;


public class DetailFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private static int NOTIFICATION_ID = 1;

    private Glumac product = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        try {
//            if (product == null) { product = ((MainActivity)getActivity()).getDatabaseHelper().getProductDao().queryForId(0); }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            product = new Glumac();
            product.setmId(savedInstanceState.getInt("id"));
            product.setmName(savedInstanceState.getString("name"));
            product.setmLastName(savedInstanceState.getString("description"));
            product.setMocena(savedInstanceState.getInt("rating"));
            //product.setmDatum(savedInstanceState.getInt(new DateFormat()));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            savedInstanceState.putInt("id", product.getmId());
            savedInstanceState.putString("name", product.getmName());
            savedInstanceState.putString("description", product.getmLastName());
            savedInstanceState.putFloat("rating", product.getMocena());

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.v("DetailFragment", "onCreateView()");

        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.detail_fragment, container, false);

        TextView name = (TextView) view.findViewById(R.id.name);
        name.setText(product.getmName());

        TextView description = (TextView) view.findViewById(R.id.description);
        description.setText(product.getmLastName());

        TextView category = (TextView) view.findViewById(R.id.category);
        category.setText(product.getmFilms().toString());

        RatingBar ratingBar = (RatingBar) view.findViewById(R.id.rating);
        ratingBar.setRating(product.getMocena());

//        ImageView imageView = (ImageView) view.findViewById(R.id.image);
//        InputStream is = null;
//        try {
//            is = getActivity().getAssets().open(product.getImage());
//            Drawable drawable = Drawable.createFromStream(is, null);
//            imageView.setImageDrawable(drawable);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        FloatingActionButton button = (FloatingActionButton) view.findViewById(R.id.buy);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Creates notification with the notification builder
                NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity());
                Bitmap bitmap = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.ic_stat_buy);
                builder.setSmallIcon(R.drawable.ic_stat_buy);
                builder.setContentTitle(getActivity().getString(R.string.notification_title));
                builder.setContentText(getActivity().getString(R.string.notification_text));
                builder.setLargeIcon(bitmap);

                // Shows notification with the notification manager (notification ID is used to update the notification later on)
                NotificationManager manager = (NotificationManager)getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                manager.notify(NOTIFICATION_ID, builder.build());
            }
        });

        return view;
    }

    public void setProduct(Glumac product) {
        this.product = product;
    }

    public void updateProduct(Glumac product) {
        this.product = product;

        TextView name = (TextView) getActivity().findViewById(R.id.name);
        name.setText(product.getmName());

        TextView description = (TextView) getActivity().findViewById(R.id.description);
        description.setText(product.getmLastName());

        RatingBar ratingBar = (RatingBar) getActivity().findViewById(R.id.rating);
        ratingBar.setRating(product.getMocena());

//        ImageView imageView = (ImageView) getActivity().findViewById(R.id.image);
//        InputStream is = null;
//        try {
//            is = getActivity().getAssets().open(product.getImage());
//            Drawable drawable = Drawable.createFromStream(is, null);
//            imageView.setImageDrawable(drawable);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        // You can retrieve the selected item using
        //product.setCategory(CategoryProvider.getCategoryById((int)id));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //product.setCategory(null);
    }

    /**
     * Kada dodajemo novi element u toolbar potrebno je da obrisemo prethodne elmente
     * zato pozivamo menu.clear() i dodajemo nove toolbar elemente
     * */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.detail_fragment_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
     * Na fragment dodajemo element za brisanje elementa
     * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.remove:
                try {
                    if (product != null) {
                        ((MainActivity) getActivity()).getDatabaseHelper().getProductDao().delete(product);
                        getActivity().onBackPressed();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
