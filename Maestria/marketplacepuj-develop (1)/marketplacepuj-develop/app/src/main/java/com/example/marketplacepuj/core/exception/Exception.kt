package com.example.marketplacepuj.core.exception

class InvalidUserPasswordException : Throwable("Invalid User Password")

class EmptyFieldException : Throwable("Empty Field")
class BadEmailFieldException : Throwable("Bad Email Field")
class BadPasswordFieldException : Throwable("Bad Password Field")
class BadPasswordLengthException : Throwable("Bad Password Length")
class EmptyCategoriesException : Throwable("Empty Categories")
class EmptyProductsException : Throwable("Empty Products")
class CategoriesException : Throwable("Categories Exception")
class ProductsException : Throwable("Products Exception")

class NoUserFoundException : Throwable("No User Found")
class NewOrderException : Throwable("New Order Exception")

class ProfileErrorException : Throwable("Profile Error Exception")
class CloseSessionException : Throwable("Close Session Exception")
class ChangePasswordException : Throwable("Close Session Exception")
class UpdateProfileException : Throwable("Update Profile Exception")
class OrderHistoryException : Throwable("Order History Exception")
