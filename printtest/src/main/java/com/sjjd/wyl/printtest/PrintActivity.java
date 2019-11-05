package com.sjjd.wyl.printtest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PrintActivity extends AppCompatActivity {

    @BindView(R.id.btnPrintExistPDF)
    Button mBtnPrintExistPDF;
    @BindView(R.id.btnCreatePrint)
    Button mBtnCreatePrint;
    @BindView(R.id.btnPrintForm)
    Button mBtnPrintForm;
    @BindView(R.id.btnPrintImg)
    Button mBtnPrintImg;


    Context mContext;

    String testPdf = Environment.getExternalStorageDirectory().getAbsolutePath() + "/test.pdf";
    SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss", Locale.CHINA);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print);
        ButterKnife.bind(this);
        mContext = this;
    }

    @OnClick({R.id.btnPrintExistPDF, R.id.btnCreatePrint, R.id.btnPrintForm, R.id.btnPrintImg, R.id.btnCreateForm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnCreateForm:
                Intent form = new Intent(this, TemplateActivity.class);
                startActivity(form);
                break;
            case R.id.btnPrintExistPDF:

                printExistPdf();
                break;
            case R.id.btnCreatePrint:
                createPrint();
                break;
            case R.id.btnPrintForm:
                formPrint();
                break;
            case R.id.btnPrintImg:
                Intent i = new Intent(this, ImageViewActivity.class);
                startActivity(i);

                break;
        }
    }

    private void formPrint() {
        String state = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "state.pdf";
        String state_src = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "state_src.pdf";
        String state_dest = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "state_dest.pdf";
        File src = new File(state_src);
        File dest = new File(state_dest);
        if (src.exists())
            src.delete();

        if (dest.exists()) {
            dest.delete();
        }
        PdfReader reader = null;
        try {
            reader = new PdfReader(state);
            PdfStamper stamper = new PdfStamper(reader,
                    new FileOutputStream(state_src));
            AcroFields fields = stamper.getAcroFields();
            fields.setField("name", "CALIFORNIA");
            fields.setField("abbr", "CA");
            fields.setField("capital", "Sacramento");
            fields.setField("city", "Los Angeles");
            fields.setField("population", "36,961,664");
            fields.setField("surface", "163,707");
            fields.setField("timezone1", "PT (UTC-8)");
            fields.setField("timezone2", "-");
            fields.setField("dst", "YES");
            stamper.close();
            reader.close();

            PdfUtil.write(state_src, dest);
            PdfUtil.print(state_dest);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }

    }

    private void printExistPdf() {
        String pdf = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "test_dest.pdf";
        File dest = new File(pdf);
        if (dest.exists()) {
            dest.delete();
        }
        PdfUtil.write(testPdf, dest);
        PdfUtil.print(pdf);

    }

    /**
     * 创建PDF文档并打印
     */
    public void createPrint() {

        Document document = new Document();//创建文档对象
        PdfWriter writer = null;
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "create_src.pdf";
        String pdf = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "create_dest.pdf";

        File src = new File(path);
        File dest = new File(pdf);
        if (src.exists())
            src.delete();

        if (dest.exists()) {
            dest.delete();
        }
        try {
            writer = PdfWriter.getInstance(document, new FileOutputStream(path));
            writer.setPdfVersion(PdfWriter.VERSION_1_7);
            document.open();
            Font font = FontFactory.getFont(PdfUtil.FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            document.add(new Paragraph("创建PDF文档并打印！", font));
            document.add(new Paragraph("生成时间：" + mSimpleDateFormat.format(new Date(System.currentTimeMillis())), font));

            document.close();
            PdfUtil.write(path, dest);
            PdfUtil.print(dest.getAbsolutePath());

        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
