package com.example.nearby.usefulClasses;

/*
* Class to store items from the Business class which are to be displayed in the list
* */

public class Product {

    private String mId;
    private String mName;
    private String mUrl;
    private String mDisplayPhone;
    private Double mRating;
    private String mRatingImageUrl;
    private Integer mReviewCount;

    public Product(String id, String name, String url, Double rating, String displayPhone,
                   String ratingImageUrl, Integer reviewCount) {
        this.mId = id;
        this.mName = name;
        this.mRating = rating;
        this.mUrl = url;
        this.mDisplayPhone = displayPhone;
        this.mRatingImageUrl = ratingImageUrl;
        this.mReviewCount = reviewCount;
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

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        this.mUrl = url;
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
        return "Product [id=" + mId + ", name=" + mName + ", url="
                + mUrl + ", rating=" + mRating + ", displayPhone=" + mDisplayPhone
                + ", ratingImageUrl=" + mRatingImageUrl + ", reviewCount="
                + mReviewCount + "]";
    }
}