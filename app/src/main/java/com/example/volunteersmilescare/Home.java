package com.example.volunteersmilescare;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.volunteersmilescare.Common.Common;
import com.example.volunteersmilescare.Model.Request;
import com.example.volunteersmilescare.ViewHolder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Home extends AppCompatActivity {

    private static final int STORAGE_CODE = 1000;
    Toolbar appBar;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase db;
    DatabaseReference requests;

    FirebaseRecyclerAdapter<Request, OrderViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        appBar = findViewById(R.id.app_bar);
        appBar.setTitle("Volunteer Id: "+ Common.currentUser.getPhone().replace("+91",""));

        db = FirebaseDatabase.getInstance();
        requests = db.getReference("Donations");

        recyclerView = findViewById(R.id.listCart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        if (Common.isConnectedToInternet(getBaseContext()))
            loadOrders();
        else{
            Toast.makeText(getBaseContext(), "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            return;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter!=null)
            adapter.startListening();
    }

    private void loadOrders() {

        FirebaseRecyclerOptions<Request> options = new FirebaseRecyclerOptions.Builder<Request>()
                .setQuery(requests,Request.class)
                .build();

        adapter =new FirebaseRecyclerAdapter<Request, OrderViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull OrderViewHolder viewHolder, final int position, @NonNull final Request model) {
                viewHolder.txtOrderId.setText(model.getOrderId());
                viewHolder.txtOrderAmount.setText(model.getAmount());
                viewHolder.txtOrderName.setText(Common.currentUser.getName());
                viewHolder.txtOrderPhone.setText(adapter.getRef(position).getKey());
                viewHolder.txtOrdeDate.setText(Common.getDate(Long.parseLong(adapter.getRef(position).getKey())));



                viewHolder.btnGenerate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.M){
                            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
                                String[] permissions={Manifest.permission.WRITE_EXTERNAL_STORAGE};
                                requestPermissions(permissions,STORAGE_CODE);
                            }
                            else{
                                savePdf();
                            }
                        }
                        else{
                            savePdf();
                        }
                    }
                });
            }

            @NonNull
            @Override
            public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View itemView = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.order_layout,viewGroup,false);
                return new OrderViewHolder(itemView);
            }
        };
        adapter.startListening();
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }

    private void savePdf() {
        Document mDoc=new Document();
        String mFileName=new SimpleDateFormat("yyyyMMdd_HHmmss",Locale.getDefault()).format(System.currentTimeMillis());
        String mFilePath= Environment.getExternalStorageDirectory() + "/" + mFileName + ".pdf";
        try{
            PdfWriter.getInstance(mDoc, new FileOutputStream(mFilePath));
            mDoc.open();

            Paragraph p1 = new Paragraph("SMILES.CARE");
            Font paraFont1= new Font();
            paraFont1.setStyle(Font.BOLD);
            paraFont1.setSize(16);
            p1.setAlignment(Paragraph.ALIGN_CENTER);
            p1.setFont(paraFont1);
            mDoc.add(p1);

            Paragraph p2 = new Paragraph("Donation Receipt");
            Font paraFont2= new Font();
            paraFont2.setStyle(Font.BOLD);
            paraFont2.setSize(14);
            p2.setAlignment(Paragraph.ALIGN_CENTER);
            p2.setFont(paraFont2);
            mDoc.add(p2);

            Paragraph p3 = new Paragraph("OrderId: "+requests.getKey());
            Font paraFont3= new Font();
            paraFont3.setStyle(Font.NORMAL);
            paraFont3.setSize(10);
            p3.setAlignment(Paragraph.ALIGN_JUSTIFIED);
            p3.setFont(paraFont3);
            mDoc.add(p3);

            Paragraph p4 = new Paragraph("Donation Amount: ₹500");
            Font paraFont4= new Font();
            paraFont4.setStyle(Font.NORMAL);
            paraFont4.setSize(10);
            p4.setAlignment(Paragraph.ALIGN_JUSTIFIED);
            p4.setFont(paraFont4);
            mDoc.add(p4);

            Paragraph p5 = new Paragraph("Name: "+Common.currentUser.getName());
            Font paraFont5= new Font();
            paraFont5.setStyle(Font.NORMAL);
            paraFont5.setSize(10);
            p5.setAlignment(Paragraph.ALIGN_JUSTIFIED);
            p5.setFont(paraFont5);
            mDoc.add(p5);

            Paragraph p6 = new Paragraph("Phone Number: "+Common.currentUser.getPhone());
            Font paraFont6= new Font();
            paraFont6.setStyle(Font.NORMAL);
            paraFont6.setSize(10);
            p6.setAlignment(Paragraph.ALIGN_JUSTIFIED);
            p6.setFont(paraFont6);
            mDoc.add(p6);

            Paragraph p7 = new Paragraph("Email ID : "+Common.currentUser.getEmail());
            Font paraFont7= new Font();
            paraFont7.setStyle(Font.NORMAL);
            paraFont7.setSize(10);
            p7.setAlignment(Paragraph.ALIGN_JUSTIFIED);
            p7.setFont(paraFont7);
            mDoc.add(p7);

            Paragraph p8 = new Paragraph("Full Address: Thapar University");
            Font paraFont8= new Font();
            paraFont8.setStyle(Font.NORMAL);
            paraFont8.setSize(10);
            p8.setAlignment(Paragraph.ALIGN_JUSTIFIED);
            p8.setFont(paraFont8);
            mDoc.add(p8);

            Paragraph p9 = new Paragraph("Volunteer ID: "+requests.child(Common.currentUser.getPhone()));
            Font paraFont9= new Font();
            paraFont9.setStyle(Font.NORMAL);
            paraFont9.setSize(10);
            p9.setAlignment(Paragraph.ALIGN_JUSTIFIED);
            p6.setFont(paraFont9);
            mDoc.add(p9);

            mDoc.addAuthor("SmilesCare");
            mDoc.close();
            Toast.makeText(this,mFileName +".pdf\nis saved to\n"+ mFilePath,  Toast.LENGTH_SHORT);
        }
        catch (Exception e){
            Toast.makeText(this,e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        Log.i("Send email", "");

        String[] TO = {Common.currentUser.getEmail()};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Donation Receipt");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Thank You for your donation!\nSMILES.CARE\nDonation Receipt\nOrderId: "+requests.getKey()+"\nDonation Amount: ₹500\nName: "+Common.currentUser.getName()+"\nPhone Number: "+Common.currentUser.getPhone()+"\nEmail ID : "+Common.currentUser.getEmail()+"\nFull Address: Thapar University\nVolunteer ID: 9780066535");
        emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(mFileName +".pdf\nis saved to\n"+ mFilePath));
        emailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
            Log.i("Finished", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(Home.this,
                    "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case STORAGE_CODE:{
                if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    savePdf();
                }
                else {
                    Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}

