package com.example.nearby.usefulClasses;

/*
* Class to store items from the Business class which are to be displayed in the list
* */


public class Product {

    private String mId;
    private String mName;
    private String mImageUrl;
    private String mDisplayPhone;
    private Double mRating;
    private String mRatingImageUrl;
    private Integer mReviewCount;
    private String mLocation;
    private String mCategory;
    private String mDetailUrl;

    public Product(String id, String name, String imageUrl, Double rating, String displayPhone,String ratingImageUrl,
                   Integer reviewCount, String city, String country, String category, String detailUrl) {
        this.mId = id;
        this.mName = name;
        this.mRating = rating;
        this.mImageUrl = imageUrl;
        this.mDisplayPhone = displayPhone;
        this.mRatingImageUrl = ratingImageUrl;
        this.mReviewCount = reviewCount;
        this.mLocation = city.concat(", ").concat(country);
        this.mCategory = category;
        this.mDetailUrl = detailUrl;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        this.mId = id;
    }

    public String getName() {  return mName; }

    public void setName(String name) {
        this.mName = name;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.mImageUrl = imageUrl;
    }

    public Double getRating() {
        return mRating;
    }

    public void setRating(Double rating) {
        this.mRating = rating;
    }

    public String getDisplayPhone() {  return mDisplayPhone;  }

    public void setDisplayPhone(String displayPhone) { this.mDisplayPhone = displayPhone;  }

    public String getRatingImageUrl() {
        return mRatingImageUrl;
    }

    public void setRatingImageUrl(String ratingImageUrl) {
        this.mRatingImageUrl = ratingImageUrl;
    }

    public Integer getReviewCount() {
        return mReviewCount;
    }

    public void setReviewCount(Integer reviewCount) {
        this.mReviewCount = reviewCount;
    }

    public String getLocation() {
        return mLocation;
    }

    public void setLocation(String city) {
        this.mLocation = city;
    }

    public String getCategory(){return  mCategory;}

    public void setCategory(String category){
        this.mCategory = category;
    }

    public String getDetailUrl(){return  mDetailUrl;}

    public void setDetailUrl(String detailUrl){
        mDetailUrl = detailUrl;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj.getClass() == this.getClass()) {
            Product other = (Product) obj;
            String thisId = this.getId();
            String otherId = other.getId();
            return thisId.equals(otherId);
        }else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + mId.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Product [id=" + mId + ", name=" + mName + ", city=" + mLocation +
                ", url=" + mImageUrl + ", rating=" + mRating + ", displayPhone=" + mDisplayPhone
                + ", ratingImageUrl=" + mRatingImageUrl + ", reviewCount=" + mReviewCount
                + ", category=" + mCategory + ", detailUrl=" + mDetailUrl +
                "]";
    }
}