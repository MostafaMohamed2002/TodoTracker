package com.mostafadevo.todo.presentation.fragments.setting

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.mostafadevo.todo.R
import com.mostafadevo.todo.Utils
import com.mostafadevo.todo.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {
    private val REQUEST_CODE = 1000
    private lateinit var binding: FragmentSettingsBinding
    private val mSettingsViewModel: SettingsViewModel by activityViewModels()
    private val sharedPreferences by lazy {
        requireActivity().getSharedPreferences(Utils.SHARED_PREF_NAME, Activity.MODE_PRIVATE)
    }
    private var isEditMode: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUserNameandEmailandLoadImage()
        handleEditSettings()
        setupPushUpdatesToFirestore()
        setupCloneCloudTodos()
        handleSwitchButtonForFirebaseSync()
        handleBackButton()
    }

    private fun handleEditSettings() {
        val resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == AppCompatActivity.RESULT_OK) {
                    val data = result.data
                    val picUri = data?.data
                    mSettingsViewModel.uploadImageToFirebase(picUri)
                }
            }
        binding.toolbar.setOnMenuItemClickListener {
            Log.d("SettingsFragment", "menu item clicked ${it.itemId}")
            when (it.itemId) {
                R.id.edit_settings -> {
                    if (!isEditMode)/*is in edit mode*/ {
                        binding.usernameTextinput.editText?.isEnabled = true
                        //hide dividers
                        binding.useremailTextinput.visibility=View.GONE
                        binding.divider.visibility = View.GONE
                        binding.divider2.visibility = View.GONE
                        binding.divider3.visibility = View.GONE
                        //hide other buttons
                        binding.pushUpdatesButton.visibility = View.GONE
                        binding.getUpdates.visibility = View.GONE
                        binding.enableSyncingSwitch.visibility = View.GONE
                        it.setIcon(R.drawable.baseline_save_24)

                    } else if (isEditMode) {
                        binding.usernameTextinput.editText?.isEnabled = false
                        //hide dividers
                        binding.useremailTextinput.visibility=View.VISIBLE
                        binding.divider.visibility = View.VISIBLE
                        binding.divider2.visibility = View.VISIBLE
                        binding.divider3.visibility = View.VISIBLE
                        //hide other buttons
                        binding.pushUpdatesButton.visibility = View.VISIBLE
                        binding.getUpdates.visibility = View.VISIBLE
                        binding.enableSyncingSwitch.visibility = View.VISIBLE
                        it.setIcon(R.drawable.baseline_edit_24)
                        mSettingsViewModel.updateUserName(binding.usernameTextinput.editText?.text.toString())
                        mSettingsViewModel.getUserImageUrl()
                        mSettingsViewModel.getUserName()
                        mSettingsViewModel.getUserImageUrl()
                    } else {

                    }
                    isEditMode = !isEditMode
                    Log.d("SettingsFragment", "isEditMode is $isEditMode")

                }

                else -> {

                }
            }
            true
        }
            binding.userImageview.setOnClickListener {
                if (isEditMode){
                    val intent = Intent(Intent.ACTION_PICK)
                    intent.type = "image/*"
                    resultLauncher.launch(intent)
                }
            }


    }

    private fun handleSwitchButtonForFirebaseSync() {
        binding.enableSyncingSwitch.isChecked =
            sharedPreferences.getBoolean(Utils.FIREBASE_SYNC_ENABLED, false)
        binding.enableSyncingSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            sharedPreferences.edit().apply {
                putBoolean(Utils.FIREBASE_SYNC_ENABLED, isChecked)
                    .apply()
            }
            if (isChecked) {
                mSettingsViewModel.startFirebaseSyncService()
            } else {
                mSettingsViewModel.stopFirebaseSyncService()
            }
        }
    }

    private fun handleBackButton() {
        binding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun setupCloneCloudTodos() {
        binding.getUpdates.setOnClickListener {
            mSettingsViewModel.getTodosFromFirebase()
        }
    }

    private fun setupUserNameandEmailandLoadImage() {
        // setup text input to be uneditable for now
        binding.usernameTextinput.editText?.isEnabled = false
        binding.useremailTextinput.editText?.isEnabled = false
        //set them to focusable
        binding.usernameTextinput.editText?.isFocusable = true
        binding.useremailTextinput.editText?.isFocusable = true
        //set them to focusable in touch mode
        binding.usernameTextinput.editText?.isFocusableInTouchMode = true
        binding.useremailTextinput.editText?.isFocusableInTouchMode = true


        // observe the user data
        mSettingsViewModel.userName.observe(viewLifecycleOwner) { userName ->
            binding.usernameTextinput.editText?.setText(userName)
            Log.d("SettingsFragment", "userName is $userName")
        }

        mSettingsViewModel.userEmail.observe(viewLifecycleOwner) { userEmail ->
            binding.useremailTextinput.editText?.setText(userEmail)
            Log.d("SettingsFragment", "userEmail is $userEmail")
        }

        mSettingsViewModel.userImageUrl.observe(viewLifecycleOwner) { imageUrl ->
            displayImage(binding.userImageview, imageUrl)
            Log.d("SettingsFragment", "imageUrl is $imageUrl")
        }
    }

    private fun getThemePrimaryColor(): Int {
        val typedValue = TypedValue()
        requireContext().theme.resolveAttribute(
            com.google.android.material.R.attr.colorPrimary,
            typedValue,
            true
        )
        return typedValue.data
    }

    private fun setupPushUpdatesToFirestore() {
        binding.pushUpdatesButton.setOnClickListener {
            mSettingsViewModel.pushTodosToFirebase()
        }
    }

    private fun displayImage(imageView: ImageView, imageUrl: String) {
        Glide.with(this)
            .load(imageUrl)
            .error(R.drawable.baseline_download_24)
            .placeholder(R.drawable.baseline_download_24)
            .into(imageView)
    }

}