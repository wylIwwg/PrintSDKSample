package com.sjjd.wyl.printtest;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.TextField;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TemplateActivity extends AppCompatActivity {

    @BindView(R.id.btnPrint)
    Button mBtnPrint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template);
        ButterKnife.bind(this);
    }

    public class FieldChunk extends PdfPageEventHelper {
        @Override
        public void onGenericTag(PdfWriter writer, Document document, Rectangle rect, String text) {
            TextField field = new TextField(writer, rect, text);
            try {
                writer.addAnnotation(field.getTextField());
            } catch (IOException ex) {
                throw new ExceptionConverter(ex);
            } catch (DocumentException ex) {
                throw new ExceptionConverter(ex);
            }
        }
    }

    @OnClick(R.id.btnPrint)
    public void onViewClicked() {
        String state = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "template.pdf";
        String state_src = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "template_src.pdf";
        String state_dest = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "template_dest.pdf";
        Font font = FontFactory.getFont(PdfUtil.FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        File src = new File(state_src);
        File dest = new File(state_dest);
        if (src.exists())
            src.delete();

        if (dest.exists()) {
            dest.delete();
        }
        Document document = new Document();
        PdfWriter writer = null;
        try {
            writer = PdfWriter.getInstance(document, new FileOutputStream(state_src));

            writer.setPageEvent(new FieldChunk());
            document.open();
            Paragraph pName = new Paragraph("公司名称：", font);
            Chunk name = new Chunk("                   ");
            name.setGenericTag("name");
            pName.add(name);

            Paragraph pAddress = new Paragraph("公司地址：", font);
            Chunk address = new Chunk("                  ");
            address.setGenericTag("address");
            pAddress.add(address);

            Paragraph pCulture = new Paragraph("公司文化：", font);
            Chunk culture = new Chunk("                  ");
            address.setGenericTag("culture");
            pCulture.add(culture);


            document.add(pName);
            document.add(pAddress);
            document.add(pCulture);

            document.close();


            PdfUtil.write(state_src, dest);
            PdfUtil.print(state_dest);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}
