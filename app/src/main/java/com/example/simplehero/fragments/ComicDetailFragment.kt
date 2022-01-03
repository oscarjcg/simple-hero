package com.example.simplehero.fragments

import android.content.res.Resources
import android.os.Bundle
import android.util.TypedValue
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.setMargins
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
import com.example.simplehero.models.comic.ComicPrice
import com.example.simplehero.models.comic.PRICE_TYPE_PRINT
import com.example.simplehero.models.comic.PRICE_TYPE_PURCHASE_DIGITAL
import com.example.simplehero.utils.*
import com.example.simplehero.viewmodels.ComicDetailViewModel

/**
 * Shows a comic detail.
 */
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

        initialize()
    }

    private fun initialize() {
        // Invalid comic
        comicDetailViewModel.comicSelected.value = Comic(INVALID_COMIC_ID)

        // Start comic request
        comicDetailViewModel.getComic(args.comicId)
    }

    private fun observeComic() {
        comicDetailViewModel.comicSelected.observe(viewLifecycleOwner, { comic ->
            // Don't show invalid comic
            val showComicSelected = (comic.id != INVALID_COMIC_ID)
            comicDetailViewModel.setShowComicSelected(showComicSelected)

            binding.title.text = comic.title
            binding.description.text = comic.description
            comic.prices?.let { addPricesToInfo(it) }

            // Image url
            val thumbnailUrl = comic.thumbnail?.let {
                    UtilsFun.httpToHttps(it.path)
                }?.let {
                    UtilsFun.buildImageUrl(
                        it,
                        randomImage(),
                        comic.thumbnail.extension)
            }

            Glide
                .with(binding.image)
                .load(thumbnailUrl)
                .error(R.drawable.comic_placeholder)
                .into(binding.image)
        })
    }

    private fun addPricesToInfo(comicPrices: ArrayList<ComicPrice>) {
        comicPrices.forEach { comicPrice ->
            // Price label
            val priceLabel = TextView(ContextThemeWrapper(activity, R.style.TextSecondary))
            priceLabel.text = getPriceType(comicPrice.type)
            priceLabel.gravity = Gravity.END
            priceLabel.layoutParams = createPriceParams()

            // Price value
            val price = TextView(ContextThemeWrapper(activity, R.style.TextSecondary))
            price.text = comicPrice.price.toString()
            price.gravity = Gravity.START
            price.layoutParams = createPriceParams()

            val linearLayout = LinearLayout(activity)
            linearLayout.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT)

            linearLayout.addView(priceLabel)
            linearLayout.addView(price)
            binding.info.addView(linearLayout)
        }
    }

    private fun getPriceType(type: String): String {
        return when (type) {
            PRICE_TYPE_PRINT -> getString(R.string.price_printed)
            PRICE_TYPE_PURCHASE_DIGITAL -> getString(R.string.price_digital)
            else -> getString(R.string.price)
        }
    }

    private fun createPriceParams(): LinearLayout.LayoutParams {
        val params = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT)
        params.weight = 1F
        params.width = 0
        params.setMargins(dpToPixels(4f))
        return params
    }

    private fun dpToPixels(dip: Float): Int {
        val r: Resources = resources
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dip,
            r.displayMetrics
        ).toInt()
    }

    private fun randomImage(): String {
        val images = listOf(
            IMAGE_VARIANT_STANDARD_FANTASTIC,
            IMAGE_VARIANT_LANDSCAPE_AMAZING,
            IMAGE_VARIANT_PORTRAIT_UNCANNY)
        return images[(images.indices).random()]
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
