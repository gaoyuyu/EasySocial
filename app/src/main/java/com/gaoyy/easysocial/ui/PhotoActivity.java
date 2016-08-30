package com.gaoyy.easysocial.ui;

import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.view.View;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.imagepipeline.image.ImageInfo;
import com.gaoyy.easysocial.R;
import com.gaoyy.easysocial.base.BaseActivity;

import me.relex.photodraweeview.OnPhotoTapListener;
import me.relex.photodraweeview.OnViewTapListener;
import me.relex.photodraweeview.PhotoDraweeView;

/**
 * 图片展示
 */
public class PhotoActivity extends BaseActivity
{
    private PhotoDraweeView mPhotoDraweeView;

    @Override
    public void initContentView()
    {
        setContentView(R.layout.activity_photo);
        Uri uri = Uri.parse(getIntent().getStringExtra("url"));
        mPhotoDraweeView = (PhotoDraweeView) findViewById(R.id.photo_drawee_view);
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
