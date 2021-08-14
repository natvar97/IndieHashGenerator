package com.indialone.indiehashgenerator

import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.indialone.indiehashgenerator.databinding.FragmentHomeBinding
import com.indialone.indiehashgenerator.viewmodels.HomeViewModel
import com.indialone.indiehashgenerator.viewmodels.ViewModelFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val mBinding get() = _binding!!
    private lateinit var homeViewModel: HomeViewModel

    override fun onResume() {
        super.onResume()
        val hashAlgorithms = resources.getStringArray(R.array.hash_algorithms)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, hashAlgorithms)
        mBinding.tvAutoComplete.setAdapter(arrayAdapter)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        homeViewModel = ViewModelProvider(this, ViewModelFactory()).get(HomeViewModel::class.java)

        setHasOptionsMenu(true)

        mBinding.btnGenerate.setOnClickListener {
            onGenerateClicked()
        }

        return mBinding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_menu, menu)
    }

    private suspend fun applyAnimation() {
        mBinding.btnGenerate.isClickable = false
        mBinding.tvTitle.animate().alpha(0f).duration = 400L
        mBinding.btnGenerate.animate().alpha(0f).duration = 400L
        mBinding.textInputLayout.animate()
            .alpha(0f)
            .translationXBy(1200f)
            .duration = 400L

        mBinding.plainText.animate()
            .alpha(0f)
            .translationXBy(-1200f)
            .duration = 400L

        delay(300)

        mBinding.successBackground.animate()
            .alpha(1f)
            .duration = 600L

        mBinding.successBackground.animate()
            .rotationBy(720f)
            .duration = 600L

        mBinding.successBackground.animate()
            .scaleXBy(900f)
            .duration = 800L

        mBinding.successBackground.animate()
            .scaleYBy(900f)
            .duration = 800L

        delay(500)

        mBinding.ivSuccess.animate()
            .alpha(1f)
            .duration = 1000L

        delay(1500)
    }

    private fun navigateToSuccess(hash: String) {
        val directions = HomeFragmentDirections.actionHomeFragmentToSuccessFragment(hash)
        findNavController().navigate(directions)
    }

    private fun onGenerateClicked() {
        val text = mBinding.plainText.text.toString()

        if (TextUtils.isEmpty(text)) {
            showSnackBar("Field is empty")
        } else {
            lifecycleScope.launch {
                applyAnimation()
                navigateToSuccess(getHashData())
            }
        }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.clear_menu -> {
                mBinding.plainText.text.clear()
                showSnackBar("Cleared")
            }
        }
        return true
    }

    private fun getHashData(): String {
        val algorithm = mBinding.tvAutoComplete.text.toString()
        val plainText = mBinding.plainText.text.toString()
        return homeViewModel.getHash(plainText = plainText, algorithm = algorithm)
    }

    private fun showSnackBar(message: String) {
        val snackBar = Snackbar.make(
            mBinding.rootLayout,
            message,
            Snackbar.LENGTH_SHORT
        )
        snackBar.setAction("OK") {}
        snackBar.setActionTextColor(resources.getColor(R.color.white))
        snackBar.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}