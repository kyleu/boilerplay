# Security

Controller actions are secured by Silhouette, see `BaseController` for all of the available methods, and `HomeController` for examples of their usage.

Service methods all require a `Credentials` object to be passed, containing the user and request information. And implicit conversion from `SecuredRequest` is provided.

See [authentication](authentication.md) for details about the authentication flow.
