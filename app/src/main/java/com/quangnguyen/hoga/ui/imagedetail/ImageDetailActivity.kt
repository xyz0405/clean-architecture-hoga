package com.quangnguyen.hoga.ui.imagedetail

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.bumptech.glide.Glide
import com.quangnguyen.hoga.R
import com.quangnguyen.hoga.di.Injector
import com.quangnguyen.hoga.domain.model.Image
import com.quangnguyen.hoga.ui.imagedetail.ImageDetailContract.Presenter
import com.quangnguyen.hoga.util.isStoragePermissionGranted
import kotlinx.android.synthetic.main.activity_image_detail.authorText
import kotlinx.android.synthetic.main.activity_image_detail.image


class ImageDetailActivity : AppCompatActivity(), ImageDetailContract.View {

  companion object {
    const val EXTRA_IMAGE_ID = "image_id"
  }

  private lateinit var presenter: Presenter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_image_detail)
  }

  override fun onStart() {
    super.onStart()
    initPresenter()
  }

  private fun initPresenter() {
    presenter = ImageDetailPresenter(this, Injector.getImageUseCase, Injector.downloadImageUseCase,
        Injector.schedulerProvider)
  }

  override fun onResume() {
    super.onResume()
    presenter.attach()
    loadImageDetail()
  }

  private fun loadImageDetail() {
    val imageId = intent.getStringExtra(EXTRA_IMAGE_ID)
    presenter.loadImage(imageId)
  }

  override fun onPause() {
    super.onPause()
    presenter.detach()
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.activity_image_detail, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    if (item == null) return false
    when (item.itemId) {
      R.id.download -> {
        if (isStoragePermissionGranted()) {
          presenter.downloadImage()
        }
      }
    }
    return true
  }

  override fun showImage(image: Image) {
    Glide.with(this).load(image.smallImageUrl).into(this.image)
    authorText.text = String.format(getString(R.string.photo_by), image.authorName)
  }

  override fun showMessage(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
  }
}