package com.retirementcalculator

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.retirementcalculator.databinding.FragmentCalculatorBinding

class CalculatorFragment : Fragment() {

    private var _binding: FragmentCalculatorBinding? = null
    private val binding get() = _binding!!
    private var pageLoaded = false

    companion object {
        private const val ARG_HTML_FILE = "html_file"

        fun newInstance(htmlFile: String): CalculatorFragment {
            return CalculatorFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_HTML_FILE, htmlFile)
                }
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalculatorBinding.inflate(inflater, container, false)

        val htmlFile = arguments?.getString(ARG_HTML_FILE) ?: "compound_calculator.html"

        with(binding.webView.settings) {
            javaScriptEnabled = true
            domStorageEnabled = true
            allowFileAccess = true
            cacheMode = WebSettings.LOAD_DEFAULT
            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            useWideViewPort = true
            loadWithOverviewMode = true
            builtInZoomControls = false
            displayZoomControls = false
            setSupportZoom(false)
        }

        binding.webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                binding.progressBar.visibility = View.GONE
                pageLoaded = true
                injectLanguage(view)
            }

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean = false
        }

        binding.webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                if (newProgress < 100) {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.progressBar.progress = newProgress
                } else {
                    binding.progressBar.visibility = View.GONE
                }
            }
        }

        binding.webView.loadUrl("file:///android_asset/$htmlFile")

        return binding.root
    }

    /** MainActivity에서 언어 변경 시 호출 */
    fun updateLanguage(lang: String) {
        if (pageLoaded && _binding != null) {
            binding.webView.evaluateJavascript("setLanguage('$lang')", null)
        }
    }

    private fun injectLanguage(view: WebView?) {
        val lang = LanguageManager.get(requireContext())
        view?.evaluateJavascript("setLanguage('$lang')", null)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        pageLoaded = false
        binding.webView.destroy()
        _binding = null
    }
}
