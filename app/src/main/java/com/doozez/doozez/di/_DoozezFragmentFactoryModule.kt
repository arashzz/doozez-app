//package com.doozez.doozez.di
//
//import androidx.fragment.app.FragmentFactory
//import androidx.lifecycle.ViewModelProvider
//import com.doozez.doozez.business.domain.util.DateUtil
//import com.doozez.doozez.framework.presentation.DoozezFragmentFactory
//import com.google.android.datatransport.runtime.dagger.Module
//import com.google.android.datatransport.runtime.dagger.Provides
//import kotlinx.coroutines.ExperimentalCoroutinesApi
//import kotlinx.coroutines.FlowPreview
//import javax.inject.Singleton
//
//@FlowPreview
//@ExperimentalCoroutinesApi
//@Module
//object _DoozezFragmentFactoryModule {
//
//    @JvmStatic
//    @Singleton
//    @Provides
//    fun provideNoteFragmentFactory(
//        viewModelFactory: ViewModelProvider.Factory,
//        dateUtil: DateUtil
//    ): FragmentFactory {
//        return DoozezFragmentFactory(
//            viewModelFactory,
//            dateUtil
//        )
//    }
//}