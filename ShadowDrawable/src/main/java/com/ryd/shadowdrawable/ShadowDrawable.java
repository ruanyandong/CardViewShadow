package com.ryd.shadowdrawable;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;

public class ShadowDrawable extends Drawable {
    /**
     * 阴影画笔
     */
    private Paint mShadowPaint;
    /**
     * 背景画笔，是View的BackGround
     */
    private Paint mBgPaint;
    /**
     * 阴影模糊半径，越大越模糊，越小越清晰，如果为0阴影消失
     */
    private int mShadowRadius;
    /**
     * 阴影和背景的形状
     */
    private int mShape = 1;
    /**
     * 矩形的圆角半径
     */
    private int mShapeRadius;
    /**
     * 阴影的x轴偏移量，正值向右偏移，负值向左偏移
     */
    private int mOffsetX;
    /**
     * 阴影的y轴偏移量，正值向下偏移，负值向上偏移
     */
    private int mOffsetY;
    /**
     * 背景颜色
     */
    private int mBgColor[];
    /**
     * 矩形绘制区域
     */
    private RectF mRect;
    /**
     * 圆角shape
     */
    public final static int SHAPE_ROUND = 1;
    /**
     * 圆形shape
     */
    public final static int SHAPE_CIRCLE = 2;

    private ShadowDrawable(int shape, int[] bgColor, int shapeRadius, int shadowColor, int shadowRadius, int offsetX, int offsetY) {
        this.mShape = shape;
        this.mBgColor = bgColor;
        this.mShapeRadius = shapeRadius;
        this.mShadowRadius = shadowRadius;
        this.mOffsetX = offsetX;
        this.mOffsetY = offsetY;

        mShadowPaint = new Paint();
        // 这里如果设置了阴影画笔的颜色为透明，则看不到阴影
        //mShadowPaint.setColor(Color.TRANSPARENT);
        // 矩形的颜色
        mShadowPaint.setColor(Color.GREEN);
        mShadowPaint.setAntiAlias(true);
        // 设置画笔的阴影模糊
        mShadowPaint.setShadowLayer(shadowRadius, offsetX, offsetY, shadowColor);

        // 这里加不加没影响 DST_ATOP相交区域进行颜色模糊
       // mShadowPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP));

        mBgPaint = new Paint();
        mBgPaint.setAntiAlias(true);
    }

