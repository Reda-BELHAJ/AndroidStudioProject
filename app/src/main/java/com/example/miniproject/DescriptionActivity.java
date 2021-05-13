package com.example.miniproject;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.miniproject.model.Mission;
import com.example.miniproject.model.User;
import com.google.android.material.textfield.TextInputEditText;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import io.realm.Realm;

public class DescriptionActivity extends AppCompatActivity {

    ImageView backarrow_icon;

    String username, role;
    int missionId;

    Realm realm;

    TextInputEditText nomMission, type, description, debut, fin, typeTransport, adresse;

    Button download;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);
        realm = Realm.getDefaultInstance();

        username = getIntent().getExtras().getString("USER_NAME");
        role = getIntent().getExtras().getString("ROLE");
        missionId = getIntent().getExtras().getInt("missionId");

        backarrow_icon = findViewById(R.id.backarrow);
        fin = findViewById(R.id.fin);
        debut = findViewById(R.id.debut);
        description = findViewById(R.id.description);
        nomMission = findViewById(R.id.nomMission);
        adresse = findViewById(R.id.adresse);
        type = findViewById(R.id.type);
        typeTransport = findViewById(R.id.typeTransport);
        download = findViewById(R.id.download);

        backarrow_icon.setOnClickListener(v -> {
            Intent intent = new Intent(DescriptionActivity.this, MissionActivity.class);
            intent.putExtra("USER_NAME", username);
            intent.putExtra("ROLE", role);
            DescriptionActivity.this.startActivity(intent);
            overridePendingTransition(R.anim.slide_left, R.anim.slide_out_right);
            finish();
        });

        if(!getMission(missionId).getEtat().equals("finish")){
            download.setVisibility(View.GONE);
        }

        download.setOnClickListener(v -> {
            try {
                createPdf(getMission(missionId).getMissionName(),
                        getMission(missionId).getTypeTransport(),
                        getMission(missionId).getAdresse(),
                        getMission(missionId).getTypeTransport(),
                        getMission(missionId).getFinMission().toString(),
                        getMission(missionId).getDebutMission().toString()
                        );
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });

        missionField(missionId);
    }

    public void missionField(int missionId){
        Mission realmObjects = getMission(missionId);

        fin.setText(realmObjects.getFinMission().toString());
        debut.setText(realmObjects.getDebutMission().toString());
        description.setText(realmObjects.getDescription());
        nomMission.setText(realmObjects.getMissionName());
        adresse.setText(realmObjects.getAdresse());
        type.setText(realmObjects.getMissionType());
        typeTransport.setText(realmObjects.getTypeTransport());
    }

    public Mission getMission(int missionId) {
        Mission realmObjects = realm.where(Mission.class).equalTo("missionId", missionId).findFirst();
        return realmObjects;
    }

    public void createPdf(String NameMission, String type, String address, String transport, String finish, String start) throws FileNotFoundException {
        String pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        File file = new File(pdfPath, "myMission.pdf"+NameMission);
        OutputStream outputStream = new FileOutputStream(file);

        PdfWriter pdfWriter = new PdfWriter(file);
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        Document document = new Document(pdfDocument);

        float colomnWidth[] = {112, 112, 112, 112, 112};
        Table table = new Table(colomnWidth);
        table.setMarginTop(50);

        Drawable img = getDrawable(R.drawable.signature);
        Bitmap bm = ((BitmapDrawable)img).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        bm.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] biteMap = stream.toByteArray();

        ImageData imageData = ImageDataFactory.create(biteMap);
        Image image = new Image(imageData);
        image.setFixedPosition(250, 80);
        image.setHeight(120);

        // Row1
        table.addCell(new Cell(1,2).add(new Paragraph("International University of Rabat").setFontSize(20f).setBold().setFontColor(new DeviceRgb(13, 20, 145))).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));

        // Row2
        table.addCell(new Cell().add(new Paragraph("Université Internationale\n"+
                " de Rabat Technopolis \n"+
                "Rabat-Shore Rocade Rabat-Salé")).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("+212 (0)5 30 10 30 00\n"+
                "contact@uir.ac.ma")).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));

        // Empty
        table.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));

        // Row3
        table.addCell(new Cell(2,1).add(new Paragraph("Mission")).setFontColor(new DeviceRgb(13, 20, 145)).setFontSize(24).setBold().setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("Name")).setFontColor(ColorConstants.WHITE).setBackgroundColor(new DeviceRgb(13, 20, 145)).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("Type")).setFontColor(ColorConstants.WHITE).setBackgroundColor(new DeviceRgb(13, 20, 145)).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("Address")).setFontColor(ColorConstants.WHITE).setBackgroundColor(new DeviceRgb(13, 20, 145)).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("Transport")).setFontColor(ColorConstants.WHITE).setBackgroundColor(new DeviceRgb(13, 20, 145)).setBorder(Border.NO_BORDER));

        // Empty
        table.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));

        // Row4
        table.addCell(new Cell().add(new Paragraph("Starting Date\n"+start)).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph(NameMission)).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph(type)).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph(address)).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph(transport)).setBorder(Border.NO_BORDER));

        // Row5
        table.addCell(new Cell().add(new Paragraph("Finishing Date\n"+finish)).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));

        document.add(table);
        document.add(image);

        Paragraph para = new Paragraph("TERMS\n"+
                "Copyright 2021 UIR.\n"+
                " Tous droits réservés.");
        para.setMarginTop(200);

        document.add(para);
        document.close();
    }
}