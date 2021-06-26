package com.doozez.doozez.ui.invitation

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.doozez.doozez.R
import com.doozez.doozez.api.ApiClient
import com.doozez.doozez.api.invitation.InvitationDetailResponse
import com.doozez.doozez.ui.invitation.adapters.InvitationListAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A fragment representing a list of Items.
 */
class InvitationListFragment : Fragment() {

    private var columnCount = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_invitation_list, container, false)
        val call = ApiClient.invitationService.getInvitationsForUser("1")
        call.enqueue(object : Callback<List<InvitationDetailResponse>> {
            override fun onResponse(call: Call<List<InvitationDetailResponse>>, response: Response<List<InvitationDetailResponse>>) {
                if (response.isSuccessful && response.body() != null) {
                    var invites = response.body().toMutableList()
                    // Set the adapter
                    if (view is RecyclerView) {
                        with(view) {
                            layoutManager = when {
                                columnCount <= 1 -> LinearLayoutManager(context)
                                else -> GridLayoutManager(context, columnCount)
                            }
                            adapter = InvitationListAdapter(invites)
                        }
                    }
                }
            }
            override fun onFailure(call: Call<List<InvitationDetailResponse>>, t: Throwable) {
                //TODO: do something
            }
        })
        return view
    }

    suspend fun populateInvites(userId:String, view:View) {

    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            InvitationListFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}