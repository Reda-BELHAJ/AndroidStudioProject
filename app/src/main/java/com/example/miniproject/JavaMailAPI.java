package com.example.miniproject;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class JavaMailAPI extends AsyncTask<Void,Void,Void>  {


    //Variables
    private Context mContext;
    private Session mSession;

    private String mEmail;
    private String mSubject;
    private String mMessage;

    private ProgressDialog mProgressDialog;

    //Constructor
    public JavaMailAPI(Context mContext, String mEmail, String mSubject, String mMessage) {
        this.mContext = mContext;
        this.mEmail = mEmail;
        this.mSubject = mSubject;
        this.mMessage = mMessage;
    }

    private void dismissProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //Show progress dialog while sending email
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(mContext);
            mProgressDialog.setOwnerActivity( (Activity) mContext);
            mProgressDialog.setTitle("Sending message");
            mProgressDialog.setMessage("Loading. Please wait...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setCancelable(false);
        }

        mProgressDialog.show();
        mProgressDialog.dismiss();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        //Dismiss progress dialog when message successfully send

        Activity activity = (Activity) mContext;

        if (activity.isDestroyed()) {
            return;
        }

        dismissProgressDialog();
    }

    @Override
    protected Void doInBackground(Void... params) {
        //Creating properties
        Properties props = new Properties();

        //Configuring properties for gmail
        //If you are not using gmail you may need to change the values
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        //Creating a new session
        mSession = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    //Authenticating the password
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(Utils.Email, Utils.Password);
                    }
                });

        try {
            //Creating MimeMessage object
            MimeMessage mm = new MimeMessage(mSession);

            //Setting sender address
            mm.setFrom(new InternetAddress(Utils.Email));
            //Adding receiver
            mm.addRecipient(Message.RecipientType.TO, new InternetAddress(mEmail));
            //Adding subject
            mm.setSubject(mSubject);
            //Adding message
            mm.setText(mMessage);
            //Sending email
            Transport.send(mm);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return null;
    }


}
