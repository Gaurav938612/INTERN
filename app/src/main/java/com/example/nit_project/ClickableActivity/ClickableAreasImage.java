package com.example.nit_project.ClickableActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.nit_project.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoViewAttacher;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class ClickableAreasImage extends View implements PhotoViewAttacher.OnPhotoTapListener, Serializable {

    private static PhotoViewAttacher attacher;
    private OnClickableAreaClickedListener listener;
    private View view;
    public ImageView imageView;
    public int xval, yval;
    public static boolean rem=true;
    public static boolean decider=false;
    public float projectedX;
    public float projectedY=0;
    public static String MODE = "VIEW";
    public static String ACTION = "ZOOM";
    private String string_info="";
    public static List<ClickableArea> clickableAreas = new ArrayList<>();
    private static int imageWidthInPx;
    private static int imageHeightInPx;
    transient Context ctx;
    public float StartX=0, StartY=0;
    private Rect rect;
    transient Paint paint = new Paint();
    static float mX = -1;
    static float mY = -1;
    static float x1 = 1, x2 = 3, y1 = 4, y2 = 4;
    String image_url;
    private static Callback agent;
    static int openlist[][]=new int[4][4];
    static String ListHeading[]=new String[10];
    static int Listdepth[]=new int[3];
    static ArrayAdapter<String> ListAdapter[]=new ArrayAdapter[10];
    static int depth=0;

    public ClickableAreasImage(Context ctx) {
        super(ctx);
        Log.d(TAG, "context Area");
        this.ctx = ctx;
        agent=(Callback) ctx;
    }

    public ClickableAreasImage(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        Log.d(TAG, "context Area");
    }

    /*public ClickableAreasImage(PhotoViewAttacher attacher, OnClickableAreaClickedListener listener, View view) {
        super(view.getContext());
        this.attacher = attacher;
        init(listener);
        this.view = view;
        imageView = attacher.getImageView();

    }*/

    public void setParameters(PhotoViewAttacher attacher, OnClickableAreaClickedListener listener, View view,String image_url) {
        this.attacher = attacher;
        init(listener);
        this.view = view;
        imageView = attacher.getImageView();
        this.image_url=image_url;
        ListAdapter[0] =new ArrayAdapter<String>(ctx,
                android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.devices));
        ListAdapter[1] =new ArrayAdapter<String>(ctx,
                android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.names));
        ListAdapter[2] =new ArrayAdapter<String>(ctx,
                android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.pendrives));
        ListAdapter[3] =new ArrayAdapter<String>(ctx,
                android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.devices));
        ListAdapter[4] =new ArrayAdapter<String>(ctx,
                android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.names));
        ListAdapter[5] =new ArrayAdapter<String>(ctx,
                android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.pendrives));
        ListAdapter[6] =new ArrayAdapter<String>(ctx,
                android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.devices));
        Listdepth[0]=Listdepth[1]=0;
        ListHeading[0]="Device";
        ListHeading[1]="company";
        ListHeading[2]="pendrive ";
        ListHeading[3]="Device";
        ListHeading[4]="company";
        ListHeading[5]="pendrive company";
        ListHeading[6]="Device";
        for(int i=0;i<4;i++)
        {
            for(int j=0;j<4;j++)
                openlist[i][j]=-1;
        }
        openlist[0][0]=0;
        openlist[1][0]=1;
        openlist[2][0]=2;
        openlist[3][0]=3;
        openlist[1][1]=4;
        openlist[1][2]=5;
        openlist[3][1]=6;
    }
    public void init(OnClickableAreaClickedListener listener) {
        this.listener = listener;
        getImageDimensions(attacher.getImageView());
        attacher.setOnPhotoTapListener(this);

    }

    private void getImageDimensions(ImageView imageView) {
       if(!decider) {
           BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
           imageWidthInPx = (int) (drawable.getBitmap().getWidth() / Resources.getSystem().getDisplayMetrics().density);
           imageHeightInPx = (int) (drawable.getBitmap().getHeight() / Resources.getSystem().getDisplayMetrics().density);
       }
    }

    @Override
    public void onPhotoTap(View view, float x, float y) {
        Log.d(TAG,"phototap called");
       /* PixelPosition pixel = ImageUtils.getPixelPosition(x, y, imageWidthInPx, imageHeightInPx);
        xval = pixel.getX();
        yval = pixel.getY();
        List<ClickableArea> clickableAreas = getClickAbleAreas(view, pixel.getX(), pixel.getY());
        for (ClickableArea ca : clickableAreas) {
            listener.onClickableAreaTouched(ca.getItem(), ca);
        }*/
    }


    @Override
    protected void onDraw(Canvas canvas) {
        if (StartY < 0||rem==true||MODE.equals("VIEW")) {
            canvas.drawRect(0, 0, 1, 1, paint);
            //drawOnRectProjectedBitMap(1,1,imageView);
            invalidate();
            return;
        }
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(6);
        super.onDraw(canvas);
        canvas.drawRect(StartX, StartY, projectedX, projectedY, paint);
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        try {
            Log.d(TAG,"touchevent extended");
            LocationXY locate = onSingleTapConfirmed(event);
            if (MODE.equals("VIEW") || ACTION.equals("MARK"))
            {
                if(MODE.equals(("VIEW"))) {
                    //Log.d(TAG,"onPhototap called");
                    PixelPosition pixel = ImageUtils.getPixelPosition(locate.x, locate.y, imageWidthInPx, imageHeightInPx);
                    Log.d(TAG,"mode view x ="+pixel.getX()+"  y="+pixel.getY()+" x="+event.getX());
                    Log.d(TAG,"locate x ="+locate.x+"  y="+locate.y+" Y="+event.getY());
                    //List<ClickableArea> clickableAreas =getClickAbleAreas(view, pixel.getX(), pixel.getY());
                    Log.d(TAG,"test element passed");
                    for (ClickableArea ca : clickableAreas) {
                        // listener.onClickableAreaTouched(ca.getItem(), ca);
                     //   tempfunc(ca.getItem(), ca);
                        if(ca.getIndex().equals(agent.GiveMeImageUrl())) {
                            Log.d(TAG,"no error in filepath");
                            Log.d(TAG,"ca.getx "+ca.getX()+" ca.getw "+ca.getW()+" original "+pixel.getX()+"ca.gety "+ca.getY()+"ca.getH "+ca.getH()+" original y "+pixel.getY());
                            if (isBetween(ca.getX(), ca.getW(), pixel.getX()) && isBetween(ca.getY(), ca.getH(), pixel.getY()))
                                tempfunc(ca.getItem(),ca);
                        }
                    }
                    // Log.d(TAG, "mode action " + MODE + " " + ACTION);
                }
                return false;
            }

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    StartX = locate.R_width;
                    StartY = locate.R_length;
                    x1 = locate.x;
                    y1 = locate.y;
                    return true;
                case MotionEvent.ACTION_MOVE:
                    drawOnRectProjectedBitMap(locate.R_width, locate.R_length, imageView);
                    break;
                case MotionEvent.ACTION_UP:
                    drawOnRectProjectedBitMap(locate.R_width, locate.R_length, imageView);
                    x2 = locate.x;
                    y2 = locate.y;
                    break;
            }
            invalidate();
            return true;
        } catch (NullPointerException e) {
            return false;
        }
    }


    /*private List<ClickableArea> getClickAbleAreas(final View view, int x, int y) {
        List<ClickableArea> clickableAreas = new ArrayList<>();
        try {
            for (ClickableArea ca : getClickableAreas()) {
                if (isBetween(ca.getX(), ca.getW(), x)) {
                    if (isBetween(ca.getY(), ca.getH(), y)) {
                        clickableAreas.add(ca);
                        xval = ca.getX();
                        yval = ca.getY();
                        mX = ca.getX();
                        mY = ca.getY();
                        projectedX = ca.getW();
                        projectedY = ca.getH();
                        break;
                    }
                }

            }
        } catch (NullPointerException e) {
            //nothing to be done in exception
            Toast.makeText(view.getContext(), "no item right now", Toast.LENGTH_SHORT).show();
        }
        createdialog();

        return clickableAreas;
    }*/

    public void createdialog() {
        if (MODE.equals("EDIT")) {
            Log.d(TAG, " x mode is " + this.MODE + "  action is " + this.ACTION);
            final EditText e = new EditText(this.view.getContext());
            final Spinner spinner=new Spinner(ctx);
            int k=Listdepth[0];
            int l=Listdepth[1];
            Log.d(TAG,k+" "+l+" "+depth);
            spinner.setAdapter(ListAdapter[openlist[k][l]]);
            LinearLayout ll=new LinearLayout(ctx);
            ll.setOrientation(LinearLayout.VERTICAL);
            ll.addView(spinner);
            ll.addView(e);
            e.setText("");
            e.append(string_info);
            final AlertDialog.Builder builder = new AlertDialog.Builder(this.view.getContext());
            builder.setTitle("Add Information");
            builder.setView(ll);
            builder.setCancelable(false);
            builder.setIcon(R.mipmap.ic_launcher);
            builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    String s = e.getText().toString();
                    settemp(s);
                    resetvalues();
                }
            });
             builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    resetvalues();
                }
            });
            final AlertDialog alert = builder.create();
            alert.getWindow().getAttributes().verticalMargin = 0.3F;
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if(i!=0){
                        alert.dismiss();
                        int k1=Listdepth[0];
                        int l1=Listdepth[1];
                        String heading=ListHeading[openlist[k1][l1]];
                         string_info = e.getText().toString();
                        string_info=string_info+heading+" : "+adapterView.getSelectedItem().toString()+"\n";
                        e.setText("");
                        e.append(string_info);
                        Log.d(TAG,string_info);
                        Listdepth[depth]=i;
                        depth++;
                        k1=Listdepth[0];
                        l1=Listdepth[1];
                        if(openlist[k1][l1]!=-1){
                        spinner.setAdapter(ListAdapter[openlist[k1][l1]]);}
                        else spinner.setAdapter(new ArrayAdapter<String>(ctx,
                                android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.empty)));
                        alert.show();
                    }
                    //else
                      //  Toast.makeText(ctx,"Make valid selection",Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            alert.show();
            ACTION = "ZOOM";
        }
    }
    private void resetvalues()
    {
        Log.d(TAG,"reset done");
        Listdepth[0]=Listdepth[1]=0;
        depth=0;
        string_info="";
    }
    //change is done
    private void settemp(String s) {
        Log.d(TAG, "settemp x1" + x1 + "settemp y1" + y1 + "settemp x2" + x2 + "settemp y2" + y2);
        PixelPosition pixel = ImageUtils.getPixelPosition(x1, y1, imageWidthInPx, imageHeightInPx);
        int X1 = pixel.getX();
        int Y1 = pixel.getY();
        PixelPosition pixel1 = ImageUtils.getPixelPosition(x2, y2, imageWidthInPx, imageHeightInPx);
        int X2 = pixel1.getX();
        int Y2 = pixel1.getY();
        this.clickableAreas.add(new ClickableArea(X1, Y1, X2, Y2,s,image_url));
        Log.d(TAG, "adding x" + X1 + "adding y" + Y1 + "adding x" + X2 + "adding y" + Y2);
        rem=true;
        string_info="";
    }

    private boolean isBetween(int start, int end, int actual) {
        return ((start <= actual && actual <= end)||(start >= actual && actual >= end));
    }
    public List<ClickableArea> getClickableAreas() {
        return clickableAreas;
    }


    public void drawOnRectProjectedBitMap(float x, float y, ImageView iv) {
        Log.d(TAG,"draw on rect");
        rem=false;
        projectedX = x;
        projectedY = y;
        invalidate();
    }

    public LocationXY onSingleTapConfirmed(MotionEvent e) {
        final RectF displayRect = this.attacher.getDisplayRect();
        final float x = e.getX(), y = e.getY();
        if (displayRect != null) {
            // Check to see if the user tapped on the photo
            if (displayRect.contains(x, y)) {
                float xResult = (x - displayRect.left)
                        / displayRect.width();
                float yResult = (y - displayRect.top)
                        / displayRect.height();
                Log.d(TAG, " x= " + xResult + "  y= " + yResult);
                Log.d(TAG, " rect x= " + displayRect.left + "  rect y= " + displayRect.top);
                return new LocationXY(xResult, yResult, x, y);
            }
        } else {
            Log.d(TAG, "Your displayRect is null ...... :)");
        }
        return null;
    }

    public class LocationXY implements Serializable {
        float x;
        float y;
        float R_width;
        float R_length;

        public LocationXY(float x, float y, float r_width, float r_length) {
            this.x = x;
            this.y = y;
            this.R_length = r_length;
            this.R_width = r_width;
        }
    }
    public void tempfunc(String item,final ClickableArea ca) {
        Log.d(TAG,"passed item recieved with filepath "+ca.getIndex()+" filepath ext "+agent.GiveMeImageUrl());
        if (item instanceof String) {
            final String text = item;

            Log.d(TAG,"size of clickable area is "+clickableAreas.size());

            //if edit mode and action mark is on
            //    if (clickableAreasImage.MODE.equals("EDIT") && clickableAreasImage.ACTION.equals("MARK"))
            //    if (clickableAreasImage.MODE.equals("VIEW")) {
            if (MODE.equals("VIEW")) {
                Log.d(TAG,"mode is "+MODE);
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Image Information");
                builder.setIcon(R.mipmap.ic_launcher);
                builder.setMessage(text)
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        }).setNegativeButton("Edit", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int id) {
                        final EditText e = new EditText(getContext());
                        e.setText("");
                        e.append(text);
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(getContext());
                        builder2.setTitle("Image Information");
                        builder2.setView(e);
                        builder2.setIcon(R.mipmap.ic_launcher);
                        builder2.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String s = e.getText().toString();
                                ca.setLabel(s);
                            }
                        });
                        builder2.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialog.cancel();
                            }
                        });
                        AlertDialog alert2 = builder2.create();
                        alert2.getWindow().getAttributes().verticalMargin = 0.3F;
                        alert2.show();
                    }
                });
                AlertDialog alert = builder.create();
                alert.getWindow().getAttributes().verticalMargin = 0.3F;
                alert.show();

                // clickableAreasImage.ext.ACTION = "ZOOM";
                ACTION = "ZOOM";

            }

        }
    }
    public interface Callback{
        public String GiveMeImageUrl();
    }
}