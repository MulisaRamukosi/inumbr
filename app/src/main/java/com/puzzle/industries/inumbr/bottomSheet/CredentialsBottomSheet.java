package com.puzzle.industries.inumbr.bottomSheet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.puzzle.industries.inumbr.databinding.BottomSheetCredentialsBinding;
import com.puzzle.industries.inumbr.repositores.CredentialsRepository;

import java.util.Objects;

public class CredentialsBottomSheet extends BottomSheetDialogFragment {

    private BottomSheetCredentialsBinding mBinding;
    private final CredentialsRepository CREDENTIALS_REPO = CredentialsRepository.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = BottomSheetCredentialsBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Objects.requireNonNull(mBinding.tilMobileNumber.getEditText()).setText(CREDENTIALS_REPO.getPhone());
        Objects.requireNonNull(mBinding.tilPassword.getEditText()).setText(CREDENTIALS_REPO.getPassword());

        mBinding.btnSetCredentials.setOnClickListener(v -> {

            String phone = mBinding.tilMobileNumber.getEditText().getText().toString().trim();
            String password = mBinding.tilPassword.getEditText().getText().toString().trim();

            CREDENTIALS_REPO.setCredentials(phone, password);

            Toast.makeText(view.getContext(), "Credentials set successfully", Toast.LENGTH_SHORT).show();
            Objects.requireNonNull(getDialog()).dismiss();
        });
    }
}
