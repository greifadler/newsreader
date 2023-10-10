# org.openapitools.client - Kotlin client library for Newsreader Backend API

## Requires

* Kotlin 1.4.30
* Gradle 6.8.3

## Build

First, create the gradle wrapper script:

```
gradle wrapper
```

Then, run:

```
./gradlew check assemble
```

This runs all tests and packages the library.

## Features/Implementation Notes

* Supports JSON inputs/outputs, File inputs, and Form inputs.
* Supports collection formats for query parameters: csv, tsv, ssv, pipes.
* Some Kotlin and Java types are fully qualified to avoid conflicts with types defined in OpenAPI definitions.
* Implementation of ApiClient is intended to reduce method counts, specifically to benefit Android targets.

<a name="documentation-for-api-endpoints"></a>
## Documentation for API Endpoints

All URIs are relative to */api*

Class | Method | HTTP request | Description
------------ | ------------- | ------------- | -------------
*DefaultApi* | [**articlesGet**](docs/DefaultApi.md#articlesget) | **GET** /Articles | Get a list of articles
*DefaultApi* | [**articlesIdGet**](docs/DefaultApi.md#articlesidget) | **GET** /Articles/{id} | Get a list of articles starting from a specific article Id
*DefaultApi* | [**articlesIdLikeDelete**](docs/DefaultApi.md#articlesidlikedelete) | **DELETE** /Articles/{id}/like | Remove like from an article
*DefaultApi* | [**articlesIdLikePut**](docs/DefaultApi.md#articlesidlikeput) | **PUT** /Articles/{id}/like | Like an article
*DefaultApi* | [**articlesLikedGet**](docs/DefaultApi.md#articleslikedget) | **GET** /Articles/liked | Get a list of articles liked by the logged-in user
*DefaultApi* | [**feedsGet**](docs/DefaultApi.md#feedsget) | **GET** /Feeds | Get a list of all news feeds
*DefaultApi* | [**usersLoginPost**](docs/DefaultApi.md#usersloginpost) | **POST** /Users/login | Log in as a user
*DefaultApi* | [**usersRegisterPost**](docs/DefaultApi.md#usersregisterpost) | **POST** /Users/register | Register a new user


<a name="documentation-for-models"></a>
## Documentation for Models

 - [nl.greimel.fabian.model.InlineObject](docs/InlineObject.md)
 - [nl.greimel.fabian.model.InlineObject1](docs/InlineObject1.md)
 - [nl.greimel.fabian.model.InlineResponse200](docs/InlineResponse200.md)
 - [nl.greimel.fabian.model.InlineResponse2001](docs/InlineResponse2001.md)
 - [nl.greimel.fabian.model.InlineResponse2001Results](docs/InlineResponse2001Results.md)
 - [nl.greimel.fabian.model.InlineResponse2002](docs/InlineResponse2002.md)
 - [nl.greimel.fabian.model.InlineResponse2003](docs/InlineResponse2003.md)


<a name="documentation-for-authorization"></a>
## Documentation for Authorization

<a name="x-authtoken"></a>
### x-authtoken

- **Type**: API key
- **API key parameter name**: x-authtoken
- **Location**: HTTP header

