package com.example.navigation_smd_7a;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;
import java.util.concurrent.Executors;


public class ProductAdapter extends ArrayAdapter<Product> {
    Context context;
    int resource;
    private final ProdStatusChangeListner ListnerOfStatus;

    public ProductAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Product> objects, ProdStatusChangeListner l) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.ListnerOfStatus = l;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            v = LayoutInflater.from(context).inflate(resource, parent, false);
        }

        TextView tvTitle = v.findViewById(R.id.tvProductTitle);
        ImageView ivEdit = v.findViewById(R.id.ivEdit);
        ImageView ivDelete = v.findViewById(R.id.ivDelete);

        Product p = getItem(position);
        assert p != null;
        String Text = p.getPrice() + " : " + p.getTitle()+ ":" + p.getStatus();
        tvTitle.setText(Text);

        ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder d = new AlertDialog.Builder(getContext());
                d.setTitle("Product");

                View dView = LayoutInflater.from(context).inflate(R.layout.add_new_product_dialog_design, null, false);
                d.setView(dView);

                TextView tvHeading = dView.findViewById(R.id.heading);
                tvHeading.setText("Update Product");
                AutoCompleteTextView estimStatus = dView.findViewById(R.id.actvStatus);
                EditText estimTitle = dView.findViewById(R.id.etTitle);
                EditText estimDate = dView.findViewById(R.id.estimDate);
                EditText estimPrice = dView.findViewById(R.id.etPrice);

                String[] options = getContext().getResources().getStringArray(R.array.autocomplete_options);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, options);
                estimStatus.setAdapter(adapter);

                estimStatus.setText(p.getStatus(), false);
                estimTitle.setText(p.getTitle());
                estimDate.setText(p.getDate());
                estimPrice.setText(String.valueOf(p.getPrice()));

                d.setPositiveButton("Save", (dialogInterface, i) -> {
                    String status = estimStatus.getText().toString();
                    String title = estimTitle.getText().toString();
                    String date = estimDate.getText().toString().trim();
                    int price = Integer.parseInt(estimPrice.getText().toString().trim());

                    try {
                        Executors.newSingleThreadExecutor().execute(() -> {
                            ProductDB db = new ProductDB(getContext());
                            db.open();
                            int val = db.updateVal(p.getId(), title, date, price, status);

                            p.setTitle(title);
                            p.setDate(date);
                            p.setPrice(price);
                            p.setStatus(status);

                            ((Activity) context).runOnUiThread(() -> {
                                notifyDataSetChanged();
                                Toast.makeText(getContext(), "Product Updated", Toast.LENGTH_SHORT).show();
                                ListnerOfStatus.onProdStatusChange();
                            });

                            db.close();
                        });

                    } catch (NumberFormatException e) {
                        Toast.makeText(getContext(), "Invalid price", Toast.LENGTH_SHORT).show();
                    }
                });

                d.setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss());
                d.show();
            }
        });


        ivDelete.setOnClickListener(view -> {
            ProductDB db = new ProductDB(context);
            db.open();
            db.remove(p.getId());
            db.close();
            remove(p);
            notifyDataSetChanged();
        });

        return v;



    }
}
