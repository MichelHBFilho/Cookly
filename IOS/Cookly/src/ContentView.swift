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
        switch(router.route) {
        case .authentication:
            AuthenticationView()
        case .homepage:
            HomepageView()
        case .newPost:
            Color.clear
        }
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
