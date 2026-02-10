//
//  CooklyApp.swift
//  Cookly
//
//  Created by Michel Filho on 01/02/26.
//

import SwiftUI
import SwiftData

@main
struct CooklyApp: App {
    @State private var networkMonitor = NetworkMonitor()
    @State private var errorManager = ErrorManager.shared
    var body: some Scene {
        WindowGroup {
            Group {
                if(networkMonitor.isConnected) {
                    ContentView()
                } else {
                    DisconnectedView()
                }
            }.sheet(isPresented: $errorManager.showError) {
                ErrorView(error: errorManager.currentError!)
            }
            .task {
                Task {
                    try await AuthService.shared.refreshToken()
                }
            }
        }
        
    }
}
