# DefaultApi

All URIs are relative to */api*

Method | HTTP request | Description
------------- | ------------- | -------------
[**articlesGet**](DefaultApi.md#articlesGet) | **GET** /Articles | Get a list of articles
[**articlesIdGet**](DefaultApi.md#articlesIdGet) | **GET** /Articles/{id} | Get a list of articles starting from a specific article Id
[**articlesIdLikeDelete**](DefaultApi.md#articlesIdLikeDelete) | **DELETE** /Articles/{id}/like | Remove like from an article
[**articlesIdLikePut**](DefaultApi.md#articlesIdLikePut) | **PUT** /Articles/{id}/like | Like an article
[**articlesLikedGet**](DefaultApi.md#articlesLikedGet) | **GET** /Articles/liked | Get a list of articles liked by the logged-in user
[**feedsGet**](DefaultApi.md#feedsGet) | **GET** /Feeds | Get a list of all news feeds
[**usersLoginPost**](DefaultApi.md#usersLoginPost) | **POST** /Users/login | Log in as a user
[**usersRegisterPost**](DefaultApi.md#usersRegisterPost) | **POST** /Users/register | Register a new user


<a name="articlesGet"></a>
# **articlesGet**
> InlineResponse2001 articlesGet(count, feed, feeds, category)

Get a list of articles

Returns a list of articles, starting with the newest article in the database that fits the specified arguments. Also returns the Id of the next article, which can be used for incremental loading.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import nl.greimel.fabian.model.*

val apiInstance = DefaultApi()
val count : kotlin.Int = 56 // kotlin.Int | The (max) number of results to return
val feed : kotlin.Int = 56 // kotlin.Int | Use this to get only articles from the specified feed Id.
val feeds : kotlin.String = feeds_example // kotlin.String | Comma-separated list of feed ids; use to filter articles.
val category : kotlin.Int = 56 // kotlin.Int | Use this to get only articles with specified category Id.
try {
    val result : InlineResponse2001 = apiInstance.articlesGet(count, feed, feeds, category)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#articlesGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#articlesGet")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **count** | **kotlin.Int**| The (max) number of results to return | [optional]
 **feed** | **kotlin.Int**| Use this to get only articles from the specified feed Id. | [optional]
 **feeds** | **kotlin.String**| Comma-separated list of feed ids; use to filter articles. | [optional]
 **category** | **kotlin.Int**| Use this to get only articles with specified category Id. | [optional]

### Return type

[**InlineResponse2001**](InlineResponse2001.md)

### Authorization


Configure x-authtoken:
    ApiClient.apiKey["x-authtoken"] = ""
    ApiClient.apiKeyPrefix["x-authtoken"] = ""

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

<a name="articlesIdGet"></a>
# **articlesIdGet**
> InlineResponse2001 articlesIdGet(id, count, feed, feeds, category)

Get a list of articles starting from a specific article Id

Returns a list of articles, starting with the article with the specified Id. Also returns the Id of the next article, which can be used for incremental loading.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import nl.greimel.fabian.model.*

val apiInstance = DefaultApi()
val id : kotlin.Int = 56 // kotlin.Int | The article Id to start from
val count : kotlin.Int = 56 // kotlin.Int | The (max) number of results to return
val feed : kotlin.Int = 56 // kotlin.Int | Use this to get only articles from the specified feed Id.
val feeds : kotlin.String = feeds_example // kotlin.String | Comma-separated list of feed ids; use to filter articles.
val category : kotlin.Int = 56 // kotlin.Int | Use this to get only articles with specified category Id.
try {
    val result : InlineResponse2001 = apiInstance.articlesIdGet(id, count, feed, feeds, category)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#articlesIdGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#articlesIdGet")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **kotlin.Int**| The article Id to start from |
 **count** | **kotlin.Int**| The (max) number of results to return | [optional]
 **feed** | **kotlin.Int**| Use this to get only articles from the specified feed Id. | [optional]
 **feeds** | **kotlin.String**| Comma-separated list of feed ids; use to filter articles. | [optional]
 **category** | **kotlin.Int**| Use this to get only articles with specified category Id. | [optional]

### Return type

[**InlineResponse2001**](InlineResponse2001.md)

### Authorization


Configure x-authtoken:
    ApiClient.apiKey["x-authtoken"] = ""
    ApiClient.apiKeyPrefix["x-authtoken"] = ""

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

<a name="articlesIdLikeDelete"></a>
# **articlesIdLikeDelete**
> articlesIdLikeDelete(id)

Remove like from an article

Marks the specified article as no longer liked by the logged-in user.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import nl.greimel.fabian.model.*

val apiInstance = DefaultApi()
val id : kotlin.Int = 56 // kotlin.Int | ID of the article from which the like is to be removed.
try {
    apiInstance.articlesIdLikeDelete(id)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#articlesIdLikeDelete")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#articlesIdLikeDelete")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **kotlin.Int**| ID of the article from which the like is to be removed. |

### Return type

null (empty response body)

### Authorization


Configure x-authtoken:
    ApiClient.apiKey["x-authtoken"] = ""
    ApiClient.apiKeyPrefix["x-authtoken"] = ""

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

<a name="articlesIdLikePut"></a>
# **articlesIdLikePut**
> articlesIdLikePut(id)

Like an article

Marks the specified article as liked by the logged-in user.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import nl.greimel.fabian.model.*

val apiInstance = DefaultApi()
val id : kotlin.Int = 56 // kotlin.Int | ID of the article to be liked.
try {
    apiInstance.articlesIdLikePut(id)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#articlesIdLikePut")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#articlesIdLikePut")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **kotlin.Int**| ID of the article to be liked. |

### Return type

null (empty response body)

### Authorization


Configure x-authtoken:
    ApiClient.apiKey["x-authtoken"] = ""
    ApiClient.apiKeyPrefix["x-authtoken"] = ""

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

<a name="articlesLikedGet"></a>
# **articlesLikedGet**
> InlineResponse2001 articlesLikedGet()

Get a list of articles liked by the logged-in user

Returns a list of articles that the logged-in user has liked.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import nl.greimel.fabian.model.*

val apiInstance = DefaultApi()
try {
    val result : InlineResponse2001 = apiInstance.articlesLikedGet()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#articlesLikedGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#articlesLikedGet")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**InlineResponse2001**](InlineResponse2001.md)

### Authorization


Configure x-authtoken:
    ApiClient.apiKey["x-authtoken"] = ""
    ApiClient.apiKeyPrefix["x-authtoken"] = ""

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

<a name="feedsGet"></a>
# **feedsGet**
> kotlin.collections.List&lt;InlineResponse200&gt; feedsGet()

Get a list of all news feeds

Returns a list of all news feeds. Every news article comes from exactly one feed.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import nl.greimel.fabian.model.*

val apiInstance = DefaultApi()
try {
    val result : kotlin.collections.List<InlineResponse200> = apiInstance.feedsGet()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#feedsGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#feedsGet")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**kotlin.collections.List&lt;InlineResponse200&gt;**](InlineResponse200.md)

### Authorization


Configure x-authtoken:
    ApiClient.apiKey["x-authtoken"] = ""
    ApiClient.apiKeyPrefix["x-authtoken"] = ""

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

<a name="usersLoginPost"></a>
# **usersLoginPost**
> InlineResponse2003 usersLoginPost(body)

Log in as a user

Logs a user in and returns an auth token, if the specified username and password are correct.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import nl.greimel.fabian.model.*

val apiInstance = DefaultApi()
val body : InlineObject1 =  // InlineObject1 | 
try {
    val result : InlineResponse2003 = apiInstance.usersLoginPost(body)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#usersLoginPost")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#usersLoginPost")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**InlineObject1**](InlineObject1.md)|  |

### Return type

[**InlineResponse2003**](InlineResponse2003.md)

### Authorization


Configure x-authtoken:
    ApiClient.apiKey["x-authtoken"] = ""
    ApiClient.apiKeyPrefix["x-authtoken"] = ""

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

<a name="usersRegisterPost"></a>
# **usersRegisterPost**
> InlineResponse2002 usersRegisterPost(body)

Register a new user

Creates a new user account, if the UserName is not already in use.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.infrastructure.*
//import nl.greimel.fabian.model.*

val apiInstance = DefaultApi()
val body : InlineObject =  // InlineObject | 
try {
    val result : InlineResponse2002 = apiInstance.usersRegisterPost(body)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#usersRegisterPost")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#usersRegisterPost")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**InlineObject**](InlineObject.md)|  |

### Return type

[**InlineResponse2002**](InlineResponse2002.md)

### Authorization


Configure x-authtoken:
    ApiClient.apiKey["x-authtoken"] = ""
    ApiClient.apiKeyPrefix["x-authtoken"] = ""

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

