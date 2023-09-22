package com.base.basemvvmcleanarchitecture.base

import android.view.LayoutInflater
import android.view.ViewGroup

// This alias makes the code more readable
typealias FragmentBindingInflater<VB> = (inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean) -> VB
typealias ActivityBindingInflater<VB> = (inflater: LayoutInflater) -> VB
