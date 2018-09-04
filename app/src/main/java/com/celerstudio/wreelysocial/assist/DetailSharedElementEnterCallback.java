package com.celerstudio.wreelysocial.assist;

import android.app.SharedElementCallback;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.celerstudio.wreelysocial.databinding.NearbyWorkspaceDetailBinding;
import com.celerstudio.wreelysocial.databinding.ItemNearbyWorkspaceBinding;
import com.celerstudio.wreelysocial.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DetailSharedElementEnterCallback extends SharedElementCallback {

    private final Intent intent;
    //    private float targetTextSize;
//    private ColorStateList targetTextColors;
    private NearbyWorkspaceDetailBinding nearbyWorkspaceDetailBinding;
    private ItemNearbyWorkspaceBinding itemNearbyWorkspaceBinding;
    private Rect authorPadding, cityPadding;

    public DetailSharedElementEnterCallback(Intent intent) {
        this.intent = intent;
    }

    @Override
    public void onSharedElementStart(List<String> sharedElementNames,
                                     List<View> sharedElements,
                                     List<View> sharedElementSnapshots) {
        TextView author = getAuthor();
        TextView city = getCity();
        authorPadding = new Rect(author.getPaddingLeft(),
                author.getPaddingTop(),
                author.getPaddingRight(),
                author.getPaddingBottom());

        cityPadding = new Rect(city.getPaddingLeft(),
                city.getPaddingTop(),
                city.getPaddingRight(),
                city.getPaddingBottom());
        if (IntentUtil.hasAll(intent,
                IntentUtil.TEXT_COLOR, IntentUtil.FONT_SIZE, IntentUtil.PADDING)) {
            author.setTextColor(intent.getIntExtra(IntentUtil.TEXT_COLOR, Color.BLACK));
            city.setTextColor(intent.getIntExtra(IntentUtil.TEXT_COLOR, Color.BLACK));
            float authorSize = intent.getFloatExtra(IntentUtil.FONT_SIZE, author.getTextSize());
            float citySize = intent.getFloatExtra(IntentUtil.FONT_SIZE, city.getTextSize());
            author.setTextSize(TypedValue.COMPLEX_UNIT_PX, authorSize);
            city.setTextSize(TypedValue.COMPLEX_UNIT_PX, citySize);
            Rect padding = intent.getParcelableExtra(IntentUtil.PADDING);
            author.setPadding(padding.left, padding.top, padding.right, padding.bottom);
            city.setPadding(padding.left, padding.top, padding.right, padding.bottom);
        }
    }

    @Override
    public void onSharedElementEnd(List<String> sharedElementNames,
                                   List<View> sharedElements,
                                   List<View> sharedElementSnapshots) {
        TextView author = getAuthor();
        TextView city = getCity();
        author.setTextSize(TypedValue.COMPLEX_UNIT_PX, author.getTextSize());
        city.setTextSize(TypedValue.COMPLEX_UNIT_PX, city.getTextSize());
        if (author.getTextColors() != null) {
            author.setTextColor(author.getTextColors());
        }

        if (city.getTextColors() != null) {
            city.setTextColor(city.getTextColors());
        }

        if (authorPadding != null) {
            author.setPadding(authorPadding.left, authorPadding.top,
                    authorPadding.right, authorPadding.bottom);
        }

        if (cityPadding != null) {
            author.setPadding(cityPadding.left, cityPadding.top,
                    cityPadding.right, cityPadding.bottom);
        }
//        if (currentDetailBinding != null) {
//            forceSharedElementLayout(currentDetailBinding.city);
//        }
    }

    @Override
    public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
        removeObsoleteElements(names, sharedElements, mapObsoleteElements(names));
        mapSharedElement(names, sharedElements, getAuthor());
        mapSharedElement(names, sharedElements, getCity());
        mapSharedElement(names, sharedElements, getPhoto());
    }

    public void setBinding(@NonNull NearbyWorkspaceDetailBinding binding) {
        nearbyWorkspaceDetailBinding = binding;
        itemNearbyWorkspaceBinding = null;
    }

    public void setBinding(@NonNull ItemNearbyWorkspaceBinding binding) {
        itemNearbyWorkspaceBinding = binding;
        nearbyWorkspaceDetailBinding = null;
    }

    private TextView getAuthor() {
        if (itemNearbyWorkspaceBinding != null) {
            return itemNearbyWorkspaceBinding.title;
        } else if (nearbyWorkspaceDetailBinding != null) {
            return nearbyWorkspaceDetailBinding.title;
        } else {
            throw new NullPointerException("Must set a binding before transitioning.");
        }
    }

    private TextView getCity() {
        if (itemNearbyWorkspaceBinding != null) {
            return itemNearbyWorkspaceBinding.city;
        } else if (nearbyWorkspaceDetailBinding != null) {
            return nearbyWorkspaceDetailBinding.city;
        } else {
            throw new NullPointerException("Must set a binding before transitioning.");
        }
    }

    private ImageView getPhoto() {
        if (itemNearbyWorkspaceBinding != null) {
            return itemNearbyWorkspaceBinding.pic;
        } else if (nearbyWorkspaceDetailBinding != null) {
            return nearbyWorkspaceDetailBinding.pic;
        } else {
            throw new NullPointerException("Must set a binding before transitioning.");
        }
    }

    /**
     * Maps all views that don't start with "android" namespace.
     *
     * @param names All shared element names.
     * @return The obsolete shared element names.
     */
    @NonNull
    private List<String> mapObsoleteElements(List<String> names) {
        List<String> elementsToRemove = new ArrayList<>(names.size());
        for (String name : names) {
            if (!Util.textIsEmpty(name) && name.startsWith("android")) continue;
            elementsToRemove.add(name);
        }
        return elementsToRemove;
    }

    /**
     * Removes obsolete elements from names and shared elements.
     *
     * @param names            Shared element names.
     * @param sharedElements   Shared elements.
     * @param elementsToRemove The elements that should be removed.
     */
    private void removeObsoleteElements(List<String> names,
                                        Map<String, View> sharedElements,
                                        List<String> elementsToRemove) {
        if (elementsToRemove.size() > 0) {
            names.removeAll(elementsToRemove);
            for (String elementToRemove : elementsToRemove) {
                sharedElements.remove(elementToRemove);
            }
        }
    }

    /**
     * Puts a shared element to transitions and names.
     *
     * @param names          The names for this transition.
     * @param sharedElements The elements for this transition.
     * @param view           The view to add.
     */
    private void mapSharedElement(List<String> names, Map<String, View> sharedElements, View view) {
        String transitionName = view.getTransitionName();
        names.add(transitionName);
        sharedElements.put(transitionName, view);
    }

    private void forceSharedElementLayout(View view) {
        int widthSpec = View.MeasureSpec.makeMeasureSpec(view.getWidth(),
                View.MeasureSpec.EXACTLY);
        int heightSpec = View.MeasureSpec.makeMeasureSpec(view.getHeight(),
                View.MeasureSpec.EXACTLY);
        view.measure(widthSpec, heightSpec);
        view.layout(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
    }

}