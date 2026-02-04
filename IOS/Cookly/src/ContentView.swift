//
//  ContentView.swift
//  Cookly
//
//  Created by Michel Filho on 01/02/26.
//

import SwiftUI
import SwiftData

struct ContentView: View {
    var body: some View {
        if(AuthService.shared.isUserLogged) {
            
        } else {
            AuthenticationView()
        }
    }
}

#Preview {
    
    Task {
        do {
            try await AuthService.shared.refreshToken()
        } catch {
            print(error)
        }
    }
    
    return ContentView()
}
