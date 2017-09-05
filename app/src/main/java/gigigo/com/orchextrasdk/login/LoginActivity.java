/*
 * Created by Orchextra
 *
 * Copyright (C) 2017 Gigigo Mobile Services SL
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package gigigo.com.orchextrasdk.login;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.gigigo.orchextra.core.Orchextra;
import com.gigigo.orchextra.core.OrchextraErrorListener;
import com.gigigo.orchextra.core.OrchextraStatusListener;
import com.gigigo.orchextra.core.domain.entities.Error;
import com.gigigo.orchextra.geofence.OxGeofenceImp;
import com.gigigo.orchextra.indoorpositioning.OxIndoorPositioningImp;
import com.gigigo.orchextra.scanner.OxScannerImp;
import gigigo.com.orchextrasdk.R;
import gigigo.com.orchextrasdk.demo.MainActivity;
import gigigo.com.orchextrasdk.settings.CredentialsPreferenceManager;
import gigigo.com.orchextrasdk.settings.SettingsActivity;
import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class LoginActivity extends AppCompatActivity implements LoginView{

  private static final String TAG = "LoginActivity";
  private static final int PERMISSIONS_REQUEST_LOCATION = 1;
  private Orchextra orchextra;

  private TextView apiKeyTextView;
  private TextView apiSecretTextView;
  private Spinner projectSpinner;
  private ArrayAdapter<CharSequence> projectsArray;
  private Button startButton;

  private LoginPresenter loginPresenter;

  private OrchextraStatusListener orchextraStatusListener = new OrchextraStatusListener() {
    @Override public void onStatusChange(boolean isReady) {
      if (isReady) {
        orchextra.getTriggerManager().setScanner(OxScannerImp.Factory.create(LoginActivity.this));
        orchextra.getTriggerManager().setGeofence(OxGeofenceImp.Factory.create(LoginActivity.this));
        orchextra.getTriggerManager()
            .setIndoorPositioning(OxIndoorPositioningImp.Factory.create(getApplication()));

        Toast.makeText(LoginActivity.this, "SDK ready", Toast.LENGTH_SHORT).show();
        MainActivity.open(LoginActivity.this);
        finish();
      } else {
        Toast.makeText(LoginActivity.this, "SDK finished", Toast.LENGTH_SHORT).show();
      }
    }
  };

  public static void open(Context context) {
    Intent intent = new Intent(context, LoginActivity.class);
    context.startActivity(intent);
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);

    loginPresenter = new LoginPresenter(this, new CredentialsPreferenceManager(this));

    initView();
  }

  private void initView() {
    initToolbar();

    initSpinner();

    apiKeyTextView = (TextView) findViewById(R.id.apiKey_editText);
    apiSecretTextView = (TextView) findViewById(R.id.apiSecret_editText);

    startButton = (Button) findViewById(R.id.start_button);
    startButton.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        loginPresenter.doLogin();
      }
    });

    loginPresenter.uiReady();
  }


  @Override
  public void createOrchextra() {
    orchextra = Orchextra.INSTANCE;
    orchextra.setErrorListener(new OrchextraErrorListener() {
      @Override public void onError(@NonNull Error error) {
        Log.e(TAG, error.toString());
        Toast.makeText(LoginActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT)
            .show();
      }
    });
  }

  @Override
  public void initOrchextra(String apiKey, String apiSecret) {
    orchextra.init(getApplication(), apiKey, apiSecret, orchextraStatusListener);
  }

  @Override
  public void showCredentialsError(String message) {
    Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT)
        .show();
  }

  private void initToolbar() {
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

    setSupportActionBar(toolbar);
    if (getSupportActionBar() != null) {
      getSupportActionBar().setDisplayHomeAsUpEnabled(false);
      getSupportActionBar().setDisplayShowHomeEnabled(false);
    }
  }

  private void initSpinner() {
    projectSpinner = (Spinner) findViewById(R.id.projects_spinner);

    projectsArray =
        ArrayAdapter.createFromResource(this, R.array.projects_array, R.layout.simple_spinner_item);

    List<String> projects = loginPresenter.readDefaultProjects(projectsArray);

    projectSpinner.setAdapter(
        new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, projects));

    projectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String project = projectsArray.getItem(position).toString();
        loginPresenter.projectSelected(project);
      }

      @Override public void onNothingSelected(AdapterView<?> parent) {

      }
    });

    projectSpinner.setSelection(0);
  }

  @Override
  public void showProjectCredentials(String apiKey, String apiSecret) {
    apiKeyTextView.setText(apiKey);
    apiSecretTextView.setText(apiSecret);
  }

  @Override
  public void enableLogin(boolean enabled) {
    startButton.setEnabled(enabled);
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_settings, menu);
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {

    if (item.getItemId() == R.id.action_settings) {
      SettingsActivity.open(this);
      return true;
    } else {
      return super.onOptionsItemSelected(item);
    }
  }


  @Override
  public boolean checkPermissions() {
    return ContextCompat.checkSelfPermission(LoginActivity.this, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
  }

  @Override
  public void requestPermission() {
    if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED) {

      if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
        Toast.makeText(this, "Explanation!!!", Toast.LENGTH_SHORT).show();
      } else {
        ActivityCompat.requestPermissions(this, new String[] { ACCESS_FINE_LOCATION },
            PERMISSIONS_REQUEST_LOCATION);
      }
    }
  }

  @Override public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
      @NonNull int[] grantResults) {

    switch (requestCode) {
      case PERMISSIONS_REQUEST_LOCATION: {
        boolean granted = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
        loginPresenter.permissionGranted(granted);
      }
    }
  }
}