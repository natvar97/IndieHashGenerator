package com.indialone.indiehashgenerator

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.indialone.indiehashgenerator.databinding.FragmentSuccessBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SuccessFragment : Fragment() {

    private val args: SuccessFragmentArgs by navArgs()

    private var _binding: FragmentSuccessBinding? = null
    private val mBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSuccessBinding.inflate(inflater, container, false)

        Log.e("Success", args.hash)

        mBinding.tvHashValue.text = args.hash

        mBinding.btnCopy.setOnClickListener {
            copiedButtonClicked()
        }


        return mBinding.root
    }

    private fun copiedButtonClicked() {
        lifecycleScope.launch {
            copyToClipBoard(args.hash)
            applyAnimations()
        }
    }

    private fun copyToClipBoard(hash: String) {
        val clipBoardManager = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("Encrypted Text", hash)
        clipBoardManager.setPrimaryClip(clipData)
    }

    private suspend fun applyAnimations() {
        mBinding.include.copiedBackground.animate().translationY(80f).duration = 200L
        mBinding.include.tvCopied.animate().translationY(80f).duration = 200L

        delay(2000)

        mBinding.include.copiedBackground.animate().translationY(-80f).duration = 500L
        mBinding.include.tvCopied.animate().translationY(-80f).duration = 500L

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}