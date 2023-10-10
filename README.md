# The Reader

"The Reader" is an Android app developed using Jetpack Compose and Kotlin. It provides users with a seamless experience to read news articles by connecting to an API. The app offers functionalities like logging in, liking/unliking articles, and more.

## Screenshots

![Latest News](demo_images/main_screen.png)
_Latest News Screen with Infinite Loading_

![Article Details](demo_images/details_screen.png)
_Article Details Screen_

![Login/Logout](demo_images/my_profile_screen.png)
_Profile/Login/Logout Screen_

![Liked Articles](demo_images/favourites_screen.png)
_Liked Articles Screen_

![Category Browse](demo_images/category_sports_screen.png)
_Category Browse Screen_

## Bonus Features

Hello! Fabian Greimel (7210009) here. Welcome to my extra-feature list:

- Generated API-access files using the gradle plugin `openApi` with a swagger file.
- Incorporated the latest Material You Design.
- Implemented the `AccountManager` (previous SharedPrefs approach marked as deprecated).
- All information from the API is shown on the details page (timestamp, related article links, categories as badges).
- Categories can be clicked to show a feed of that particular category.
- App has been localized for German/AT.
- Sharing feature available on the details page.
- Added subtle animations.
- Made an attempt to add a home widget (code still present, though not functional).
- Custom icon & splash screen.
- Clean architecture (no Repo/Service layer to avoid boilerplate for this project's scope).
- Other features:
    - Implemented custom font - Roboto Slab.
    - Use of inheritance with `BasicViewModel`.

## Using the openApi generator gradle plugin

For more details, visit the official [openApi Generator Gradle Plugin GitHub repository](https://github.com/OpenAPITools/openapi-generator/tree/master/modules/openapi-generator-gradle-plugin).

Here's how it's configured:

```kotlin
openApiGenerate {
    generatorName.set("kotlin")
    inputSpec.set("$projectDir/../swagger/api.yaml")
    outputDir.set("$projectDir/../modules/api")
    apiPackage.set("nl.greimel.fabian.api")
    modelPackage.set("nl.greimel.fabian.model")
}
