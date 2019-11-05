package com.sjjd.wyl.printtest;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by wyl on 2018/12/29.
 */
public class PdfUtil {
    public static final String FONT = "assets/NotoSansCJKsc-Regular.otf";
    static String tail = "\u001B%-12345X@PJL EOJ\n" +
            "\u001B%-12345X";
    static String header1 = "\u001B%-12345X@PJL SET STRINGCODESET=UTF8\n";
    static String header2 = "@PJL JOB NAME=";
    static String header3 =
            "@PJL COMMENT \"87660z (0.3.1584.23029); Windows 10 Enterprise 10.0.15063.1; Unidrv 0.3.15063.138\"\n" +
                    "@PJL COMMENT \"Username: wuxingj; App Filename: %1$s; 12-28-2018\"\n" +
                    "@PJL COMMENT \"NUP = NUP_1\"\n" +
                    "@PJL SET JOBATTR=\"OS=Android\"\n" +
                    "@PJL SET JOBATTR=\"OS Version=%2$s\"\n" +
                    "@PJL SET JOBATTR=\"Render Type=Discrete V3\"\n" +
                    "@PJL SET JOBATTR=\"Render Name=HP Color LaserJet Pro M452 PCL 6\"\n" +
                    "@PJL SET JOBATTR=\"Render Version=13.26\"\n" +
                    "@PJL SET JOBATTR=\"JobAcct1=wuxingj\"\n" +
                    "@PJL SET JOBATTR=\"JobAcct2=WUXINGJ16\"\n" +
                    "@PJL SET JOBATTR=\"JobAcct3=AUTH\"\n" +
                    "@PJL SET JOBATTR=\"JobAcct4=20181228141929\"\n" +
                    "@PJL SET JOBATTR=\"JobAcct5=09c91034-c700-4fc8-a290-39aef130b6a2\"\n" +
                    "@PJL SET JOBATTR=\"JobAcct6=Adobe Acrobat Reader DC \"\n" +
                    "@PJL SET JOBATTR=\"JobAcct7=AcroRd32.exe\"\n" +
                    "@PJL SET JOBATTR=\"JobAcct8=wuxingj\"\n" +
                    "@PJL SET JOBATTR=\"JobAcct9=\"\n" +
                    "@PJL DMINFO ASCIIHEX=\"0400040101020D101001153230313831323238303631393239\"\n" +
                    "@PJL SET GRAYSCALE=OFF\n" +
                    "@PJL SET PLANESINUSE=3\n" +
                    "@PJL SET HOLD=OFF\n" +
                    "@PJL SET USERNAME=\"WUXINGJ\"\n" +
                    "@PJL SET SEPARATORPAGE=OFF\n" +
                    "@PJL SET FOLD=\"\"\n" +
                    "@PJL SET PUNCH=OFF\n" +
                    "@PJL SET PROCESSINGACTION=APPEND\n" +
                    "@PJL SET PROCESSINGTYPE=\"PUNCH\"\n" +
                    "@PJL SET PROCESSINGOPTION=\"NONE\"\n" +
                    "@PJL SET PROCESSINGBOUNDARY=MOPY\n" +
                    "@PJL SET QTY =1\n" +
                    "@PJL SET OUTBIN=AUTO\n" +
                    "@PJL SET EDGETOEDGE=NO\n" +
                    "@PJL SET PROCESSINGACTION=APPEND\n" +
                    "@PJL SET PROCESSINGTYPE=\"STAPLING\"\n" +
                    "@PJL SET PROCESSINGOPTION=\"NONE\"\n" +
                    "@PJL SET PROCESSINGBOUNDARY=MOPY\n" +
                    "@PJL SET PRINTQUALITY=NORMAL\n" +
                    "@PJL ENTER LANGUAGE=PDF\n";


    public static void write(String path, File pdf) {

        try {
            FileInputStream fis = new FileInputStream(path);
            FileOutputStream fos = new FileOutputStream(pdf);

            //文件尾信息
            String tail = "\u001B%-12345X@PJL EOJ\n" +
                    "\u001B%-12345X";

            fos.write(header1.getBytes());
            fos.write((header2 + "\"" + pdf.getName() + "\"" + "\r\n").getBytes());
            fos.write(String.format(header3, pdf.getName(), Build.MODEL).getBytes());

            int len = 0;
            byte[] b = new byte[1024];
            while ((len = fis.read(b)) > 0) {
                fos.write(b, 0, len);
            }

            fos.write(tail.getBytes());//添加文件尾

            fos.flush();
            fos.close();
            fis.close();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public static void print(String filePath) {
        try {
            String execStr = "cp " + filePath + " /dev/usb/lp0";
            new ExeCommand(true).run(execStr, 10000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static RelativeLayout mLayout;
    static TextView mTextView;

    public static void showDialog(Context context, ViewGroup root, String txt) {
        if (mLayout == null && mTextView == null) {
            mLayout = new RelativeLayout(context);
            RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(-1, -1);
            RelativeLayout.LayoutParams pt = new RelativeLayout.LayoutParams(-2, -2);
            pt.addRule(RelativeLayout.CENTER_IN_PARENT);
            mTextView = new TextView(context);
            mTextView.setText(txt);
            mTextView.setTextColor(Color.RED);
            mTextView.setLayoutParams(pt);
            mLayout.addView(mTextView);
            mLayout.setBackgroundColor(Color.GRAY);
            mLayout.setLayoutParams(p);
            mLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            root.addView(mLayout);
        } else {
            mTextView.setText(txt);
        }

    }

    public static void hideDialog(ViewGroup root) {
        if (mLayout != null && mTextView != null) {
            mLayout.removeView(mTextView);
            mTextView = null;
            root.removeView(mLayout);
            mLayout = null;
        }

    }
}
