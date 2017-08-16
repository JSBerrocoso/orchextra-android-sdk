package gigigo.com.orchextrasdk;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.gigigo.orchextra.core.Orchextra;
import com.gigigo.orchextra.core.OrchextraErrorListener;
import com.gigigo.orchextra.core.OrchextraStatusListener;
import com.gigigo.orchextra.core.domain.entities.Error;
import com.gigigo.orchextra.geofence.OxGeofenceImp;
import com.gigigo.orchextra.scanner.OxScannerImp;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MainActivity extends AppCompatActivity {

  private static final String TAG = "MainActivity";
  private static final int PERMISSIONS_REQUEST_LOCATION = 1;
  public static String API_KEY = "34a4654b9804eab82aae05b2a5f949eb2a9f412c";
  public static String API_SECRET = "2d5bce79e3e6e9cabf6d7b040d84519197dc22f3";
  private Orchextra orchextra;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    orchextra = Orchextra.INSTANCE;
    orchextra.setErrorListener(new OrchextraErrorListener() {
      @Override public void onError(@NonNull Error error) {
        Log.e(TAG, error.toString());
        Toast.makeText(MainActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT)
            .show();
      }
    });

    initView();
  }

  private void initView() {
    Button initButton = (Button) findViewById(R.id.init_button);
    initButton.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {

        if (ContextCompat.checkSelfPermission(MainActivity.this, ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
          orchextra.init(MainActivity.this, API_KEY, API_SECRET, orchextraStatusListener);
        } else {
          requestPermission();
        }
      }
    });

    Button browserButton = (Button) findViewById(R.id.browser_button);
    browserButton.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (orchextra.isReady()) {
          orchextra.openBrowser("https://www.google.es");
        } else {
          Toast.makeText(MainActivity.this, "SDK sin inicializar", Toast.LENGTH_SHORT).show();
        }
      }
    });

    Button webviewButton = (Button) findViewById(R.id.webview_button);
    webviewButton.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (orchextra.isReady()) {
          orchextra.openWebView("https://www.google.es");
        } else {
          Toast.makeText(MainActivity.this, "SDK sin inicializar", Toast.LENGTH_SHORT).show();
        }
      }
    });

    Button scannerButton = (Button) findViewById(R.id.scanner_button);
    scannerButton.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (orchextra.isReady()) {
          orchextra.openScanner();
        } else {
          Toast.makeText(MainActivity.this, "SDK sin inicializar", Toast.LENGTH_SHORT).show();
        }
      }
    });
  }

  private void requestPermission() {
    if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED) {

      if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
        Toast.makeText(this, "Expanation!!!", Toast.LENGTH_SHORT).show();
      } else {
        ActivityCompat.requestPermissions(this, new String[] { ACCESS_FINE_LOCATION },
            PERMISSIONS_REQUEST_LOCATION);
      }
    }
  }

  @Override public void onRequestPermissionsResult(int requestCode, String permissions[],
      int[] grantResults) {

    switch (requestCode) {
      case PERMISSIONS_REQUEST_LOCATION: {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
          orchextra.init(MainActivity.this, API_KEY, API_SECRET, orchextraStatusListener);
        } else {
          Toast.makeText(MainActivity.this, "Lo necesitamos!!!", Toast.LENGTH_SHORT).show();
        }
      }
    }
  }

  private OrchextraStatusListener orchextraStatusListener = new OrchextraStatusListener() {
    @Override public void onStatusChange(boolean isReady) {
      if (isReady) {
        orchextra.getTriggerManager().setScanner(OxScannerImp.Factory.create());
        orchextra.getTriggerManager().setGeofence(OxGeofenceImp.Factory.create());

        Toast.makeText(MainActivity.this, "SDK ready", Toast.LENGTH_SHORT).show();
      } else {
        Toast.makeText(MainActivity.this, "SDK finished", Toast.LENGTH_SHORT).show();
      }
    }
  };
}
