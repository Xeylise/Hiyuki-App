package com.example.firstapp;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private LinearLayout contenedorEcualizador;
    private LinearLayout contenidoInicial;
    private LinearLayout mensajeBienvenida;

    private Button btnComenzar;
    private Button btnVolverInicio;

    private final ArrayList<ObjectAnimator> animaciones =
            new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contenidoInicial = findViewById(R.id.initialContent);
        mensajeBienvenida = findViewById(R.id.welcomeMessage);

        btnComenzar = findViewById(R.id.btnMostrarMensaje);
        btnVolverInicio = findViewById(R.id.btnVolverInicio);

        // Mantener el diseño rojo de los botones
        configurarBoton(btnComenzar);
        configurarBoton(btnVolverInicio);

        iniciarEcualizador();

        btnComenzar.setOnClickListener(v -> mostrarBienvenida());

        btnVolverInicio.setOnClickListener(v -> volverAlInicio());
    }

    private void configurarBoton(Button boton) {
        boton.setBackgroundTintList(null);
        boton.setBackgroundResource(R.drawable.button_crimson);
        boton.setStateListAnimator(null);
    }

    private void iniciarEcualizador() {

        contenedorEcualizador =
                findViewById(R.id.soundContainer);

        contenedorEcualizador.removeAllViews();
        animaciones.clear();

        int[] alturas = {18, 30, 23, 38, 26, 34, 19};
        int[] duraciones = {550, 720, 480, 680, 520, 760, 590};
        int[] retrasos = {0, 180, 90, 260, 140, 320, 60};

        for (int i = 0; i < alturas.length; i++) {

            View barra = new View(this);

            int alturaPx = dp(alturas[i]);

            barra.setBackgroundResource(R.drawable.equalizer_bar);

            LinearLayout.LayoutParams parametros =
                    new LinearLayout.LayoutParams(dp(5), alturaPx);

            parametros.setMargins(dp(3), 0, dp(3), 0);

            contenedorEcualizador.addView(barra, parametros);

            barra.setPivotY(alturaPx);

            ObjectAnimator animacion = ObjectAnimator.ofFloat(
                    barra,
                    View.SCALE_Y,
                    0.25f,
                    1.0f
            );

            animacion.setDuration(duraciones[i]);
            animacion.setStartDelay(retrasos[i]);
            animacion.setRepeatCount(ValueAnimator.INFINITE);
            animacion.setRepeatMode(ValueAnimator.REVERSE);

            animaciones.add(animacion);
            animacion.start();
        }
    }

    private void mostrarBienvenida() {

        btnComenzar.setEnabled(false);

        btnComenzar.animate()
                .scaleX(0.94f)
                .scaleY(0.94f)
                .setDuration(100)
                .withEndAction(() -> {

                    btnComenzar.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(120)
                            .start();

                    contenidoInicial.animate()
                            .alpha(0f)
                            .setDuration(350)
                            .withEndAction(() -> {

                                contenidoInicial.setVisibility(View.GONE);

                                detenerEcualizador();

                                mensajeBienvenida.setAlpha(0f);
                                mensajeBienvenida.setVisibility(View.VISIBLE);

                                mensajeBienvenida.animate()
                                        .alpha(1f)
                                        .setDuration(650)
                                        .setInterpolator(
                                                new AccelerateDecelerateInterpolator()
                                        )
                                        .start();
                            })
                            .start();
                })
                .start();
    }

    private void volverAlInicio() {

        btnVolverInicio.setEnabled(false);

        btnVolverInicio.animate()
                .scaleX(0.94f)
                .scaleY(0.94f)
                .setDuration(100)
                .withEndAction(() -> {

                    mensajeBienvenida.animate()
                            .alpha(0f)
                            .setDuration(350)
                            .withEndAction(() -> {

                                mensajeBienvenida.setVisibility(View.GONE);

                                contenidoInicial.setAlpha(0f);
                                contenidoInicial.setVisibility(View.VISIBLE);

                                btnComenzar.setEnabled(true);
                                btnComenzar.setScaleX(1f);
                                btnComenzar.setScaleY(1f);

                                iniciarEcualizador();

                                contenidoInicial.animate()
                                        .alpha(1f)
                                        .setDuration(650)
                                        .setInterpolator(
                                                new AccelerateDecelerateInterpolator()
                                        )
                                        .start();

                                btnVolverInicio.setScaleX(1f);
                                btnVolverInicio.setScaleY(1f);
                                btnVolverInicio.setEnabled(true);
                            })
                            .start();
                })
                .start();
    }

    private void detenerEcualizador() {

        for (ObjectAnimator animacion : animaciones) {
            animacion.cancel();
        }

        animaciones.clear();
    }

    private int dp(float valor) {
        return (int) (
                valor * getResources()
                        .getDisplayMetrics().density + 0.5f
        );
    }

    @Override
    protected void onDestroy() {

        detenerEcualizador();

        super.onDestroy();
    }
}



