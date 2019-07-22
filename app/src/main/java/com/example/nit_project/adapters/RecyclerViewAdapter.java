package com.example.nit_project.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.nit_project.R;
import com.example.nit_project.activities.AnimeActivity;
import com.example.nit_project.activities.AdminActivity;
import com.example.nit_project.MySingleton;
import com.example.nit_project.model.Anime;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private Context mContext;
    private List<Anime> mData;
    String status;
    RequestOptions option;
    String change_status_URL;

    public RecyclerViewAdapter(Context mContext, List<Anime> mdata, String status) {
        this.mContext = mContext;
        this.mData = mdata;
        this.status = status;
        option = new RequestOptions().centerCrop().placeholder(R.drawable.loading_shape).error(R.drawable.loading_shape);

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (status.equals("requested")) {
            view = inflater.inflate(R.layout.anime_row_pending, parent, false);
        } else if (status.equals("approved")) {
            view = inflater.inflate(R.layout.anime_row_approved, parent, false);
        }else if(status.equals("blocksomeone")){
            view = inflater.inflate(R.layout.anime_row_blocksomeone, parent, false);
        }
        else {
            view = inflater.inflate(R.layout.anime_row_blocked, parent, false);
        }

        // click listener here
        //view = inflater.inflate(R.layout.anime_row_item,null,false);
        //      view.setLayoutParams(new RecyclerView.LayoutParams(
        //             RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));

        final MyViewHolder viewHolder = new MyViewHolder(view);

        viewHolder.view_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(mContext, AnimeActivity.class);
                i.putExtra("user_id", mData.get(viewHolder.getAdapterPosition()).getUser_id());
                i.putExtra("first_name", mData.get(viewHolder.getAdapterPosition()).getFirst_name());
                i.putExtra("middle_name", mData.get(viewHolder.getAdapterPosition()).getMiddle_name());
                i.putExtra("last_name", mData.get(viewHolder.getAdapterPosition()).getLast_name());
                i.putExtra("email", mData.get(viewHolder.getAdapterPosition()).getEmail());
                i.putExtra("contact", mData.get(viewHolder.getAdapterPosition()).getContact_number());
                i.putExtra("batch", mData.get(viewHolder.getAdapterPosition()).getBatch_number());
                i.putExtra("address", mData.get(viewHolder.getAdapterPosition()).getAddress());
                i.putExtra("code", mData.get(viewHolder.getAdapterPosition()).getCode());
                i.putExtra("status", mData.get(viewHolder.getAdapterPosition()).getStatus());
                i.putExtra("image_url", mData.get(viewHolder.getAdapterPosition()).getImage_url());

                mContext.startActivity(i);

            }
        });

        if (status.equals("requested")) {
            viewHolder.approve.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    updator(viewHolder,view,"approved");
                    Button approve;
                    approve=view.findViewById(R.id.btn_approve);
                    approve.setVisibility(View.GONE);

                }

                //    Button approve=view.findViewById(R.id.btn_approve);
                //    Button reject=view.findViewById(R.id.btn_reject);
                //    approve.setVisibility(View.GONE);
                //    reject.setVisibility(View.GONE);
            });
        viewHolder.reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updator(viewHolder,view,"rejected");
                Button reject;
                reject=view.findViewById(R.id.btn_reject);
                reject.setVisibility(View.GONE);

            }
        });

        return viewHolder;
    }
        else if(status.equals("blocked"))
    {
        viewHolder.unblock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updator(viewHolder,view,"approved");
                Button unblock;
                unblock=view.findViewById(R.id.btn_unblock);
                unblock.setVisibility(View.GONE);

            }
        });
        return viewHolder;
    }
        else if(status.equals("blocksomeone"))
        {
            viewHolder.block.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    updator(viewHolder,view,"blocked");
                    Button block;
                    block=view.findViewById(R.id.btn_block);
                    block.setVisibility(View.GONE);

                }
            });
            return viewHolder;
        }
        else
                return viewHolder;


}
private void updator(final MyViewHolder viewHolder,final View view, final String change_to)
{

    String ChangeStatus_URL = "http://noobieuser.000webhostapp.com//AdminPage//ChangeStatus.php";
    StringRequest stringRequest = new StringRequest(Request.Method.POST,ChangeStatus_URL,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.contains("success")) {
                        Toast.makeText(mContext, "successfully executed!!", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            String text = "";

            if (error instanceof TimeoutError) {
                Toast.makeText(mContext,"Timeout Error", Toast.LENGTH_SHORT).show();
                text = "Timeout Error";
            } else if (error instanceof NoConnectionError) {
                Toast.makeText(mContext,"Connection  Error", Toast.LENGTH_SHORT).show();
                text = "No Connection ";
            } else if (error instanceof AuthFailureError) {
                Toast.makeText(mContext,"Authentication Error", Toast.LENGTH_SHORT).show();
                text = "Authentication Error ";
            } else if (error instanceof NetworkError) {
                Toast.makeText(mContext,"Network Error", Toast.LENGTH_SHORT).show();
                text = "Network Error";
            } else if (error instanceof ServerError) {
                Toast.makeText(mContext,"Server Error", Toast.LENGTH_SHORT).show();
                text = "Server Error";
            } else  {
                //  Toast.makeText(mContext,"Json Error", Toast.LENGTH_SHORT).show();
                text = "Something went wrong ,please try again";
                Toast.makeText(mContext,"Something went wrong!!!", Toast.LENGTH_SHORT).show();
            }
        }
    }) {
        @Override
        protected Map<String, String> getParams() {
            Map<String, String> params = new HashMap<String, String>();
            params.put("user_id", mData.get(viewHolder.getAdapterPosition()).getUser_id());
            params.put("status", change_to);
            return params;
        }

        @Override
        public Map<String, String> getHeaders() {
            Map<String, String> headers = new HashMap<String, String>();
            headers.put("User-Agent", "noobie");
            return headers;
        }
    };

    MySingleton.getInstance(mContext).addToRequestQueue(stringRequest);
}

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tv_full_name.setText(mData.get(position).getFirst_name() + " " + mData.get(position).getLast_name());
        holder.tv_rating.setText(mData.get(position).getContact_number());
        holder.tv_studio.setText(mData.get(position).getEmail());
        holder.tv_categorie.setText(mData.get(position).getStatus());


        // load image from the internet using Glide
        Glide.with(mContext).load(mData.get(position).getImage_url()).apply(option).into(holder.img_thumbnail);


    }

    @Override
    public int getItemCount() {
        return mData.size();
        //return 5;
    }

public static class MyViewHolder extends RecyclerView.ViewHolder {

    TextView tv_full_name, tv_rating, tv_studio, tv_categorie;
    ImageView img_thumbnail;
    ConstraintLayout view_container;
    Button approve, reject, unblock,block;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        approve = itemView.findViewById(R.id.btn_approve);
        reject = itemView.findViewById(R.id.btn_reject);
        unblock = itemView.findViewById(R.id.btn_unblock);
        block=itemView.findViewById(R.id.btn_block);
        view_container = itemView.findViewById(R.id.container);
        tv_full_name = itemView.findViewById(R.id.full_name);
        tv_studio = itemView.findViewById(R.id.studio);
        tv_rating = itemView.findViewById(R.id.rating);
        tv_categorie = itemView.findViewById(R.id.categorie);
        img_thumbnail = itemView.findViewById(R.id.thumbnail);

    }


}

}
