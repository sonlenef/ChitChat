package tech.leson.chitchat.ui.main

import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2.ORIENTATION_HORIZONTAL
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.navigation_footer_main.*
import kotlinx.android.synthetic.main.navigation_header_main.*
import tech.leson.chitchat.BR
import tech.leson.chitchat.R
import tech.leson.chitchat.ViewModelProviderFactory
import tech.leson.chitchat.databinding.ActivityMainBinding
import tech.leson.chitchat.ui.base.BaseActivity
import tech.leson.chitchat.ui.main.contact.ContactFragment
import tech.leson.chitchat.ui.main.dialog.UpdateDialog
import tech.leson.chitchat.ui.main.group.GroupFragment
import tech.leson.chitchat.ui.main.home.HomeFragment
import tech.leson.chitchat.ui.main.profile.ProfileFragment
import tech.leson.chitchat.ui.main.qrcode.QRFragment
import tech.leson.chitchat.ui.search.SearchActivity
import tech.leson.chitchat.ui.update.UpdateActivity
import tech.leson.chitchat.ui.username.UsernameActivity
import tech.leson.chitchat.utils.AppUtils.Companion.delayBtnOnClick
import javax.inject.Inject


class MainActivity :
    BaseActivity<ActivityMainBinding, MainNavigator, MainViewModel>(),
    MainNavigator, HasAndroidInjector {

    private val mHomeFragment = HomeFragment.getInstance()
    private val mContactFragment = ContactFragment.getInstance()
    private val mQRFragment = QRFragment.getInstance()
    private val mGroupFragment = GroupFragment.getInstance()
    private val mProfileFragment = ProfileFragment.getInstance()

    companion object {
        const val ACTIVITY = "main"
        const val REQUEST_CODE = 1111
        var tabCurrent = TAB.TAB_HOME
        private var instance: Intent? = null

        @JvmStatic
        fun getIntent(context: Context) = instance ?: synchronized(this) {
            instance ?: Intent(context, MainActivity::class.java).also { instance = it }
        }
    }

    private lateinit var mTabs: ArrayList<Fragment>

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    @Inject
    lateinit var factory: ViewModelProviderFactory

    @Inject
    lateinit var mainTabAdapter: MainTabAdapter

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.activity_main
    override val viewModel: MainViewModel
        get() = factory.let {
            ViewModelProvider(
                this,
                it
            ).get(MainViewModel::class.java)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setNavigator(this)
        btnIconAppBar.setImageResource(R.drawable.ic_search)
        btnAppBar.visibility = View.INVISIBLE
    }


    @Suppress("DEPRECATION")
    override fun init() {
        mTabs = ArrayList()
        mTabs.add(mHomeFragment)
        mTabs.add(mContactFragment)
        mTabs.add(mQRFragment)
        mTabs.add(mGroupFragment)
        mTabs.add(mProfileFragment)

        mainTabAdapter.mTabs = mTabs
        viewTabs.orientation = ORIENTATION_HORIZONTAL
        viewTabs.adapter = mainTabAdapter
        viewTabs.offscreenPageLimit = 4

        TabLayoutMediator(tabMain, viewTabs) { tab, position ->
            when (position) {
                0 -> {
                    tab.icon = ContextCompat.getDrawable(this, R.drawable.ic_chat)
                    viewTabs.currentItem = position
                }
                1 -> {
                    tab.icon = ContextCompat.getDrawable(this, R.drawable.ic_contact)
                }
                2 -> {
                    tab.view.isClickable = false
                }
                3 -> {
                    tab.icon = ContextCompat.getDrawable(this, R.drawable.ic_user_group)
                }
                4 -> {
                    tab.icon = ContextCompat.getDrawable(this, R.drawable.ic_user)
                    btnAppBar.visibility = View.INVISIBLE
                }
            }
        }.attach()

        tabMain.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewTabs.currentItem = tab!!.position
                val tabIconColor = ContextCompat.getColor(this@MainActivity, R.color.green)
                when (tab.position) {
                    0 -> {
                        tabCurrent = TAB.TAB_HOME
                        tab.icon!!.setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN)
                        btnAppBar.visibility = View.INVISIBLE
                    }
                    1 -> {
                        tabCurrent = TAB.TAB_CONTACT
                        tab.icon!!.setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN)
                        btnIconAppBar.setImageResource(R.drawable.ic_search)
                        btnAppBar.visibility = View.VISIBLE
                    }
                    2 -> {
                        tabCurrent = TAB.TAB_QR_CODE
                        btnIconAppBar.setImageResource(R.drawable.ic_scan)
                        btnAppBar.visibility = View.VISIBLE
                    }
                    3 -> {
                        tabCurrent = TAB.TAB_GROUP
                        tab.icon!!.setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN)
                        btnAppBar.visibility = View.INVISIBLE
                    }
                    4 -> {
                        tabCurrent = TAB.TAB_PROFILE
                        tab.icon!!.setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN)
                        btnIconAppBar.setImageResource(R.drawable.ic_edit)
                        btnAppBar.visibility = View.VISIBLE
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                if (tab!!.position != 2) {
                    val tabIconColor = ContextCompat.getColor(this@MainActivity, R.color.textColor)
                    tab.icon!!.setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN)
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
        tabMain.getTabAt(0)?.select()
        val tabIconColor = ContextCompat.getColor(this@MainActivity, R.color.green)
        tabMain.getTabAt(0)?.icon!!.setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN)
    }

    override fun getUserFailed(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    override fun openSearchView() {
        startActivity(SearchActivity.getIntent(this))
        delayBtnOnClick(btnAppBar)
    }

    override fun openScanView() {}

    override fun onTabQRCodeClick() {
        tabMain.getTabAt(2)?.select()
    }

    override fun openDialogUpdate() {
        if (supportFragmentManager.findFragmentByTag("Update") == null) {
            UpdateDialog.getInstance().show(supportFragmentManager, "Update")
        }
    }

    override fun handleError(throwable: Throwable?) {}

    override fun androidInjector(): DispatchingAndroidInjector<Any>? {
        return dispatchingAndroidInjector
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE) {
            if (resultCode == UpdateActivity.RESULT_CODE || resultCode == UsernameActivity.RESULT_CODE) {
                mProfileFragment.updateData()
            }
        }
    }
}
