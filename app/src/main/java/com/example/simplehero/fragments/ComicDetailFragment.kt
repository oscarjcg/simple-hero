package com.example.simplehero.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.simplehero.R
import com.example.simplehero.databinding.FragmentComicDetailBinding
import com.example.simplehero.utils.Connectivity
import com.example.simplehero.utils.IMAGE_VARIANT_STANDARD_LARGE
import com.example.simplehero.utils.ImageUtils
import com.example.simplehero.utils.UIEvent
import com.example.simplehero.viewmodels.ComicViewModel


class ComicDetailFragment : Fragment() {

    private lateinit var binding: FragmentComicDetailBinding
    private val args: ComicDetailFragmentArgs by navArgs()
    private val comicViewModel: ComicViewModel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentComicDetailBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = comicViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeComic()
        observeUIEvents()

        comicViewModel.getComic(args.comicId)
    }

    private fun observeComic() {
        comicViewModel.comicSelected.observe(viewLifecycleOwner, { comic ->
            binding.title.text = comic.title
            binding.description.text = comic.description

            val thumbnailUrl = ImageUtils.buildImageUrl(
                comic.thumbnail.path,
                IMAGE_VARIANT_STANDARD_LARGE,
                comic.thumbnail.extension)
            Glide
                .with(binding.image)
                .load(thumbnailUrl)
                .into(binding.image)
        })
    }

    private fun observeUIEvents() {
        comicViewModel.uiState.observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let { uiEvent ->
                handleUIEvent(uiEvent)
            }
        })
    }

    private fun handleUIEvent(event: UIEvent<Nothing>) {
        when (event) {
            is UIEvent.CheckInternet -> {
                checkInternet()
            }
            is UIEvent.NoResults -> {
                comicViewModel.setStateInfo(true, getText(R.string.no_results) as String)
            }
        }
    }

    private fun checkInternet() {
        if (context?.let { Connectivity.isOnline(it) } == false) {
            Toast.makeText(activity, R.string.no_internet, Toast.LENGTH_SHORT)
                .show()
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment ComicDetailFragment.
         */
        @JvmStatic
        fun newInstance() =
            ComicDetailFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}
