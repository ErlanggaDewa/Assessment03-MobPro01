package org.d3if1053.hitungzakatpenghasilan.ui.hitung

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import kotlinx.coroutines.launch
import org.d3if1053.hitungzakatpenghasilan.R
import org.d3if1053.hitungzakatpenghasilan.data.SettingDataStore
import org.d3if1053.hitungzakatpenghasilan.data.dataStore
import org.d3if1053.hitungzakatpenghasilan.databinding.FragmentHitungBinding
import org.d3if1053.hitungzakatpenghasilan.db.ZakatDatabase
import org.d3if1053.hitungzakatpenghasilan.network.ApiStatus

class HitungFragment : Fragment() {

    companion object {
        fun newInstance() = HitungFragment()
    }

    private lateinit var savingDataStore: SettingDataStore
    private lateinit var binding: FragmentHitungBinding
    private var isSavingStatus = true

    private val viewModel: HitungViewModel by lazy {
        val db = ZakatDatabase.getInstance(requireContext())
        val factory = HitungViewModelFactory(db.dao)
        ViewModelProvider(this, factory)[HitungViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_hitung, container, false)

        binding.hitung.setOnClickListener { hitungZakat() }
        binding.reset.setOnClickListener { resetField() }

        viewModel.getGoldData().observe(viewLifecycleOwner) {
            binding.inputHargaEmas.setText(it.data.current.buy.toLong().toString())
        }

        viewModel.getStatus().observe(viewLifecycleOwner) {
            updateProgress(it)
        }

        viewModel.scheduleUpdater(requireActivity().application)

        // update UI from any change rotation
        updateUI()

        // enable menu
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        savingDataStore = SettingDataStore(requireContext().dataStore)
        savingDataStore.preferenceFlow.asLiveData().observe(viewLifecycleOwner) { value ->
            isSavingStatus = value
            activity?.invalidateOptionsMenu()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.overflow_menu, menu)
        inflater.inflate(R.menu.share_menu, menu)
        setSaveTitle(menu.findItem(R.id.save_data_manager))
    }

    private fun getShareIntent(): Intent {
        val shareIntent = Intent(Intent.ACTION_SEND)
        val message: String = outputShareText()

        shareIntent.setType("text/plain")
            .putExtra(
                Intent.EXTRA_TEXT,
                message
            )
        if (shareIntent.resolveActivity(requireActivity().packageManager) != null
        ) {
            startActivity(shareIntent)
        }
        return shareIntent
    }

    private fun outputShareText() = if (viewModel.zakatModel.totalZakat == 0L)
        getString(
            R.string.share_tidak_wajib_text
        )
    else
        getString(
            R.string.share_wajib_text, viewModel.zakatModel.totalZakat
        )

    private fun shareSuccess() {
        startActivity(getShareIntent())
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.share -> shareSuccess()
            R.id.save_data_manager -> toggleSaving(item)
        }
        return NavigationUI.onNavDestinationSelected(
            item,
            requireView().findNavController()
        )
                || super.onOptionsItemSelected(item)
    }

    private fun toggleSaving(item: MenuItem) {
        isSavingStatus = !isSavingStatus
        lifecycleScope.launch {
            savingDataStore.saveSavingToPreferencesStore(
                isSavingStatus,
                requireContext()
            )
        }
        setSaveTitle(item)

    }

    private fun hitungZakat() {
        clearError()

        val hargaEmas = binding.inputHargaEmas.text.toString()
        val penghasilan = binding.inputPenghasilan.text.toString()
        val bonus = binding.inputBonus.text.toString()

        if (validateUserInput(hargaEmas, penghasilan, bonus)) return


        updateOutputViewModel(viewModel.isPayZakat(hargaEmas, penghasilan, bonus, isSavingStatus))
        updateUI()

    }

    private fun updateOutputViewModel(isPayZakat: Boolean) {
        if (isPayZakat) {
            viewModel.zakatModel.outputZakat =
                getString(R.string.wajib_bayar_zakat, viewModel.zakatModel.totalZakat)
        } else {
            viewModel.zakatModel.outputZakat =
                getString(R.string.tidak_bayar_zakat)
        }
    }

    private fun updateUI() {
        binding.outputZakat.text = viewModel.zakatModel.outputZakat
    }

    private fun validateUserInput(
        hargaEmas: String,
        penghasilan: String,
        bonus: String
    ): Boolean {
        if (!viewModel.isNumeric(hargaEmas)) {
            // Set error text
            binding.hargaEmasHint.error = getString(R.string.error)
            return true
        } else if (!viewModel.isNumeric(penghasilan)) {
            binding.penghasilanHint.error = getString(R.string.error)
            return true
        } else if (!viewModel.isNumeric(bonus)) {
            binding.bonusHint.error = getString(R.string.error)
            return true
        }
        return false
    }

    private fun clearError() {
        // Clear error text
        binding.hargaEmasHint.error = null
        binding.penghasilanHint.error = null
        binding.bonusHint.error = null
    }


    private fun resetField() {
        viewModel.zakatModel.outputZakat = ""
        viewModel.zakatModel.totalZakat = 0

        binding.hargaEmasHint.error = null
        binding.penghasilanHint.error = null
        binding.bonusHint.error = null
        binding.inputPenghasilan.text = null
        binding.inputBonus.text = null
        binding.outputZakat.text = null
    }

    private fun setSaveTitle(menuItem: MenuItem) {
        if (isSavingStatus) {
            menuItem.setTitle(R.string.save_title)
        } else {
            menuItem.setTitle(R.string.unsave_title)
        }
    }

    private fun updateProgress(status: ApiStatus) {
        when (status) {
            ApiStatus.LOADING -> {
                binding.progressBar.visibility = View.VISIBLE
                binding.hitung.isEnabled = false
                binding.reset.isEnabled = false
            }
            ApiStatus.SUCCESS -> {
                binding.progressBar.visibility = View.GONE
                binding.hitung.isEnabled = true
                binding.reset.isEnabled = true
            }
            ApiStatus.FAILED -> {
                binding.hitung.isEnabled = false
                binding.reset.isEnabled = false
                binding.outputZakat.setText(R.string.koneksi_error_message)
                binding.progressBar.visibility = View.GONE
                binding.networkError.visibility = View.VISIBLE
            }
        }
    }
}