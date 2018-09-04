package com.celerstudio.wreelysocial.assist;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.celerstudio.wreelysocial.R;
import com.celerstudio.wreelysocial.models.NearbyWorkspace;
import com.celerstudio.wreelysocial.util.Util;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Picasso;

/**
 * Demonstrates customizing the info window and/or its contents.
 */
public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    // These are both viewgroups containing an ImageView with id "badge" and two TextViews with id
    // "title" and "snippet".

    private Context context;

    public CustomInfoWindowAdapter(Context ctx) {
        context = ctx;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        NearbyWorkspace item = (NearbyWorkspace) marker.getTag();
        View view = ((Activity) context).getLayoutInflater()
                .inflate(R.layout.item_nearby_workspace, null);

        TextView monthPrice = view.findViewById(R.id.month_price);
        TextView title = view.findViewById(R.id.title);
        TextView city = view.findViewById(R.id.city);
        TextView address = view.findViewById(R.id.address);
        ImageView pic = view.findViewById(R.id.pic);
        ImageView direction = view.findViewById(R.id.direction);

        Picasso.with(context).load(item.getImage()).into(pic);
        float mp = Util.textIsEmpty(item.getMonthPrice()) ? 0 : Float.parseFloat(item.getMonthPrice());
        if (mp <= 0) {
            monthPrice.setVisibility(View.GONE);
        } else {
            monthPrice.setText(item.getCurrency() + " " + item.getMonthPrice() + "/Month");
        }

        title.setText(item.getName());
        city.setText(item.getCity());
        address.setText(item.getAddress() + " " + item.getPostalArea());

        return view;
    }


}