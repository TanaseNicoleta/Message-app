package com.example.messageapp.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.PluralsRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.messageapp.ProfileActivity;
import com.example.messageapp.R;
import com.example.messageapp.util.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UpdateUserProfileDialog extends AppCompatDialogFragment {

    public static final String NUME = "nume";
    public static final String PRENUME = "prenume";
    public static final String TELEFON = "telefon";
    public static final String EMAIL = "email";
    public static final String USERS = "users";
    private String inputNume, inputPrenume, inputEmail, inputTelefon;
    private EditText etNume, etPrenume, etTelefon, etEmail;
    private User profile;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=getActivity().getLayoutInflater();
        View view=inflater.inflate(R.layout.layout_dialog_update_profile,null);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference(USERS);
        final String userId = user.getUid();

        initComponents(view);
        database.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                profile = snapshot.getValue(User.class);
                if (profile != null) {
                    String Nume = profile.getNume();
                    String Prenume = profile.getPrenume();
                    String Email = profile.getEmail();
                    String Telefon = profile.getTelefon();

                    etEmail.setText(Email);
                    etNume.setText(Nume);
                    etPrenume.setText(Prenume);
                    etTelefon.setText(Telefon);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        builder.setView(view)
                .setTitle(R.string.update_profile)
                .setNegativeButton(getString(R.string.renunta), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .setPositiveButton(R.string.salveaza, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (etNume.getText().toString() != profile.getNume() && !etNume.getText().toString().trim().isEmpty() && etNume != null)
                            database.child(userId).child(NUME).setValue(etNume.getText().toString().trim());
                        if (etPrenume.getText().toString() != profile.getNume() && !etPrenume.getText().toString().trim().isEmpty() && etPrenume != null)
                            database.child(userId).child(PRENUME).setValue(etPrenume.getText().toString().trim());
                        if(etTelefon != null && !etTelefon.getText().toString().trim().isEmpty() && Patterns.PHONE.matcher(etTelefon.getText()).matches() && etTelefon.getText().toString().trim() != profile.getTelefon())
                            database.child(userId).child(TELEFON).setValue(etTelefon.getText().toString().trim());
                        if(etEmail != null && !etEmail.getText().toString().trim().isEmpty() && Patterns.EMAIL_ADDRESS.matcher(etEmail.getText()).matches() && etEmail.getText().toString().trim() != profile.getEmail())
                        {
                            database.child(userId).child(EMAIL).setValue(etEmail.getText().toString().trim());
                            user.updateEmail(etEmail.getText().toString().trim());
                        }

                    }
                });

        return builder.create();

    }

    private void initComponents(View view) {
        etEmail = view.findViewById(R.id.input_email_edit_user_dialog);
        etNume = view.findViewById(R.id.input_nume_edit_user_dialog);
        etPrenume = view.findViewById(R.id.input_prenume_edit_user_dialog);
        etTelefon = view.findViewById(R.id.input_telefon_edit_user_dialog);
    }


}
