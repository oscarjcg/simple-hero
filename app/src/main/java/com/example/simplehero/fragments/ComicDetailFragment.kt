package com.example.simplehero.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.example.simplehero.R
import com.example.simplehero.databinding.FragmentComicDetailBinding
import com.example.simplehero.models.comic.Comic
import com.example.simplehero.utils.*
import com.example.simplehero.utils.IMAGE_VARIANT_STANDARD_LARGE
import com.example.simplehero.viewmodels.ComicDetailViewModel
import com.example.simplehero.viewmodels.ComicViewModel


class ComicDetailFragment : Fragment() {

    private lateinit var binding: FragmentComicDetailBinding
    private val args: ComicDetailFragmentArgs by navArgs()
    private val comicDetailViewModel: ComicDetailViewModel by activityViewModels()
    private lateinit var navController: NavController

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
        binding.viewModel = comicDetailViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()
        setToolbar()

        observeComic()
        observeUIEvents()
        comicDetailViewModel.comicSelected.value = Comic(INVALID_COMIC_ID)

        comicDetailViewModel.getComic(args.comicId)
    }

    private fun observeComic() {
        comicDetailViewModel.comicSelected.observe(viewLifecycleOwner, { comic ->
            val showComicSelected = comic.id != INVALID_COMIC_ID
            comicDetailViewModel.setShowComicSelected(showComicSelected)
            binding.title.text = comic.title
            binding.description.text = comic.description

            val thumbnailUrl = comic.thumbnail?.let {
                    UtilsFun.httpToHttps(it.path)
                }?.let {
                    UtilsFun.buildImageUrl(
                        it,
                        IMAGE_VARIANT_PORTRAIT_UNCANNY,
                        comic.thumbnail.extension)
            }
            Glide
                .with(binding.image)
                .load(thumbnailUrl)
                .error(R.drawable.comic_placeholder)
                .into(binding.image)
        })
    }

    private fun observeUIEvents() {
        comicDetailViewModel.uiState.observe(viewLifecycleOwner, {
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
                comicDetailViewModel.setStateInfo(true, getText(R.string.no_results) as String)
            }
        }
    }

    private fun checkInternet() {
        if (context?.let { Connectivity.isOnline(it) } == false) {
            Toast.makeText(activity, R.string.no_internet, Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun setToolbar() {
        val navHostFragment = NavHostFragment.findNavController(this)
        binding.toolbar.setupWithNavController( navHostFragment)

        binding.toolbar.title = ""

        setHasOptionsMenu(true)
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
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
