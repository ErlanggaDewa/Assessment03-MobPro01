package org.d3if1053.hitungzakatpenghasilan.ui.biodata

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import org.d3if1053.hitungzakatpenghasilan.R
import org.d3if1053.hitungzakatpenghasilan.databinding.FragmentBiodataBinding
import org.d3if1053.hitungzakatpenghasilan.model.UserModel
import org.d3if1053.hitungzakatpenghasilan.network.ApiStatus

class BiodataFragment : Fragment() {

    private lateinit var binding: FragmentBiodataBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_biodata, container, false)

        val viewModel: BiodataViewModel by viewModels()
        viewModel.getUser().observe(viewLifecycleOwner, Observer<UserModel> {
            // update UI
            binding.name.text = it.name
            binding.bio.text = it.bio
            binding.college.text = it.company
            binding.location.text = it.location
            binding.linkedIn.text = it.blog
            Glide.with(binding.userImage.context)
                .load(it.avatar_url)
                .error(R.drawable.ic_baseline_broken_image_24)
                .into(binding.userImage)

        })

        viewModel.getStatus().observe(viewLifecycleOwner) {
            updateProgress(it)
        }

        return binding.root
    }

    private fun updateProgress(status: ApiStatus) {
        when (status) {
            ApiStatus.LOADING -> {
                binding.showBiodata.visibility = View.GONE
                binding.progressBar.visibility = View.VISIBLE
            }
            ApiStatus.SUCCESS -> {
                binding.showBiodata.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
            }
            ApiStatus.FAILED -> {
                binding.showBiodata.visibility = View.GONE
                binding.progressBar.visibility = View.GONE
                binding.networkError.visibility = View.VISIBLE
            }
        }
    }


}