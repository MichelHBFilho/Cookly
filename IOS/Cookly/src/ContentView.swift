//
//  ContentView.swift
//  Cookly
//
//  Created by Michel Filho on 01/02/26.
//

import SwiftUI
import SwiftData

struct ContentView: View {
    @State private var authService = AuthService.shared
    @ObservedObject private var router = Router.shared
    
    var body: some View {
        Group {
            switch(router.route) {
            case .authentication:
                AuthenticationView()
                    .transition(.slide)
            case .homepage:
                HomepageView()
                    .transition(.slide)
            case .newPost:
                NewPostView()
                    .transition(.slide)
            }
        }
        .animation(.easeInOut, value: router.route)
    }
}

#Preview {
    
    Task {
        do {
            try? KeychainService.shared.delete(from: "accessToken")
            try? KeychainService.shared.delete(from: "refreshToken")
        } catch {
            print(error)
        }
    }
    
    return ContentView()
}
