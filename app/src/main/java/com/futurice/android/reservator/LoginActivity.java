package com.futurice.android.reservator;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import android.content.DialogInterface;

import com.futurice.android.reservator.common.PreferenceManager;
import com.futurice.android.reservator.model.AddressBook;
import com.futurice.android.reservator.model.AddressBookUpdatedListener;
import com.futurice.android.reservator.model.ReservatorException;

public class LoginActivity extends ReservatorActivity implements AddressBookUpdatedListener {

    private boolean addressBookOk = false;
    private boolean roomListOk = false;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (PreferenceManager.getInstance(this).getApplicationConfigured() == false)
        {
            showWizard(null);
            return;
        }

        setContentView(R.layout.login_activity);

        // Check Google Calendar
        if (getResApplication().getDataProxy().hasFatalError()) {

            showWizard(getString(R.string.noCalendarsError));
            return;
        } else {
            roomListOk = true;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        AddressBook ab = this.getResApplication().getAddressBook();
        ab.addDataUpdatedListener(this);
        ab.refetchEntries();
        checkAndGo();
    }

    public void onPause() {
        super.onPause();

        AddressBook ab = this.getResApplication().getAddressBook();
        ab.removeDataUpdatedListener(this);
    }


    private void checkAndGo() {
        if (addressBookOk && roomListOk) {

            final Intent i = new Intent(this, LobbyActivity.class);
            startActivity(i);
        }
    }

    @Override
    public void addressBookUpdated() {
        addressBookOk = true;
        checkAndGo();
    }

    @Override
    public void addressBookUpdateFailed(ReservatorException e) {
        addressBookOk = false;

        // show error message and return to config screen
        showWizard(e.getMessage());
    }

    private void showWizard(String errorMessage)
    {
        final Intent i = new Intent(this, WizardActivity.class);

        if(errorMessage == null)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(errorMessage)
                    .setTitle(R.string.calendarError)
                    .setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            startActivity(i);
                        }
                    });
            builder.create().show();
        }

        startActivity(i);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        finish();
    }
}