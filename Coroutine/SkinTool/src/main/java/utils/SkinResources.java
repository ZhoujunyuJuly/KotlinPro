package utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.view.View;

public class SkinResources {
    private String mSkinPkgName;
    private boolean isDefaultSkin = true;
    //
    private Resources mAppResources;
    private Resources mSkinResources;
    private volatile static SkinResources mInstance;

    private SkinResources(Context context){
        mAppResources = context.getResources();

    }

    public static void init(Context context){
        if( mInstance == null){
            synchronized (SkinResources.class){
                if( mInstance == null){
                    mInstance = new SkinResources(context);
                }
            }
        }
    }

    public static SkinResources getInstance(){
        return mInstance;
    }

    public void reset(){
        mSkinResources = null;
        isDefaultSkin = true;
        mSkinPkgName = "";
    }

    public void applySkin(Resources resources,String pkgName) {
        mSkinResources = resources;
        mSkinPkgName = pkgName;
        isDefaultSkin = TextUtils.isEmpty(pkgName) || resources == null;
    }

    public int getIdentifier(int resId){
        if( isDefaultSkin ){
            return resId;
        }

        String resName = mAppResources.getResourceName(resId);
        String resType = mAppResources.getResourceTypeName(resId);
        @SuppressLint("DiscouragedApi")
        int skinId = mSkinResources.getIdentifier(resName,resType,mSkinPkgName);
        return skinId;
    }

    public int getColor(int resId){
        if( isDefaultSkin ){
            return mAppResources.getColor(resId);
        }
        int skinId = getIdentifier(resId);
        if( skinId == 0){
            return mAppResources.getColor(resId);
        }
        return mSkinResources.getColor(skinId);
    }

}
