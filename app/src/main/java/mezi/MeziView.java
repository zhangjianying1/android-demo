package mezi;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.example.caiyue.androidstuiodemo.R;



/**
 * Created by caiyue on 2018/4/8.
 */

public class MeziView extends View {
    public float bitmapX;
    public float bitmapY;
    public Handler handler;
    public MeziView(Context context){
        super(context);
        bitmapY = 200;
        bitmapX = 0;

    }
    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        Paint paint  = new Paint();

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.article);
        canvas.drawBitmap(bitmap, bitmapX, bitmapY, paint);
        if (bitmap.isRecycled()) {
            bitmap.recycle();
        }
    }
}
