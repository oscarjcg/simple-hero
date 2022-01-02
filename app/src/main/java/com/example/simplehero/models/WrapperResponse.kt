package com.example.simplehero.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class WrapperResponse {
    @SerializedName("code")
    @Expose
    var code: Int? = null

    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("data")
    @Expose
    var data: DataResponse? = null
}
