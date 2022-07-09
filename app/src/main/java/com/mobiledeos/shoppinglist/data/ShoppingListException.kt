package com.mobiledeos.shoppinglist.data

enum class ShoppingListErrorCodes {
    Success,
    OwnerIdIsEmpty,
    ErrorUpdatingList,
    ErrorUpdatingThing,
    ErrorAddingUser,
    UserAuthenticationError
}

class ShoppingListException(message: String, val code: ShoppingListErrorCodes = ShoppingListErrorCodes.Success): Exception (message)