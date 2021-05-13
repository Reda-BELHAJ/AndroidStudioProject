package com.example.miniproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.miniproject.model.Mission;
import com.example.miniproject.model.User;
import com.google.android.material.textfield.TextInputEditText;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

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
                createPdf();
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

    public void createPdf() throws FileNotFoundException {
        String pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        File file = new File(pdfPath, "myMission.pdf");
        OutputStream outputStream = new FileOutputStream(file);

        PdfWriter pdfWriter = new PdfWriter(file);
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        Document document = new Document(pdfDocument);


        float colomnWidth[] = {112, 112, 112, 112, 112};
        Table table = new Table(colomnWidth);

        // Row1
        table.addCell(new Cell(1,2).add(new Paragraph("International University of Rabat")));
//        table.addCell(new Cell().add(new Paragraph("")));
        table.addCell(new Cell().add(new Paragraph("")));
        table.addCell(new Cell().add(new Paragraph("")));
        table.addCell(new Cell().add(new Paragraph("")));

        // Row2
        table.addCell(new Cell().add(new Paragraph("Université Internationale\n"+
                " de Rabat Technopolis \n"+
                "Rabat-Shore Rocade Rabat-Salé")));
        table.addCell(new Cell().add(new Paragraph("+212 (0)5 30 10 30 00\n"+
                "contact@uir.ac.ma")));
        table.addCell(new Cell().add(new Paragraph("")));
        table.addCell(new Cell().add(new Paragraph("")));

        // Empty
        table.addCell(new Cell().add(new Paragraph("")));
        table.addCell(new Cell().add(new Paragraph("")));
        table.addCell(new Cell().add(new Paragraph("")));
        table.addCell(new Cell().add(new Paragraph("")));
        table.addCell(new Cell().add(new Paragraph("")));

        // Row3
        table.addCell(new Cell(2,1).add(new Paragraph("Mission")));
        table.addCell(new Cell().add(new Paragraph("Name")));
        table.addCell(new Cell().add(new Paragraph("Type")));
        table.addCell(new Cell().add(new Paragraph("Address")));
        table.addCell(new Cell().add(new Paragraph("Transport")));

        // Row4
        table.addCell(new Cell().add(new Paragraph("Starting Date")));
        table.addCell(new Cell().add(new Paragraph("")));
        table.addCell(new Cell().add(new Paragraph("")));
        table.addCell(new Cell().add(new Paragraph("")));
        table.addCell(new Cell().add(new Paragraph("")));

        // Row5
        table.addCell(new Cell().add(new Paragraph("Finishing Date")));
        table.addCell(new Cell().add(new Paragraph("")));
        table.addCell(new Cell().add(new Paragraph("")));
        table.addCell(new Cell().add(new Paragraph("")));
        table.addCell(new Cell().add(new Paragraph("")));

        // Row6
        table.addCell(new Cell().add(new Paragraph("")));
        table.addCell(new Cell().add(new Paragraph("")));
        table.addCell(new Cell().add(new Paragraph("")));
        table.addCell(new Cell().add(new Paragraph("")));
        table.addCell(new Cell().add(new Paragraph("")));

        // Row7
        table.addCell(new Cell().add(new Paragraph("")));
        table.addCell(new Cell().add(new Paragraph("")));
        table.addCell(new Cell().add(new Paragraph("")));
        table.addCell(new Cell().add(new Paragraph("")));
        table.addCell(new Cell().add(new Paragraph("")));

        // Row8
        table.addCell(new Cell().add(new Paragraph("")));
        table.addCell(new Cell().add(new Paragraph("")));
        table.addCell(new Cell().add(new Paragraph("")));
        table.addCell(new Cell().add(new Paragraph("")));
        table.addCell(new Cell().add(new Paragraph("")));

        // Row9
        table.addCell(new Cell().add(new Paragraph("")));
        table.addCell(new Cell().add(new Paragraph("")));
        table.addCell(new Cell().add(new Paragraph("")));
        table.addCell(new Cell().add(new Paragraph("")));
        table.addCell(new Cell().add(new Paragraph("")));

        // Row10
        table.addCell(new Cell().add(new Paragraph("")));
        table.addCell(new Cell().add(new Paragraph("")));
        table.addCell(new Cell().add(new Paragraph("")));
        table.addCell(new Cell().add(new Paragraph("")));
        table.addCell(new Cell().add(new Paragraph("")));

        document.add(table);
        document.close();
    }
}