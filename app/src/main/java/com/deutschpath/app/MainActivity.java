package com.deutschpath.app;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import androidx.webkit.WebViewAssetLoader;
import java.util.ArrayList;
import java.util.Locale;
import org.json.JSONObject;

public class MainActivity extends Activity {
    private static final int REQ_AUDIO = 77;
    private WebView webView;
    private WebViewAssetLoader assetLoader;
    private TextToSpeech tts;
    private boolean ttsReady = false;
    private SpeechRecognizer sr;
    private boolean pendingStt = false;

    private class Bridge {
        @JavascriptInterface
        public void speak(final String text, final double rate) {
            runOnUiThread(new Runnable() {
                public void run() {
                    if (tts != null && ttsReady) {
                        tts.setSpeechRate((float) rate);
                        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "dp");
                    } else {
                        Toast.makeText(MainActivity.this, "Almanca ses paketi yok: Ayarlar > Metin okuma bölümünden Almanca sesi yükleyin.", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    private class SttBridge {
        @JavascriptInterface
        public void start() {
            runOnUiThread(new Runnable() {
                public void run() {
                    startStt();
                }
            });
        }
    }

    private void sendStt(final String text, final String err) {
        runOnUiThread(new Runnable() {
            public void run() {
                if (webView != null) {
                    webView.evaluateJavascript("window.onSTT&&window.onSTT(" + JSONObject.quote(text) + "," + JSONObject.quote(err) + ")", null);
                }
            }
        });
    }

    private void startStt() {
        if (checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            pendingStt = true;
            requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, REQ_AUDIO);
            return;
        }
        if (!SpeechRecognizer.isRecognitionAvailable(this)) {
            sendStt("", "unavailable");
            return;
        }
        if (sr != null) {
            sr.destroy();
        }
        sr = SpeechRecognizer.createSpeechRecognizer(this);
        sr.setRecognitionListener(new RecognitionListener() {
            public void onReadyForSpeech(Bundle params) {
                Toast.makeText(MainActivity.this, "Dinliyorum... Almanca konuşun", Toast.LENGTH_SHORT).show();
            }
            public void onBeginningOfSpeech() {}
            public void onRmsChanged(float rmsdB) {}
            public void onBufferReceived(byte[] buffer) {}
            public void onEndOfSpeech() {}
            public void onError(int error) {
                sendStt("", "error" + error);
            }
            public void onResults(Bundle results) {
                ArrayList<String> list = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (list != null && !list.isEmpty()) {
                    sendStt(list.get(0), "");
                } else {
                    sendStt("", "empty");
                }
            }
            public void onPartialResults(Bundle partialResults) {}
            public void onEvent(int eventType, Bundle params) {}
        });
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "de-DE");
        i.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, false);
        sr.startListening(i);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQ_AUDIO) {
            boolean ok = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
            if (ok && pendingStt) {
                startStt();
            } else {
                sendStt("", "denied");
            }
            pendingStt = false;
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assetLoader = new WebViewAssetLoader.Builder()
                .addPathHandler("/assets/", new WebViewAssetLoader.AssetsPathHandler(this))
                .build();
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int r = tts.setLanguage(Locale.GERMANY);
                    ttsReady = r != TextToSpeech.LANG_MISSING_DATA && r != TextToSpeech.LANG_NOT_SUPPORTED;
                }
            }
        });
        webView = new WebView(this);
        webView.addJavascriptInterface(new Bridge(), "AndroidTTS");
        webView.addJavascriptInterface(new SttBridge(), "AndroidSTT");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);
        webView.getSettings().setAllowFileAccess(false);
        webView.getSettings().setAllowContentAccess(false);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public android.webkit.WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                return assetLoader.shouldInterceptRequest(request.getUrl());
            }
        });
        setContentView(webView);
        webView.loadUrl("https://appassets.androidplatform.net/assets/index.html");
    }

    @Override
    protected void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        if (sr != null) {
            sr.destroy();
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (webView != null && webView.canGoBack()) webView.goBack();
        else super.onBackPressed();
    }
}
