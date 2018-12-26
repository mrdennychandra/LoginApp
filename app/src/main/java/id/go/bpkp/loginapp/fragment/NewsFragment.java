package id.go.bpkp.loginapp.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import id.go.bpkp.loginapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment extends Fragment {


    WebView webview;

    public NewsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_news, container, false);
        webview = (WebView) view.findViewById(R.id.webview);
        webview.loadUrl("http://www.bpkp.go.id/berita.bpkp");
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setWebViewClient(new MyWebViewClient());
        return view;
    }

    //To keep page navigation within the WebView and hence within the app
    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, String url) {
            return false;
        }
    }

}
