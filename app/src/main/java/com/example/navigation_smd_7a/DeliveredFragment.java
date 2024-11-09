package com.example.navigation_smd_7a;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

public class DeliveredFragment extends Fragment implements ProdStatusChangeListner  {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    Context context;
    private ProductAdapter adapter;
    private ArrayList<Product> products;
    private ListView NOL;

    public DeliveredFragment() {
    }

    private void loadData() {
        ProductDB db = new ProductDB(context);
        db.open();
        products = db.fetchProducts("Delivered");
        adapter = new ProductAdapter(context, R.layout.product_item_design, products, this);
        NOL.setAdapter(adapter);
        db.close();
    }

    @Override
    public void onProdStatusChange() {
        loadData();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(@NonNull Context c) {
        super.onAttach(c);
        this.context = c;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_new_order, container, false);
        NOL = v.findViewById(R.id.NOL);
        loadData();
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        onProdStatusChange();
    }


}
