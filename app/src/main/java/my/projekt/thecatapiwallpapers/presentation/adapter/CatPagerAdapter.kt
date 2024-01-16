package my.projekt.thecatapiwallpapers.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.request.CachePolicy
import coil.transform.CircleCropTransformation
import my.projekt.thecatapiwallpapers.databinding.CatItemBinding
import my.projekt.thecatapiwallpapers.domain.model.CatResponse

class CatPagerAdapter(private val catList: List<CatResponse>) :
    RecyclerView.Adapter<CatPagerAdapter.CatViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CatItemBinding.inflate(inflater, parent, false)
        return CatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CatViewHolder, position: Int) {
        val cat = catList[position]
        holder.bind(cat)
    }

    override fun getItemCount(): Int = catList.size

    class CatViewHolder(private val binding: CatItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(cat: CatResponse) {
            binding.catImageView.load(cat.url) {
                crossfade(true)
                transformations(CircleCropTransformation())
                // Опционально: устанавливаем стратегию кэширования
                memoryCachePolicy(CachePolicy.ENABLED)
                diskCachePolicy(CachePolicy.ENABLED)
            }
        }
    }
}
