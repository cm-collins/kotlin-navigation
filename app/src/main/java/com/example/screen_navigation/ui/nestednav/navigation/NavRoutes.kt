package com.example.screen_navigation.ui.nestednav.navigation

sealed class AppRoutes(val route: String) {
    data object HomeScreen : AppRoutes(route = "Home")
    data object CheckoutScreen : AppRoutes(route = "checkout")
    data object ProfileScreen : AppRoutes(route = "profile")
    data object ConfirmationScreen : AppRoutes(route = "confirmation")
    data object LoginScreen : AppRoutes(route = "login")
    data object SignupScreen : AppRoutes(route = "sign_up")
    data object ResetPinScreen : AppRoutes(route = "reset_pin")
    data object LogoutScreen : AppRoutes(route = "logout")

    // Nested navigation
    data object Auth : AppRoutes("auth")

    data object Main : AppRoutes("main")
}