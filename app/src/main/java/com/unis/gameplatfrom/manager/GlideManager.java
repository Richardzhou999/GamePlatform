package com.unis.gameplatfrom.manager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.security.MessageDigest;

import com.unis.gameplatfrom.R;

/**
 * Glide封装
 */
public final class GlideManager {

    private static int sCommonPlaceholder = R.drawable.logo;

    private static int sRoundPlaceholder = R.drawable.logo;

    /**
     * 设置圆形图片占位图
     *
     * @param roundPlaceholder
     */
    public static void setRoundPlaceholder(int roundPlaceholder) {
        sRoundPlaceholder = roundPlaceholder;
    }

    /**
     * 设置通用占位图
     *
     * @param commonPlaceholder
     */
    public static void setCommonPlaceholder(int commonPlaceholder) {
        sCommonPlaceholder = commonPlaceholder;
    }

    /**
     * 加载网络图片
     *
     * @param obj
     * @param iv
     */
    public static void loadImg(Object obj, ImageView iv) {
        loadImg(obj, iv, sCommonPlaceholder);
    }

    /**
     * 加载网络gif
     *
     * @param obj
     * @param iv
     */
    public static void loadGif(Object obj, ImageView iv) {
        loadGif(obj, iv, sCommonPlaceholder);
    }

    /**
     * 加载网络gif和图片
     *
     * @param obj
     * @param iv
     */
    public static void loadImgAndGif(Object obj, ImageView iv) {
        loadImgAndGif(obj, iv, sCommonPlaceholder);
    }

    /**
     * 加载网络图片
     *
     * @param obj
     * @param iv
     * @param placeholder 占位图
     */
    public static void loadImg(Object obj, ImageView iv, int placeholder) {
        Context context = iv.getContext();
        RequestManager manager = Glide.with(context);
        RequestBuilder<Drawable> drawableTypeRequest = null;

        if (obj instanceof String) {
            drawableTypeRequest = manager.load((String) obj);
        } else if (obj instanceof Integer) {
            drawableTypeRequest = manager.load((Integer) obj);
        } else if (obj instanceof Uri) {
            drawableTypeRequest = manager.load((Uri) obj);
        } else if (obj instanceof File) {
            drawableTypeRequest = manager.load((File) obj);
        }
        if (drawableTypeRequest == null) {
            return;
        }

        RequestOptions options = new RequestOptions()
                .placeholder(placeholder)
                .error(placeholder)
                .dontAnimate();

        drawableTypeRequest.apply(options)
                .into(iv);

    }


    public static void loadGif(Object obj, ImageView iv, int placeholder) {
        Context context = iv.getContext();
        RequestManager manager = Glide.with(context);
        RequestBuilder<Drawable> drawableTypeRequest = null;

        if (obj instanceof String) {
            drawableTypeRequest = manager.load((String) obj);
        } else if (obj instanceof Integer) {
            drawableTypeRequest = manager.load((Integer) obj);
        } else if (obj instanceof Uri) {
            drawableTypeRequest = manager.load((Uri) obj);
        } else if (obj instanceof File) {
            drawableTypeRequest = manager.load((File) obj);
        }
        if (drawableTypeRequest == null) {
            return;
        }

        RequestOptions options = new RequestOptions()
                .placeholder(placeholder)
                .error(placeholder)
                .diskCacheStrategy(DiskCacheStrategy.DATA);


        drawableTypeRequest.apply(options)
                .into(iv);

    }

    public static void loadImgAndGif(Object obj, ImageView iv, int placeholder) {
        Context context = iv.getContext();
        RequestManager manager = Glide.with(context);
        RequestBuilder<Drawable> drawableTypeRequest = null;

        if (obj instanceof String) {
            drawableTypeRequest = manager.load((String) obj);
        } else if (obj instanceof Integer) {
            drawableTypeRequest = manager.load((Integer) obj);
        } else if (obj instanceof Uri) {
            drawableTypeRequest = manager.load((Uri) obj);
        } else if (obj instanceof File) {
            drawableTypeRequest = manager.load((File) obj);
        }
        if (drawableTypeRequest == null) {
            return;
        }

        RequestOptions options = new RequestOptions()
                .placeholder(placeholder)
                .error(placeholder)
                .diskCacheStrategy(DiskCacheStrategy.DATA);


        drawableTypeRequest.apply(options)
                .into(iv);

    }














    /**
     * 加载圆形网络图片
     *
     * @param obj
     * @param iv
     */
    public static void loadRoundImg(Object obj, ImageView iv) {
        loadRoundImg(obj, iv, sRoundPlaceholder);
    }

    /**
     * 加载圆形网络图片
     *
     * @param obj
     * @param iv
     * @param placeholder 占位图
     */
    public static void loadRoundImg(Object obj, ImageView iv, int placeholder) {
        Context context = iv.getContext();
        RequestManager manager = Glide.with(context);
        RequestBuilder<Drawable> drawableTypeRequest = null;

        if (obj instanceof String) {
            drawableTypeRequest = manager.load((String) obj);
        } else if (obj instanceof Integer) {
            drawableTypeRequest = manager.load((Integer) obj);
        } else if (obj instanceof Uri) {
            drawableTypeRequest = manager.load((Uri) obj);
        } else if (obj instanceof File) {
            drawableTypeRequest = manager.load((File) obj);
        }
        if (drawableTypeRequest == null) {
            return;
        }

        RequestOptions options = new RequestOptions()
                .placeholder(placeholder)
                .error(placeholder)
                .dontAnimate()
                .transform(new GlideCircleTransform());

        drawableTypeRequest.apply(options)
                .into(iv);

    }

    /**
     * Glide加载圆形图片
     */
    public static class GlideCircleTransform extends BitmapTransformation {

//        public GlideCircleTransform(Context context) {
//            super(context);
//        }

        @Override
        protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
            return circleCrop(pool, toTransform);
        }

        @Override
        public void updateDiskCacheKey(MessageDigest messageDigest) {

        }

        private static Bitmap circleCrop(BitmapPool pool, Bitmap source) {
            if (source == null) {
                return null;
            }
            int size = Math.min(source.getWidth(), source.getHeight());
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);

            Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
            if (result == null) {
                result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);
            return result;
        }
//
//        @Override
//        public String getId() {
//            return getClass().getName();
//        }
    }
}


