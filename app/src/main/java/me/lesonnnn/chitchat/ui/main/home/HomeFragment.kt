package me.lesonnnn.chitchat.ui.main.home

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import dagger.android.DispatchingAndroidInjector
import me.lesonnnn.chitchat.BR
import me.lesonnnn.chitchat.R
import me.lesonnnn.chitchat.ViewModelProviderFactory
import me.lesonnnn.chitchat.databinding.FragmentHomeBinding
import me.lesonnnn.chitchat.ui.base.BaseFragment
import javax.inject.Inject

class HomeFragment : BaseFragment<FragmentHomeBinding, HomeNavigator, HomeViewModel>(), HomeNavigator {

    companion object {
        private var instance: HomeFragment? = null

        @JvmStatic
        fun getInstance() = instance ?: synchronized(this) {
            instance ?: HomeFragment().also { instance = it }
        }
    }

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>
    @Inject
    lateinit var factory: ViewModelProviderFactory

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_home
    override val viewModel: HomeViewModel
        get() = factory.let {
            ViewModelProvider(
                this, it
            ).get(HomeViewModel::class.java)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setNavigator(this)
    }

    override fun init() {
    }

}
