package com.example.android.zadatak;

import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import adapters.DrawerListAdapter;
import db.DatabaseHelper;
import db.model.Category;
import db.model.Product;
import dialogs.AboutDialog;
import fragments.DetailFragment;
import fragments.ListFragment;
import model.NavigationItem;

public class MainActivity extends AppCompatActivity implements ListFragment.OnProductSelectedListener {

    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItemFromDrawer(position);
        }
    }
    // Container Activity must implement this interface
    public interface OnProductSelectedListener {
        void onProductSelected(int id);
    }

    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private ActionBarDrawerToggle drawerToggle;
    private RelativeLayout drawerPane;
    private CharSequence drawerTitle;
    private CharSequence title;

    private ArrayList<NavigationItem> navigationItems = new ArrayList<NavigationItem>();

    private AlertDialog dialog;

    private boolean landscapeMode = false;
    private boolean listShown = false;
    private boolean detailShown = false;

    private int productId = 0;

    private DatabaseHelper databaseHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // Draws navigation items
        navigationItems.add(new NavigationItem(getString(R.string.drawer_home), getString(R.string.drawer_home_long), R.drawable.ic_action_product));
        navigationItems.add(new NavigationItem(getString(R.string.drawer_settings), getString(R.string.drawer_Settings_long), R.drawable.ic_action_settings));
        navigationItems.add(new NavigationItem(getString(R.string.drawer_about), getString(R.string.drawer_about_long), R.drawable.ic_action_about));

        title = drawerTitle = getTitle();
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerList = (ListView) findViewById(R.id.navList);

        // Populate the Navigtion Drawer with options
        drawerPane = (RelativeLayout) findViewById(R.id.drawerPane);
        DrawerListAdapter adapter = new DrawerListAdapter(this, navigationItems);

        // set a custom shadow that overlays the main content when the drawer opens
        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        drawerList.setOnItemClickListener(new DrawerItemClickListener());
        drawerList.setAdapter(adapter);

        // Enable ActionBar app icon to behave as action to toggle nav drawer
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_drawer);
            actionBar.setHomeButtonEnabled(true);
            actionBar.show();
        }

        drawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                drawerLayout,         /* DrawerLayout object */
                toolbar,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(title);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(drawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        if (savedInstanceState == null) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ListFragment listFragment = new ListFragment();
            ft.add(R.id.displayList, listFragment, "List_Fragment");
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.commit();
            selectItemFromDrawer(0);
        }

        if (findViewById(R.id.displayDetail) != null) {
            landscapeMode = true;
            getFragmentManager().popBackStack();

            DetailFragment detailFragment = (DetailFragment) getFragmentManager().findFragmentById(R.id.displayDetail);
            if (detailFragment == null) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                detailFragment = new DetailFragment();
                ft.replace(R.id.displayDetail, detailFragment, "Detail_Fragment1");
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
                detailShown = true;
            }
        }

        listShown = true;
        detailShown = false;
        productId = 0;

        addInitCateogry();
    }

    private void addInitCateogry(){
        try {
            if (getDatabaseHelper().getCategoryDao().queryForAll().size() == 0){
                Category food = new Category();
                food.setName("Food");

                Category other = new Category();
                other.setName("Other");

                getDatabaseHelper().getCategoryDao().create(food);
                getDatabaseHelper().getCategoryDao().create(other);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //da bi dodali podatak u bazu, potrebno je da napravimo objekat klase
    //koji reprezentuje tabelu i popunimo podacima
    private void addItem() throws SQLException {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_layout);

        final Spinner imagesSpinner = (Spinner) dialog.findViewById(R.id.product_image);
        List<String> imagesList = new ArrayList<String>();
        imagesList.add("apples.jpg");
        imagesList.add("bananas.jpg");
        imagesList.add("oranges.jpg");
        ArrayAdapter<String> imagesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, imagesList);
        imagesSpinner.setAdapter(imagesAdapter);
        imagesSpinner.setSelection(0);

        final Spinner productsSpinner = (Spinner) dialog.findViewById(R.id.product_category);
        List<Category> list = getDatabaseHelper().getCategoryDao().queryForAll();
        ArrayAdapter<Category> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list);
        productsSpinner.setAdapter(dataAdapter);
        productsSpinner.setSelection(0);

        final EditText productName = (EditText) dialog.findViewById(R.id.product_name);
        final EditText productDescr = (EditText) dialog.findViewById(R.id.product_description);
        final EditText productRating = (EditText) dialog.findViewById(R.id.product_rating);

        Button ok = (Button) dialog.findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = productName.getText().toString();
                String desct = productDescr.getText().toString();
                float price = Float.parseFloat(productRating.getText().toString());
                Category categoty = (Category) productsSpinner.getSelectedItem();
                String image = (String) imagesSpinner.getSelectedItem();


                Product product = new Product();
                product.setmName(name);
                product.setDescription(desct);
                product.setRating(price);
                product.setImage(image);
                product.setCategory(categoty);

                try {
                    getDatabaseHelper().getProductDao().create(product);
                    refresh();
                    Toast.makeText(MainActivity.this, "Product inserted", Toast.LENGTH_SHORT).show();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        Button cancel = (Button) dialog.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_item_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                refresh();
                break;
            case R.id.action_add:
                try {
                    addItem();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    // refresh() prikazuje novi sadrzaj.Povucemo nov sadrzaj iz baze i popunimo listu
    private void refresh() {
        ListView listview = (ListView) findViewById(R.id.products);

        if (listview != null){
            ArrayAdapter<Product> adapter = (ArrayAdapter<Product>) listview.getAdapter();

            if(adapter!= null)
            {
                try {
                    adapter.clear();
                    List<Product> list = getDatabaseHelper().getProductDao().queryForAll();

                    adapter.addAll(list);

                    adapter.notifyDataSetChanged();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        drawerToggle.onConfigurationChanged(newConfig);
    }

    private void selectItemFromDrawer(int position) {
        if (position == 0){

        } else if (position == 1){
            Intent settings = new Intent(MainActivity.this,SettingsActivity.class);
            startActivity(settings);
        } else if (position == 2){
            if (dialog == null){
                dialog = new AboutDialog(MainActivity.this).prepareDialog();
            } else {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }

            dialog.show();
        }

        drawerList.setItemChecked(position, true);
        setTitle(navigationItems.get(position).getTitle());
        drawerLayout.closeDrawer(drawerPane);
    }

    @Override
    public void onProductSelected(int id) {

        productId = id;

        try {
            Product product = getDatabaseHelper().getProductDao().queryForId(id);

            if (landscapeMode) {
                DetailFragment detailFragment = (DetailFragment) getFragmentManager().findFragmentById(R.id.displayDetail);
                detailFragment.updateProduct(product);
            } else {
                DetailFragment detailFragment = new DetailFragment();
                detailFragment.setProduct(product);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.displayList, detailFragment, "Detail_Fragment2");
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.addToBackStack("Detail_Fragment2");
                ft.commit();
                listShown = false;
                detailShown = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {

        if (landscapeMode) {
            finish();
        } else if (listShown == true) {
            finish();
        } else if (detailShown == true) {
            getFragmentManager().popBackStack();
            ListFragment listFragment = new ListFragment();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.displayList, listFragment, "List_Fragment");
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.commit();
            listShown = true;
            detailShown = false;
        }

    }

    //Metoda koja komunicira sa bazom podataka
    public DatabaseHelper getDatabaseHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        }
        return databaseHelper;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        // nakon rada sa bazo podataka potrebno je obavezno
        //osloboditi resurse!
        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }
}
