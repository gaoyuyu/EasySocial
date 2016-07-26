package com.gaoyy.easysocial;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.gaoyy.easysocial.base.BaseActivity;
import com.gaoyy.easysocial.utils.Tool;

import me.relex.photodraweeview.PhotoDraweeView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.imagepipeline.image.ImageInfo;

import me.relex.photodraweeview.OnPhotoTapListener;
import me.relex.photodraweeview.OnViewTapListener;

import android.graphics.drawable.Animatable;

public class PhotoActivity extends BaseActivity
{
    private PhotoDraweeView mPhotoDraweeView;
    @Override
    public void initContentView()
    {
        setContentView(R.layout.activity_photo);
        Uri uri = Uri.parse(getIntent().getStringExtra("url"));
            mPhotoDraweeView = (PhotoDraweeView) findViewById(R.id.photo_drawee_view);
//        final PhotoDraweeView mPhotoDraweeView = new PhotoDraweeView(this);
//        setContentView(mPhotoDraweeView);
        PipelineDraweeControllerBuilder controller = Fresco.newDraweeControllerBuilder();
        controller.setUri(uri);
        controller.setOldController(mPhotoDraweeView.getController());
        controller.setControllerListener(new BaseControllerListener<ImageInfo>()
        {
            @Override
            public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable)
            {
                super.onFinalImageSet(id, imageInfo, animatable);
                if (imageInfo == null || mPhotoDraweeView == null)
                {
                    return;
                }
                mPhotoDraweeView.update(imageInfo.getWidth(), imageInfo.getHeight());
            }
        });
        mPhotoDraweeView.setController(controller.build());
        mPhotoDraweeView.setOnPhotoTapListener(new OnPhotoTapListener()
        {
            @Override
            public void onPhotoTap(View view, float x, float y)
            {
            }
        });
        mPhotoDraweeView.setOnViewTapListener(new OnViewTapListener()
        {
            @Override
            public void onViewTap(View view, float x, float y)
            {
            }
        });

    }

}
