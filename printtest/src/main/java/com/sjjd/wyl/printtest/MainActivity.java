package com.sjjd.wyl.printtest;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "  MainActivity ";
    TextView mTextView;
    RelativeLayout rlroot;

    public static final String FONT = "assets/NotoSansCJKsc-Regular.otf";
    public static final String TEXT = "These are the protagonists in 'Hero', a movie by Zhang Yimou:\n"
            + "\u7121\u540d (Nameless), \u6b98\u528d (Broken Sword), "
            + "\u98db\u96ea (Flying Snow), \u5982\u6708 (Moon), "
            + "\u79e6\u738b (the King), and \u9577\u7a7a (Sky).";
    public static final String CHINESE = "\u5341\u950a\u57cb\u4f0f";
    public static final String JAPANESE = "\u8ab0\u3082\u77e5\u3089\u306a\u3044";
    public static final String KOREAN = "\ube48\uc9d1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = findViewById(R.id.tvInfo);
        rlroot = findViewById(R.id.rlRoot);
        //申请root权限
        String apkRoot = "chmod 777 " + getPackageCodePath();
        RootCommand(apkRoot);

    }

    public static boolean RootCommand(String command) {
        Process process = null;
        DataOutputStream os = null;
        try {
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(command + "\n");
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
        } catch (Exception e) {
            return false;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                process.destroy();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return true;
    }

    public void print(View view) {
        mTextView.setText(mTextView.getText() + getHandSetInfo() + "\n");
        print();

    }

    private String getHandSetInfo() {
        String handSetInfo =
                "型号:" + android.os.Build.MODEL +
                        ",SDK版本:" + android.os.Build.VERSION.SDK +
                        ",系统版本:" + android.os.Build.VERSION.RELEASE;
        return handSetInfo;
    }


    String header1 = "\u001B%-12345X@PJL SET STRINGCODESET=UTF8\n";
    String header2 = "@PJL JOB NAME=";
    String header3 =
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


    public void print() {


        File mFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/test.pdf");
        if (mFile.exists()) {
            mTextView.setText(mTextView.getText() + " filepath: " + mFile.getAbsolutePath() + "\n");
            Log.e(TAG, "print: " + mFile.getAbsolutePath() + "  canRead? " +
                    mFile.canRead() + "  canWrite? " + mFile.canWrite());
            String execStr = "cp /mnt/embsd/test.pdf /dev/usb/lp0";
            //print(mFile.getAbsolutePath());
            new ExeCommand(true).run(execStr, 5000);
            // new ExeCommand(true).run("cp /mnt/embsd/testtest.pdf /dev/usb/lp0", 10000);
            //execRootCmdSilent(execStr);
        }
    }

    public static int execRootCmdSilent(String cmd) {
        int result = -1;
        DataOutputStream dos = null;

        try {
            Process p = Runtime.getRuntime().exec("su");
            dos = new DataOutputStream(p.getOutputStream());

            Log.e(TAG, cmd);
            dos.writeBytes(cmd + "\n");
            dos.flush();
            dos.writeBytes("exit\n");
            dos.flush();
            p.waitFor();
            result = p.exitValue();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (dos != null) {
                try {
                    dos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    private void print(String filePath) {
        // String str = "cp " + filePath + " /dev/usb/lp0";
        //cp /mnt/embsd/test.pdf /dev/usb/lp0
        //cat /mnt/embsd/test.pdf > /dev/usb/lp0
        try {
            // String execStr = "cp /mnt/embsd/test.pdf /dev/usb/lp0";
            //  mTextView.setText(mTextView.getText() + " filepath: " + filePath + "\n");
            String execStr = "cp " + filePath + " /dev/usb/lp0";
            new ExeCommand(true).run(execStr, 10000);
           /* Process proc = Runtime.getRuntime().exec(execStr);
            if (proc.waitFor() != 0) {
                Log.e(TAG, "cp exit value = " + proc.exitValue());
            } else {
                Log.e(TAG, "cp value = 0");
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void createPDF() {
        //需要延迟保存  否则对布局生成的图片是空
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (rlroot != null) {
                    Bitmap bitmap = convertViewToBitmap(rlroot);
                    final File mFile = FileUtils.saveFile(bitmap);
                    rlroot.destroyDrawingCache();//释放缓存占用的资源

                    if (mFile == null)
                        return;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Document document = new Document(PageSize.A4.rotate());//创建文档对象
                            PdfWriter writer = null;
                            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + System.currentTimeMillis() + ".pdf";
                            try {
                                writer = PdfWriter.getInstance(document, new FileOutputStream(path));

                                document.open();
                                Font font = FontFactory.getFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                                document.add(new Paragraph(TEXT, font));
                                document.add(new Paragraph(CHINESE, font));
                                document.add(new Paragraph(JAPANESE, font));
                                document.add(new Paragraph(KOREAN, font));
                                document.add(new Paragraph("中文测试！", font));


                                document.close();
                                print(path);
                            } catch (DocumentException e) {
                                e.printStackTrace();
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }



                           /* Document doc = new Document();
                            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + System.currentTimeMillis() + ".pdf";
                            FileOutputStream fos;
                            try {
                                fos = new FileOutputStream(path);
                                PdfWriter.getInstance(doc, fos);
                                doc.open();

                                Image myImg = Image.getInstance(mFile.getAbsolutePath());
                                myImg.setAlignment(Image.ALIGN_CENTER);
                                //add image to document
                                doc.add(myImg);

                                doc.close();
                                fos.flush();
                                fos.close();

                            } catch (DocumentException de) {

                            } catch (IOException e) {

                            } finally {

                            }*/
                        }
                    }, 1000);

                }
            }
        }, 1000);


        // print(path);

    }

    String HEADER = "\u001B%-12345X\n" +
            "@PJL SET JOB NAME=\"PJL_TEST_\"\n" +
            "@PJL SET PAPER=A4\n" +
            "@PJL SET QTY=1\n" +
            "@PJL SET DUPLEX=OFF\n" +
            "@PJL ENTER LANGUAGE=PDF\n" +
            "%PDF-1.5";
    String FOOTER = "%%EOF\u001B%-12345X";

    public static Bitmap convertViewToBitmap(View view) {

        view.setDrawingCacheEnabled(true);
        Bitmap mCache = view.getDrawingCache();
        Bitmap obmp = Bitmap.createBitmap(mCache);
        view.setDrawingCacheEnabled(false);
        return obmp;
    }

    public void printview(View view) {
        //createPDF();

        Document document = new Document(PageSize.A4.rotate());//创建文档对象
        PdfWriter writer = null;
        long mMillis = System.currentTimeMillis();
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + mMillis + ".pdf";
        String pdf = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + mMillis + "-test.pdf";
        try {
            writer = PdfWriter.getInstance(document, new FileOutputStream(path));
            writer.setPdfVersion(PdfWriter.VERSION_1_7);
            document.open();
            Font font = FontFactory.getFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            document.add(new Paragraph(TEXT, font));
            document.add(new Paragraph(CHINESE, font));
            document.add(new Paragraph(JAPANESE, font));
            document.add(new Paragraph(KOREAN, font));
            document.add(new Paragraph("中文测试！", font));

            document.close();
            //print(path);
            write(path, new File(pdf));

        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void write(String path, File pdf) {
        String pdfPath = pdf.getAbsolutePath();
        try {
            FileInputStream fis = new FileInputStream(path);
            FileOutputStream fos = new FileOutputStream(pdf);


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

            fos.write(tail.getBytes());

            fos.flush();
            fos.close();
            fis.close();

           /* File mFile = new File(pdfPath);
            RandomAccessFile raf = new RandomAccessFile(mFile, "rw");
            raf.seek(mFile.length());

            raf.close();*/

            print(pdf.getAbsolutePath());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void printview() {
        final String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/view.pdf";
        File mFile = new File(path);
        if (mFile.exists()) {
            mFile.delete();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                Document doc = new Document();// 创建一个document对象
                FileOutputStream fos;
                try {
                    fos = new FileOutputStream(path); // pdf_address为Pdf文件保存到sd卡的路径
                    PdfWriter.getInstance(doc, fos);
                    doc.open();
                    doc.setPageCount(1);
                    doc.add(new Paragraph("123456",
                            setChineseFont())); // result为保存的字符串
                    // ,setChineseFont()为pdf字体
                    // 一定要记得关闭document对象
                    doc.close();
                    fos.flush();
                    fos.close();
                    print(path);
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                } catch (DocumentException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    /**
     * 设置PDF字体(较为耗时)
     */
    public Font setChineseFont() {

        BaseFont bfChinese = null;
        try {
            bfChinese = BaseFont.createFont("assets/simsun.ttc", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);

        } catch (DocumentException e) {
            // Do sth. here
        } catch (IOException e) {
            // Do sth. here
        }
        Font font = new Font(bfChinese, 16, Font.NORMAL);
        return font;

    }

    public void print1(View view) {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "PJLTestFile_tray3.prn";
        print(path);
    }

    public void print2(View view) {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "PJLTestFile_A4_2_Tray2_duplex.prn";
        print(path);

    }
}
