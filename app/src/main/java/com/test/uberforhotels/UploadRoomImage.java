package com.test.uberforhotels;

public class UploadRoomImage {
    private String mImageName;
    private String mImageUrl;

    public UploadRoomImage(){
// empty constructor
    }

    public UploadRoomImage(String imageName, String imageUrl){
        mImageName = imageName;
        mImageUrl = imageUrl;
    }

    public String getmImageName() {
        return mImageName;
    }

    public void setmImageName(String mImageName) {
        this.mImageName = mImageName;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public void setmImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }
}
