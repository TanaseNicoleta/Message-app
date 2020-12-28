package com.example.messageapp.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.messageapp.R;

public class EditBankDialog extends AppCompatDialogFragment {
    private EditText etComision;
    private EditBankDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_edit_bank_commission_dialog, null);

        builder.setView(view).setTitle(R.string.dialog_edit_commission)
                .setNegativeButton(R.string.renunta, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
        .setPositiveButton(R.string.salveaza, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                float comision = Float.parseFloat(etComision.getText().toString());
            }
        });

        etComision = view.findViewById(R.id.input_edit_commission);
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (EditBankDialogListener) context;
    }

    public interface EditBankDialogListener {
        void applyTexts(float comisison);
    }
}
