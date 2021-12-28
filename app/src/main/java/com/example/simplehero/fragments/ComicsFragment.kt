package com.example.simplehero.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.simplehero.adapters.ComicsAdapter
import com.example.simplehero.databinding.FragmentComicsBinding
import com.example.simplehero.viewmodels.ComicViewModel

private const val GRID_WIDTH_SPAN = 2

class ComicsFragment : Fragment() {

    private lateinit var binding: FragmentComicsBinding
    private val comicViewModel: ComicViewModel by activityViewModels()

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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initComicsEmptyList()
        observeComics()

        val doctorStrangeId = 1009282
        comicViewModel.getComics(doctorStrangeId)
    }

    private fun initComicsEmptyList() {
        comicsAdapter = ComicsAdapter(ArrayList())
        binding.comics.layoutManager = GridLayoutManager(activity, GRID_WIDTH_SPAN)
        binding.comics.adapter = comicsAdapter
    }

    private fun observeComics() {
        comicViewModel.comics.observe(viewLifecycleOwner, { comics ->
            comicsAdapter.setComics(comics)
        })
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
}