    /**
     * drawable的边框，drawable绘制的区域
     * right - left = view的宽度
     * bottom - top = view的高度
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    @Override
    public void setBounds(int left, int top, int right, int bottom) {
        super.setBounds(left, top, right, bottom);
        // left 0 top 0 right 550 bottom 275
        // mShadowRadius 40 mOffsetX 0 mOffsetY 0
        Log.d(this.getClass().getSimpleName(), "setBounds: left "+left+" top "+top+" right "+right+" bottom "+bottom);
        Log.d(this.getClass().getSimpleName(), "setBounds: mShadowRadius "+mShadowRadius+" mOffsetX "+mOffsetX+" mOffsetY "+mOffsetY);
        mRect = new RectF(
                left + mShadowRadius - mOffsetX,
                top + mShadowRadius - mOffsetY,
                right - mShadowRadius - mOffsetX,
                bottom - mShadowRadius - mOffsetY
        );
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        if (mBgColor != null) {
            if (mBgColor.length == 1) {
                mBgPaint.setColor(mBgColor[0]);
            } else {
                // 设置渐变着色器
                mBgPaint.setShader(new LinearGradient(mRect.left, mRect.height() / 2,
                        mRect.right,
                        mRect.height() / 2,
                        mBgColor,
                        null,
                        Shader.TileMode.CLAMP));
            }
        }

        if (mShape == SHAPE_ROUND) {
            /**
             * 画圆角矩形
             * mRect 绘制区域
             * rx 矩形圆角的x方向的半径
             * ry 矩形圆角的y方向的半径
             * paint
             */
            canvas.drawRoundRect(mRect, mShapeRadius, mShapeRadius, mShadowPaint);
            // 可以去掉
            canvas.drawRoundRect(mRect, mShapeRadius, mShapeRadius, mBgPaint);
        } else {
            canvas.drawCircle(mRect.centerX(), mRect.centerY(), Math.min(mRect.width(), mRect.height())/ 2, mShadowPaint);
            // 可以去掉
            canvas.drawCircle(mRect.centerX(), mRect.centerY(), Math.min(mRect.width(), mRect.height())/ 2, mBgPaint);
        }
    }

    /**
     * 设置颜色的透明度
     * @param alpha
     */
    @Override
    public void setAlpha(int alpha) {
        mShadowPaint.setAlpha(alpha);
    }

    /**
     * 设置滤镜效果
     * @param colorFilter
     */
    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        mShadowPaint.setColorFilter(colorFilter);
    }

    /**
     * 返回drawable的不透明度
     * UNKNOWN, 未知
     * TRANSLUCENT, 半透明
     * TRANSPARENT, 透明
     * OPAQUE 不透明
     * @return
     */
    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    public static void setShadowDrawable(View view, int bgColor, int shapeRadius, int shadowColor, int shadowRadius, int offsetX, int offsetY) {
        ShadowDrawable drawable = new ShadowDrawable.Builder()
                .setBgColor(bgColor)
                .setShapeRadius(shapeRadius)
                .setShadowColor(shadowColor)
                .setShadowRadius(shadowRadius)
                .setOffsetX(offsetX)
                .setOffsetY(offsetY)
                .builder();
        view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        ViewCompat.setBackground(view, drawable);
    }

    public static void setShadowDrawable(View view, Drawable drawable) {
        view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        ViewCompat.setBackground(view, drawable);
    }

    public static void setShadowDrawable(View view, int shapeRadius, int shadowColor, int shadowRadius, int offsetX, int offsetY) {
        ShadowDrawable drawable = new ShadowDrawable.Builder()
                .setShapeRadius(shapeRadius)
                .setShadowColor(shadowColor)
                .setShadowRadius(shadowRadius)
                .setOffsetX(offsetX)
                .setOffsetY(offsetY)
                .builder();
        view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        ViewCompat.setBackground(view, drawable);
    }

    public static void setShadowDrawable(View view, int shape, int bgColor, int shapeRadius, int shadowColor, int shadowRadius, int offsetX, int offsetY) {
        ShadowDrawable drawable = new ShadowDrawable.Builder()
                .setShape(shape)
                .setBgColor(bgColor)
                .setShapeRadius(shapeRadius)
                .setShadowColor(shadowColor)
                .setShadowRadius(shadowRadius)
                .setOffsetX(offsetX)
                .setOffsetY(offsetY)
                .builder();
        view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        ViewCompat.setBackground(view, drawable);
    }

    public static void setShadowDrawable(View view, int[] bgColor, int shapeRadius, int shadowColor, int shadowRadius, int offsetX, int offsetY) {
        ShadowDrawable drawable = new ShadowDrawable.Builder()
                .setBgColor(bgColor)
                .setShapeRadius(shapeRadius)
                .setShadowColor(shadowColor)
                .setShadowRadius(shadowRadius)
                .setOffsetX(offsetX)
                .setOffsetY(offsetY)
                .builder();
        view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        ViewCompat.setBackground(view, drawable);
    }

    public static class Builder {
        private int mShape;
        private int mShapeRadius;
        private int mShadowColor;
        private int mShadowRadius;
        private int mOffsetX = 0;
        private int mOffsetY = 0;
        private int[] mBgColor;

        public Builder() {
            mShape = ShadowDrawable.SHAPE_ROUND;
            mShapeRadius = 12;
            mShadowColor = Color.parseColor("#4d000000");
            mShadowRadius = 18;
            mOffsetX = 0;
            mOffsetY = 0;
            mBgColor = new int[1];
            mBgColor[0] = Color.TRANSPARENT;
        }

        public Builder setShape(int mShape) {
            this.mShape = mShape;
            return this;
        }

        public Builder setShapeRadius(int ShapeRadius) {
            this.mShapeRadius = ShapeRadius;
            return this;
        }

        public Builder setShadowColor(int shadowColor) {
            this.mShadowColor = shadowColor;
            return this;
        }

        public Builder setShadowRadius(int shadowRadius) {
            this.mShadowRadius = shadowRadius;
            return this;
        }

        public Builder setOffsetX(int OffsetX) {
            this.mOffsetX = OffsetX;
            return this;
        }

        public Builder setOffsetY(int OffsetY) {
            this.mOffsetY = OffsetY;
            return this;
        }

        public Builder setBgColor(int BgColor) {
            this.mBgColor[0] = BgColor;
            return this;
        }

        public Builder setBgColor(int[] BgColor) {
            this.mBgColor = BgColor;
            return this;
        }

        public ShadowDrawable builder() {
            return new ShadowDrawable(mShape, mBgColor, mShapeRadius, mShadowColor, mShadowRadius, mOffsetX, mOffsetY);
        }
    }

}

