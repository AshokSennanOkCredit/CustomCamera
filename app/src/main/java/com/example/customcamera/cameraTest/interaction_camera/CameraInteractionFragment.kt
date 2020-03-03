package com.example.customcamera.cameraTest.interaction_camera

import android.app.VoiceInteractor
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.customcamera.R
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.android.synthetic.main.camera_interaction_layout.view.*
import java.util.concurrent.TimeUnit

class CameraInteractionFragment(context: Context,attr:AttributeSet):ConstraintLayout(context,attr){
    val disposable = CompositeDisposable()
    private lateinit var interactor: Interactor
    private val clickSubject = PublishSubject.create<Unit>()
    private val backSubject = PublishSubject.create<Unit>()

    fun addInteractor(interactor:Interactor){
        this.interactor = interactor
    }

    fun off_Torch(){
        flash_iv.setImageDrawable(context?.getDrawable(R.drawable.ic_flash_off))
    }

    fun on_Torch(){
        flash_iv.setImageDrawable(context?.getDrawable(R.drawable.ic_flash_on))
    }

    init {
        View.inflate(context,R.layout.camera_interaction_layout,this)
        camera_capture_button.setOnClickListener {
            clickSubject.onNext(Unit)
        }
        flash_iv.setOnClickListener {
            interactor.onFlashClicked()
        }
        back.setOnClickListener {
            backSubject.onNext(Unit)
        }
        disposable.add(
            clickSubject.throttleFirst(300,TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe{
                interactor.onClick()
            }
        )
        disposable.add(
            backSubject.throttleFirst(300,TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe{
                interactor.onBackClicked()
            }
        )
    }

    override fun onDetachedFromWindow() {
        disposable.dispose()
        super.onDetachedFromWindow()
    }

    interface Interactor{
        fun onClick()
        fun onFlashClicked()
        fun onBackClicked()
    }
}