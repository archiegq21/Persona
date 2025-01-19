package com.apps.usergen.ui.util

import com.apps.usergen.data.User

fun User.Name.toFullname(): String = "${title} ${first} ${last}"
