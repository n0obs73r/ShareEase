package com.ogps.shareease.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ogps.shareease.R

private const val USER_ID = "UserID"

class Publish : Fragment() {

    private var userID: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userID = it.getString(USER_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_publish, container, false)
    }

    companion object {
        fun newInstance(userID: String) =
            Publish().apply {
                arguments = Bundle().apply {
                    putString(USER_ID, userID)
                }
            }
    }
}