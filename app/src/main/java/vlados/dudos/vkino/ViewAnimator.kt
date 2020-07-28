package vlados.dudos.vkino


import android.view.View
import com.github.florent37.viewanimator.ViewAnimator

class ViewAnimator {

    fun animateFadeIn(
        view: View,
        duration: Int,
        addTranslation: Boolean,
        translationLeft: Boolean
    ) {
        if (addTranslation)
            ViewAnimator
                .animate(view)
                .alpha(0F, 1F)
                .translationX(if (translationLeft) (-50).toFloat() else 50F, 0F)
                .duration(duration.toLong())
                .start()
        else
            ViewAnimator
                .animate(view)
                .alpha(0F, 1F)
                .duration(duration.toLong())
                .start()
    }

    fun topAnimate(
        view: View,
        duration: Int,
        addTranslation: Boolean,
        translationTop: Boolean
    ){
        if (addTranslation)
            ViewAnimator
                .animate(view)
                .alpha(0F, 1F)
                .translationX(if (translationTop) (50).toFloat() else (-50).toFloat(), 0F)
                .duration(duration.toLong())
                .start()
        else
            ViewAnimator
                .animate(view)
                .alpha(0F, 1F)
                .duration(duration.toLong())
                .start()
    }


}