package com.example.anafl.projetofirebase;

public class Upload {
    private String mImageUrl;
    private String mId;

    public Upload(){

    }
    public Upload( String imagemUrl, String id){

        mImageUrl = imagemUrl;
        mId = id;

    }

    public String getmImageUrl(){

        return mImageUrl;
    }
    public void setmImagemUrl(String imageUrl)
    {
        mImageUrl = imageUrl;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String id) {
        this.mId = id;
    }
}
