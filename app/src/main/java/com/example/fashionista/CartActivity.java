package com.example.fashionista;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.example.fashionista.Model.Cart;
import com.example.fashionista.Prevalent.Prevalent;
import com.example.fashionista.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class CartActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button NextProcessBtn;
    private TextView txtTotalAmount, txtMsg1;
    private int overTotalPrice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = findViewById(R.id.cart_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        NextProcessBtn = (Button) findViewById(R.id.next_btn);
        txtTotalAmount = (TextView) findViewById(R.id.page_title);
        txtMsg1 = (TextView) findViewById(R.id.msg1);


        NextProcessBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                txtTotalAmount.setText("Total Price = $" + String.valueOf(overTotalPrice));
                Intent intent = new Intent(CartActivity.this, ConfirmFinalOrderActivity.class);
                // send the total price to the next  - confirm final order activity
                intent.putExtra("Total Price", String.valueOf(overTotalPrice));
                startActivity(intent);
                finish();
            }
        });
    }
    @Override
    protected void onStart(){
        super.onStart();
        CheckOrderState();
        // reference to cart list node in firebase database
        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");
        //binds a Query to a RecyclerView and responds to all real-time events included items being added, removed, moved, or changed.
        // Best used with small result sets since all results are loaded at once.
        FirebaseRecyclerOptions<Cart> options =
                new FirebaseRecyclerOptions.Builder<Cart>()
                        .setQuery(cartListRef.child("User View")
                                .child(Prevalent.currentOnlineUser.getPhone())
                                .child("Products"), Cart.class).build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter
                = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull final Cart model){
                holder.txtProductQuantity.setText("Quantity = " + model.getQuantity());
                holder.txtProductPrice.setText("Price " + model.getPrice() + "$");
                holder.txtProductName.setText(model.getPname());

                int oneTypeProductTPrice = 0;
                try{
                    oneTypeProductTPrice = ((Integer.parseInt(model.getPrice()))) * Integer.parseInt(model.getQuantity());
                }catch(Exception e){
                    System.out.println("Exception raised!!\n");
                }

                //int oneTypeProductTPrice = ((Integer.valueOf(model.getPrice()))) * Integer.valueOf(model.getQuantity());
                overTotalPrice += oneTypeProductTPrice;
                // on clicking the items in cart we get two options -  edit and remove
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view){
                        CharSequence options[] = new CharSequence[]{"Edit","Remove"};
                        AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                        builder.setTitle("Cart Options");
                        // options that appear in the builder
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i){
                                // i==0 means Edit option from CharSequence
                                if (i == 0){
                                    // send the user to product details activity
                                    Intent intent = new Intent(CartActivity.this, ProductDetailsActivity.class);
                                   // Intents are asynchronous messages which allow Android components to request functionality from other components of the Android system.
                                    // For example an Activity can send an Intents to the Android system which starts another Activity .
                                    // putExtra() adds extended data to the intent.
                                    // ProductDetailsActivity can access tje pid of product now from this intent
                                    intent.putExtra("pid", model.getPid());
                                    startActivity(intent);
                                }
                                // remove option for a product in cart
                                if (i == 1){
                                    // remvoves the item from firebase database
                                    cartListRef.child("User View")
                                            .child(Prevalent.currentOnlineUser.getPhone())
                                            .child("Products")
                                            .child(model.getPid())
                                            .removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task){
                                                    if (task.isSuccessful()){
                                                        Toast.makeText(CartActivity.this, "Item removed successfully!", Toast.LENGTH_SHORT).show();
                                                        //Intent intent = new Intent(CartActivity.this, CartActivity.class);
                                                        //startActivity(intent);
                                                    }
                                                }
                                            });
                                }
                            }
                        });
                        builder.show();
                    }
                });
            }
            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout, parent, false);
                CartViewHolder holder = new CartViewHolder(view);
                return holder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
    private void CheckOrderState(){
        DatabaseReference ordersRef;
        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentOnlineUser.getPhone());
        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                if (dataSnapshot.exists()){
                    String shippingState = dataSnapshot.child("state").getValue().toString();
                    String userName = dataSnapshot.child("fname").getValue().toString();
                    if (shippingState.equals("shipped")){
                        txtTotalAmount.setText("Dear " + userName + "\n order is shipped successfully.");
                        recyclerView.setVisibility(View.GONE);

                        txtMsg1.setVisibility(View.VISIBLE);
                        txtMsg1.setText("Congratulations, your final order has been Shipped successfully. Soon you will received your order at your door step.");
                        NextProcessBtn.setVisibility(View.GONE);

                        Toast.makeText(CartActivity.this, "you can purchase more products, once you received your first final order.", Toast.LENGTH_SHORT).show();
                    }
                    else if(shippingState.equals("not shipped")){
                        txtTotalAmount.setText("Shipping State : Not Shipped");
                        recyclerView.setVisibility(View.GONE);

                        txtMsg1.setVisibility(View.VISIBLE);
                        NextProcessBtn.setVisibility(View.GONE);

                        Toast.makeText(CartActivity.this, "you can purchase more products, once you received your first final order.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
