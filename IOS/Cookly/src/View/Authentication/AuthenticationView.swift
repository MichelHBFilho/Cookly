//
//  AuthenticationView.swift
//  Cookly
//
//  Created by Michel Filho on 02/02/26.
//

import SwiftUI

struct AuthenticationView: View {
    @State private var showLoginScreen = true
    var body: some View {
        if(showLoginScreen) {
            LoginView()
        } else {
            RegisterView()
        }
        
        MyButton(
            title: (showLoginScreen) ? "Go register" : "Go login",
            color: .cooklyGray) {
                showLoginScreen.toggle()
            }
    }
}

#Preview {
    AuthenticationView()
}
