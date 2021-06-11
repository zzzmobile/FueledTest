import resources.Data

/**

# Fueled Kotlin Exercise

A blogging platform stores the following information that is available through separate API endpoints:
+ user accounts
+ blog posts for each user
+ comments for each blog post

### Objective
The organization needs to identify the 3 most engaging bloggers on the platform. Using only Kotlin and the Kotlin standard library, output the top 3 users with the highest average number of comments per post in the following format:

`[name]` - `[id]`, Score: `[average_comments]`

Instead of connecting to a remote API, we are providing this data in form of JSON files, which have been made accessible through a custom Resource enum with a `data` method that provides the contents of the file.

### What we're looking to evaluate
1. How you choose to model your data
2. How you transform the provided JSON data to your data model
3. How you use your models to calculate this average value
4. How you use this data point to sort the users

 */

// 1. First, start by modeling the data objects that will be used.
data class User(
    var id: Long = 0,
    var name: String = "",
    var username: String = "",
    var email: String = "",
    var address: Address = Address(),
    var phone: String = "",
    var website: String = "",
    var company: Company = Company(),
    var posts: List<Post> = listOf(),

    var average_comments: Float   // average count of comments
)

data class Address(
    var street: String = "",
    var suit: String = "",
    var city: String = "",
    var zipCode: String = "",
    var geo: Location = Location()
)

data class Location(
    var lat: Double = 0.0,
    var lng: Double = 0.0
)

data class Company(
    var name: String = "",
    var catchPhrase: String = "",
    var bs: String = ""
)

data class Post(
    var id: Long = 0,
    var userId: Long = 0,
    var title: String = "",
    var body: String = "",
    var comments: List<Comment> = listOf()
)

data class Comment(
    var id: Long = 0,
    var name: String = "",
    var body: String = "",
    var postId: Long = 0,
    var email: String = ""
)

fun main(vararg args: String) {
    // 2. Next, decode the JSON source using `[Data.getUsers()]`
    val allUsers: Array<User> = Data.getUsers()
    val allPosts: Array<Post> = Data.getPosts()
    val allComments: Array<Comment> = Data.getComments()

    // set comments for each post
    allPosts.forEach { post -> post.comments = allComments.filter { it.postId == post.id } }

    // set posts for each user
    allUsers.forEach { user -> user.posts = allPosts.filter { it.userId == user.id } }

    // 3. Finally, calculate the average number of comments per user and use it
    //    to find the 3 most engaging bloggers and output the result.
    val sortedUsers: List<User> = allUsers.sortedWith(compareByDescending {
        it.average_comments = it.posts.fold(0) {R, post -> R + post.comments.size} * 1.0f / it.posts.size
        it.average_comments
    }).toList()

    sortedUsers.take(3).forEach {
        println("${it.name} - ${it.id}, Score: ${it.average_comments}")
    }
}