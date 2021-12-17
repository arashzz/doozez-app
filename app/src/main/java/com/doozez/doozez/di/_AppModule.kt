//package com.doozez.doozez.di
//
//import android.content.SharedPreferences
//import com.doozez.doozez.business.domain.models.invitation.InvitationFactory
//import com.doozez.doozez.business.domain.util.DateUtil
//import com.google.android.datatransport.runtime.dagger.Module
//import com.google.android.datatransport.runtime.dagger.Provides
//import kotlinx.coroutines.ExperimentalCoroutinesApi
//import kotlinx.coroutines.FlowPreview
//import java.text.SimpleDateFormat
//import java.util.*
//import javax.inject.Singleton
//
//@ExperimentalCoroutinesApi
//@FlowPreview
//@Module
//object AppModule {
//
//
//    // https://developer.android.com/reference/java/text/SimpleDateFormat.html?hl=pt-br
//    @JvmStatic
//    @Singleton
//    @Provides
//    fun provideDateFormat(): SimpleDateFormat {
//        val sdf = SimpleDateFormat("yyyy-MM-dd hh:mm:ss a", Locale.ENGLISH)
//        sdf.timeZone = TimeZone.getTimeZone("UTC-7") // match firestore
//        return sdf
//    }
//
//    @JvmStatic
//    @Singleton
//    @Provides
//    fun provideDateUtil(dateFormat: SimpleDateFormat): DateUtil {
//        return DateUtil(
//            dateFormat
//        )
//    }
//
//    @JvmStatic
//    @Singleton
//    @Provides
//    fun provideSharedPrefsEditor(
//        sharedPreferences: SharedPreferences
//    ): SharedPreferences.Editor {
//        return sharedPreferences.edit()
//    }
//
//    @JvmStatic
//    @Singleton
//    @Provides
//    fun provideInvitationFactory(dateUtil: DateUtil): InvitationFactory {
//        return InvitationFactory()
//    }
//
////    @JvmStatic
////    @Singleton
////    @Provides
////    fun provideNoteDAO(noteDatabase: NoteDatabase): NoteDao {
////        return noteDatabase.noteDao()
////    }
//
////    @JvmStatic
////    @Singleton
////    @Provides
////    fun provideNoteCacheMapper(dateUtil: DateUtil): CacheMapper {
////        return CacheMapper(dateUtil)
////    }
//
//    @JvmStatic
//    @Singleton
//    @Provides
//    fun provideNoteNetworkMapper(dateUtil: DateUtil): NetworkMapper {
//        return NetworkMapper(dateUtil)
//    }
//
//    @JvmStatic
//    @Singleton
//    @Provides
//    fun provideFirebaseAuth(): FirebaseAuth {
//        return FirebaseAuth.getInstance()
//    }
//
//    @JvmStatic
//    @Singleton
//    @Provides
//    fun provideNoteDaoService(
//        noteDao: NoteDao,
//        noteEntityMapper: CacheMapper,
//        dateUtil: DateUtil
//    ): NoteDaoService{
//        return NoteDaoServiceImpl(noteDao, noteEntityMapper, dateUtil)
//    }
//
//    @JvmStatic
//    @Singleton
//    @Provides
//    fun provideNoteCacheDataSource(
//        noteDaoService: NoteDaoService
//    ): NoteCacheDataSource {
//        return NoteCacheDataSourceImpl(noteDaoService)
//    }
//
//    @JvmStatic
//    @Singleton
//    @Provides
//    fun provideFirestoreService(
//        firebaseAuth: FirebaseAuth,
//        firebaseFirestore: FirebaseFirestore,
//        networkMapper: NetworkMapper
//    ): NoteFirestoreService {
//        return NoteFirestoreServiceImpl(
//            firebaseAuth,
//            firebaseFirestore,
//            networkMapper
//        )
//    }
//
//    @JvmStatic
//    @Singleton
//    @Provides
//    fun provideNoteNetworkDataSource(
//        firestoreService: NoteFirestoreServiceImpl
//    ): NoteNetworkDataSource {
//        return NoteNetworkDataSourceImpl(
//            firestoreService
//        )
//    }
//
//    @JvmStatic
//    @Singleton
//    @Provides
//    fun provideNoteDetailInteractors(
//        noteCacheDataSource: NoteCacheDataSource,
//        noteNetworkDataSource: NoteNetworkDataSource
//    ): NoteDetailInteractors{
//        return NoteDetailInteractors(
//            DeleteNote(noteCacheDataSource, noteNetworkDataSource),
//            UpdateNote(noteCacheDataSource, noteNetworkDataSource)
//        )
//    }
//
//    @JvmStatic
//    @Singleton
//    @Provides
//    fun provideNoteListInteractors(
//        noteCacheDataSource: NoteCacheDataSource,
//        noteNetworkDataSource: NoteNetworkDataSource,
//        noteFactory: NoteFactory
//    ): NoteListInteractors {
//        return NoteListInteractors(
//            InsertNewNote(noteCacheDataSource, noteNetworkDataSource, noteFactory),
//            DeleteNote(noteCacheDataSource, noteNetworkDataSource),
//            SearchNotes(noteCacheDataSource),
//            GetNumNotes(noteCacheDataSource),
//            RestoreDeletedNote(noteCacheDataSource, noteNetworkDataSource),
//            DeleteMultipleNotes(noteCacheDataSource, noteNetworkDataSource),
//            InsertMultipleNotes(noteCacheDataSource, noteNetworkDataSource)
//        )
//    }
//
//    @JvmStatic
//    @Singleton
//    @Provides
//    fun provideSyncNotes(
//        noteCacheDataSource: NoteCacheDataSource,
//        noteNetworkDataSource: NoteNetworkDataSource,
//        dateUtil: DateUtil
//    ): SyncNotes{
//        return SyncNotes(
//            noteCacheDataSource,
//            noteNetworkDataSource,
//            dateUtil
//
//        )
//    }
//
//    @JvmStatic
//    @Singleton
//    @Provides
//    fun provideSyncDeletedNotes(
//        noteCacheDataSource: NoteCacheDataSource,
//        noteNetworkDataSource: NoteNetworkDataSource
//    ): SyncDeletedNotes{
//        return SyncDeletedNotes(
//            noteCacheDataSource,
//            noteNetworkDataSource
//        )
//    }
//
//    @JvmStatic
//    @Singleton
//    @Provides
//    fun provideNoteNetworkSyncManager(
//        syncNotes: SyncNotes,
//        deletedNotes: SyncDeletedNotes
//    ): NoteNetworkSyncManager {
//        return NoteNetworkSyncManager(
//            syncNotes,
//            deletedNotes
//        )
//    }
//
//}
