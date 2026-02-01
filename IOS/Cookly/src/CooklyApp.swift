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
    
    var body: some Scene {
        WindowGroup {
            if(networkMonitor.isConnected) {
                ContentView()
            } else {
                DisconnectedView()
            }
        }
    }
}
