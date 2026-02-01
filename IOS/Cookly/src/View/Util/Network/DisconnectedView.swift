//
//  DisconnectedView.swift
//  Cookly
//
//  Created by Michel Filho on 01/02/26.
//

import SwiftUI

struct DisconnectedView: View {
    var body: some View {
        VStack {
            Image(systemName: "wifi.slash")
                .resizable()
                .scaledToFit()
                .frame(width: 100)
                .foregroundStyle(.errorRed)
                .padding()
            
            Text("You're disconnected.")
                .fontWeight(.semibold)
        }
    }
}

#Preview {
    DisconnectedView()
}
