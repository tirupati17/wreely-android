package com.celerstudio.wreelysocial.models;

import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

import com.celerstudio.wreelysocial.R;
import com.google.gson.annotations.SerializedName;
import com.squareup.picasso.Picasso;

public class NearbyWorkspace implements Parcelable {

    @SerializedName("cs_id")
    private String id;
    @SerializedName("NAME")
    private String name;
    private String address;
    private String city;
    private String state;
    private String country;
    private String image;
    private String currency;
    @SerializedName("day_price")
    private String dayPrice;
    @SerializedName("week_price")
    private String weekPrice;
    @SerializedName("month_price")
    private String monthPrice;
    private String description;
    @SerializedName("meeting_rooms")
    private String meetingRooms;
    @SerializedName("private_rooms")
    private String privateRooms;
    @SerializedName("cw_url")
    private String url;
    @SerializedName("additional_images")
    private String additionalImages;
    @SerializedName("postal_area")
    private String postalArea;
    private String latitude;
    private String longitude;
    @SerializedName("star_rating")
    private String starRating;

    @SerializedName("num_reviews")
    private String numReviews;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

//    @BindingAdapter({"bind:image"})
//    public static void loadImage(ImageView view, String imageUrl) {
//        Picasso.with(view.getContext())
//                .load(imageUrl)
//                .placeholder(R.drawable.place_holder)
//                .into(view);
//    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDayPrice() {
        return dayPrice;
    }

    public void setDayPrice(String dayPrice) {
        this.dayPrice = dayPrice;
    }

    public String getWeekPrice() {
        return weekPrice;
    }

    public void setWeekPrice(String weekPrice) {
        this.weekPrice = weekPrice;
    }

    public String getMonthPrice() {
        return monthPrice;
    }

    public void setMonthPrice(String monthPrice) {
        this.monthPrice = monthPrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMeetingRooms() {
        return meetingRooms;
    }

    public void setMeetingRooms(String meetingRooms) {
        this.meetingRooms = meetingRooms;
    }

    public String getPrivateRooms() {
        return privateRooms;
    }

    public void setPrivateRooms(String privateRooms) {
        this.privateRooms = privateRooms;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAdditionalImages() {
        return additionalImages;
    }

    public void setAdditionalImages(String additionalImages) {
        this.additionalImages = additionalImages;
    }

    public String getPostalArea() {
        return postalArea;
    }

    public void setPostalArea(String postalArea) {
        this.postalArea = postalArea;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getStarRating() {
        return starRating;
    }

    public void setStarRating(String starRating) {
        this.starRating = starRating;
    }

    public String getNumReviews() {
        return numReviews;
    }

    public void setNumReviews(String numReviews) {
        this.numReviews = numReviews;
    }

    public NearbyWorkspace() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.address);
        dest.writeString(this.city);
        dest.writeString(this.state);
        dest.writeString(this.country);
        dest.writeString(this.image);
        dest.writeString(this.currency);
        dest.writeString(this.dayPrice);
        dest.writeString(this.weekPrice);
        dest.writeString(this.monthPrice);
        dest.writeString(this.description);
        dest.writeString(this.meetingRooms);
        dest.writeString(this.privateRooms);
        dest.writeString(this.url);
        dest.writeString(this.additionalImages);
        dest.writeString(this.postalArea);
        dest.writeString(this.latitude);
        dest.writeString(this.longitude);
        dest.writeString(this.starRating);
        dest.writeString(this.numReviews);
    }

    protected NearbyWorkspace(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.address = in.readString();
        this.city = in.readString();
        this.state = in.readString();
        this.country = in.readString();
        this.image = in.readString();
        this.currency = in.readString();
        this.dayPrice = in.readString();
        this.weekPrice = in.readString();
        this.monthPrice = in.readString();
        this.description = in.readString();
        this.meetingRooms = in.readString();
        this.privateRooms = in.readString();
        this.url = in.readString();
        this.additionalImages = in.readString();
        this.postalArea = in.readString();
        this.latitude = in.readString();
        this.longitude = in.readString();
        this.starRating = in.readString();
        this.numReviews = in.readString();
    }

    public static final Creator<NearbyWorkspace> CREATOR = new Creator<NearbyWorkspace>() {
        @Override
        public NearbyWorkspace createFromParcel(Parcel source) {
            return new NearbyWorkspace(source);
        }

        @Override
        public NearbyWorkspace[] newArray(int size) {
            return new NearbyWorkspace[size];
        }
    };
}
