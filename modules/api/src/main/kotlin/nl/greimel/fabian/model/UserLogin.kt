/**
 * Newsreader Backend API
 *
 * No description provided (generated by Openapi Generator https://github.com/openapitools/openapi-generator)
 *
 * The version of the OpenAPI document: 1.0.0
 * 
 *
 * Please note:
 * This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * Do not edit this file manually.
 */

@file:Suppress(
    "ArrayInDataClass",
    "EnumEntryName",
    "RemoveRedundantQualifierName",
    "UnusedImport"
)

package nl.greimel.fabian.model


import com.squareup.moshi.Json

/**
 * 
 *
 * @param userName 
 * @param password 
 */

data class UserLogin (

    @Json(name = "UserName")
    val userName: kotlin.String? = null,

    @Json(name = "Password")
    val password: kotlin.String? = null

)

