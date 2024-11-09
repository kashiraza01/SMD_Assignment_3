package com.example.navigation_smd_7a;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewOrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewOrderFragment extends Fragment implements ProdStatusChangeListner  {

    Context context;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ProductAdapter adapter;
    private ArrayList<Product> products;
    private ListView NOL;

    public NewOrderFragment() {
    }

    private void loadDta() {
        ProductDB productDB = new ProductDB(context);
        productDB.open();
        products = productDB.fetchProducts("New");
        adapter = new ProductAdapter(context, R.layout.product_item_design, products, this);
        NOL.setAdapter(adapter);
        productDB.close();
    }

    @Override
    public void onProdStatusChange() {
        loadDta();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        onProdStatusChange();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewOrderFragment.
     */

    // TODO: Rename and change types and number of parameters
    public static NewOrderFragment newInstance(String param1, String param2) {
        NewOrderFragment fragment = new NewOrderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_new_order, container, false);
        NOL = v.findViewById(R.id.NOL);
        loadDta();
        return v;
    }


}
