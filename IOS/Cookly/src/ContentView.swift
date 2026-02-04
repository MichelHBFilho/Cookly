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
    
    var body: some View {
        if(authService.isUserLogged) {
            
        } else {
            AuthenticationView()
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
