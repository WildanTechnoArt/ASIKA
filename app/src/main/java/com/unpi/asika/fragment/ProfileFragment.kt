package com.unpi.asika.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.firebase.ui.auth.AuthUI
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.theartofdev.edmodo.cropper.CropImage
import com.unpi.asika.GlideApp
import com.unpi.asika.R
import com.unpi.asika.activity.EditProfileActivity
import com.unpi.asika.activity.LoginFormActivity
import com.unpi.asika.utils.Constant
import id.zelory.compressor.Compressor
import kotlinx.android.synthetic.main.fragment_profile.*
import java.io.ByteArrayOutputStream
import java.io.File

class ProfileFragment : Fragment() {

    private var mUserId: String? = null
    private val dbPhoto = FirebaseFirestore.getInstance()
    private val dbProfile = FirebaseFirestore.getInstance()
    private val photoReference = FirebaseStorage.getInstance().reference
    private val thumbReference = FirebaseStorage.getInstance().reference
    private val database = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestData()
        swipe_refresh?.setOnRefreshListener {
            requestData()
        }
        fab_change_photo.setOnClickListener {
            getPhotoFromStorage(view.context)
        }
        img_logout.setOnClickListener {
            logout()
        }
        btn_edit.setOnClickListener {
            startActivity(Intent(context, EditProfileActivity::class.java))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == Constant.GALLERY_PICK && resultCode == Activity.RESULT_OK) {
            val imageUri = data?.data
            context?.let {
                CropImage.activity(imageUri)
                    .setAspectRatio(1, 1)
                    .setMinCropWindowSize(200, 200)
                    .start(it, this@ProfileFragment)
            }
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            val result = CropImage.getActivityResult(data)

            if (resultCode == Activity.RESULT_OK) {

                uploadPhotoProfile(result)

            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(
                    context, "Crop Image Error",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            Constant.PERMISSION_STORAGE -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    val galleryIntent = Intent()
                    galleryIntent.type = "image/*"
                    galleryIntent.action = Intent.ACTION_GET_CONTENT
                    startActivityForResult(
                        Intent.createChooser(galleryIntent, "SELECT IMAGE"),
                        Constant.GALLERY_PICK
                    )
                }
                return
            }
        }
    }

    private fun logout() {
        val alert = context?.let { data ->
            MaterialAlertDialogBuilder(data)
                .setTitle("Konfirmasi")
                .setMessage("Anda yakin ingin keluar?")
                .setPositiveButton("Ya") { _, _ ->
                    context?.let { it1 ->
                        AuthUI.getInstance()
                            .signOut(it1)
                            .addOnCompleteListener {
                                if (it.isSuccessful) {
                                    startActivity(Intent(context, LoginFormActivity::class.java))
                                    (context as AppCompatActivity).finish()
                                } else {
                                    swipe_refresh?.isRefreshing = false
                                    Toast.makeText(
                                        context,
                                        getString(R.string.request_problem),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                    }
                }
                .setNegativeButton("Tidak") { dialog, _ ->
                    dialog.dismiss()
                }
        }
        alert?.create()
        alert?.show()
    }

    private fun requestData() {
        mUserId = FirebaseAuth.getInstance().currentUser?.uid.toString()
        swipe_refresh?.isRefreshing = true
        showPhoto()
        showProfile()
    }

    private fun showPhoto() {
        dbPhoto.collection("photos")
            .document(mUserId.toString())
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Toast.makeText(context, error.localizedMessage, Toast.LENGTH_SHORT).show()
                } else {
                    val getPhoto = value?.getString("photoUrl").toString()
                    context?.let {
                        GlideApp.with(it)
                            .load(getPhoto)
                            .placeholder(R.drawable.icon_profile)
                            .into(img_profile)
                    }
                }

                swipe_refresh?.isRefreshing = false
            }
    }

    private fun showProfile() {
        dbProfile.collection("users")
            .document(mUserId.toString())
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Toast.makeText(context, error.localizedMessage, Toast.LENGTH_SHORT).show()
                } else {
                    val getName = value?.getString("username").toString()
                    val getEmail = value?.getString("email").toString()
                    val getAddress = value?.getString("address").toString()
                    val getPhone = value?.getString("phone").toString()

                    tv_username.text = getName
                    tv_email.text = getEmail
                    tv_address.text = getAddress
                    tv_phone_number.text = getPhone
                }

                swipe_refresh?.isRefreshing = false
            }
    }

    private fun getPhotoFromStorage(context: Context) {
        if (ContextCompat.checkSelfPermission(
                context, android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            activity?.let {
                ActivityCompat.requestPermissions(
                    it, arrayOf(
                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ),
                    Constant.PERMISSION_STORAGE
                )
            }

        } else {
            val galleryIntent = Intent()
            galleryIntent.type = "image/*"
            galleryIntent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(galleryIntent, "SELECT IMAGE"),
                Constant.GALLERY_PICK
            )
        }
    }

    private fun uploadPhotoProfile(result: CropImage.ActivityResult) {
        swipe_refresh?.isRefreshing = true

        val resultUri = result.uri

        val thumbImage = File(resultUri.path.toString())

        val thumbBitmap = Compressor(context)
            .setMaxHeight(200)
            .setMaxWidth(200)
            .setQuality(75)
            .compressToBitmap(thumbImage)

        val baos = ByteArrayOutputStream()
        thumbBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val imageByte = baos.toByteArray()

        val imageURL = "photo_profile/$mUserId.jpg"
        val thumbURL = "thumb_image/$mUserId.jpg"
        val imagePath = photoReference.child(imageURL)
        val thumbPath = thumbReference.child(thumbURL)

        imagePath.putFile(resultUri).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                photoReference.child(imageURL).downloadUrl.addOnSuccessListener { imageUri: Uri? ->

                    val uploadTask: UploadTask = thumbPath.putBytes(imageByte)
                    val downloadUrl = imageUri.toString()

                    uploadTask.addOnCompleteListener { task ->
                        if (task.isSuccessful) {

                            thumbReference.child(thumbURL).downloadUrl.addOnSuccessListener { thumbUri: Uri? ->

                                val thumbUrl = thumbUri.toString()

                                val dataMap = HashMap<String, String>()
                                dataMap["photoUrl"] = downloadUrl
                                dataMap["thumbUrl"] = thumbUrl

                                database.collection("photos").document(mUserId.toString())
                                    .set(dataMap)
                                    .addOnCompleteListener {
                                        if (task.isSuccessful) {
                                            Toast.makeText(
                                                context,
                                                "Foto profil berhasil diubah",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                            }

                        } else {
                            swipe_refresh?.isRefreshing = false
                            Toast.makeText(
                                context,
                                getString(R.string.request_problem),
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    }

                }.addOnFailureListener {
                    swipe_refresh?.isRefreshing = false
                    Toast.makeText(context, it.localizedMessage?.toString(), Toast.LENGTH_SHORT)
                        .show()
                }

            } else {
                swipe_refresh?.isRefreshing = false
                Toast.makeText(context, getString(R.string.request_problem), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}