package my.projekt.thecatapiwallpapers.ui

import android.content.Context
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable
import my.projekt.thecatapiwallpapers.R
import my.projekt.thecatapiwallpapers.data.CatApiClient
import my.projekt.thecatapiwallpapers.databinding.ActivityMainBinding
import my.projekt.thecatapiwallpapers.domain.model.CatResponse
import my.projekt.thecatapiwallpapers.presentation.adapter.CatPagerAdapter
import retrofit2.Call
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private val catService = CatApiClient.create()
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val catList = mutableListOf<CatResponse>()
    private lateinit var adapter: CatPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        if (isNetworkAvailable()) {
            loadRandomCats()
            setupViewPager()
        } else {
            showError("Отсутствует интернет-соединение")
        }
    }

    private fun setupViewPager() {
        adapter = CatPagerAdapter(catList)
        binding.catViewPager.adapter = adapter

        // Добавим индикатор страниц
        val pageIndicator = findViewById<LinearLayout>(R.id.pageIndicator)
        for (i in catList.indices) {
            val indicator = ShimmerDrawable().apply {
                setShimmer(Shimmer.ColorHighlightBuilder().build())
            }
            val params = LinearLayout.LayoutParams(
                resources.getDimensionPixelSize(R.dimen.page_indicator_size),
                resources.getDimensionPixelSize(R.dimen.page_indicator_size)
            )
            params.marginEnd = resources.getDimensionPixelSize(R.dimen.page_indicator_margin)
            indicator.setBounds(0, 0, params.width, params.height)
            indicator.callback = binding.catViewPager
            pageIndicator.addView(ImageView(this).apply {
                layoutParams = params
                setImageDrawable(indicator)
            })
        }

        binding.catViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                // Обновляем индикатор страниц при прокрутке
                updatePageIndicator(position)
                // Загружаем следующее изображение при достижении последней страницы
                if (position == catList.size - 1) {
                    loadRandomCats()
                }
            }
        })
    }

    private fun updatePageIndicator(currentPosition: Int) {
        val pageIndicator = findViewById<LinearLayout>(R.id.pageIndicator)
        for (i in 0 until pageIndicator.childCount) {
            val indicator = (pageIndicator.getChildAt(i) as ImageView).drawable as ShimmerDrawable
            indicator.setShimmer(Shimmer.ColorHighlightBuilder().build())

        }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as android.net.ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun loadRandomCats() {
        val call = catService.getRandomCat("live_Mn7VIpPGWGJCbSyEGwquAllFpaLWfHYZRpBGexVQm2dHjcmUVYydJi7aBsdxiEkQ")

        call.enqueue(object : retrofit2.Callback<List<CatResponse>> {
            override fun onResponse(
                call: Call<List<CatResponse>>,
                response: Response<List<CatResponse>>
            ) {
                if (response.isSuccessful) {
                    val cats = response.body()
                    cats?.let {
                        catList.addAll(it)
                        adapter.notifyDataSetChanged()
                        // Обновляем индикатор страниц
                        updatePageIndicator(0)
                    } ?: showError("Нет данных о котах")
                } else {
                    showError("Ошибка загрузки данных. Код: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<CatResponse>>, t: Throwable) {
                showError("Ошибка загрузки данных: ${t.message}")
            }
        })
    }
}
