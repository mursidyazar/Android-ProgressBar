package org.kod5.android;

import android.app.Activity;
import android.content.res.Resources.Theme;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private ProgressBar progressBar;
	private TextView textView;
	private Handler handler = new Handler();
	private Button baslat;
	private Button durdur;
	private Button sifirla;
	private int progressStatus = 0; 
	private boolean suspended = false;//Durdur butonuna basıldığında bu değeri true yapacağız.
	private boolean stopped = false;//Sıfırla butonuna basıldığında bu değeri true yapacağız.

	@Override
	// Bu metod uygulama açıldığında çalıştırılan metod.
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Bileşenleri eşleştiriyoruz.
		progressBar = (ProgressBar) findViewById(R.id.progressBar1);
		textView = (TextView) findViewById(R.id.textView2);
		baslat = (Button) findViewById(R.id.button1);
		durdur = (Button) findViewById(R.id.button2);
		sifirla = (Button) findViewById(R.id.button3);

		progressBar.setMax(60); //ProgressBar'ın Max değerini belirliyoruz.
		progressBar.setIndeterminate(false); //ProgressBar'ın tekrar eden bir animasyon ile çalışmasını önlüyoruz.

		initValues();

		baslat.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				setKronometre().start();
				stopped=false;
				baslat.setEnabled(false);
				durdur.setEnabled(true);
				sifirla.setEnabled(true);
			}
		});
		durdur.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (suspended) {
					suspended = false;
					durdur.setText("Durdur");
				} else {
					suspended = true;
					durdur.setText("Devam Et");
				}
			}
		});
		sifirla.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				stopped=true;
				initValues();
			}
		});

	}
	
	private void initValues() { //Başlangıç değerlerini set ediyoruz.
		progressStatus=0;
		progressBar.setProgress(progressStatus);
		textView.setText("0sn / 60sn");
		baslat.setEnabled(true);
		durdur.setEnabled(false);
		sifirla.setEnabled(false);
		durdur.setText("Durdur");
		suspended=false;
	}
	
	private Thread setKronometre(){
		return new Thread(new Runnable() {
			public void run() {
					while (progressStatus < 60) {
						while (suspended) { //Eğer kronometre durdurulduysa bekle
							try {
								Thread.sleep(300);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						if(stopped) // Eğer kronometre sıfırlandıysa işlemi sonlandır
							break;
						progressStatus += 1;
						// yeni değeri ekranda göster ve progressBar'a set et.
						handler.post(new Runnable() {
							public void run() {
								progressBar.setProgress(progressStatus);
								textView.setText(progressStatus + "sn / "
										+ progressBar.getMax()+"sn");
							}
						});
						
						try {
							// Sleep for 1 second.
							// Just to display the progress slowly
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
			}
		});
	}
}
