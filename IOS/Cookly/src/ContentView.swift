//
//  ContentView.swift
//  Cookly
//
//  Created by Michel Filho on 01/02/26.
//

import SwiftUI
import SwiftData

struct ContentView: View {
    @Environment(\.modelContext) private var modelContext
    @Environment(NetworkMonitor.self) private var networkMonitor

    var body: some View {
    }
}

#Preview {
    ContentView()
}
