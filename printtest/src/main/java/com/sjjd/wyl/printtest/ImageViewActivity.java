package com.sjjd.wyl.printtest;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ImageViewActivity extends AppCompatActivity {

    @BindView(R.id.rlImgRoot)
    RelativeLayout mRlImgRoot;
    @BindView(R.id.btnPrintView)
    Button mBtnPrintView;

    String src_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "img_src.pdf";
    String dest_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "img_dest.pdf";

    @BindView(R.id.rlBase)
    RelativeLayout mRlBase;


    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);
        ButterKnife.bind(this);
        mContext = this;
    }

    public void printView(int page) {
        PdfUtil.showDialog(mContext, mRlBase, "正在生成文件......");
        if (mRlImgRoot != null) {
            Bitmap bitmap = convertViewToBitmap(mRlImgRoot);
            //  final File mFile = FileUtils.saveFile(bitmap);
            mRlImgRoot.destroyDrawingCache();//释放缓存占用的资源


            File src = new File(src_path);
            if (src.exists()) {
                src.delete();
            }
            File dest = new File(dest_path);
            if (dest.exists()) {
                dest.delete();
            }
            Document document = new Document(PageSize.A4.rotate());
            PdfWriter writer = null;
            try {
                writer = PdfWriter.getInstance(document, new FileOutputStream(src));
                MyFooter event = new MyFooter();
                writer.setPageEvent(event);
                document.open();
                for (int i = 1; i <= page; i++) {
                    //设置字体，支持中文
                    Font font = FontFactory.getFont(PdfUtil.FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

                    document.add(new Paragraph("这是第" + i + "页的数据!", font));


                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    Image img = Image.getInstance(stream.toByteArray());
                    img.scaleToFit(770, 523);
                    float offsetX = (770 - img.getScaledWidth()) / 2;
                    float offsetY = (523 - img.getScaledHeight()) / 2;
                    img.setAbsolutePosition(36 + offsetX, 36 + offsetY);
                    document.add(img);
                    document.newPage();
                }

                document.close();

                PdfUtil.write(src_path, dest);
                PdfUtil.showDialog(mContext, mRlBase, "开始打印......");

                PdfUtil.print(dest_path);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        PdfUtil.hideDialog(mRlBase);
                    }
                }, 1000);
            } catch (DocumentException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    class MyFooter extends PdfPageEventHelper {
        Font font = FontFactory.getFont(PdfUtil.FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

        public void onEndPage(PdfWriter writer, Document document) {
            PdfContentByte cb = writer.getDirectContent();
            Phrase header = new Phrase("这是页眉", font);
            Phrase footer = new Phrase("这是页脚", font);
            ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
                    header,
                    (document.right() - document.left()) / 2 + document.leftMargin(),
                    document.top() + 10, 0);
            ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
                    footer,
                    (document.right() - document.left()) / 2 + document.leftMargin(),
                    document.bottom() - 10, 0);
        }
    }


    public static Bitmap convertViewToBitmap(View view) {

        view.setDrawingCacheEnabled(true);
        Bitmap mCache = view.getDrawingCache();
        Bitmap obmp = Bitmap.createBitmap(mCache);
        view.setDrawingCacheEnabled(false);
        return obmp;
    }


    @OnClick({R.id.btnPrintView, R.id.btnPrintView3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnPrintView:
                printView(1);
                break;
            case R.id.btnPrintView3:
                printView(2);
                break;
        }
    }
}
