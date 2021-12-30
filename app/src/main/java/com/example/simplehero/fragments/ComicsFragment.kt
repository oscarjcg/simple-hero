package com.example.simplehero.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.simplehero.R
import com.example.simplehero.adapters.ComicsAdapter
import com.example.simplehero.databinding.FragmentComicsBinding
import com.example.simplehero.models.Comic
import com.example.simplehero.utils.Connectivity
import com.example.simplehero.utils.UIEvent
import com.example.simplehero.viewmodels.ComicViewModel

private const val GRID_WIDTH_SPAN = 2

class ComicsFragment : Fragment(), ComicsAdapter.ActionInterface {

    private lateinit var binding: FragmentComicsBinding
    private val comicViewModel: ComicViewModel by activityViewModels()
    private lateinit var navController: NavController

    private lateinit var comicsAdapter: ComicsAdapter

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
        binding = FragmentComicsBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = comicViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()

        initComicsEmptyList()
        observeComics()
        comicsListOnEndListener()
        observeUIEvents()

        requestComics()
    }

    private fun initComicsEmptyList() {
        comicsAdapter = ComicsAdapter(ArrayList(), this)
        binding.comics.layoutManager = GridLayoutManager(activity, GRID_WIDTH_SPAN)
        binding.comics.adapter = comicsAdapter
    }

    private fun observeComics() {
        comicViewModel.comics.observe(viewLifecycleOwner, { comics ->
            comicsAdapter.setComics(comics)
        })
    }

    private fun comicsListOnEndListener() {
        binding.comics.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (!recyclerView.canScrollVertically(1)) {
                    if (!comicViewModel.loading.value!!) {
                        requestComics()
                    }
                }
            }
        })
    }

    private fun requestComics() {
        val doctorStrangeId = 1009282
        comicViewModel.getComics(doctorStrangeId)
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
         * @return A new instance of fragment ComicsFragment.
         */
        @JvmStatic
        fun newInstance() =
            ComicsFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    override fun onClickComic(comic: Comic) {
        val action = ComicsFragmentDirections.actionComicsFragmentToComicDetailFragment(comic.id)
        navController.navigate(action)
    }
}
