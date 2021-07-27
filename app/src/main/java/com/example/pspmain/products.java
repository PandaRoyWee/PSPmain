package com.example.pspmain;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class products extends AppCompatActivity {

    int image[] = {R.drawable.bench,R.drawable.benchpress,R.drawable.dumbbell,R.drawable.pullupbar,R.drawable.pushupstand};
    String names[] = {"Bench", "Bench Press", "Dumbbells", "Pull-up Bar", "Push-up Stand"};
    String desc[] = {"Bench for training with Dumbbells", "Bench Press Set for chest workouts", "Dumbbells for workouts", "Pull-up bars to train pull ups at home", "Push-up stand for push-ups"};

    List<productmodel> productList = new ArrayList<>();

    GridView gridView;

    CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        gridView = findViewById(R.id.gridView);

        for(int i=0; i < names.length; i++){

            productmodel ProductModel = new productmodel(names[i],desc[i],image[i]);
            productList.add(ProductModel);

        }

        customAdapter = new CustomAdapter(productList, this);

        gridView.setAdapter(customAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);

        MenuItem menuItem = menu.findItem(R.id.search_view);

        SearchView searchView = (SearchView) menuItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                customAdapter.getFilter().filter(newText);

                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.search_view){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class CustomAdapter extends BaseAdapter implements Filterable {

        private List<productmodel> productmodelList;
        private List<productmodel> productListFiltered;
        private Context context;

        public CustomAdapter(List<productmodel> productList, Context context) {
            this.productmodelList = productList;
            this.productListFiltered = productList;
            this.context = context;
        }

        @Override
        public int getCount() {
            return productListFiltered.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = getLayoutInflater().inflate(R.layout.row_products, null);

            ImageView imageView = view.findViewById(R.id.productView);
            TextView tvNames = view.findViewById(R.id.tvName);

            imageView.setImageResource(productListFiltered.get(position).getImage());
            tvNames.setText(productListFiltered.get(position).getName());

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    startActivity(new Intent(products.this, ProductViewActivity.class).putExtra("item", productListFiltered.get(position)));

                }
            });

            return view;
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {

                    FilterResults filterResults = new FilterResults();

                    if (constraint == null || constraint.length() == 0) {
                        filterResults.count = productmodelList.size();
                        filterResults.values = productmodelList;

                    } else {

                        String searchStr = constraint.toString().toLowerCase();
                        List<productmodel> resultData = new ArrayList<>();

                        for (productmodel productModel : productmodelList) {

                            if (productModel.getName().toLowerCase().contains(searchStr)) {
                                resultData.add(productModel);
                            }

                            filterResults.count = resultData.size();
                            filterResults.values = resultData;

                        }
                    }

                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {

                    productListFiltered = (List<productmodel>) results.values;
                    notifyDataSetChanged();

                }
            };

            return filter;
        }
    }

}