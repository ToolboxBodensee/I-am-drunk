package org.toolboxbodensee.iamdrunk;

/**
 * Created by simon on 26.09.15.
 */
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class PunktDrawView extends View implements View.OnTouchListener{
    Paint paint = new Paint();
    double speed = 0.8;
    double counter = 0;
    boolean first = true;

    double a2x = 0,  a2y = 0;
    double ax = 0,  ay = 0;
    double vx = (Math.random() - 0.5) * speed * 5,  vy = (Math.random() - 0.5) * speed * 5;
    double x = 100, y = 100;
    double timer = 10;
    double score = 0;
    public PunktDrawView(Context context) {
        super(context);
        paint.setColor(Color.BLACK);
        this.setOnTouchListener(this);
    }
    public void move()
    {
        if(Math.random()< 0.2)
        ax += (Math.random() - 0.5) * 5* speed; ay += (Math.random() - 0.5) * 5 * speed;
        vx += ax; vy += ay;
        if(vx > speed * 80) {
            vx /= 2;
            ax /= -2;
        }
        if(vx < speed * -80) {
            vx /= 2;
            ax /= -2;
        }
        if(vy > speed * 80) {
            vy /= 2;
            ay /= -2;
        }
        if(vy < speed * -80) {
            vy /= 2;
            ay /= -2;
        }
        x += vx; y += vy;
        timer -= 0.1 + counter;
        score += 0.1 + counter;
        if(x > this.getWidth()-150)
            x = -50;
        else if(x < -50)
            x = this.getWidth()-150;
        if(y > this.getHeight()-150)
            y = -50;
        else if(y < -50)
            y = this.getHeight()-150 ;
        counter += 0.001;
    }
    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawBitmap(BitmapFactory.decodeResource(this.getContext().getResources(),
                R.drawable.punkt19302087), (float)x, (float)y, paint);
        paint.setTextSize(36);
        timer*=100;
        timer = Math.round(timer);
        timer/=100;
        score*=100;
        score = Math.round(score);
        score/=100;
        canvas.drawText("Zeit verbleibend:" + timer, 100, 100, paint);
        canvas.drawText("Score:" + score, 100,200, paint);
        if(timer>0)
            move();
        else {
            paint.setTextSize(72);
            if(score < 25)
            {
                canvas.drawText("Du bist zu betrunken" + timer, 100, 300, paint);
            }
            else{
                canvas.drawText("Du bist noch nüchtern!", 50,300,paint );
                if(first) {
                    launchAppChooser();
                    first=false;
                }
            }

            timer = 0;
        }
        try {
            Thread.sleep(30);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        invalidate();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(timer >0 && event.getAction() == MotionEvent.ACTION_DOWN)
        {
            if((event.getX()- (x + 100))*(event.getX()-( x + 100)) + (event.getY()-(y + 100))*(event.getY()-(y + 100)) < 13000)
                timer += 4;
            Log.e("hjgkig", (event.getX()- (x + 100))*(event.getX()-( x + 100)) + (event.getY()-(y + 100))*(event.getY()-(y + 100)) + "");

        }
        return true;
    }

    private void launchAppChooser() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.getContext().startActivity(intent);
    }
}