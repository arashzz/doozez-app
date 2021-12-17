//package com.doozez.doozez.di
//
//import com.doozez.doozez.MainActivity
//import com.doozez.doozez.framework.presentation.invitation.InvitationListFragment
//import com.google.android.datatransport.runtime.dagger.BindsInstance
//import com.google.android.datatransport.runtime.dagger.Component
//import kotlinx.coroutines.ExperimentalCoroutinesApi
//import kotlinx.coroutines.FlowPreview
//import javax.inject.Singleton
//
//@ExperimentalCoroutinesApi
//@FlowPreview
//@Singleton
//@Component(
//    modules = [
////        ProductionModule::class,
//        AppModule::class,
//        NoteViewModelModule::class,
//        DoozezFragmentFactoryModule::class
//    ]
//)
//interface AppComponent {
//
////    val noteNetworkSync: NoteNetworkSyncManager
//
//    @Component.Factory
//    interface Factory{
//
//        fun create(@BindsInstance app: BaseApplication): AppComponent
//    }
//
//    fun inject(mainActivity: MainActivity)
//
////    fun inject(splashFragment: SplashFragment)
//
//    fun inject(invitationListFragment: InvitationListFragment)
//
////    fun inject(noteDetailFragment: NoteDetailFragment)
//}
